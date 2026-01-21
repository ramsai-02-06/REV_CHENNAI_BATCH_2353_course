# AWS CodeBuild

## What is CodeBuild?

CodeBuild is a fully managed continuous integration service that compiles source code, runs tests, and produces deployable artifacts.

```
┌─────────────┐    ┌─────────────────────────────┐    ┌─────────────┐
│   Source    │───▶│        CodeBuild            │───▶│  Artifacts  │
│   (GitHub)  │    │  ┌─────────────────────┐   │    │    (S3)     │
│             │    │  │   Build Container   │   │    │             │
│             │    │  │  - Install deps     │   │    │  - JAR      │
│             │    │  │  - Run tests        │   │    │  - Docker   │
│             │    │  │  - Build artifact   │   │    │  - ZIP      │
│             │    │  └─────────────────────┘   │    │             │
└─────────────┘    └─────────────────────────────┘    └─────────────┘
```

---

## Build Project Setup

### Create via Console

1. Go to **CodeBuild** → **Create build project**
2. Configure:
   - **Project name**: my-app-build
   - **Source**: GitHub, CodeCommit, or S3
   - **Environment**: Managed image or custom Docker
   - **Buildspec**: Use buildspec.yml from source
   - **Artifacts**: S3 bucket

### Create via CLI

```bash
aws codebuild create-project \
  --name my-spring-app-build \
  --source type=GITHUB,location=https://github.com/user/my-spring-app \
  --environment type=LINUX_CONTAINER,computeType=BUILD_GENERAL1_SMALL,image=aws/codebuild/amazonlinux2-x86_64-standard:4.0 \
  --service-role arn:aws:iam::123456789:role/CodeBuildServiceRole \
  --artifacts type=S3,location=my-artifacts-bucket,name=my-app
```

---

## buildspec.yml

The build specification file defines what CodeBuild does.

### Basic Structure

```yaml
version: 0.2

env:
  variables:
    KEY: "value"

phases:
  install:
    commands:
      - echo "Installing..."

  pre_build:
    commands:
      - echo "Preparing..."

  build:
    commands:
      - echo "Building..."

  post_build:
    commands:
      - echo "Finishing..."

artifacts:
  files:
    - '**/*'

cache:
  paths:
    - '/root/.m2/**/*'
```

### Spring Boot Example

```yaml
version: 0.2

env:
  variables:
    JAVA_HOME: "/usr/lib/jvm/java-17-amazon-corretto"

phases:
  install:
    runtime-versions:
      java: corretto17
    commands:
      - echo "Java version:"
      - java -version

  pre_build:
    commands:
      - echo "Downloading dependencies..."
      - mvn dependency:resolve

  build:
    commands:
      - echo "Running unit tests..."
      - mvn test
      - echo "Building JAR..."
      - mvn package -DskipTests
      - echo "Build completed successfully"

  post_build:
    commands:
      - echo "Build completed on $(date)"
      - mv target/*.jar target/app.jar

artifacts:
  files:
    - target/app.jar
    - appspec.yml
    - scripts/**/*
  discard-paths: no

cache:
  paths:
    - '/root/.m2/**/*'

reports:
  junit-reports:
    files:
      - 'target/surefire-reports/*.xml'
    file-format: JUNITXML
```

### Angular Example

```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      nodejs: 18
    commands:
      - npm install

  pre_build:
    commands:
      - echo "Running linter..."
      - npm run lint

  build:
    commands:
      - echo "Running tests..."
      - npm run test -- --watch=false --browsers=ChromeHeadless
      - echo "Building production..."
      - npm run build -- --configuration=production

  post_build:
    commands:
      - echo "Build complete"

artifacts:
  files:
    - 'dist/**/*'
  base-directory: 'dist/my-angular-app'

cache:
  paths:
    - 'node_modules/**/*'
```

---

## Build Phases

Each phase runs sequentially. If any phase fails, the build stops.

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ INSTALL  │───▶│PRE_BUILD │───▶│  BUILD   │───▶│POST_BUILD│
└──────────┘    └──────────┘    └──────────┘    └──────────┘
     │               │               │               │
 Setup Java     Download        Compile         Package
 Install npm    dependencies    Run tests       Upload
```

### Phase Details

| Phase | Purpose | Example Commands |
|-------|---------|------------------|
| **install** | Setup runtime | `runtime-versions`, install tools |
| **pre_build** | Prepare | Download deps, login to registries |
| **build** | Main work | Compile, test, package |
| **post_build** | Cleanup | Rename files, notifications |

### finally Block

Runs regardless of build success/failure.

```yaml
phases:
  build:
    commands:
      - mvn test
    finally:
      - echo "Uploading test results..."
      - aws s3 cp target/surefire-reports s3://my-bucket/reports/ --recursive
```

---

## Environment Variables

### Types of Variables

```yaml
env:
  # Plain text (visible in logs)
  variables:
    BUILD_ENV: "production"
    LOG_LEVEL: "info"

  # From SSM Parameter Store (encrypted)
  parameter-store:
    DB_PASSWORD: "/myapp/db/password"

  # From Secrets Manager
  secrets-manager:
    API_KEY: "arn:aws:secretsmanager:us-east-1:123456789:secret:myapp/api-key"

  # Exported to subsequent build steps
  exported-variables:
    - BUILD_VERSION
```

### Built-in Variables

| Variable | Description |
|----------|-------------|
| `AWS_REGION` | Build region |
| `CODEBUILD_BUILD_ID` | Unique build ID |
| `CODEBUILD_BUILD_NUMBER` | Build number |
| `CODEBUILD_SOURCE_VERSION` | Commit SHA |
| `CODEBUILD_RESOLVED_SOURCE_VERSION` | Full commit SHA |

### Using Variables

```yaml
phases:
  build:
    commands:
      - echo "Building commit $CODEBUILD_RESOLVED_SOURCE_VERSION"
      - docker build -t myapp:$CODEBUILD_BUILD_NUMBER .
```

---

## Artifacts

### Basic Artifact Configuration

```yaml
artifacts:
  files:
    - target/*.jar
    - appspec.yml
    - scripts/**/*
  name: my-app-$(date +%Y-%m-%d)
  discard-paths: no
```

### Multiple Artifacts

```yaml
artifacts:
  secondary-artifacts:
    backend:
      files:
        - target/*.jar
      name: backend-artifact
    frontend:
      files:
        - dist/**/*
      name: frontend-artifact
      base-directory: dist
```

### Artifact Options

| Option | Description |
|--------|-------------|
| `files` | Files to include |
| `name` | Artifact name |
| `discard-paths` | Flatten directory structure |
| `base-directory` | Root for relative paths |

---

## Caching

Cache dependencies to speed up builds.

```yaml
cache:
  paths:
    # Maven
    - '/root/.m2/**/*'

    # Gradle
    - '/root/.gradle/caches/**/*'
    - '/root/.gradle/wrapper/**/*'

    # npm
    - 'node_modules/**/*'

    # pip
    - '/root/.cache/pip/**/*'
```

### Cache Behavior

```
First Build:                    Subsequent Builds:
┌──────────────────┐           ┌──────────────────┐
│ Download deps    │           │ Restore cache    │
│ (5 minutes)      │           │ (30 seconds)     │
└────────┬─────────┘           └────────┬─────────┘
         │                              │
         ▼                              ▼
┌──────────────────┐           ┌──────────────────┐
│ Save to cache    │           │ Build faster!    │
└──────────────────┘           └──────────────────┘
```

---

## Build Triggers

### Webhook (GitHub)

```bash
# CodeBuild creates webhook automatically when you connect GitHub
# Triggers on push to specified branches
```

### CodePipeline Trigger

```
GitHub Push → CodePipeline → CodeBuild
```

### Scheduled Builds (EventBridge)

```bash
# Create EventBridge rule for nightly builds
aws events put-rule \
  --name "NightlyBuild" \
  --schedule-expression "cron(0 2 * * ? *)"

aws events put-targets \
  --rule NightlyBuild \
  --targets "Id"="1","Arn"="arn:aws:codebuild:us-east-1:123456789:project/my-app-build"
```

---

## Docker Builds

### Build and Push to ECR

```yaml
version: 0.2

env:
  variables:
    AWS_ACCOUNT_ID: "123456789012"
    IMAGE_REPO_NAME: "my-app"
    IMAGE_TAG: "latest"

phases:
  pre_build:
    commands:
      - echo "Logging in to ECR..."
      - aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

  build:
    commands:
      - echo "Building Docker image..."
      - docker build -t $IMAGE_REPO_NAME:$IMAGE_TAG .
      - docker tag $IMAGE_REPO_NAME:$IMAGE_TAG $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$IMAGE_REPO_NAME:$IMAGE_TAG

  post_build:
    commands:
      - echo "Pushing to ECR..."
      - docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$IMAGE_REPO_NAME:$IMAGE_TAG
      - echo "Writing image URI to file..."
      - printf '{"ImageURI":"%s"}' $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$IMAGE_REPO_NAME:$IMAGE_TAG > imageDetail.json

artifacts:
  files:
    - imageDetail.json
```

### Dockerfile

```dockerfile
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Test Reports

### JUnit Reports

```yaml
reports:
  junit-reports:
    files:
      - 'target/surefire-reports/*.xml'
    file-format: JUNITXML

  coverage-reports:
    files:
      - 'target/site/jacoco/jacoco.xml'
    file-format: JACOCOXML
```

### View Reports

Reports appear in CodeBuild console under **Reports** tab.

---

## Build Environments

### Managed Images

| Image | Languages |
|-------|-----------|
| `amazonlinux2-x86_64-standard:4.0` | Java, Python, Node, Go, Ruby |
| `amazonlinux2-x86_64-standard:5.0` | Latest versions |

### Compute Types

| Type | vCPU | Memory | Use Case |
|------|------|--------|----------|
| `BUILD_GENERAL1_SMALL` | 3 | 3 GB | Small projects |
| `BUILD_GENERAL1_MEDIUM` | 7 | 7 GB | Medium projects |
| `BUILD_GENERAL1_LARGE` | 15 | 15 GB | Large projects |

### Custom Docker Image

```yaml
# Use your own Docker image
environment:
  type: LINUX_CONTAINER
  image: 123456789.dkr.ecr.us-east-1.amazonaws.com/my-build-image:latest
  computeType: BUILD_GENERAL1_SMALL
```

---

## Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| Build timeout | Increase timeout in project settings |
| Permission denied | Check IAM role permissions |
| Cache not working | Verify cache paths exist |
| Tests failing | Check test reports in console |

### View Build Logs

```bash
# Get recent builds
aws codebuild list-builds-for-project --project-name my-app-build

# Get build details
aws codebuild batch-get-builds --ids my-app-build:abc123

# Logs are in CloudWatch
# /aws/codebuild/my-app-build
```

### Debug Locally

```bash
# Use CodeBuild local agent
docker pull amazon/aws-codebuild-local

# Run build locally
./codebuild_build.sh -i aws/codebuild/amazonlinux2-x86_64-standard:4.0 -a ./artifacts
```

---

## Summary

### Key Files

| File | Purpose |
|------|---------|
| `buildspec.yml` | Build instructions |
| `Dockerfile` | Container build |

### Common Commands

```bash
# Start build
aws codebuild start-build --project-name my-app-build

# View builds
aws codebuild list-builds-for-project --project-name my-app-build

# Get build status
aws codebuild batch-get-builds --ids BUILD_ID
```

### Checklist

- [ ] Create CodeBuild project
- [ ] Write buildspec.yml
- [ ] Configure IAM role
- [ ] Set up artifacts bucket
- [ ] Configure caching
- [ ] Test build locally

## Next Topic

Continue to [CodePipeline](./04-codepipeline.md) to create complete CI/CD pipelines.

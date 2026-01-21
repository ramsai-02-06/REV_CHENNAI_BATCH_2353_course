# AWS CodePipeline

## What is CodePipeline?

CodePipeline is a continuous delivery service that automates the build, test, and deploy phases of your release process.

```
┌─────────────────────────────────────────────────────────────┐
│                      CodePipeline                            │
├──────────────┬──────────────┬──────────────┬───────────────┤
│   Source     │    Build     │    Test      │    Deploy     │
│   Stage      │    Stage     │    Stage     │    Stage      │
├──────────────┼──────────────┼──────────────┼───────────────┤
│   GitHub     │  CodeBuild   │  CodeBuild   │  CodeDeploy   │
│   webhook    │  compile     │  test        │  EC2          │
└──────────────┴──────────────┴──────────────┴───────────────┘
                              │
                    Automatic flow on commit
```

---

## Pipeline Structure

### Stages and Actions

```
Pipeline
├── Stage: Source
│   └── Action: GitHub Source
│
├── Stage: Build
│   └── Action: CodeBuild Project
│
├── Stage: Test
│   └── Action: CodeBuild Test Project
│
└── Stage: Deploy
    ├── Action: Deploy to Staging
    └── Action: Deploy to Production
```

### Parallel vs Sequential Actions

```
Sequential (default):
┌───────┐    ┌───────┐    ┌───────┐
│ Build │───▶│ Test  │───▶│Deploy │
└───────┘    └───────┘    └───────┘

Parallel (same runOrder):
            ┌───────────┐
       ┌───▶│Unit Tests │
┌─────┐│    └───────────┘
│Build├┤
└─────┘│    ┌───────────┐
       └───▶│Int Tests  │
            └───────────┘
```

---

## Creating a Pipeline

### Via Console (Recommended for Learning)

1. **CodePipeline** → **Create pipeline**
2. **Pipeline settings**:
   - Name: my-app-pipeline
   - Service role: New or existing
3. **Source stage**:
   - Provider: GitHub (Version 2)
   - Connection: Create or use existing
   - Repository: your-repo
   - Branch: main
4. **Build stage**:
   - Provider: AWS CodeBuild
   - Project: Select or create
5. **Deploy stage**:
   - Provider: AWS CodeDeploy
   - Application: Select or create

### Via CLI

```bash
aws codepipeline create-pipeline --cli-input-json file://pipeline.json
```

---

## Complete Pipeline Example

### Project Structure

```
my-spring-app/
├── src/
│   └── main/java/...
├── scripts/
│   ├── start.sh
│   ├── stop.sh
│   └── validate.sh
├── pom.xml
├── buildspec.yml      # CodeBuild
├── appspec.yml        # CodeDeploy
└── README.md
```

### buildspec.yml

```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      java: corretto17

  pre_build:
    commands:
      - echo "Running tests..."
      - mvn test

  build:
    commands:
      - echo "Building JAR..."
      - mvn package -DskipTests
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
```

### appspec.yml

```yaml
version: 0.0
os: linux

files:
  - source: target/app.jar
    destination: /opt/myapp/

permissions:
  - object: /opt/myapp
    owner: ec2-user
    group: ec2-user
    mode: 755

hooks:
  ApplicationStop:
    - location: scripts/stop.sh
      timeout: 60
      runas: root

  AfterInstall:
    - location: scripts/after_install.sh
      timeout: 60
      runas: root

  ApplicationStart:
    - location: scripts/start.sh
      timeout: 60
      runas: ec2-user

  ValidateService:
    - location: scripts/validate.sh
      timeout: 120
      runas: ec2-user
```

### Deployment Scripts

**scripts/stop.sh**
```bash
#!/bin/bash
echo "Stopping application..."
sudo systemctl stop myapp || true
echo "Application stopped"
```

**scripts/after_install.sh**
```bash
#!/bin/bash
echo "Setting permissions..."
sudo chown -R ec2-user:ec2-user /opt/myapp
sudo chmod +x /opt/myapp/app.jar
```

**scripts/start.sh**
```bash
#!/bin/bash
echo "Starting application..."
sudo systemctl daemon-reload
sudo systemctl start myapp
echo "Application started"
```

**scripts/validate.sh**
```bash
#!/bin/bash
echo "Validating deployment..."
sleep 30

# Check if service is running
if ! systemctl is-active --quiet myapp; then
    echo "Service is not running!"
    exit 1
fi

# Check health endpoint
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health)
if [ "$HTTP_STATUS" != "200" ]; then
    echo "Health check failed with status: $HTTP_STATUS"
    exit 1
fi

echo "Deployment validated successfully!"
```

---

## Pipeline Configuration (JSON)

```json
{
  "pipeline": {
    "name": "my-spring-app-pipeline",
    "roleArn": "arn:aws:iam::123456789:role/CodePipelineServiceRole",
    "stages": [
      {
        "name": "Source",
        "actions": [
          {
            "name": "GitHub_Source",
            "actionTypeId": {
              "category": "Source",
              "owner": "AWS",
              "provider": "CodeStarSourceConnection",
              "version": "1"
            },
            "configuration": {
              "ConnectionArn": "arn:aws:codestar-connections:us-east-1:123456789:connection/abc123",
              "FullRepositoryId": "username/my-spring-app",
              "BranchName": "main"
            },
            "outputArtifacts": [
              {"name": "SourceOutput"}
            ]
          }
        ]
      },
      {
        "name": "Build",
        "actions": [
          {
            "name": "Build",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "configuration": {
              "ProjectName": "my-spring-app-build"
            },
            "inputArtifacts": [
              {"name": "SourceOutput"}
            ],
            "outputArtifacts": [
              {"name": "BuildOutput"}
            ]
          }
        ]
      },
      {
        "name": "Deploy",
        "actions": [
          {
            "name": "Deploy_to_EC2",
            "actionTypeId": {
              "category": "Deploy",
              "owner": "AWS",
              "provider": "CodeDeploy",
              "version": "1"
            },
            "configuration": {
              "ApplicationName": "my-spring-app",
              "DeploymentGroupName": "production"
            },
            "inputArtifacts": [
              {"name": "BuildOutput"}
            ]
          }
        ]
      }
    ],
    "artifactStore": {
      "type": "S3",
      "location": "my-pipeline-artifacts-bucket"
    }
  }
}
```

---

## Multi-Environment Pipeline

### Staging → Production

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  Source  │───▶│  Build   │───▶│ Deploy   │───▶│ Approval │───▶│ Deploy   │
│          │    │          │    │ Staging  │    │          │    │Production│
└──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘
```

### Pipeline with Approval

```json
{
  "name": "Approval",
  "actions": [
    {
      "name": "Manual_Approval",
      "actionTypeId": {
        "category": "Approval",
        "owner": "AWS",
        "provider": "Manual",
        "version": "1"
      },
      "configuration": {
        "NotificationArn": "arn:aws:sns:us-east-1:123456789:deployment-approval",
        "CustomData": "Please review staging before production deployment"
      }
    }
  ]
}
```

### Environment-Specific Deployments

```json
{
  "name": "Deploy",
  "actions": [
    {
      "name": "Deploy_Staging",
      "actionTypeId": {
        "category": "Deploy",
        "owner": "AWS",
        "provider": "CodeDeploy",
        "version": "1"
      },
      "configuration": {
        "ApplicationName": "my-app",
        "DeploymentGroupName": "staging"
      },
      "runOrder": 1
    },
    {
      "name": "Deploy_Production",
      "actionTypeId": {
        "category": "Deploy",
        "owner": "AWS",
        "provider": "CodeDeploy",
        "version": "1"
      },
      "configuration": {
        "ApplicationName": "my-app",
        "DeploymentGroupName": "production"
      },
      "runOrder": 2
    }
  ]
}
```

---

## Deploy to S3 (Angular)

### Pipeline for Static Website

```
┌──────────┐    ┌──────────┐    ┌──────────┐
│  Source  │───▶│  Build   │───▶│Deploy S3 │
│ (GitHub) │    │(ng build)│    │          │
└──────────┘    └──────────┘    └──────────┘
```

### buildspec.yml (Angular)

```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      nodejs: 18
    commands:
      - npm install

  build:
    commands:
      - npm run build -- --configuration=production

artifacts:
  files:
    - '**/*'
  base-directory: 'dist/my-angular-app'
```

### S3 Deploy Action

```json
{
  "name": "Deploy_to_S3",
  "actionTypeId": {
    "category": "Deploy",
    "owner": "AWS",
    "provider": "S3",
    "version": "1"
  },
  "configuration": {
    "BucketName": "my-angular-app-bucket",
    "Extract": "true"
  },
  "inputArtifacts": [
    {"name": "BuildOutput"}
  ]
}
```

---

## Notifications

### SNS Notifications

```bash
# Create SNS topic
aws sns create-topic --name pipeline-notifications

# Subscribe email
aws sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:123456789:pipeline-notifications \
  --protocol email \
  --notification-endpoint your-email@example.com
```

### EventBridge Rule

```bash
# Create rule for pipeline failures
aws events put-rule \
  --name "PipelineFailure" \
  --event-pattern '{
    "source": ["aws.codepipeline"],
    "detail-type": ["CodePipeline Pipeline Execution State Change"],
    "detail": {
      "state": ["FAILED"]
    }
  }'

# Add SNS target
aws events put-targets \
  --rule PipelineFailure \
  --targets "Id"="1","Arn"="arn:aws:sns:us-east-1:123456789:pipeline-notifications"
```

---

## IAM Roles

### CodePipeline Service Role

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "codecommit:*",
        "codebuild:*",
        "codedeploy:*",
        "s3:*",
        "iam:PassRole"
      ],
      "Resource": "*"
    }
  ]
}
```

### Trust Policy

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "codepipeline.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

---

## Pipeline Commands

### Common Operations

```bash
# List pipelines
aws codepipeline list-pipelines

# Get pipeline details
aws codepipeline get-pipeline --name my-app-pipeline

# Get pipeline state
aws codepipeline get-pipeline-state --name my-app-pipeline

# Start pipeline manually
aws codepipeline start-pipeline-execution --name my-app-pipeline

# Stop execution
aws codepipeline stop-pipeline-execution \
  --pipeline-name my-app-pipeline \
  --pipeline-execution-id abc123 \
  --abandon

# Retry failed stage
aws codepipeline retry-stage-execution \
  --pipeline-name my-app-pipeline \
  --stage-name Deploy \
  --pipeline-execution-id abc123 \
  --retry-mode FAILED_ACTIONS
```

### Update Pipeline

```bash
# Export current pipeline
aws codepipeline get-pipeline --name my-app-pipeline > pipeline.json

# Edit pipeline.json, then update
aws codepipeline update-pipeline --cli-input-json file://pipeline.json
```

---

## Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| Source not triggering | Check GitHub connection status |
| Build failing | Check CodeBuild logs |
| Deploy failing | Check CodeDeploy events |
| Permission errors | Verify IAM roles |

### View Logs

```bash
# Pipeline execution history
aws codepipeline list-pipeline-executions --pipeline-name my-app-pipeline

# Get execution details
aws codepipeline get-pipeline-execution \
  --pipeline-name my-app-pipeline \
  --pipeline-execution-id abc123
```

### Debugging Steps

1. **Check Pipeline State**: Console shows where it failed
2. **Check Action Details**: Click on failed action
3. **View CloudWatch Logs**: For CodeBuild failures
4. **Check CodeDeploy Events**: For deployment failures

---

## Summary

### Pipeline Flow

```
1. Developer pushes to GitHub
2. CodePipeline detects change
3. CodeBuild compiles and tests
4. CodeDeploy deploys to EC2
5. Validation confirms success
```

### Key Components

| Component | File | Purpose |
|-----------|------|---------|
| Build | `buildspec.yml` | Compile, test, package |
| Deploy | `appspec.yml` | Deploy to EC2 |
| Scripts | `scripts/` | Start/stop/validate |

### Checklist

- [ ] Create CodeBuild project
- [ ] Create CodeDeploy application
- [ ] Create CodePipeline
- [ ] Configure GitHub connection
- [ ] Test pipeline execution
- [ ] Set up notifications

### Quick Reference

```bash
# Start pipeline
aws codepipeline start-pipeline-execution --name my-pipeline

# Check status
aws codepipeline get-pipeline-state --name my-pipeline

# View history
aws codepipeline list-pipeline-executions --name my-pipeline
```

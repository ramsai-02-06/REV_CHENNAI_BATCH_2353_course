# DevOps Introduction

## What is DevOps?

DevOps combines software development (Dev) and IT operations (Ops) to shorten the development lifecycle while delivering features and updates frequently.

### Core Principles

```
Traditional                     DevOps
───────────                     ──────
Dev and Ops separate    →       Unified teams
Manual deployments      →       Automated pipelines
Deploy monthly          →       Deploy daily/hourly
Fix issues slowly       →       Fast feedback loops
```

**Key Principles:**
- **Collaboration**: Dev and Ops work together
- **Automation**: Automate builds, tests, deployments
- **Continuous Improvement**: Iterate and improve processes
- **Fast Feedback**: Quick detection and resolution of issues

---

## CI/CD Pipeline

CI/CD automates the process of building, testing, and deploying applications.

### Pipeline Overview

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  Source  │───▶│  Build   │───▶│   Test   │───▶│  Deploy  │
│   Code   │    │          │    │          │    │          │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
     │               │               │               │
   GitHub       CodeBuild       Automated        CodeDeploy
   Push          Compile         Tests           to EC2/S3
```

### Continuous Integration (CI)

Developers frequently merge code into a shared repository with automated builds and tests.

**Benefits:**
- Early detection of bugs
- Reduced integration problems
- Faster development cycle

### Continuous Delivery/Deployment (CD)

Code is automatically deployed to staging/production after passing tests.

**Continuous Delivery**: Deploy manually with one click
**Continuous Deployment**: Fully automated deployment

---

## AWS CI/CD Services

AWS provides managed services for the entire CI/CD pipeline.

### Service Overview

| Service | Purpose | Comparison |
|---------|---------|------------|
| **CodeCommit** | Git repository hosting | Like GitHub |
| **CodeBuild** | Build and test | Like Jenkins |
| **CodeDeploy** | Deployment automation | Deploy to EC2, Lambda |
| **CodePipeline** | Pipeline orchestration | Connects all services |

### How They Work Together

```
┌─────────────────────────────────────────────────────────────┐
│                     AWS CodePipeline                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│   ┌──────────┐    ┌──────────┐    ┌──────────┐             │
│   │CodeCommit│───▶│CodeBuild │───▶│CodeDeploy│             │
│   │ (Source) │    │ (Build)  │    │ (Deploy) │             │
│   └──────────┘    └──────────┘    └──────────┘             │
│        │               │               │                    │
│     Git push       Build JAR      Deploy to                │
│     triggers       Run tests      EC2/S3                   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Pipeline Stages Explained

### 1. Source Stage

Code changes trigger the pipeline.

**Supported Sources:**
- AWS CodeCommit
- GitHub
- Bitbucket
- S3 bucket

```yaml
# Example: GitHub webhook triggers pipeline
Source:
  Provider: GitHub
  Repository: my-app
  Branch: main
```

### 2. Build Stage

Compile code, run tests, create artifacts.

```yaml
# buildspec.yml for CodeBuild
version: 0.2
phases:
  install:
    runtime-versions:
      java: corretto17
  build:
    commands:
      - mvn clean package
artifacts:
  files:
    - target/*.jar
```

### 3. Test Stage

Run automated tests to validate code.

**Test Types:**
- **Unit Tests**: Test individual methods
- **Integration Tests**: Test component interactions
- **Security Scans**: Check for vulnerabilities

### 4. Deploy Stage

Deploy artifacts to target environment.

**Deployment Targets:**
- EC2 instances
- S3 buckets (static websites)
- Elastic Beanstalk
- ECS/EKS containers
- Lambda functions

---

## Simple Pipeline Example

### Spring Boot App Pipeline

```
Developer pushes code to GitHub
        │
        ▼
┌─────────────────┐
│ CodePipeline    │
│ detects change  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ CodeBuild       │
│ - mvn test      │
│ - mvn package   │
│ - Create JAR    │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ CodeDeploy      │
│ - Stop old app  │
│ - Deploy JAR    │
│ - Start new app │
└─────────────────┘
```

### buildspec.yml (CodeBuild)

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
      - echo "Building application..."
      - mvn clean package -DskipTests

artifacts:
  files:
    - target/*.jar
    - scripts/*
  discard-paths: no
```

### appspec.yml (CodeDeploy)

```yaml
version: 0.0
os: linux

files:
  - source: target/app.jar
    destination: /opt/myapp/

hooks:
  ApplicationStop:
    - location: scripts/stop.sh
      timeout: 60

  ApplicationStart:
    - location: scripts/start.sh
      timeout: 60
```

---

## DevOps Best Practices

### 1. Version Control Everything

```
Repository should contain:
├── src/                    # Application code
├── buildspec.yml           # Build configuration
├── appspec.yml            # Deployment configuration
├── scripts/               # Deployment scripts
│   ├── start.sh
│   └── stop.sh
└── tests/                 # Automated tests
```

### 2. Automate Everything

| Manual Process | Automated Alternative |
|----------------|----------------------|
| Build locally | CodeBuild |
| Copy files to server | CodeDeploy |
| Run tests manually | Pipeline test stage |
| Deploy on Friday evening | Continuous deployment |

### 3. Fail Fast

Run quick checks first, expensive ones later:

```
1. Lint/syntax check (seconds)
2. Unit tests (minutes)
3. Build artifact (minutes)
4. Integration tests (longer)
5. Deploy to staging
6. Deploy to production
```

### 4. Keep Pipelines Fast

Target: Complete pipeline in under 10 minutes.

**Tips:**
- Cache dependencies
- Run tests in parallel
- Use appropriate instance sizes
- Skip unnecessary steps

### 5. Secure Your Pipeline

- Never hardcode credentials
- Use IAM roles for services
- Encrypt artifacts
- Scan for vulnerabilities

---

## Environment Strategy

### Multiple Environments

```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ Development  │───▶│   Staging    │───▶│  Production  │
│              │    │              │    │              │
│ - Latest code│    │ - Test env   │    │ - Live users │
│ - Unstable   │    │ - QA testing │    │ - Stable     │
└──────────────┘    └──────────────┘    └──────────────┘
```

### Branch Strategy

```
main (production)
  │
  └── develop (staging)
        │
        ├── feature/user-auth
        │
        └── feature/payment
```

**Pipeline triggers:**
- `feature/*` → Build and test only
- `develop` → Deploy to staging
- `main` → Deploy to production

---

## Monitoring and Feedback

### Pipeline Monitoring

```
CloudWatch Metrics:
├── Build success/failure rate
├── Build duration
├── Deploy success rate
└── Pipeline execution time
```

### Notifications

```yaml
# SNS notification on failure
Pipeline:
  Notifications:
    - Event: FAILED
      Target: sns-topic-arn
      Message: "Pipeline failed!"
```

### Rollback Strategy

Always have a way to roll back:

```bash
# Manual rollback with CodeDeploy
aws deploy create-deployment \
  --application-name MyApp \
  --deployment-group-name Production \
  --revision revisionType=S3,... \
  --description "Rollback to previous version"
```

---

## Summary

| Concept | Description |
|---------|-------------|
| **DevOps** | Culture of Dev + Ops collaboration |
| **CI** | Continuous Integration - automated build/test |
| **CD** | Continuous Delivery/Deployment |
| **Pipeline** | Automated workflow: Source → Build → Test → Deploy |

### AWS CI/CD Stack

| Service | Role |
|---------|------|
| CodeCommit | Source code repository |
| CodeBuild | Build and test |
| CodeDeploy | Deploy to servers |
| CodePipeline | Orchestrate the pipeline |

### Key Files

| File | Purpose |
|------|---------|
| `buildspec.yml` | CodeBuild instructions |
| `appspec.yml` | CodeDeploy instructions |

## Next Topic

Continue to [AWS CI/CD Tools](./02-aws-cicd-tools.md) to learn about AWS DevOps services in detail.

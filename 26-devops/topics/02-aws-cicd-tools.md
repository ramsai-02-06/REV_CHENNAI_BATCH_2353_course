# AWS CI/CD Tools

## Overview

AWS provides a suite of developer tools for building CI/CD pipelines. These services integrate seamlessly with each other and with your existing tools.

```
┌─────────────────────────────────────────────────────────────┐
│                    AWS Developer Tools                       │
├─────────────────────────────────────────────────────────────┤
│  Source          │  Build          │  Deploy                │
│                  │                 │                        │
│  CodeCommit      │  CodeBuild      │  CodeDeploy           │
│  (Git repos)     │  (CI server)    │  (Deployment)         │
│                  │                 │                        │
│  + GitHub        │                 │  Targets:             │
│  + Bitbucket     │                 │  - EC2                │
│  + S3            │                 │  - Lambda             │
│                  │                 │  - ECS                │
├─────────────────────────────────────────────────────────────┤
│                      CodePipeline                            │
│              (Orchestrates the entire workflow)              │
└─────────────────────────────────────────────────────────────┘
```

---

## CodeCommit

AWS-hosted Git repository service.

### Why CodeCommit?

| Feature | Benefit |
|---------|---------|
| Fully managed | No servers to maintain |
| Secure | Encrypted at rest and in transit |
| Scalable | Handle any repository size |
| IAM integration | Fine-grained access control |
| Free tier | 5 active users, 50 GB storage |

### Create Repository

```bash
# Create repository
aws codecommit create-repository \
  --repository-name my-app \
  --repository-description "My application"

# Clone repository (HTTPS)
git clone https://git-codecommit.us-east-1.amazonaws.com/v1/repos/my-app

# Clone repository (SSH)
git clone ssh://git-codecommit.us-east-1.amazonaws.com/v1/repos/my-app
```

### Setup Git Credentials

**Option 1: HTTPS with Git Credentials**

```bash
# IAM → Users → Security credentials → HTTPS Git credentials
# Generate credentials and configure git
git config --global credential.helper '!aws codecommit credential-helper $@'
git config --global credential.UseHttpPath true
```

**Option 2: SSH Keys**

```bash
# Upload SSH public key to IAM
aws iam upload-ssh-public-key \
  --user-name your-user \
  --ssh-public-key-body file://~/.ssh/id_rsa.pub
```

### Basic Operations

```bash
# Clone
git clone https://git-codecommit.us-east-1.amazonaws.com/v1/repos/my-app
cd my-app

# Add files
echo "# My App" > README.md
git add .
git commit -m "Initial commit"
git push origin main

# Create branch
git checkout -b feature/new-feature
git push -u origin feature/new-feature
```

---

## CodeBuild

Fully managed build service that compiles source code, runs tests, and produces artifacts.

### Why CodeBuild?

| Feature | Benefit |
|---------|---------|
| No servers | Scales automatically |
| Pay per use | Only pay for build minutes |
| Pre-configured | Java, Node, Python, etc. |
| Custom images | Use your own Docker images |
| Parallel builds | Run multiple builds simultaneously |

### Build Project Setup

```bash
# Create build project via CLI
aws codebuild create-project \
  --name my-app-build \
  --source type=GITHUB,location=https://github.com/user/my-app \
  --environment type=LINUX_CONTAINER,computeType=BUILD_GENERAL1_SMALL,image=aws/codebuild/amazonlinux2-x86_64-standard:4.0 \
  --service-role arn:aws:iam::123456789:role/CodeBuildServiceRole \
  --artifacts type=S3,location=my-artifacts-bucket
```

### buildspec.yml

The build specification file defines build commands.

```yaml
version: 0.2

env:
  variables:
    JAVA_HOME: "/usr/lib/jvm/java-17-amazon-corretto"

phases:
  install:
    runtime-versions:
      java: corretto17

  pre_build:
    commands:
      - echo "Installing dependencies..."
      - mvn dependency:resolve

  build:
    commands:
      - echo "Running tests..."
      - mvn test
      - echo "Building JAR..."
      - mvn package -DskipTests

  post_build:
    commands:
      - echo "Build completed on $(date)"

artifacts:
  files:
    - target/*.jar
    - appspec.yml
    - scripts/**/*
  discard-paths: no

cache:
  paths:
    - '/root/.m2/**/*'
```

### Build Phases

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ INSTALL  │───▶│PRE_BUILD │───▶│  BUILD   │───▶│POST_BUILD│
│          │    │          │    │          │    │          │
│ Setup    │    │ Prepare  │    │ Compile  │    │ Cleanup  │
│ runtime  │    │ deps     │    │ test     │    │ report   │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
```

### Environment Variables

```yaml
env:
  variables:
    # Plain text variables
    BUILD_ENV: "production"

  parameter-store:
    # From SSM Parameter Store
    DB_PASSWORD: "/myapp/db/password"

  secrets-manager:
    # From Secrets Manager
    API_KEY: "myapp/api-key"
```

### Start Build Manually

```bash
# Start a build
aws codebuild start-build --project-name my-app-build

# Start with environment variable override
aws codebuild start-build \
  --project-name my-app-build \
  --environment-variables-override name=BUILD_ENV,value=staging
```

---

## CodeDeploy

Automates application deployments to EC2, Lambda, or ECS.

### Why CodeDeploy?

| Feature | Benefit |
|---------|---------|
| Zero downtime | Rolling or blue/green deployments |
| Rollback | Automatic rollback on failure |
| Health checks | Monitor deployment health |
| Hooks | Run scripts at each stage |

### Deployment Types

```
In-Place Deployment:
┌─────────┐    ┌─────────┐    ┌─────────┐
│ Stop    │───▶│ Deploy  │───▶│ Start   │
│ App v1  │    │ App v2  │    │ App v2  │
└─────────┘    └─────────┘    └─────────┘
(Brief downtime)

Blue/Green Deployment:
┌─────────┐              ┌─────────┐
│ Blue    │  Traffic ───▶│ Green   │
│ (v1)    │  switch      │ (v2)    │
└─────────┘              └─────────┘
(Zero downtime)
```

### appspec.yml

Deployment specification for EC2/on-premises.

```yaml
version: 0.0
os: linux

files:
  - source: /
    destination: /opt/myapp

permissions:
  - object: /opt/myapp
    owner: ec2-user
    group: ec2-user
    mode: 755

hooks:
  BeforeInstall:
    - location: scripts/before_install.sh
      timeout: 300
      runas: root

  AfterInstall:
    - location: scripts/after_install.sh
      timeout: 300
      runas: root

  ApplicationStart:
    - location: scripts/start.sh
      timeout: 300
      runas: ec2-user

  ApplicationStop:
    - location: scripts/stop.sh
      timeout: 300
      runas: ec2-user

  ValidateService:
    - location: scripts/validate.sh
      timeout: 300
      runas: ec2-user
```

### Deployment Lifecycle

```
┌────────────────────┐
│   ApplicationStop  │  Stop current version
└─────────┬──────────┘
          ▼
┌────────────────────┐
│   BeforeInstall    │  Cleanup, backup
└─────────┬──────────┘
          ▼
┌────────────────────┐
│   Install (files)  │  Copy new files
└─────────┬──────────┘
          ▼
┌────────────────────┐
│   AfterInstall     │  Configure, set permissions
└─────────┬──────────┘
          ▼
┌────────────────────┐
│   ApplicationStart │  Start new version
└─────────┬──────────┘
          ▼
┌────────────────────┐
│   ValidateService  │  Health check
└────────────────────┘
```

### Deployment Scripts

**scripts/stop.sh**
```bash
#!/bin/bash
sudo systemctl stop myapp || true
```

**scripts/start.sh**
```bash
#!/bin/bash
sudo systemctl start myapp
```

**scripts/validate.sh**
```bash
#!/bin/bash
sleep 10
curl -f http://localhost:8080/health || exit 1
```

### Setup CodeDeploy Agent on EC2

```bash
# Install CodeDeploy agent (Amazon Linux 2)
sudo yum install -y ruby wget
cd /home/ec2-user
wget https://aws-codedeploy-us-east-1.s3.amazonaws.com/latest/install
chmod +x ./install
sudo ./install auto
sudo systemctl start codedeploy-agent
sudo systemctl enable codedeploy-agent
```

---

## CodePipeline

Orchestrates the entire CI/CD workflow.

### Pipeline Structure

```
┌─────────────────────────────────────────────────────────────┐
│                       CodePipeline                           │
├──────────────┬──────────────┬──────────────┬───────────────┤
│   Source     │    Build     │    Test      │    Deploy     │
│   Stage      │    Stage     │    Stage     │    Stage      │
├──────────────┼──────────────┼──────────────┼───────────────┤
│   GitHub     │   CodeBuild  │   CodeBuild  │  CodeDeploy   │
│   (trigger)  │   (compile)  │   (test)     │  (EC2)        │
└──────────────┴──────────────┴──────────────┴───────────────┘
```

### Create Pipeline (Console)

1. **Source Stage**: Connect to GitHub/CodeCommit
2. **Build Stage**: Select CodeBuild project
3. **Deploy Stage**: Select CodeDeploy application
4. **Review and Create**

### Create Pipeline (CLI)

```bash
aws codepipeline create-pipeline --cli-input-json file://pipeline.json
```

**pipeline.json**
```json
{
  "pipeline": {
    "name": "MyAppPipeline",
    "roleArn": "arn:aws:iam::123456789:role/CodePipelineServiceRole",
    "stages": [
      {
        "name": "Source",
        "actions": [
          {
            "name": "SourceAction",
            "actionTypeId": {
              "category": "Source",
              "owner": "ThirdParty",
              "provider": "GitHub",
              "version": "1"
            },
            "configuration": {
              "Owner": "username",
              "Repo": "my-app",
              "Branch": "main",
              "OAuthToken": "{{resolve:secretsmanager:github-token}}"
            },
            "outputArtifacts": [{"name": "SourceOutput"}]
          }
        ]
      },
      {
        "name": "Build",
        "actions": [
          {
            "name": "BuildAction",
            "actionTypeId": {
              "category": "Build",
              "owner": "AWS",
              "provider": "CodeBuild",
              "version": "1"
            },
            "configuration": {
              "ProjectName": "my-app-build"
            },
            "inputArtifacts": [{"name": "SourceOutput"}],
            "outputArtifacts": [{"name": "BuildOutput"}]
          }
        ]
      },
      {
        "name": "Deploy",
        "actions": [
          {
            "name": "DeployAction",
            "actionTypeId": {
              "category": "Deploy",
              "owner": "AWS",
              "provider": "CodeDeploy",
              "version": "1"
            },
            "configuration": {
              "ApplicationName": "MyApp",
              "DeploymentGroupName": "Production"
            },
            "inputArtifacts": [{"name": "BuildOutput"}]
          }
        ]
      }
    ],
    "artifactStore": {
      "type": "S3",
      "location": "my-pipeline-artifacts"
    }
  }
}
```

### Pipeline Commands

```bash
# List pipelines
aws codepipeline list-pipelines

# Get pipeline state
aws codepipeline get-pipeline-state --name MyAppPipeline

# Start pipeline execution
aws codepipeline start-pipeline-execution --name MyAppPipeline

# Stop execution
aws codepipeline stop-pipeline-execution \
  --pipeline-name MyAppPipeline \
  --pipeline-execution-id abc123
```

---

## IAM Roles

Each service needs appropriate IAM permissions.

### CodeBuild Service Role

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject"
      ],
      "Resource": "arn:aws:s3:::my-artifacts-bucket/*"
    }
  ]
}
```

### CodeDeploy Service Role

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:Describe*",
        "autoscaling:*",
        "s3:Get*"
      ],
      "Resource": "*"
    }
  ]
}
```

### EC2 Instance Role (for CodeDeploy)

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:GetObjectVersion"
      ],
      "Resource": "arn:aws:s3:::my-artifacts-bucket/*"
    }
  ]
}
```

---

## Summary

| Service | Purpose | Key File |
|---------|---------|----------|
| **CodeCommit** | Git repository | - |
| **CodeBuild** | Build and test | `buildspec.yml` |
| **CodeDeploy** | Deploy to servers | `appspec.yml` |
| **CodePipeline** | Orchestrate workflow | Pipeline config |

### Quick Reference

```bash
# CodeCommit
aws codecommit create-repository --repository-name my-app

# CodeBuild
aws codebuild start-build --project-name my-build

# CodeDeploy
aws deploy create-deployment --application-name MyApp ...

# CodePipeline
aws codepipeline start-pipeline-execution --name MyPipeline
```

## Next Topic

Continue to [CodeBuild Deep Dive](./03-codebuild.md) for detailed build configurations.

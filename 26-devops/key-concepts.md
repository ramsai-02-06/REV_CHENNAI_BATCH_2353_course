# DevOps Key Concepts

## Overview

Quick reference for AWS CI/CD services and DevOps practices.

---

## 1. DevOps Fundamentals

### CI/CD Pipeline

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│  Source  │───▶│  Build   │───▶│   Test   │───▶│  Deploy  │
└──────────┘    └──────────┘    └──────────┘    └──────────┘
   GitHub       CodeBuild       CodeBuild       CodeDeploy
```

### Key Principles

| Principle | Description |
|-----------|-------------|
| **Automation** | Automate builds, tests, deployments |
| **Fast Feedback** | Know quickly if something breaks |
| **Version Control** | Everything in Git |
| **Continuous Improvement** | Iterate and improve |

---

## 2. AWS CI/CD Services

| Service | Purpose | Key File |
|---------|---------|----------|
| **CodeCommit** | Git repository | - |
| **CodeBuild** | Build & test | `buildspec.yml` |
| **CodeDeploy** | Deploy to EC2 | `appspec.yml` |
| **CodePipeline** | Orchestration | Pipeline config |

---

## 3. CodeBuild

### buildspec.yml Structure

```yaml
version: 0.2

phases:
  install:
    runtime-versions:
      java: corretto17

  pre_build:
    commands:
      - mvn dependency:resolve

  build:
    commands:
      - mvn test
      - mvn package -DskipTests

artifacts:
  files:
    - target/*.jar
    - appspec.yml
    - scripts/**/*

cache:
  paths:
    - '/root/.m2/**/*'
```

### Build Phases

```
INSTALL → PRE_BUILD → BUILD → POST_BUILD
   │          │          │         │
 Setup     Prepare    Compile   Cleanup
 Java      deps       test
```

### Environment Variables

```yaml
env:
  variables:
    BUILD_ENV: "production"
  parameter-store:
    DB_PASSWORD: "/myapp/db/password"
```

---

## 4. CodeDeploy

### appspec.yml Structure

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

  ValidateService:
    - location: scripts/validate.sh
      timeout: 120
```

### Deployment Lifecycle

```
ApplicationStop → BeforeInstall → Install → AfterInstall → ApplicationStart → ValidateService
```

### Deployment Scripts

**stop.sh**
```bash
#!/bin/bash
sudo systemctl stop myapp || true
```

**start.sh**
```bash
#!/bin/bash
sudo systemctl start myapp
```

**validate.sh**
```bash
#!/bin/bash
sleep 30
curl -f http://localhost:8080/actuator/health || exit 1
```

---

## 5. CodePipeline

### Pipeline Structure

```
Pipeline
├── Source Stage (GitHub)
├── Build Stage (CodeBuild)
├── [Optional] Approval Stage
└── Deploy Stage (CodeDeploy)
```

### Common Commands

```bash
# Start pipeline
aws codepipeline start-pipeline-execution --name my-pipeline

# Check status
aws codepipeline get-pipeline-state --name my-pipeline

# View history
aws codepipeline list-pipeline-executions --name my-pipeline
```

---

## Quick Reference

### Project Structure

```
my-app/
├── src/
├── pom.xml
├── buildspec.yml      # CodeBuild
├── appspec.yml        # CodeDeploy
└── scripts/
    ├── start.sh
    ├── stop.sh
    └── validate.sh
```

### Key Commands

```bash
# CodeBuild
aws codebuild start-build --project-name my-build

# CodeDeploy
aws deploy create-deployment --application-name MyApp ...

# CodePipeline
aws codepipeline start-pipeline-execution --name my-pipeline
```

---

## Checklist

### Initial Setup
- [ ] Create CodeBuild project
- [ ] Create CodeDeploy application
- [ ] Create CodeDeploy deployment group
- [ ] Install CodeDeploy agent on EC2
- [ ] Create CodePipeline
- [ ] Connect GitHub

### Per Project
- [ ] Add buildspec.yml
- [ ] Add appspec.yml
- [ ] Create deployment scripts
- [ ] Configure IAM roles
- [ ] Test pipeline

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Build fails | Check CloudWatch logs for CodeBuild |
| Deploy fails | Check CodeDeploy events in console |
| Pipeline stuck | Check action details in CodePipeline |
| Permission denied | Verify IAM roles |
| Agent not running | Restart CodeDeploy agent on EC2 |

### Useful Commands

```bash
# CodeDeploy agent status
sudo systemctl status codedeploy-agent

# CodeBuild logs
# CloudWatch → /aws/codebuild/project-name

# CodeDeploy logs on EC2
tail -f /var/log/aws/codedeploy-agent/codedeploy-agent.log
```

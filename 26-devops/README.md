# DevOps and CI/CD

## Overview

DevOps practices and CI/CD pipelines for automated build, test, and deployment using AWS services.

## Learning Objectives

By the end of this module, you will be able to:
- Understand DevOps principles and CI/CD concepts
- Set up AWS CI/CD services (CodeBuild, CodeDeploy, CodePipeline)
- Create automated build and test pipelines
- Deploy applications to EC2 using CodeDeploy
- Configure complete CI/CD workflows

---

## Topics Covered

### 1. [DevOps Introduction](./topics/01-devops-introduction.md)
Core DevOps concepts and principles.

- What is DevOps
- CI/CD pipeline overview
- AWS CI/CD services introduction
- Best practices

### 2. [AWS CI/CD Tools](./topics/02-aws-cicd-tools.md)
Overview of AWS developer tools.

- CodeCommit (Git repositories)
- CodeBuild (Build and test)
- CodeDeploy (Deployment automation)
- CodePipeline (Orchestration)

### 3. [CodeBuild](./topics/03-codebuild.md)
Deep dive into AWS CodeBuild.

- buildspec.yml configuration
- Build phases and artifacts
- Environment variables
- Caching and optimization
- Docker builds

### 4. [CodePipeline](./topics/04-codepipeline.md)
Creating complete CI/CD pipelines.

- Pipeline structure
- Multi-stage deployments
- Manual approvals
- Notifications
- Troubleshooting

---

## Topic Flow

```
┌─────────────────────┐
│ 1. DevOps Intro     │  Concepts, CI/CD basics
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 2. AWS CI/CD Tools  │  Service overview
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 3. CodeBuild        │  Build configuration
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 4. CodePipeline     │  Complete pipelines
└─────────────────────┘
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      CodePipeline                            │
├──────────────┬──────────────┬──────────────┬───────────────┤
│   Source     │    Build     │    Test      │    Deploy     │
├──────────────┼──────────────┼──────────────┼───────────────┤
│   GitHub     │  CodeBuild   │  CodeBuild   │  CodeDeploy   │
│              │  (compile)   │  (test)      │  (EC2/S3)     │
└──────────────┴──────────────┴──────────────┴───────────────┘
```

---

## Key Files

| File | Service | Purpose |
|------|---------|---------|
| `buildspec.yml` | CodeBuild | Build instructions |
| `appspec.yml` | CodeDeploy | Deployment instructions |

### buildspec.yml Example

```yaml
version: 0.2
phases:
  install:
    runtime-versions:
      java: corretto17
  build:
    commands:
      - mvn test
      - mvn package
artifacts:
  files:
    - target/*.jar
```

### appspec.yml Example

```yaml
version: 0.0
os: linux
files:
  - source: /
    destination: /opt/myapp
hooks:
  ApplicationStart:
    - location: scripts/start.sh
```

---

## Prerequisites

- AWS account
- Basic Git knowledge
- Spring Boot or Angular application
- EC2 instance (from AWS module)

## Additional Resources

- [AWS CodePipeline Documentation](https://docs.aws.amazon.com/codepipeline/)
- [AWS CodeBuild Documentation](https://docs.aws.amazon.com/codebuild/)
- [AWS CodeDeploy Documentation](https://docs.aws.amazon.com/codedeploy/)

---

**Duration:** 2 days | **Difficulty:** Intermediate | **Prerequisites:** AWS Fundamentals module

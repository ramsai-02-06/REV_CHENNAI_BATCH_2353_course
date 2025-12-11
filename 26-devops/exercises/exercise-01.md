# Exercise: Set Up a CI/CD Pipeline with GitHub Actions

## Objective
Create a complete CI/CD pipeline using GitHub Actions that builds, tests, and deploys an application.

## Requirements

### Pipeline Stages

1. **Build**
   - Compile code
   - Run linting
   - Build Docker image

2. **Test**
   - Unit tests
   - Integration tests
   - Code coverage report

3. **Security Scan**
   - Dependency vulnerability scan
   - Static code analysis
   - Container image scan

4. **Deploy**
   - Deploy to staging (on PR)
   - Deploy to production (on main merge)
   - Environment-specific configurations

### GitHub Actions Features to Use

- Workflow triggers (push, PR, schedule)
- Job dependencies and matrix builds
- Secrets management
- Caching dependencies
- Artifacts upload/download
- Environment protection rules

### Workflow Structure
```
.github/
├── workflows/
│   ├── ci.yml          # Build and test
│   ├── cd.yml          # Deploy
│   └── security.yml    # Security scans
└── actions/
    └── setup-java/     # Custom action (optional)
```

### ci.yml Example
```yaml
name: CI Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven

      - name: Build with Maven
        run: mvn -B package --file pom.xml

      - name: Run tests
        run: mvn test

      - name: Upload coverage
        uses: codecov/codecov-action@v3

  docker:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - name: Build and push Docker image
        uses: docker/build-push-action@v5
        with:
          push: ${{ github.ref == 'refs/heads/main' }}
          tags: user/app:${{ github.sha }}
```

## Tasks

1. Create CI workflow for build and test
2. Add code coverage reporting
3. Create CD workflow for deployment
4. Set up environment secrets
5. Configure branch protection rules
6. Add security scanning
7. Implement staging and production deployments

## Skills Tested
- GitHub Actions syntax
- CI/CD concepts
- Workflow optimization (caching, parallelization)
- Secrets management
- Environment deployments
- Branch protection and approvals

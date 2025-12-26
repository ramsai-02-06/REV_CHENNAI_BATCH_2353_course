# Project 3: Cloud-Native Microservices Deployment

## Project Overview
**Individual Learner Project 3 (Capstone)**

This is your third and final individual project, serving as your capstone. Building on your previous two projects, this capstone demonstrates your ability to build, containerize, and deploy a cloud-native microservices application on Google Cloud Platform. You'll work with the full modern stack: Spring Boot microservices, Docker containers, Kubernetes orchestration, and GCP services.

## Timeline
**Start:** Week 12
**Duration:** Weeks 12-13
**Completion:** Week 13 with presentation

## Technologies Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Cloud (Config, Gateway, Service Discovery)
- MySQL or PostgreSQL
- RESTful APIs
- Microservices Architecture

### Containerization & Orchestration
- Docker
- Docker Compose
- Kubernetes
- Helm (optional)

### Cloud Platform
- Google Cloud Platform (GCP)
- GKE (Google Kubernetes Engine)
- Cloud SQL
- Cloud Storage
- Cloud Build
- Cloud IAM

### DevOps
- CI/CD Pipelines
- Infrastructure as Code
- GitOps principles

### Frontend (Optional but Recommended)
- Angular 16 or React
- Deployed separately or as part of API Gateway

## Learning Objectives
- Design microservices architecture
- Containerize applications with Docker
- Orchestrate containers with Kubernetes
- Deploy to cloud infrastructure
- Implement CI/CD pipelines
- Apply cloud best practices
- Monitor and troubleshoot cloud applications
- Implement service-to-service communication
- Handle distributed system challenges

## Project Architecture

### Microservices Design
Your application should consist of at least 3-4 microservices:

**Example E-Commerce Architecture:**
1. **User Service** - User management, authentication
2. **Product Service** - Product catalog management
3. **Order Service** - Order processing
4. **API Gateway** - Entry point, routing, load balancing

**Additional Services (Optional):**
- **Config Server** - Centralized configuration
- **Service Registry** - Service discovery (Eureka)
- **Notification Service** - Email/SMS notifications
- **Payment Service** - Payment processing

### High-Level Architecture
```
                        Internet
                           |
                     API Gateway (GKE)
                           |
        ----------------------------------------
        |              |           |           |
    User Service  Product Svc  Order Svc  Other Services
        |              |           |           |
        ----------------------------------------
                           |
                      Cloud SQL
                           |
                    Cloud Storage
```

## Project Ideas

### Option 1: E-Commerce Microservices
**Services:**
- User Service (Registration, Authentication)
- Product Service (Catalog, Inventory)
- Order Service (Order Management, Checkout)
- Payment Service (Payment Processing)
- Notification Service (Email/SMS)
- API Gateway

### Option 2: Social Media Platform
**Services:**
- User Service (Profiles, Authentication)
- Post Service (Posts, Feed)
- Comment Service (Comments, Interactions)
- Media Service (Image/Video Upload)
- Notification Service
- API Gateway

### Option 3: Banking Application
**Services:**
- Customer Service (Customer Management)
- Account Service (Account Operations)
- Transaction Service (Transactions, History)
- Loan Service (Loan Management)
- Notification Service
- API Gateway

### Option 4: Healthcare System
**Services:**
- Patient Service
- Doctor Service
- Appointment Service
- Medical Records Service
- Billing Service
- API Gateway

## Project Structure

```
project-root/
├── user-service/
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   └── README.md
├── product-service/
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   └── README.md
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   └── README.md
├── api-gateway/
│   ├── src/
│   ├── Dockerfile
│   ├── pom.xml
│   └── README.md
├── config-server/ (optional)
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── kubernetes/
│   ├── user-service-deployment.yaml
│   ├── user-service-service.yaml
│   ├── product-service-deployment.yaml
│   ├── product-service-service.yaml
│   ├── order-service-deployment.yaml
│   ├── order-service-service.yaml
│   ├── api-gateway-deployment.yaml
│   ├── api-gateway-service.yaml
│   ├── mysql-deployment.yaml
│   ├── mysql-service.yaml
│   ├── configmap.yaml
│   └── secrets.yaml
├── docker-compose.yml
├── cloudbuild.yaml
├── .gitlab-ci.yml or .github/workflows/
├── docs/
│   ├── architecture.md
│   ├── api-documentation.md
│   └── deployment-guide.md
└── README.md
```

## Implementation Phases

### Phase 1: Microservices Development (Days 1-4)
1. Design microservices architecture
2. Identify service boundaries and responsibilities
3. Design database schema for each service
4. Implement each microservice with Spring Boot
5. Create REST APIs for each service
6. Implement inter-service communication
7. Add exception handling and validation
8. Test services locally

### Phase 2: API Gateway and Service Discovery (Days 5-6)
1. Implement API Gateway using Spring Cloud Gateway
2. Configure routing rules
3. Implement rate limiting (optional)
4. Set up service discovery with Eureka (optional)
5. Configure load balancing
6. Test gateway routing

### Phase 3: Containerization (Days 7-8)
1. Create Dockerfile for each microservice
2. Build Docker images
3. Test containers locally
4. Create docker-compose.yml for local testing
5. Push images to Docker Hub or GCP Container Registry
6. Optimize Docker images

### Phase 4: Kubernetes Configuration (Days 9-10)
1. Create Kubernetes deployment files
2. Create Kubernetes service files
3. Configure ConfigMaps for application properties
4. Create Secrets for sensitive data
5. Set up persistent volumes for databases
6. Configure ingress (optional)
7. Test locally with Minikube or Kind

### Phase 5: GCP Setup and Deployment (Days 11-13)
1. Set up GCP project
2. Enable necessary APIs
3. Create GKE cluster
4. Set up Cloud SQL instance
5. Configure IAM and service accounts
6. Deploy applications to GKE
7. Configure networking and load balancing
8. Set up Cloud Storage (if needed)
9. Test deployment

### Phase 6: CI/CD Pipeline (Days 14-15)
1. Set up Cloud Build or GitLab CI/CD
2. Create build configuration
3. Automate Docker image building
4. Automate deployment to GKE
5. Set up automated testing
6. Configure rollback strategies

### Phase 7: Monitoring and Testing (Days 16-17)
1. Set up logging (Cloud Logging)
2. Configure monitoring (Cloud Monitoring)
3. Set up alerts
4. Test all features end-to-end
5. Load testing
6. Security testing

### Phase 8: Documentation and Presentation (Days 18-20)
1. Write comprehensive documentation
2. Create architecture diagrams
3. Document deployment process
4. Prepare presentation
5. Create demo script
6. Record demo video (optional)

## Required Features

### Microservices (Must Have)
- [ ] At least 3 microservices
- [ ] API Gateway for routing
- [ ] Each service has its own database (or database schema)
- [ ] RESTful APIs for each service
- [ ] Inter-service communication
- [ ] Proper error handling
- [ ] Health check endpoints

### Containerization (Must Have)
- [ ] Dockerfile for each service
- [ ] Docker images built and tested
- [ ] docker-compose for local testing
- [ ] Images pushed to registry

### Kubernetes (Must Have)
- [ ] Deployment configurations for each service
- [ ] Service configurations
- [ ] ConfigMaps for configuration
- [ ] Secrets for sensitive data
- [ ] Horizontal Pod Autoscaling (optional)

### GCP Deployment (Must Have)
- [ ] GKE cluster created
- [ ] All services deployed to GKE
- [ ] Cloud SQL database configured
- [ ] Services accessible via external IP/domain
- [ ] IAM configured properly

### CI/CD (Should Have)
- [ ] Automated build pipeline
- [ ] Automated deployment
- [ ] Basic testing in pipeline

### Additional Features (Nice to Have)
- [ ] Service mesh (Istio)
- [ ] Distributed tracing
- [ ] Centralized logging
- [ ] Custom domain with SSL
- [ ] Blue-Green or Canary deployment
- [ ] Infrastructure as Code (Terraform)

## Code Examples

### Dockerfile Example
```dockerfile
# Multi-stage build
FROM maven:3.8-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes Deployment Example
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: gcr.io/YOUR_PROJECT/user-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: DB_HOST
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: database.host
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secrets
              key: password
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

### Kubernetes Service Example
```yaml
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  type: ClusterIP
  selector:
    app: user-service
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
```

### Cloud Build Configuration
```yaml
steps:
  # Build Docker image
  - name: 'gcr.io/cloud-builders/docker'
    args: ['build', '-t', 'gcr.io/$PROJECT_ID/user-service:$SHORT_SHA', './user-service']

  # Push to Container Registry
  - name: 'gcr.io/cloud-builders/docker'
    args: ['push', 'gcr.io/$PROJECT_ID/user-service:$SHORT_SHA']

  # Deploy to GKE
  - name: 'gcr.io/cloud-builders/kubectl'
    args:
    - 'set'
    - 'image'
    - 'deployment/user-service'
    - 'user-service=gcr.io/$PROJECT_ID/user-service:$SHORT_SHA'
    env:
    - 'CLOUDSDK_COMPUTE_REGION=us-central1'
    - 'CLOUDSDK_CONTAINER_CLUSTER=my-cluster'

images:
  - 'gcr.io/$PROJECT_ID/user-service:$SHORT_SHA'
```

### docker-compose.yml for Local Testing
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: myapp
    ports:
      - "3306:3306"

  user-service:
    build: ./user-service
    ports:
      - "8081:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/myapp
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - mysql

  product-service:
    build: ./product-service
    ports:
      - "8082:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/myapp
    depends_on:
      - mysql

  order-service:
    build: ./order-service
    ports:
      - "8083:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/myapp
    depends_on:
      - mysql

  api-gateway:
    build: ./api-gateway
    ports:
      - "8080:8080"
    depends_on:
      - user-service
      - product-service
      - order-service
```

## GCP Setup Commands

### Create GKE Cluster
```bash
# Create cluster
gcloud container clusters create my-cluster \
  --zone us-central1-a \
  --num-nodes 3 \
  --machine-type n1-standard-2 \
  --enable-autoscaling \
  --min-nodes 1 \
  --max-nodes 5

# Get credentials
gcloud container clusters get-credentials my-cluster --zone us-central1-a

# Verify
kubectl get nodes
```

### Create Cloud SQL Instance
```bash
gcloud sql instances create my-mysql-instance \
  --database-version=MYSQL_8_0 \
  --tier=db-n1-standard-1 \
  --region=us-central1

gcloud sql databases create myapp --instance=my-mysql-instance

gcloud sql users create myuser \
  --instance=my-mysql-instance \
  --password=mypassword
```

### Deploy to GKE
```bash
# Apply configurations
kubectl apply -f kubernetes/

# Check deployments
kubectl get deployments

# Check pods
kubectl get pods

# Check services
kubectl get services

# Get external IP
kubectl get service api-gateway
```

## Evaluation Criteria

### Architecture & Design (25%)
- Microservices design and boundaries
- Database design
- API design
- Scalability considerations
- Documentation quality

### Implementation (25%)
- Code quality
- Microservices functionality
- Error handling
- Inter-service communication
- RESTful API implementation

### Containerization (15%)
- Proper Dockerfile configuration
- Image optimization
- Docker compose setup
- Container best practices

### Kubernetes (15%)
- Deployment configurations
- Service configurations
- Resource management
- Health checks and probes

### Cloud Deployment (15%)
- Successful GCP deployment
- GKE configuration
- Cloud SQL setup
- Networking and security

### CI/CD & DevOps (5%)
- Pipeline configuration
- Automated deployment
- Testing automation

## Submission Guidelines

### Deliverables
1. Complete source code for all microservices
2. Docker configuration files
3. Kubernetes manifests
4. CI/CD pipeline configuration
5. Comprehensive documentation
6. Architecture diagrams
7. Deployment guide
8. Presentation (15-20 minutes)
9. Demo video (optional)

### Documentation Must Include
- Architecture overview with diagrams
- Service descriptions and responsibilities
- API documentation for each service
- Database schemas
- Deployment instructions
- GCP setup guide
- Troubleshooting guide
- Cost analysis

### Submission Checklist
- [ ] All services deployed and running on GKE
- [ ] Services accessible via API Gateway
- [ ] Database connected and functional
- [ ] CI/CD pipeline working
- [ ] All documentation complete
- [ ] Architecture diagrams included
- [ ] README with clear instructions
- [ ] Code properly organized and commented
- [ ] Demo prepared
- [ ] Presentation ready

## Cost Considerations

### GCP Free Tier
- Take advantage of GCP free tier
- Use f1-micro or g1-small instances
- Shut down resources when not in use
- Monitor costs in GCP Console

### Cost Optimization Tips
1. Use preemptible VMs for non-critical workloads
2. Set up budget alerts
3. Delete unused resources
4. Use appropriate machine types
5. Clean up after project completion

## Best Practices

### Microservices
- Single Responsibility Principle
- Database per service
- API versioning
- Asynchronous communication where appropriate
- Circuit breakers for resilience

### Docker
- Use multi-stage builds
- Minimize image size
- Don't run as root
- Use specific image tags
- Scan for vulnerabilities

### Kubernetes
- Use namespaces
- Set resource limits
- Implement health checks
- Use ConfigMaps and Secrets
- Enable logging and monitoring

### Security
- Use secrets for sensitive data
- Implement RBAC
- Use private container registry
- Enable encryption at rest and in transit
- Regular security updates

### DevOps
- Infrastructure as Code
- Automated testing
- Rollback strategies
- Monitoring and alerting
- Documentation

## Common Challenges and Solutions

### Challenge: Service Communication
**Solution:** Use REST APIs with proper error handling, consider message queues for async communication

### Challenge: Database Management
**Solution:** Use Cloud SQL or database per service pattern, implement proper connection pooling

### Challenge: Configuration Management
**Solution:** Use ConfigMaps and Secrets, consider Spring Cloud Config

### Challenge: Debugging in Kubernetes
**Solution:** Use `kubectl logs`, `kubectl describe`, enable debug logs

### Challenge: Cost Overruns
**Solution:** Set up budget alerts, use autoscaling, shut down when not in use

## Tips for Success

1. **Start Simple**: Begin with 2-3 services, add complexity gradually
2. **Test Locally First**: Use docker-compose before deploying to cloud
3. **Automate Early**: Set up CI/CD pipeline early in development
4. **Monitor Costs**: Check GCP billing dashboard daily
5. **Document as You Go**: Don't wait until the end to document
6. **Use Existing Services**: Leverage Spring Cloud, don't reinvent the wheel
7. **Plan Your Demo**: Practice your demonstration beforehand
8. **Ask for Help**: Don't wait until the last minute if stuck
9. **Version Control**: Commit frequently with meaningful messages
10. **Clean Up**: Delete GCP resources after presentation to avoid charges

## Resources

### Spring Cloud
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)

### Docker & Kubernetes
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)

### Google Cloud Platform
- [GKE Documentation](https://cloud.google.com/kubernetes-engine/docs)
- [Cloud SQL Documentation](https://cloud.google.com/sql/docs)
- [Cloud Build Documentation](https://cloud.google.com/build/docs)
- [GCP Free Tier](https://cloud.google.com/free)

### Related Modules
- [Module 19: Microservices](../../19-microservices/)
- [Module 23: Docker](../../23-docker/)
- [Module 24: Kubernetes](../../24-kubernetes/)
- [Module 25: Google Cloud Platform](../../25-gcp/)
- [Module 26: DevOps](../../26-devops/)

---

**This is your third and final individual project - showcase everything you've learned and build something impressive!**

**Remember to delete your GCP resources after the project to avoid unexpected charges!**

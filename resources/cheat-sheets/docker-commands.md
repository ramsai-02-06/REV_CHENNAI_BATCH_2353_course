# Docker Commands Cheat Sheet

## Installation

```bash
# Ubuntu
sudo apt update
sudo apt install docker.io
sudo systemctl start docker
sudo systemctl enable docker

# Check version
docker --version
docker version
```

## Images

```bash
# List images
docker images
docker image ls

# Pull image
docker pull ubuntu
docker pull nginx:latest
docker pull mysql:8.0

# Search for images
docker search nginx

# Remove image
docker rmi image_name
docker rmi image_id
docker image rm nginx

# Remove unused images
docker image prune

# Build image from Dockerfile
docker build -t myapp:1.0 .
docker build -t myapp:1.0 -f Dockerfile.prod .

# Tag image
docker tag myapp:1.0 username/myapp:1.0

# Push image to Docker Hub
docker push username/myapp:1.0

# Save image to tar file
docker save -o myapp.tar myapp:1.0

# Load image from tar file
docker load -i myapp.tar

# View image history
docker history image_name
```

## Containers

```bash
# Run container
docker run nginx
docker run -d nginx          # Detached mode
docker run -d --name mynginx nginx
docker run -p 8080:80 nginx  # Port mapping

# List containers
docker ps                    # Running containers
docker ps -a                 # All containers
docker container ls

# Stop container
docker stop container_id
docker stop container_name

# Start container
docker start container_id

# Restart container
docker restart container_id

# Remove container
docker rm container_id
docker rm container_name
docker rm -f container_id    # Force remove running container

# Remove all stopped containers
docker container prune

# Run command in container
docker exec -it container_id bash
docker exec container_id ls /app
docker exec -it container_name sh

# View logs
docker logs container_id
docker logs -f container_id  # Follow logs
docker logs --tail 100 container_id

# View container details
docker inspect container_id

# View container stats
docker stats
docker stats container_id

# Copy files
docker cp file.txt container_id:/app/
docker cp container_id:/app/file.txt .

# Commit container to image
docker commit container_id myimage:1.0
```

## Dockerfile

```dockerfile
# Base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy files
COPY . .
COPY target/myapp.jar app.jar

# Run commands
RUN apt-get update && apt-get install -y curl

# Expose port
EXPOSE 8080

# Environment variables
ENV APP_ENV=production
ENV DB_HOST=localhost

# Command to run
CMD ["java", "-jar", "app.jar"]
# or
ENTRYPOINT ["java", "-jar", "app.jar"]

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1
```

### Multi-stage Build

```dockerfile
# Build stage
FROM maven:3.8-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

## Docker Compose

### docker-compose.yml

```yaml
version: '3.8'

services:
  web:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/mydb
    depends_on:
      - db
    volumes:
      - ./logs:/app/logs
    networks:
      - mynetwork

  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: mydb
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
    networks:
      - mynetwork

networks:
  mynetwork:
    driver: bridge

volumes:
  db_data:
```

### Docker Compose Commands

```bash
# Start services
docker-compose up
docker-compose up -d         # Detached mode
docker-compose up --build    # Rebuild images

# Stop services
docker-compose down
docker-compose down -v       # Remove volumes

# View logs
docker-compose logs
docker-compose logs -f web

# List services
docker-compose ps

# Execute command
docker-compose exec web bash

# Build images
docker-compose build

# Pull images
docker-compose pull

# Restart service
docker-compose restart web

# Scale service
docker-compose up -d --scale web=3
```

## Volumes

```bash
# Create volume
docker volume create myvolume

# List volumes
docker volume ls

# Inspect volume
docker volume inspect myvolume

# Remove volume
docker volume rm myvolume

# Remove unused volumes
docker volume prune

# Mount volume
docker run -v myvolume:/data nginx
docker run -v /host/path:/container/path nginx
docker run -v $(pwd):/app nginx
```

## Networks

```bash
# List networks
docker network ls

# Create network
docker network create mynetwork
docker network create --driver bridge mynetwork

# Inspect network
docker network inspect mynetwork

# Connect container to network
docker network connect mynetwork container_id

# Disconnect container
docker network disconnect mynetwork container_id

# Remove network
docker network rm mynetwork

# Run container in network
docker run --network mynetwork nginx
```

## System

```bash
# System information
docker info

# Disk usage
docker system df

# Clean up
docker system prune          # Remove unused data
docker system prune -a       # Remove all unused images
docker system prune --volumes  # Include volumes

# Events
docker events
docker events --since '2024-01-01'

# Version
docker version
```

## Registry

```bash
# Login to Docker Hub
docker login
docker login -u username -p password

# Logout
docker logout

# Tag image
docker tag myapp:1.0 username/myapp:1.0

# Push to Docker Hub
docker push username/myapp:1.0

# Pull from Docker Hub
docker pull username/myapp:1.0

# Private registry
docker tag myapp registry.example.com/myapp:1.0
docker push registry.example.com/myapp:1.0
```

## Example Workflows

### Java Spring Boot

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/myapp.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

```bash
# Build
mvn clean package
docker build -t myapp:1.0 .

# Run
docker run -d -p 8080:8080 --name myapp myapp:1.0

# Check logs
docker logs -f myapp

# Stop and remove
docker stop myapp
docker rm myapp
```

### Full Stack with Compose

```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      DB_URL: jdbc:mysql://db:3306/mydb
    depends_on:
      - db

  frontend:
    build: ./frontend
    ports:
      - "4200:80"
    depends_on:
      - backend

  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: mydb
    volumes:
      - db_data:/var/lib/mysql

volumes:
  db_data:
```

```bash
docker-compose up --build -d
```

## Best Practices

1. **Use specific versions**: `FROM node:18` not `FROM node:latest`
2. **Multi-stage builds**: Smaller final images
3. **.dockerignore**: Exclude unnecessary files
4. **One process per container**: Single responsibility
5. **Use volumes**: Persist data outside containers
6. **Non-root user**: Don't run as root
7. **Health checks**: Implement health endpoints
8. **Layer caching**: Order commands for better caching

### .dockerignore

```
node_modules
npm-debug.log
.git
.env
*.md
```

---

**Common Issues:**
- Permission denied: `sudo usermod -aG docker $USER`
- Port already in use: Change port mapping
- Cannot connect to daemon: `sudo systemctl start docker`

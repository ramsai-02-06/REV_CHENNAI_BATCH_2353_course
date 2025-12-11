# Exercise: Containerize a Full-Stack Application

## Objective
Learn Docker by containerizing a Spring Boot backend and Angular frontend, then orchestrating them with Docker Compose.

## Requirements

### Application to Containerize
- **Backend**: Spring Boot REST API
- **Frontend**: Angular application
- **Database**: MySQL

### Docker Concepts to Demonstrate

1. **Dockerfile for Spring Boot**
   - Multi-stage build
   - JDK base image
   - Layer optimization

2. **Dockerfile for Angular**
   - Build stage with Node
   - Production stage with Nginx

3. **Docker Compose**
   - Service definitions
   - Network configuration
   - Volume mounts
   - Environment variables
   - Depends_on for startup order

4. **Docker Commands**
   - Build, run, stop, remove
   - Logs, exec, inspect
   - Network, volume management

### Project Structure
```
project/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── nginx.conf
│   └── Dockerfile
├── docker-compose.yml
├── docker-compose.prod.yml
└── .env
```

### Dockerfile Examples

#### Spring Boot (backend/Dockerfile)
```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Angular (frontend/Dockerfile)
```dockerfile
# Build stage
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration=production

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist/frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

### docker-compose.yml
```yaml
version: '3.8'
services:
  db:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
      MYSQL_DATABASE: appdb
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/appdb
    depends_on:
      - db

  frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

## Tasks

1. Write Dockerfiles for backend and frontend
2. Create docker-compose.yml
3. Build and run the stack
4. Test the application
5. View logs and debug issues
6. Push images to Docker Hub (optional)

## Skills Tested
- Dockerfile creation
- Multi-stage builds
- Docker Compose orchestration
- Container networking
- Volume management
- Environment configuration

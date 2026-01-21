# Spring Boot CLI

## Introduction

The Spring Boot Command Line Interface (CLI) is a tool for rapid prototyping with Spring. It allows you to run Groovy scripts without setting up a full Maven/Gradle project.

**Key Point:** The CLI is primarily for prototyping and learning, not for production applications.

---

## When to Use Spring Boot CLI

### Use CLI For:
- Quick prototyping and proof of concepts
- Learning Spring Boot concepts
- Creating demos
- Generating new projects (`spring init`)

### Don't Use CLI For:
- Production applications
- Team projects
- Large applications
- Complex builds

---

## Installation

### Using SDKMAN! (Recommended)

```bash
# Install SDKMAN!
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Spring Boot CLI
sdk install springboot

# Verify
spring --version
```

### Using Homebrew (macOS)

```bash
brew tap spring-io/tap
brew install spring-boot

spring --version
```

---

## Essential Commands

| Command | Description |
|---------|-------------|
| `spring --version` | Show version |
| `spring help` | Show help |
| `spring init` | Generate new project |
| `spring run` | Run Groovy script |

---

## Project Initialization

The most useful CLI feature is generating new projects from the command line.

### Basic Usage

```bash
# Create a simple project
spring init my-project

# Specify dependencies
spring init --dependencies=web,data-jpa,h2 my-project

# Full example
spring init \
  --dependencies=web,data-jpa,security \
  --java-version=17 \
  --build=maven \
  --group=com.example \
  --artifact=myapp \
  myapp
```

### List Available Dependencies

```bash
spring init --list
```

### Common Dependency Combinations

```bash
# REST API
spring init --dependencies=web,data-jpa,mysql my-api

# Web with security
spring init --dependencies=web,security,thymeleaf my-webapp

# Microservice
spring init --dependencies=web,actuator,cloud-eureka my-service
```

---

## Running Groovy Scripts

For quick prototyping, you can run Spring applications as Groovy scripts.

### Hello World

```groovy
// hello.groovy
@RestController
class HelloController {
    @GetMapping("/")
    String home() {
        "Hello, Spring Boot CLI!"
    }
}
```

```bash
spring run hello.groovy
# Access: http://localhost:8080/
```

### With Dependencies (@Grab)

```groovy
// app.groovy
@Grab("spring-boot-starter-data-jpa")
@Grab("h2")

@Entity
class User {
    @Id @GeneratedValue
    Long id
    String name
}

@Repository
interface UserRepository extends JpaRepository<User, Long> {}

@RestController
class UserController {
    @Autowired UserRepository repo

    @GetMapping("/users")
    List<User> list() { repo.findAll() }

    @PostMapping("/users")
    User create(@RequestBody User user) { repo.save(user) }
}
```

```bash
spring run app.groovy
```

### Running Options

```bash
# Different port
spring run app.groovy -- --server.port=9090

# With profile
spring run app.groovy -- --spring.profiles.active=dev
```

---

## CLI vs Traditional Approach

| Aspect | CLI (Groovy) | Traditional (Java + Maven) |
|--------|--------------|---------------------------|
| Setup time | Instant | Minutes |
| Build file | None | pom.xml required |
| Use case | Prototyping | Production |
| Team collaboration | Poor | Good |
| IDE support | Limited | Full |

---

## Summary

| Concept | Details |
|---------|---------|
| **Purpose** | Rapid prototyping, project generation |
| **Key Command** | `spring init` for creating projects |
| **Groovy Scripts** | Quick testing without build files |
| **Production Use** | Not recommended |

**Recommendation:** Use `spring init` to generate projects, then switch to a standard Maven/Gradle workflow for actual development.

## Next Topic

Continue to [application.properties / .yml](./06-application-properties-yml.md) to learn about configuration management.

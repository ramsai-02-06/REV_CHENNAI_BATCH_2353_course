# Stage 2: Maven Build System

## Goal
Learn proper project structure and dependency management with Maven.

## What Changed from Stage 1

### Project Structure
```
stage-1 (flat):              stage-2 (Maven standard):
src/                         src/
├── Main.java                └── main/
├── Task.java                    ├── java/
├── TaskRepository.java          │   └── com/example/taskmanager/
└── ...                          │       ├── Main.java
                                 │       ├── model/
                                 │       │   ├── Task.java
                                 │       │   └── TaskStatus.java
                                 │       ├── repository/
                                 │       │   ├── TaskRepository.java
                                 │       │   └── ConnectionManager.java
                                 │       ├── service/
                                 │       │   └── TaskService.java
                                 │       └── ui/
                                 │           └── ConsoleUI.java
                                 └── resources/
                                     └── db.properties
```

### Dependency Management
```xml
<!-- Stage 1: Manual JAR download and classpath -->
java -cp "out:mysql-connector-j-8.0.33.jar" Main

<!-- Stage 2: Maven handles it -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>
```

### Configuration Externalized
```properties
# Stage 1: Hardcoded in Java
new DatabaseConfig("localhost", "taskmanager", "root", "password");

# Stage 2: External properties file
db.host=localhost
db.name=taskmanager
db.username=root
db.password=password
```

## What Stays the Same
- The code logic is identical
- Still using raw JDBC
- Still manual dependency wiring in Main.java
- The pain points from Stage 1 remain (except JAR management)

## How to Run

```bash
# Compile and download dependencies
mvn compile

# Run the application
mvn exec:java

# Or package and run as JAR
mvn package
java -jar target/task-manager-1.0-SNAPSHOT.jar
```

## Maven Commands Cheat Sheet

| Command | Description |
|---------|-------------|
| `mvn compile` | Compile source code |
| `mvn test` | Run tests |
| `mvn package` | Create JAR file |
| `mvn clean` | Delete target directory |
| `mvn exec:java` | Run main class |
| `mvn dependency:tree` | Show dependency tree |

## Key Maven Concepts

### 1. POM (Project Object Model)
The `pom.xml` file defines:
- Project coordinates (groupId, artifactId, version)
- Dependencies
- Build configuration
- Plugins

### 2. Standard Directory Layout
```
src/main/java      → Application source code
src/main/resources → Configuration files
src/test/java      → Test source code
src/test/resources → Test configuration
target/            → Build output (generated)
```

### 3. Dependency Scope
```xml
<dependency>
    <groupId>...</groupId>
    <artifactId>...</artifactId>
    <scope>compile</scope>  <!-- default: available everywhere -->
    <scope>test</scope>     <!-- only for testing -->
    <scope>runtime</scope>  <!-- not needed for compilation -->
</dependency>
```

## Improvements Over Stage 1

| Aspect | Stage 1 | Stage 2 |
|--------|---------|---------|
| Dependencies | Manual JAR download | Maven manages |
| Build | javac commands | `mvn compile` |
| Structure | Flat files | Packages & layers |
| Config | Hardcoded | Properties file |

## Still Painful

- Manual wiring in Main.java (Stage 3 fixes this)
- Raw JDBC boilerplate (Stage 5 fixes this)
- No auto-reload on changes
- No embedded database for testing

## Exercises

1. **Add a new dependency** - Add SLF4J for logging
2. **Run dependency:tree** - Understand transitive dependencies
3. **Modify db.properties** - Change database without recompiling

## What's Next?
Stage 3 introduces Spring Core to eliminate manual dependency wiring.

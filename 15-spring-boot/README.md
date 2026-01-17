# Spring Boot

## Overview
Spring Boot simplifies Spring application development with auto-configuration and embedded servers.

## Learning Objectives
By the end of this module, you will understand and be able to apply the key concepts and practices of Spring Boot.

## Topics Covered

### 1. [What is Spring Boot?](./topics/01-what-is-spring-boot.md)
- Introduction to Spring Boot
- Core features and benefits
- Spring Boot architecture
- Your first Spring Boot application
- Key annotations and concepts

### 2. [Spring Boot vs Spring Framework](./topics/02-spring-boot-vs-spring-framework.md)
- Understanding the differences
- Configuration comparison
- Dependency management
- Deployment approaches
- When to use each

### 3. [Auto-configuration](./topics/03-auto-configuration.md)
- How auto-configuration works
- Conditional annotations
- Customizing auto-configuration
- Creating custom auto-configurations
- Debugging auto-configuration

### 4. [Starter Dependencies](./topics/04-starter-dependencies.md)
- Understanding starter dependencies
- Common starters (web, data, security, etc.)
- Dependency management with spring-boot-starter-parent
- Creating custom starters
- Best practices

### 5. [Spring Boot CLI](./topics/05-spring-boot-cli.md)
- Installing the CLI
- Running Groovy scripts
- Using @Grab for dependencies
- Project initialization
- CLI commands and options

### 6. [application.properties / .yml](./topics/06-application-properties-yml.md)
- Configuration file formats
- Common configuration properties
- Profile-specific configuration
- Custom properties and @ConfigurationProperties
- Property precedence and externalized configuration

### 7. [Spring Profiles](./topics/07-spring-profiles.md)
- What are Spring Profiles and why use them
- Creating profile-specific configuration files
- Activating profiles (properties, command line, environment variables)
- @Profile annotation for conditional beans
- Profile expressions (!, &, |)
- Profile groups (Spring Boot 2.4+)
- Best practices for environment management

### 8. [Testing Basics](./topics/08-testing-basics.md)
- Testing pyramid: unit, integration, end-to-end
- spring-boot-starter-test dependencies
- Unit testing with Mockito (@Mock, @InjectMocks)
- @SpringBootTest for integration tests
- @WebMvcTest for controller testing with MockMvc
- @DataJpaTest for repository testing
- @MockBean for replacing beans
- AssertJ fluent assertions
- Testing best practices


## Key Concepts
Refer to the curriculum and lecture notes for detailed explanations of each topic.

## Exercises
See the [exercises](./exercises/) directory for hands-on practice problems and solutions.

## Code Examples
Check the module materials and exercises for practical code examples.

## Additional Resources
- Official documentation
- Online tutorials and courses
- Community forums and discussions

## Assessment
Make sure you are comfortable with all topics listed above before proceeding to the next module.

## Next Steps
Continue to the next module in the curriculum sequence.

---
**Time Estimate:** 4 days | **Difficulty:** Intermediate | **Prerequisites:** Module 14 (Spring Framework)

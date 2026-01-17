# Spring Framework

## Overview
Spring Framework is the most popular Java framework for enterprise applications. This module covers Spring Core, Dependency Injection, and AOP.

## Learning Objectives
By the end of this module, you will understand and be able to apply the key concepts and practices of Spring Framework.

## Topics Covered

### 1. [Spring Framework Overview](./topics/01-spring-overview.md)
- What is Spring Framework?
- Spring Framework Architecture
- Spring Ecosystem (Boot, Data, Security, Cloud, MVC, Batch)
- Spring vs Java EE comparison
- Why choose Spring?

### 2. [Maven Basics](./topics/02-maven-basics.md)
- Introduction to Maven
- Maven installation and setup
- Maven Project Structure (standard directory layout)
- Project Object Model (POM)
- Maven Build Lifecycle and phases
- Essential Maven Commands
- Dependency Management
- Maven Repositories

### 3. [Inversion of Control (IoC)](./topics/03-inversion-of-control.md)
- IoC Concept and principles
- Spring IoC Container architecture
- Container types: BeanFactory vs ApplicationContext
- ApplicationContext features and implementations
- Container lifecycle
- Practical examples

### 4. [Bean Configuration](./topics/04-bean-configuration.md)
- What is a Spring Bean?
- XML-based Configuration
- Annotation-based Configuration (@Component, @Service, @Repository)
- Java-based Configuration (@Configuration, @Bean)
- Bean Scopes (singleton, prototype, request, session, application)
- Bean Lifecycle (initialization and destruction callbacks)
- Configuration comparison and best practices

### 5. [Dependency Injection](./topics/05-dependency-injection.md)
- What is Dependency Injection?
- Constructor Injection (recommended)
- Setter Injection
- Field Injection
- @Autowired Annotation
- @Qualifier for disambiguating dependencies
- @Primary annotation
- Autowiring modes
- Handling circular dependencies

### 6. [Aspect-Oriented Programming (AOP)](./topics/06-aspect-oriented-programming.md)
- AOP Concepts and terminology
- Cross-cutting Concerns (logging, security, transactions)
- Aspect, Advice, Pointcut, Join Point
- @Aspect Annotation
- Advice types: @Before, @After, @AfterReturning, @AfterThrowing, @Around
- Pointcut expressions
- AOP Proxies (JDK Dynamic Proxy vs CGLIB)
- Practical AOP examples

### 7. [Common Pitfalls and Troubleshooting](./topics/07-common-pitfalls.md)
- NoSuchBeanDefinitionException and solutions
- NoUniqueBeanDefinitionException (@Qualifier, @Primary)
- Circular dependency detection and resolution
- Component scan misconfiguration
- Bean scope misunderstandings
- Field injection issues
- @Transactional pitfalls
- Debugging tips and tools


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
**Time Estimate:** 5 days | **Difficulty:** Intermediate | **Prerequisites:** Previous modules

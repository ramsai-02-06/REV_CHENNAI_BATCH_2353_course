# Spring Framework

## Overview

Spring Framework is the most popular Java framework for building enterprise applications. It provides comprehensive infrastructure support, making Java development easier and more productive. This module covers the core concepts: Inversion of Control, Dependency Injection, Bean management, and Aspect-Oriented Programming.

## Learning Objectives

By the end of this module, you will be able to:
- Understand the Spring Framework architecture and its role in enterprise development
- Configure Spring applications using XML, annotations, and Java-based configuration
- Work with BeanFactory and ApplicationContext containers
- Apply Dependency Injection patterns effectively
- Implement cross-cutting concerns using AOP
- Troubleshoot common Spring configuration issues

---

## Topics Covered

### 1. [Spring Framework Overview](./topics/01-spring-overview.md)
Introduction to Spring Framework and its ecosystem. Understand why Spring became the de-facto standard for Java enterprise development and how its modules work together.

- What is Spring Framework?
- Spring Framework Architecture
- Spring Ecosystem (Boot, Data, Security, Cloud, MVC, Batch)
- Spring vs Java EE comparison
- Why choose Spring?

### 2. [Maven Basics](./topics/02-maven-basics.md)
Learn the build tool used to manage Spring projects. Maven handles dependency management, project structure, and build lifecycle - essential knowledge before creating Spring applications.

- Introduction to Maven
- Maven Project Structure (standard directory layout)
- Project Object Model (POM)
- Maven Build Lifecycle and phases
- Dependency Management
- Maven Repositories

### 3. [Inversion of Control (IoC)](./topics/03-inversion-of-control.md)
The foundational principle of Spring. IoC inverts the traditional control flow - instead of your code creating dependencies, the framework provides them. This topic introduces the IoC container and its two main implementations.

- IoC Concept and principles
- Spring IoC Container architecture
- Container types: BeanFactory vs ApplicationContext
- ApplicationContext features and implementations
- Container lifecycle

### 4. [Bean Configuration](./topics/04-bean-configuration.md)
Learn the three ways to configure Spring beans. Understand when to use each approach and how to manage bean scopes and lifecycle.

- What is a Spring Bean?
- XML-based Configuration (traditional)
- Annotation-based Configuration (@Component, @Service, @Repository)
- Java-based Configuration (@Configuration, @Bean)
- Bean Scopes (singleton, prototype, request, session)
- Bean Lifecycle (initialization and destruction callbacks)

### 5. [Dependency Injection](./topics/05-dependency-injection.md)
Master the patterns for injecting dependencies into beans. Learn the recommended approaches and how to handle common scenarios like multiple implementations.

- What is Dependency Injection?
- Constructor Injection (recommended)
- Setter Injection
- Field Injection
- @Autowired annotation
- @Qualifier for disambiguating dependencies
- @Primary annotation
- Handling circular dependencies

### 6. [Aspect-Oriented Programming (AOP)](./topics/06-aspect-oriented-programming.md)
Handle cross-cutting concerns like logging, security, and transactions without cluttering business logic. AOP allows you to separate these concerns into reusable aspects.

- AOP Concepts and terminology
- Cross-cutting Concerns (logging, security, transactions)
- Aspect, Advice, Pointcut, Join Point
- @Aspect annotation
- Advice types: @Before, @After, @AfterReturning, @AfterThrowing, @Around
- Pointcut expressions
- AOP Proxies (JDK Dynamic Proxy vs CGLIB)

### 7. [Common Pitfalls and Troubleshooting](./topics/07-common-pitfalls.md)
Recognize and resolve common Spring configuration errors. This topic helps you debug issues faster and avoid common mistakes.

- NoSuchBeanDefinitionException and solutions
- NoUniqueBeanDefinitionException (@Qualifier, @Primary)
- Circular dependency detection and resolution
- Component scan misconfiguration
- Bean scope misunderstandings
- @Transactional pitfalls
- Debugging tips and tools

### 8. [XML Configuration Deep Dive](./topics/08-xml-configuration-deep-dive.md)
Deep dive into XML-based Spring configuration. Covers BeanFactory usage, ApplicationContext implementations, and advanced XML configuration patterns essential for understanding Spring internals and maintaining legacy applications.

- BeanFactory interface and usage
- ClassPathXmlApplicationContext and FileSystemXmlApplicationContext
- Constructor and setter injection in XML
- Bean inheritance with parent attribute
- Factory methods and factory beans
- Property placeholders and externalized configuration
- Multi-file configuration with imports

---

## Topic Flow

```
┌─────────────────┐
│  1. Overview    │  What is Spring?
└────────┬────────┘
         ▼
┌─────────────────┐
│  2. Maven       │  Project setup & dependencies
└────────┬────────┘
         ▼
┌─────────────────┐
│  3. IoC         │  Core principle: container manages objects
└────────┬────────┘
         ▼
┌─────────────────┐
│  4. Bean Config │  Three ways to configure beans
└────────┬────────┘
         ▼
┌─────────────────┐
│  5. DI Patterns │  How to inject dependencies
└────────┬────────┘
         ▼
┌─────────────────┐
│  6. AOP         │  Cross-cutting concerns
└────────┬────────┘
         ▼
┌─────────────────┐
│  7. Pitfalls    │  Troubleshooting & debugging
└────────┬────────┘
         ▼
┌─────────────────┐
│  8. XML Deep    │  BeanFactory, ApplicationContext, advanced XML
│     Dive        │
└─────────────────┘
```

---

## Key Concepts Summary

| Concept | Description |
|---------|-------------|
| **IoC Container** | Manages object creation and lifecycle |
| **BeanFactory** | Basic container with lazy initialization |
| **ApplicationContext** | Extended container with enterprise features |
| **Bean** | Object managed by the Spring container |
| **Dependency Injection** | Container provides dependencies to beans |
| **AOP** | Separates cross-cutting concerns from business logic |

---

## Exercises

See the [exercises](./exercises/) directory for hands-on practice problems and solutions.

## Additional Resources

- [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/)
- [Spring Guides](https://spring.io/guides)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

---

## Next Steps

After completing this module, continue to **Spring Boot** to learn how Spring Boot simplifies Spring application development with auto-configuration and starter dependencies.

---

**Duration:** 5 days | **Difficulty:** Intermediate | **Prerequisites:** Core Java, JDBC basics

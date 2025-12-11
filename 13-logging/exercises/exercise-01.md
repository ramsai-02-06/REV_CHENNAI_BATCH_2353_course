# Exercise: Implement Logging in a REST Application

## Objective
Add comprehensive logging to a Java application using SLF4J with Logback, demonstrating logging best practices, log levels, MDC, and log file configuration.

## Requirements

### Application to Log
Use a simple Order Processing Service with the following components:
- OrderController (REST endpoints)
- OrderService (business logic)
- OrderRepository (data access)
- PaymentService (external API calls)

### Logging Requirements

1. **Log Levels**
   - TRACE: Method entry/exit, detailed debugging
   - DEBUG: Variable values, flow information
   - INFO: Business events (order created, payment processed)
   - WARN: Recoverable issues (retry attempts, fallback used)
   - ERROR: Exceptions and failures

2. **MDC (Mapped Diagnostic Context)**
   - Request ID for tracing
   - User ID for context
   - Transaction ID for business correlation

3. **Logback Configuration**
   - Console appender for development
   - Rolling file appender for production
   - Different log levels per package

## Skills Tested
- SLF4J API usage
- Logback configuration
- MDC for request tracing
- Appropriate log levels
- Log file management

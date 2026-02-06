# Security in Microservices

## Introduction to Microservices Security

Security in microservices is more complex than in monolithic applications due to distributed nature, multiple entry points, and inter-service communication.

### Security Challenges

1. **Multiple Entry Points**: Each service is a potential attack vector
2. **Service-to-Service Communication**: Internal APIs need protection
3. **Distributed Authentication**: Managing identity across services
4. **Data Protection**: Data in transit and at rest
5. **Network Security**: Multiple network boundaries

### Security Layers

```
┌─────────────────────────────────────────┐
│  Edge Security (API Gateway)            │ ← Rate limiting, SSL/TLS
├─────────────────────────────────────────┤
│  Authentication & Authorization         │ ← OAuth2, JWT
├─────────────────────────────────────────┤
│  Service-to-Service Security            │ ← mTLS, Service mesh
├─────────────────────────────────────────┤
│  Application Security                   │ ← Input validation, RBAC
├─────────────────────────────────────────┤
│  Data Security                          │ ← Encryption, key management
└─────────────────────────────────────────┘
```

---

## OAuth2

### What is OAuth2?

OAuth2 is an authorization framework that enables applications to obtain limited access to user accounts on an HTTP service. It works by delegating user authentication to the service that hosts the user account.

### OAuth2 Roles

1. **Resource Owner**: User who owns the data
2. **Client**: Application requesting access to resources
3. **Resource Server**: API server hosting protected resources
4. **Authorization Server**: Issues access tokens after authentication

### OAuth2 Flow

```
┌──────────────┐                                  ┌──────────────────┐
│   Client     │                                  │ Authorization    │
│ Application  │                                  │     Server       │
└──────┬───────┘                                  └────────┬─────────┘
       │                                                   │
       │ 1. Authorization Request                         │
       │──────────────────────────────────────────────────>│
       │                                                   │
       │ 2. Authorization Grant                           │
       │<──────────────────────────────────────────────────│
       │                                                   │
       │ 3. Access Token Request                          │
       │──────────────────────────────────────────────────>│
       │                                                   │
       │ 4. Access Token                                  │
       │<──────────────────────────────────────────────────│
       │                                                   │
       │                    ┌──────────────────┐          │
       │ 5. Access Resource │  Resource Server │          │
       │───────────────────>│                  │          │
       │                    └──────────────────┘          │
```

### OAuth2 Grant Types

#### 1. Authorization Code Grant (Most Common)

```
User → Login Page → Authorization Server → Authorization Code → Client
Client → Exchange Code for Token → Authorization Server → Access Token
Client → Use Token → Resource Server
```

#### 2. Client Credentials Grant (Service-to-Service)

```
Client → Client ID + Secret → Authorization Server → Access Token
```

#### 3. Password Grant (Legacy, Not Recommended)

```
User → Username + Password → Client → Authorization Server → Access Token
```

#### 4. Refresh Token Grant

```
Client → Refresh Token → Authorization Server → New Access Token
```

### Authorization Server

For production microservices, use established identity providers:
- **Keycloak** (open source, recommended)
- **Auth0** (cloud service)
- **Okta** (enterprise)

These handle the complexity of authorization servers including user management, SSO, and token issuance.

---

## Resource Server

### What is Resource Server?

A resource server hosts protected resources and validates access tokens to authorize requests.

### Setup

**Dependencies:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

**Configuration:**
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );
        return http.build();
    }
}
```

**application.yml:**
```yaml
spring:
  application:
    name: order-service
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9000
          # Or use jwk-set-uri directly
          # jwk-set-uri: http://localhost:9000/oauth2/jwks

server:
  port: 8081
```

### Accessing Token Information

```java
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        String userId = jwt.getClaim("user_id");
        List<String> roles = jwt.getClaim("roles");

        log.info("User: {}, Roles: {}", username, roles);

        return orderService.getOrdersByUser(userId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Order createOrder(@RequestBody OrderRequest request,
                            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return orderService.createOrder(userId, request);
    }
}
```

### Method-Level Security

```java
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PostAuthorize;

@Service
public class OrderService {

    @PreAuthorize("hasRole('USER')")
    public Order createOrder(OrderRequest request) {
        return orderRepository.save(new Order(request));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.subject")
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @PostAuthorize("returnObject.userId == authentication.principal.subject or hasRole('ADMIN')")
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
```

**Enable Method Security:**
```java
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
}
```

### Service-to-Service Communication with OAuth2

**Client Configuration:**
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          payment-service:
            client-id: order-service
            client-secret: secret
            authorization-grant-type: client_credentials
            scope: read,write
        provider:
          payment-service:
            token-uri: http://localhost:9000/oauth2/token
```

**Using WebClient with OAuth2:**
```java
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository,
                new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository)
            );

        oauth2.setDefaultClientRegistrationId("payment-service");

        return WebClient.builder()
            .apply(oauth2.oauth2Configuration())
            .build();
    }
}

@Service
public class PaymentService {

    private final WebClient webClient;

    public PaymentService(WebClient webClient) {
        this.webClient = webClient;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        return webClient.post()
            .uri("http://payment-service/api/payments")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(PaymentResponse.class)
            .block();
    }
}
```

---

## Single Sign-On (SSO)

### What is SSO?

Single Sign-On allows users to log in once and access multiple applications without re-authenticating.

```
User → Login → Authorization Server → Token
User → App 1 (uses token) ✓
User → App 2 (uses same token) ✓
User → App 3 (uses same token) ✓
```

### SSO with Keycloak

**Configuration:**
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          keycloak:
            client-id: order-service
            client-secret: ${CLIENT_SECRET}
            scope: openid,profile,email
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/realms/microservices
            user-name-attribute: preferred_username
```

**Security Configuration:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/home", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }
}
```

**Accessing User Info:**
```java
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OidcUser principal, Model model) {
        model.addAttribute("username", principal.getPreferredUsername());
        model.addAttribute("email", principal.getEmail());
        return "home";
    }
}
```

---

## Session Hijacking Prevention

### What is Session Hijacking?

Session hijacking is when an attacker steals a user's session ID to gain unauthorized access.

### Prevention Techniques

#### 1. HTTPS Only

```yaml
server:
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${KEYSTORE_PASSWORD}
    key-store-type: PKCS12
```

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .requiresChannel(channel -> channel
            .anyRequest().requiresSecure()  // Force HTTPS
        );
    return http.build();
}
```

#### 2. Secure Cookies

```yaml
server:
  servlet:
    session:
      cookie:
        secure: true      # Only send over HTTPS
        http-only: true   # Not accessible via JavaScript
        same-site: strict # Prevent CSRF
      timeout: 30m        # Short session timeout
```

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionFixation().newSession()  // New session on login
        );
    return http.build();
}
```

#### 3. Session Fixation Protection

**What is Session Fixation?**

Session fixation is an attack where the attacker sets (or "fixes") a user's session ID before the user logs in. When the user authenticates, they unknowingly use the attacker's pre-set session ID, giving the attacker access to the authenticated session.

**Attack Flow:**

```
1. Attacker visits site → Gets session ID: ABC123
2. Attacker tricks victim to use ABC123 (via malicious link, XSS, etc.)
3. Victim logs in with session ID ABC123
4. Session ABC123 is now authenticated
5. Attacker uses ABC123 → Has victim's access!
```

**Example Attack Scenario:**

```
Attacker sends email with link:
https://bank.com/login;jsessionid=ATTACKER_SESSION_ID

Victim clicks link → Logs in with that session ID
Attacker now uses same session ID → Full access to victim's account
```

**Spring Security Protection Options:**

Spring Security provides three session fixation strategies:

```java
http.sessionManagement(session -> session
    // Option 1: Create entirely new session (most secure)
    .sessionFixation().newSession()

    // Option 2: Migrate attributes to new session (default in Spring Security)
    // .sessionFixation().migrateSession()

    // Option 3: Disable protection (NOT recommended)
    // .sessionFixation().none()
);
```

| Strategy | Behavior | Use Case |
|----------|----------|----------|
| `newSession()` | Discards old session, creates fresh one | Most secure, use when no session data needed |
| `migrateSession()` | New session ID, copies attributes | Default; keeps shopping cart, etc. |
| `none()` | No protection | Never use in production |

**Why `migrateSession()` is the default:**

It balances security with usability. If a user has items in their shopping cart (stored in session) before logging in, `migrateSession()` preserves that data while still generating a new session ID.

#### 4. Token-Based Authentication (Stateless)

Use JWT instead of sessions to eliminate session hijacking risk:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
}
```

#### 5. Logout Properly

```java
@RestController
public class AuthController {

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
```

### Session Security Checklist

| Technique | Purpose |
|-----------|---------|
| HTTPS | Encrypt session ID in transit |
| Secure Cookie | Only send cookie over HTTPS |
| HttpOnly Cookie | Prevent JavaScript access (XSS protection) |
| SameSite Cookie | Prevent CSRF attacks |
| Session Fixation | New session ID on login |
| Short Timeout | Limit exposure window |
| Proper Logout | Clear session and security context |

---

## Web Security Fundamentals

Brief overview of common web security concepts relevant to microservices.

### CORS (Cross-Origin Resource Sharing)

**What is CORS?**

CORS is a browser security feature that restricts web pages from making requests to a different domain than the one that served the page.

```
Browser at https://myapp.com
    ↓
Tries to call https://api.example.com/users
    ↓
Browser blocks request (different origin)
```

**Why it exists:** Prevents malicious sites from making unauthorized requests using your credentials.

**When you need CORS:**
- Frontend (React/Angular) on `localhost:3000` calling API on `localhost:8080`
- Web app calling a different domain's API

**Spring Configuration:**

```java
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}

// Or per-controller
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController { }
```

**In Microservices:** Configure CORS at the API Gateway level (single entry point).

---

### CSRF (Cross-Site Request Forgery)

**What is CSRF?**

CSRF tricks authenticated users into submitting unintended requests. A malicious site makes requests to your app using the user's existing session.

```
1. User logs into bank.com (session cookie set)
2. User visits evil.com
3. evil.com has: <img src="https://bank.com/transfer?to=hacker&amount=1000">
4. Browser sends request WITH user's bank.com cookies
5. Bank processes transfer (user was authenticated)
```

**Why disabled for REST APIs?**

Stateless APIs using JWT tokens are NOT vulnerable to CSRF:
- JWT is sent in Authorization header, not cookies
- Malicious sites cannot access your JavaScript to read the token

```java
// Safe to disable for stateless JWT APIs
http.csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
```

**When you NEED CSRF protection:**
- Session-based authentication (cookies)
- Server-rendered web apps with forms

```java
// Enable for session-based apps
http.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
```

---

### DDoS (Distributed Denial of Service)

**What is DDoS?**

DDoS attacks overwhelm a service with massive traffic, making it unavailable to legitimate users.

```
Normal: 100 requests/second → Server handles fine
DDoS:   1,000,000 requests/second → Server overwhelmed → Service down
```

**Basic Mitigation Strategies:**

| Strategy | Implementation |
|----------|---------------|
| **Rate Limiting** | Limit requests per client (API Gateway) |
| **Cloud Protection** | AWS Shield, Cloudflare, etc. |
| **Auto-Scaling** | Scale resources during traffic spikes |
| **Load Balancing** | Distribute traffic across instances |

**Rate Limiting in API Gateway:**

```yaml
# Spring Cloud Gateway rate limiting
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10  # requests/second
                redis-rate-limiter.burstCapacity: 20
```

**Note:** Full DDoS protection requires infrastructure-level solutions (cloud providers, CDNs). Application-level rate limiting helps but cannot stop large-scale attacks alone.

---

## Security Best Practices

1. **HTTPS Only**: Force HTTPS for all communications
2. **Input Validation**: Use `@Valid` and Bean Validation annotations
3. **SQL Injection Prevention**: Use parameterized queries (JPA handles this)
4. **Secrets Management**: Use environment variables or secret managers (never hardcode)
5. **Secure Cookies**: Set `HttpOnly`, `Secure`, and `SameSite` attributes
6. **CSRF Protection**: Enable for web applications
7. **Rate Limiting**: Implement at API Gateway level
8. **Actuator Security**: Restrict access to management endpoints

```yaml
# Secure actuator endpoints
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when-authorized
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| **OAuth2** | Authorization framework for delegated access |
| **Resource Server** | Validates access tokens, protects resources |
| **SSO** | Single login for multiple applications |
| **Session Hijacking** | Prevent with HTTPS, secure cookies, session fixation protection |
| **JWT** | Stateless, token-based authentication |
| **Best Practices** | HTTPS, validation, secrets management, monitoring |

## Security Checklist

- [ ] Use HTTPS for all communications
- [ ] Implement OAuth2/JWT for authentication
- [ ] Enable CSRF protection
- [ ] Secure cookies (HttpOnly, Secure, SameSite)
- [ ] Validate all inputs
- [ ] Use parameterized queries
- [ ] Implement rate limiting
- [ ] Short session timeouts
- [ ] Monitor and log security events
- [ ] Keep dependencies updated
- [ ] Use secrets management
- [ ] Enable CORS appropriately
- [ ] Implement method-level security
- [ ] Secure actuator endpoints
- [ ] Regular security audits

## Next Topic

Continue to [Configuration Management](./07-configuration-management.md) to learn about centralized configuration with Spring Cloud Config.

# Reverse Engineering in Software Development

## What is Reverse Engineering?

Reverse engineering is the process of analyzing software to understand its structure, functionality, and behavior without access to its original source code or design documentation. It involves working backwards from a finished product to understand how it was built and how it operates.

```
┌─────────────────────────────────────────────────────────────────────┐
│                                                                     │
│   FORWARD ENGINEERING              REVERSE ENGINEERING              │
│                                                                     │
│   Requirements                     Compiled Binary                  │
│        │                                 │                          │
│        ▼                                 ▼                          │
│   Design Specs        ◄───────►    Behavior Analysis                │
│        │                                 │                          │
│        ▼                                 ▼                          │
│   Source Code                      Decompiled Code                  │
│        │                                 │                          │
│        ▼                                 ▼                          │
│   Executable                       Understanding                    │
│                                                                     │
│   (Building)                       (Discovering)                    │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Definition

**Reverse Engineering** is the systematic examination of a software system to:
- Extract design and implementation details
- Understand algorithms and data structures
- Document undocumented functionality
- Identify patterns, protocols, and interfaces
- Recover lost source code or documentation

### Types of Reverse Engineering

| Type | Description | Example |
|------|-------------|---------|
| **Static Analysis** | Examining code without executing it | Reading decompiled bytecode |
| **Dynamic Analysis** | Observing software during execution | Debugging, profiling, tracing |
| **Protocol Analysis** | Understanding communication formats | Analyzing network packets |
| **Data Structure Analysis** | Understanding data organization | Examining file formats |
| **Behavioral Analysis** | Documenting what software does | Black-box testing |

---

## Goals of Reverse Engineering

### Primary Goals

```
┌─────────────────────────────────────────────────────────────────────┐
│                    REVERSE ENGINEERING GOALS                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌─────────────────┐    ┌─────────────────┐    ┌────────────────┐ │
│   │   UNDERSTAND    │    │     LEARN       │    │   MAINTAIN     │ │
│   │   ───────────   │    │     ─────       │    │   ────────     │ │
│   │ • Architecture  │    │ • Techniques    │    │ • Legacy code  │ │
│   │ • Algorithms    │    │ • Patterns      │    │ • Bug fixes    │ │
│   │ • Data flow     │    │ • Best practices│    │ • Updates      │ │
│   └─────────────────┘    └─────────────────┘    └────────────────┘ │
│                                                                     │
│   ┌─────────────────┐    ┌─────────────────┐    ┌────────────────┐ │
│   │    INTEGRATE    │    │    SECURITY     │    │   DOCUMENT     │ │
│   │    ─────────    │    │    ────────     │    │   ────────     │ │
│   │ • Third-party   │    │ • Vulnerability │    │ • Lost docs    │ │
│   │ • APIs          │    │   assessment    │    │ • Architecture │ │
│   │ • Protocols     │    │ • Malware       │    │ • APIs         │ │
│   │                 │    │   analysis      │    │                │ │
│   └─────────────────┘    └─────────────────┘    └────────────────┘ │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### 1. Understanding Existing Systems

**Goal**: Comprehend how software works internally

**Use Cases**:
- Analyzing third-party libraries
- Understanding framework internals
- Investigating unexpected behavior
- Learning from production code

```java
// Example: Understanding how ArrayList.add() works internally
// By decompiling ArrayList.class, we discover:

public boolean add(E e) {
    modCount++;
    add(e, elementData, size);
    return true;
}

private void add(E e, Object[] elementData, int s) {
    if (s == elementData.length)
        elementData = grow();  // Dynamic array growth!
    elementData[s] = e;
    size = s + 1;
}

// Discovery: ArrayList grows dynamically when capacity is exceeded
```

### 2. Learning and Education

**Goal**: Study implementation techniques from real-world software

**Benefits**:
- Learn design patterns in practice
- Understand optimization techniques
- See how experts solve problems
- Improve coding skills

### 3. Maintaining Legacy Systems

**Goal**: Support software with lost or incomplete documentation

**Scenarios**:
- Original developers unavailable
- Documentation outdated or missing
- Source code partially lost
- Need to fix bugs without source

### 4. Integration and Interoperability

**Goal**: Understand interfaces for integration purposes

**Applications**:
- Building compatible implementations
- Creating adapters for existing systems
- Understanding proprietary protocols
- Extending closed-source software

### 5. Security Analysis

**Goal**: Identify vulnerabilities and threats

**Activities**:
- Penetration testing (authorized)
- Malware analysis
- Vulnerability research
- Security auditing

### 6. Documentation Recovery

**Goal**: Create documentation from existing code

**Outputs**:
- Architecture diagrams
- API documentation
- Data flow diagrams
- Technical specifications

---

## Objectives of Reverse Engineering

### Learning Objectives

| Objective | Description | Outcome |
|-----------|-------------|---------|
| **Identify Components** | Locate modules, classes, functions | Component inventory |
| **Map Dependencies** | Understand relationships between parts | Dependency diagram |
| **Trace Data Flow** | Follow data through the system | Data flow diagram |
| **Document Interfaces** | Identify APIs and protocols | Interface specification |
| **Understand Algorithms** | Comprehend logic and computations | Algorithm documentation |
| **Recover Architecture** | Reconstruct overall design | Architecture diagram |

### Specific Objectives by Category

#### Code Understanding Objectives

```
┌─────────────────────────────────────────────────────────────────────┐
│                  CODE UNDERSTANDING OBJECTIVES                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  STRUCTURAL                    BEHAVIORAL                           │
│  ──────────                    ──────────                           │
│  • Identify class hierarchy    • Trace method execution             │
│  • Map package organization    • Understand state changes           │
│  • Find design patterns        • Document error handling            │
│  • Locate configuration        • Identify side effects              │
│                                                                     │
│  DATA                          CONTROL FLOW                         │
│  ────                          ────────────                         │
│  • Identify data structures    • Map decision points                │
│  • Understand serialization    • Find loops and iterations          │
│  • Find validation logic       • Trace exception paths              │
│  • Map database schema         • Identify concurrency               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

#### Security Objectives

| Objective | Focus Area |
|-----------|------------|
| Find vulnerabilities | Input validation, injection points |
| Identify authentication | Login mechanisms, token handling |
| Analyze authorization | Permission checks, role validation |
| Examine cryptography | Encryption algorithms, key storage |
| Assess data protection | Sensitive data handling |

#### Performance Objectives

| Objective | Analysis Target |
|-----------|-----------------|
| Identify bottlenecks | Slow algorithms, inefficient loops |
| Find memory issues | Leaks, excessive allocation |
| Understand caching | Cache strategies, invalidation |
| Analyze I/O | File, network, database operations |

---

## Steps in Reverse Engineering

### The Systematic Approach

```
┌─────────────────────────────────────────────────────────────────────┐
│                REVERSE ENGINEERING PROCESS                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   STEP 1              STEP 2              STEP 3                    │
│   ┌──────────┐       ┌──────────┐       ┌──────────┐               │
│   │ PLANNING │──────►│ RECON    │──────►│ ANALYSIS │               │
│   └──────────┘       └──────────┘       └──────────┘               │
│   Define scope       Gather info        Examine code               │
│   Set objectives     Identify tools     Static analysis            │
│   Plan approach      Map surface        Dynamic analysis           │
│                                                                     │
│   STEP 4              STEP 5              STEP 6                    │
│   ┌──────────┐       ┌──────────┐       ┌──────────┐               │
│   │HYPOTHESIS│──────►│ TESTING  │──────►│ DOCUMENT │               │
│   └──────────┘       └──────────┘       └──────────┘               │
│   Form theories      Validate ideas     Record findings            │
│   Identify patterns  Debug & trace      Create diagrams            │
│   Make predictions   Verify behavior    Write reports              │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Step 1: Planning and Preparation

**Activities**:
- Define clear objectives
- Understand scope and constraints
- Identify legal considerations
- Select appropriate tools
- Set up analysis environment

```
Planning Checklist:
┌────────────────────────────────────────────────────────────────┐
│ □ Define what you want to learn                                │
│ □ Identify target software/component                           │
│ □ Verify legal authorization                                   │
│ □ Gather required tools                                        │
│ □ Set up isolated analysis environment                         │
│ □ Document initial assumptions                                 │
│ □ Plan analysis approach (static/dynamic/both)                 │
└────────────────────────────────────────────────────────────────┘
```

### Step 2: Reconnaissance

**Objective**: Gather initial information about the target

**Activities**:

```java
// For Java applications, gather:

// 1. File structure
myapp.jar
├── META-INF/
│   └── MANIFEST.MF           // Main class, version info
├── com/example/
│   ├── Main.class            // Entry point
│   ├── service/
│   ├── model/
│   └── util/
└── resources/
    └── application.properties // Configuration

// 2. Dependencies (check MANIFEST.MF or pom.xml if available)
// 3. External configuration
// 4. Runtime requirements
```

**Information to Collect**:

| Category | Information |
|----------|-------------|
| **Technology** | Language, framework, libraries |
| **Structure** | Packages, modules, components |
| **Entry Points** | Main class, public APIs |
| **Dependencies** | External libraries, services |
| **Configuration** | Property files, environment vars |

### Step 3: Static Analysis

**Objective**: Analyze code without executing it

#### Decompilation

```bash
# Extract JAR contents
jar -tf myapp.jar

# Decompile using CFR
java -jar cfr.jar myapp.jar --outputdir ./decompiled

# Using IntelliJ IDEA
# Simply open .class file - automatic decompilation
```

#### Code Structure Analysis

```java
// Analyze class structure using javap
// Command: javap -p -c com.example.UserService

public class com.example.UserService {
    private final com.example.UserRepository repository;

    public com.example.UserService(com.example.UserRepository);
    public java.util.List findAll();
    public com.example.User findById(java.lang.Long);
    public com.example.User save(com.example.User);
    public void delete(java.lang.Long);
}

// From this we learn:
// - Service depends on Repository (dependency injection)
// - Standard CRUD operations implemented
// - Uses Long for IDs
```

#### Pattern Recognition

```java
// Look for common patterns in decompiled code:

// Singleton Pattern
public class ConfigManager {
    private static ConfigManager instance;
    private ConfigManager() {}
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
}

// Factory Pattern
public class ConnectionFactory {
    public static Connection create(String type) {
        switch (type) {
            case "mysql": return new MySqlConnection();
            case "postgres": return new PostgresConnection();
            default: throw new IllegalArgumentException();
        }
    }
}

// Observer Pattern - look for listeners, callbacks, event handlers
```

### Step 4: Dynamic Analysis

**Objective**: Observe software behavior during execution

#### Debugging

```java
// Set breakpoints at key locations:
// 1. Entry points (main method, API handlers)
// 2. Decision points (if statements, switches)
// 3. Data transformations
// 4. Error handling

public class PaymentProcessor {

    public PaymentResult process(PaymentRequest request) {
        // BREAKPOINT 1: Inspect incoming request
        validate(request);

        // BREAKPOINT 2: Check validation result
        PaymentGateway gateway = selectGateway(request);

        // BREAKPOINT 3: See which gateway selected
        TransactionResult result = gateway.execute(request);

        // BREAKPOINT 4: Examine transaction result
        return mapToPaymentResult(result);
    }
}
```

#### Tracing and Logging

```java
// Add trace logging to understand flow
public class TracingProxy implements InvocationHandler {
    private final Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.println("ENTER: " + method.getName());
        System.out.println("ARGS: " + Arrays.toString(args));

        Object result = method.invoke(target, args);

        System.out.println("EXIT: " + method.getName());
        System.out.println("RESULT: " + result);

        return result;
    }
}
```

#### Memory Analysis

```bash
# Generate heap dump
jmap -dump:format=b,file=heap.hprof <pid>

# Analyze with tools like:
# - Eclipse Memory Analyzer (MAT)
# - VisualVM
# - IntelliJ Profiler
```

### Step 5: Hypothesis Testing

**Objective**: Validate understanding through experimentation

```java
// Example: Testing hypothesis about caching behavior

// Hypothesis: "This service caches user data for 5 minutes"

// Test 1: First call
User user1 = userService.findById(1L);  // Database query logged

// Test 2: Immediate second call
User user2 = userService.findById(1L);  // No database query = cached!

// Test 3: Wait 5 minutes, call again
Thread.sleep(300000);
User user3 = userService.findById(1L);  // Database query logged = cache expired

// Hypothesis confirmed!
```

### Step 6: Documentation

**Objective**: Record findings in useful formats

#### Class Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                        DISCOVERED ARCHITECTURE                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌────────────────┐         ┌─────────────────┐                    │
│  │ UserController │────────►│  UserService    │                    │
│  └────────────────┘         └────────┬────────┘                    │
│                                      │                              │
│                                      ▼                              │
│                             ┌─────────────────┐                    │
│                             │ UserRepository  │                    │
│                             └────────┬────────┘                    │
│                                      │                              │
│                                      ▼                              │
│                             ┌─────────────────┐                    │
│                             │    Database     │                    │
│                             └─────────────────┘                    │
│                                                                     │
│  Relationships:                                                     │
│  • Controller uses Service (dependency injection)                   │
│  • Service uses Repository (data access abstraction)                │
│  • Repository extends JpaRepository (Spring Data JPA)               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

#### API Documentation

| Endpoint | Method | Parameters | Returns | Notes |
|----------|--------|------------|---------|-------|
| /api/users | GET | page, size | Page<User> | Paginated |
| /api/users/{id} | GET | id (Long) | User | 404 if not found |
| /api/users | POST | User (body) | User | Validates email |
| /api/users/{id} | PUT | id, User | User | Full update |
| /api/users/{id} | DELETE | id | void | Soft delete |

---

## Tools for Reverse Engineering

### Java-Specific Tools

```
┌─────────────────────────────────────────────────────────────────────┐
│                     JAVA REVERSE ENGINEERING TOOLS                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  DECOMPILERS                       DEBUGGERS                        │
│  ───────────                       ─────────                        │
│  • JD-GUI                          • IntelliJ IDEA Debugger         │
│  • CFR                             • Eclipse Debugger               │
│  • Procyon                         • jdb (command line)             │
│  • Fernflower (IntelliJ)           • VisualVM                       │
│  • JAD                                                              │
│                                                                     │
│  BYTECODE ANALYSIS                 PROFILERS                        │
│  ─────────────────                 ─────────                        │
│  • javap (built-in)                • JProfiler                      │
│  • ASM Bytecode Viewer             • YourKit                        │
│  • Bytecode Visualizer             • Java Flight Recorder           │
│  • JBE (Java Bytecode Editor)      • Async Profiler                 │
│                                                                     │
│  MEMORY ANALYSIS                   STATIC ANALYSIS                  │
│  ───────────────                   ───────────────                  │
│  • Eclipse MAT                     • SonarQube                      │
│  • VisualVM                        • SpotBugs                       │
│  • JConsole                        • PMD                            │
│  • jmap, jhat                      • Checkstyle                     │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Tool Comparison

| Tool | Type | Best For | Ease of Use |
|------|------|----------|-------------|
| **JD-GUI** | Decompiler | Quick viewing | Easy |
| **CFR** | Decompiler | Modern Java features | Medium |
| **IntelliJ IDEA** | IDE + Decompiler | Integrated workflow | Easy |
| **javap** | Bytecode viewer | Quick inspection | Medium |
| **VisualVM** | Profiler + Monitor | Performance analysis | Easy |
| **Eclipse MAT** | Memory analyzer | Memory leak detection | Medium |
| **JProfiler** | Profiler | Deep performance analysis | Medium |

### Decompilers in Detail

#### JD-GUI

```bash
# Download from: http://java-decompiler.github.io/

# Usage:
# 1. Open JD-GUI
# 2. File > Open > Select JAR or class file
# 3. Browse decompiled source
# 4. File > Save All Sources (export)
```

**Features**:
- Simple drag-and-drop interface
- Syntax highlighting
- Navigation between classes
- Export to ZIP

#### CFR (Class File Reader)

```bash
# Download: https://www.benf.org/other/cfr/

# Decompile single class
java -jar cfr.jar MyClass.class

# Decompile entire JAR
java -jar cfr.jar myapp.jar --outputdir ./output

# With options
java -jar cfr.jar myapp.jar \
    --outputdir ./output \
    --comments true \
    --decodelambdas true
```

**Advantages**:
- Handles modern Java features (lambdas, streams)
- Command-line friendly
- Good reconstruction of control flow

#### Using IntelliJ IDEA

```
IntelliJ IDEA Built-in Decompiler (Fernflower):

1. Open any .class file directly
   - IntelliJ automatically decompiles

2. Navigate to library classes
   - Ctrl+Click on any class name
   - View decompiled source

3. Attach source vs. Decompile
   - If source available: shows original
   - If not: shows decompiled version

4. Compare decompiled with original
   - Useful for understanding compilation
```

### Bytecode Analysis with javap

```bash
# Basic class info
javap com.example.MyClass

# Output:
# Compiled from "MyClass.java"
# public class com.example.MyClass {
#   public com.example.MyClass();
#   public void doSomething();
# }

# With private members
javap -p com.example.MyClass

# With bytecode instructions
javap -c com.example.MyClass

# Full verbose output
javap -v com.example.MyClass
```

#### Understanding Bytecode

```java
// Original Java code
public int add(int a, int b) {
    return a + b;
}

// Bytecode (javap -c output)
public int add(int, int);
  Code:
     0: iload_1       // Load first parameter
     1: iload_2       // Load second parameter
     2: iadd          // Add integers
     3: ireturn       // Return integer result
```

### Debugging Tools

#### IntelliJ IDEA Debugger

```
Key Features for Reverse Engineering:

1. Evaluate Expression (Alt+F8)
   - Test hypotheses about code behavior
   - Call methods to understand results
   - Inspect complex expressions

2. Memory View
   - See all instances of a class
   - Track object creation
   - Identify memory issues

3. Stream Debugger
   - Visualize stream operations
   - See intermediate results
   - Debug lambda expressions

4. Method Breakpoints
   - Break on method entry/exit
   - Useful for interface implementations
   - Track polymorphic calls

5. Field Watchpoints
   - Break when field is read/modified
   - Track state changes
   - Find unexpected modifications
```

#### VisualVM

```
VisualVM Capabilities:

1. Monitor
   - CPU usage over time
   - Memory usage (heap, metaspace)
   - Thread count and states
   - Class loading

2. Sampler
   - CPU sampling (find hot methods)
   - Memory sampling (allocation hotspots)

3. Profiler
   - Detailed CPU profiling
   - Memory profiling
   - Lock profiling

4. Threads
   - Thread timeline
   - Thread dumps
   - Deadlock detection

5. Heap Dump
   - Create heap dump
   - Analyze object retention
   - Find memory leaks
```

### Network Analysis Tools

| Tool | Purpose | Use Case |
|------|---------|----------|
| **Wireshark** | Packet capture | Protocol analysis |
| **Fiddler** | HTTP proxy | Web traffic inspection |
| **Charles Proxy** | HTTP proxy | Mobile/Web debugging |
| **tcpdump** | CLI packet capture | Server-side analysis |
| **Browser DevTools** | HTTP inspection | Web application analysis |

---

## Practical Examples

### Example 1: Understanding a Library

**Scenario**: Understand how Jackson JSON parsing works

```java
// Step 1: Write test code
ObjectMapper mapper = new ObjectMapper();
String json = "{\"name\":\"John\",\"age\":30}";
User user = mapper.readValue(json, User.class);

// Step 2: Set breakpoint in your code
// Step 3: Step Into readValue() method
// Step 4: Observe internal flow:
//   - JsonParser created
//   - Token reading
//   - BeanDeserializer invoked
//   - Property setters called

// Discovery: Jackson uses reflection to set bean properties
```

### Example 2: Analyzing Closed-Source JAR

```bash
# Step 1: Extract JAR
mkdir analysis
cd analysis
jar -xf ../unknown-lib.jar

# Step 2: List package structure
find . -name "*.class" | head -20

# Step 3: Decompile with CFR
java -jar cfr.jar ../unknown-lib.jar --outputdir ./decompiled

# Step 4: Analyze main classes
cat decompiled/com/vendor/Main.java

# Step 5: Look for entry points
grep -r "public static void main" decompiled/

# Step 6: Find API classes
ls decompiled/com/vendor/api/
```

### Example 3: Debugging Integration Issue

```java
// Problem: Third-party library throws cryptic error

// Step 1: Enable debug logging
System.setProperty("org.apache.http.wire", "DEBUG");

// Step 2: Set exception breakpoint on the error type
// Debug > View Breakpoints > + > Java Exception Breakpoint

// Step 3: When breakpoint hits, examine:
// - Call stack (how we got here)
// - Variables (what data caused the error)
// - Step through code to find root cause

// Step 4: Document finding
// "Library requires X header but our request missing it"
```

### Example 4: Performance Investigation

```java
// Problem: Application is slow

// Step 1: Profile with VisualVM
// Connect to running application
// Start CPU sampling

// Step 2: Identify hot methods
// Look for methods consuming most CPU time

// Step 3: Examine the hot method
public List<User> findActiveUsers() {
    return users.stream()
        .filter(u -> isActive(u))        // Called N times
        .filter(u -> hasPermission(u))   // Database call each time!
        .collect(Collectors.toList());
}

// Step 4: Discovery
// hasPermission() makes DB call for each user
// N+1 query problem identified

// Step 5: Document and fix
// Batch load permissions instead of per-user
```

---

## Best Practices

### Do's and Don'ts

```
┌─────────────────────────────────────────────────────────────────────┐
│                    REVERSE ENGINEERING PRACTICES                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  DO:                                DON'T:                          │
│  ───                                ──────                          │
│  ✓ Get proper authorization         ✗ Reverse engineer maliciously │
│  ✓ Use isolated environments        ✗ Modify production systems    │
│  ✓ Document everything              ✗ Skip the planning phase      │
│  ✓ Start with reconnaissance        ✗ Jump directly to decompiling │
│  ✓ Form hypotheses first            ✗ Randomly browse code         │
│  ✓ Validate findings                ✗ Assume without testing       │
│  ✓ Respect intellectual property    ✗ Violate licensing terms      │
│  ✓ Share knowledge appropriately    ✗ Distribute decompiled code   │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Ethical Considerations

| Situation | Guideline |
|-----------|-----------|
| **Own code** | Always permissible |
| **Open source** | Follow license terms |
| **Third-party libraries** | For learning/debugging only |
| **Proprietary software** | Requires explicit permission |
| **Security research** | Follow responsible disclosure |
| **Competitive analysis** | Legal gray area - consult legal |

### Documentation Standards

```markdown
# Reverse Engineering Report Template

## Target
- Name: [Software name]
- Version: [Version]
- Purpose: [What it does]

## Objectives
- [ ] Primary objective
- [ ] Secondary objectives

## Methodology
- Tools used
- Approach taken
- Time spent

## Findings

### Architecture
[Description of overall structure]

### Key Components
| Component | Purpose | Dependencies |
|-----------|---------|--------------|
| ... | ... | ... |

### Algorithms
[Description of important algorithms discovered]

### Data Structures
[Description of data structures used]

## Conclusions
[Summary of findings and recommendations]

## References
[Links to tools, documentation, related materials]
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| **Definition** | Analyzing software to understand without source code |
| **Goals** | Understanding, learning, maintenance, integration, security |
| **Objectives** | Identify components, map dependencies, trace data flow |
| **Steps** | Plan → Recon → Analyze → Hypothesize → Test → Document |
| **Tools** | Decompilers (JD-GUI, CFR), Debuggers (IntelliJ), Profilers (VisualVM) |
| **Ethics** | Always get authorization, respect IP, document responsibly |

### Quick Reference: When to Use Which Tool

| Task | Recommended Tool |
|------|-----------------|
| Quick class inspection | javap, IntelliJ |
| Full decompilation | CFR, JD-GUI |
| Runtime debugging | IntelliJ Debugger |
| Performance analysis | VisualVM, JProfiler |
| Memory analysis | Eclipse MAT, VisualVM |
| Network traffic | Wireshark, Fiddler |
| HTTP APIs | Browser DevTools, Postman |

---

**Remember**: Reverse engineering is a valuable skill for understanding, debugging, and learning. Always use it ethically and within legal boundaries. The goal is to become a better developer by understanding how software works at a deeper level.

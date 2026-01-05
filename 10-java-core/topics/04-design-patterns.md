# Design Patterns

## SOLID Principles

SOLID is an acronym for five design principles that make software designs more understandable, flexible, and maintainable.

### Single Responsibility Principle (SRP)

A class should have only one reason to change (one responsibility).

```java
// BAD: Multiple responsibilities
class User {
    private String name;
    private String email;

    // User management
    public void save() {
        // Save to database
    }

    // Email functionality
    public void sendEmail(String message) {
        // Send email
    }

    // Report generation
    public void generateReport() {
        // Generate report
    }
}

// GOOD: Single responsibility
class User {
    private String name;
    private String email;

    // Getters and setters
}

class UserRepository {
    public void save(User user) {
        // Save to database
    }
}

class EmailService {
    public void sendEmail(User user, String message) {
        // Send email
    }
}

class ReportGenerator {
    public void generateUserReport(User user) {
        // Generate report
    }
}
```

### Open/Closed Principle (OCP)

Software entities should be open for extension but closed for modification.

```java
// BAD: Modifying existing code for new types
class AreaCalculator {
    public double calculateArea(Object shape) {
        if (shape instanceof Circle) {
            Circle circle = (Circle) shape;
            return Math.PI * circle.radius * circle.radius;
        } else if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return rect.width * rect.height;
        }
        // Need to modify this method for new shapes!
        return 0;
    }
}

// GOOD: Open for extension, closed for modification
interface Shape {
    double calculateArea();
}

class Circle implements Shape {
    private double radius;

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    private double width;
    private double height;

    @Override
    public double calculateArea() {
        return width * height;
    }
}

class Triangle implements Shape {
    private double base;
    private double height;

    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
}

class AreaCalculator {
    public double calculateArea(Shape shape) {
        return shape.calculateArea();  // No modification needed!
    }
}
```

### Liskov Substitution Principle (LSP)

Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

```java
// BAD: Violates LSP
class Bird {
    public void fly() {
        System.out.println("Flying");
    }
}

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly!");
    }
}

// Client code breaks when using Penguin
void makeBirdFly(Bird bird) {
    bird.fly();  // Throws exception if bird is Penguin!
}

// GOOD: Follows LSP
interface Bird {
    void eat();
}

interface FlyingBird extends Bird {
    void fly();
}

class Sparrow implements FlyingBird {
    @Override
    public void eat() {
        System.out.println("Eating");
    }

    @Override
    public void fly() {
        System.out.println("Flying");
    }
}

class Penguin implements Bird {
    @Override
    public void eat() {
        System.out.println("Eating");
    }
}
```

### Interface Segregation Principle (ISP)

Clients should not be forced to depend on interfaces they don't use.

```java
// BAD: Fat interface
interface Worker {
    void work();
    void eat();
    void sleep();
}

class HumanWorker implements Worker {
    @Override
    public void work() { /* work */ }

    @Override
    public void eat() { /* eat */ }

    @Override
    public void sleep() { /* sleep */ }
}

class RobotWorker implements Worker {
    @Override
    public void work() { /* work */ }

    @Override
    public void eat() { /* robots don't eat! */ }

    @Override
    public void sleep() { /* robots don't sleep! */ }
}

// GOOD: Segregated interfaces
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class HumanWorker implements Workable, Eatable, Sleepable {
    @Override
    public void work() { /* work */ }

    @Override
    public void eat() { /* eat */ }

    @Override
    public void sleep() { /* sleep */ }
}

class RobotWorker implements Workable {
    @Override
    public void work() { /* work */ }
}
```

### Dependency Inversion Principle (DIP)

High-level modules should not depend on low-level modules. Both should depend on abstractions.

```java
// BAD: High-level depends on low-level
class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}

class UserService {
    private MySQLDatabase database = new MySQLDatabase();

    public void saveUser(String user) {
        database.save(user);  // Tightly coupled to MySQL!
    }
}

// GOOD: Both depend on abstraction
interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}

class MongoDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println("Saving to MongoDB: " + data);
    }
}

class UserService {
    private Database database;

    // Dependency injection
    public UserService(Database database) {
        this.database = database;
    }

    public void saveUser(String user) {
        database.save(user);  // Works with any Database implementation!
    }
}

// Usage
Database mysql = new MySQLDatabase();
UserService service1 = new UserService(mysql);

Database mongo = new MongoDatabase();
UserService service2 = new UserService(mongo);
```

---

## Creational Patterns

### Singleton Pattern

Ensure a class has only one instance and provide global access to it.

```java
// Eager initialization
public class Singleton {
    private static final Singleton instance = new Singleton();

    private Singleton() {
        // Private constructor
    }

    public static Singleton getInstance() {
        return instance;
    }
}

// Lazy initialization (not thread-safe)
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}

// Thread-safe lazy initialization (synchronized)
public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}

// Double-checked locking
public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {}

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}

// Bill Pugh Singleton (best approach)
public class BillPughSingleton {
    private BillPughSingleton() {}

    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
}

// Enum Singleton (Joshua Bloch's approach)
public enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("Doing something");
    }
}

// Usage
EnumSingleton.INSTANCE.doSomething();
```

### Factory Pattern

Define an interface for creating objects, but let subclasses decide which class to instantiate.

```java
// Product interface
interface Shape {
    void draw();
}

// Concrete products
class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

class Triangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Drawing Triangle");
    }
}

// Simple Factory
class ShapeFactory {
    public static Shape createShape(String type) {
        return switch (type.toLowerCase()) {
            case "circle" -> new Circle();
            case "rectangle" -> new Rectangle();
            case "triangle" -> new Triangle();
            default -> throw new IllegalArgumentException("Unknown shape: " + type);
        };
    }
}

// Usage
Shape circle = ShapeFactory.createShape("circle");
circle.draw();

// Factory Method Pattern
abstract class ShapeCreator {
    public abstract Shape createShape();

    public void renderShape() {
        Shape shape = createShape();
        shape.draw();
    }
}

class CircleCreator extends ShapeCreator {
    @Override
    public Shape createShape() {
        return new Circle();
    }
}

class RectangleCreator extends ShapeCreator {
    @Override
    public Shape createShape() {
        return new Rectangle();
    }
}

// Usage
ShapeCreator creator = new CircleCreator();
creator.renderShape();
```

### Builder Pattern

Construct complex objects step by step.

```java
// Product
class User {
    private final String firstName;     // Required
    private final String lastName;      // Required
    private final int age;              // Optional
    private final String phone;         // Optional
    private final String address;       // Optional

    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    // Static nested Builder class
    public static class UserBuilder {
        private final String firstName;
        private final String lastName;
        private int age;
        private String phone;
        private String address;

        public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

// Usage
User user1 = new User.UserBuilder("John", "Doe")
    .age(30)
    .phone("123-456-7890")
    .address("123 Main St")
    .build();

User user2 = new User.UserBuilder("Jane", "Smith")
    .age(25)
    .build();
```

---

## Structural Patterns

### Adapter Pattern

Convert interface of a class into another interface clients expect.

```java
// Target interface (what client expects)
interface MediaPlayer {
    void play(String audioType, String fileName);
}

// Adaptee (incompatible interface)
interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}

class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing VLC file: " + fileName);
    }

    @Override
    public void playMp4(String fileName) {
        // Do nothing
    }
}

class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        // Do nothing
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing MP4 file: " + fileName);
    }
}

// Adapter
class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedPlayer;

    public MediaAdapter(String audioType) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("vlc")) {
            advancedPlayer.playVlc(fileName);
        } else if (audioType.equalsIgnoreCase("mp4")) {
            advancedPlayer.playMp4(fileName);
        }
    }
}

// Client
class AudioPlayer implements MediaPlayer {
    @Override
    public void play(String audioType, String fileName) {
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing MP3 file: " + fileName);
        } else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
            MediaAdapter adapter = new MediaAdapter(audioType);
            adapter.play(audioType, fileName);
        } else {
            System.out.println("Invalid format: " + audioType);
        }
    }
}

// Usage
MediaPlayer player = new AudioPlayer();
player.play("mp3", "song.mp3");
player.play("vlc", "movie.vlc");
player.play("mp4", "video.mp4");
```

### Decorator Pattern

Attach additional responsibilities to an object dynamically.

```java
// Component interface
interface Coffee {
    String getDescription();
    double getCost();
}

// Concrete component
class SimpleCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Simple Coffee";
    }

    @Override
    public double getCost() {
        return 2.0;
    }
}

// Decorator base class
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public String getDescription() {
        return coffee.getDescription();
    }

    @Override
    public double getCost() {
        return coffee.getCost();
    }
}

// Concrete decorators
class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.5;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Sugar";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.2;
    }
}

class WhippedCreamDecorator extends CoffeeDecorator {
    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return coffee.getDescription() + ", Whipped Cream";
    }

    @Override
    public double getCost() {
        return coffee.getCost() + 0.7;
    }
}

// Usage
Coffee coffee = new SimpleCoffee();
System.out.println(coffee.getDescription() + " $" + coffee.getCost());

coffee = new MilkDecorator(coffee);
System.out.println(coffee.getDescription() + " $" + coffee.getCost());

coffee = new SugarDecorator(coffee);
System.out.println(coffee.getDescription() + " $" + coffee.getCost());

coffee = new WhippedCreamDecorator(coffee);
System.out.println(coffee.getDescription() + " $" + coffee.getCost());

// Output:
// Simple Coffee $2.0
// Simple Coffee, Milk $2.5
// Simple Coffee, Milk, Sugar $2.7
// Simple Coffee, Milk, Sugar, Whipped Cream $3.4
```

### Facade Pattern

Provide a unified interface to a set of interfaces in a subsystem.

```java
// Complex subsystem
class CPU {
    public void freeze() {
        System.out.println("CPU: Freezing");
    }

    public void jump(long position) {
        System.out.println("CPU: Jumping to " + position);
    }

    public void execute() {
        System.out.println("CPU: Executing");
    }
}

class Memory {
    public void load(long position, byte[] data) {
        System.out.println("Memory: Loading data at " + position);
    }
}

class HardDrive {
    public byte[] read(long lba, int size) {
        System.out.println("HardDrive: Reading " + size + " bytes from " + lba);
        return new byte[size];
    }
}

// Facade
class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void start() {
        System.out.println("Starting computer...");
        cpu.freeze();
        memory.load(0, hardDrive.read(0, 1024));
        cpu.jump(0);
        cpu.execute();
        System.out.println("Computer started!");
    }
}

// Usage
ComputerFacade computer = new ComputerFacade();
computer.start();  // Simple interface to complex subsystem
```

---

## Behavioral Patterns

### Strategy Pattern

Define a family of algorithms, encapsulate each one, and make them interchangeable.

```java
// Strategy interface
interface PaymentStrategy {
    void pay(double amount);
}

// Concrete strategies
class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with credit card " + cardNumber);
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with PayPal account " + email);
    }
}

class BitcoinPayment implements PaymentStrategy {
    private String walletAddress;

    public BitcoinPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with Bitcoin wallet " + walletAddress);
    }
}

// Context
class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        paymentStrategy.pay(amount);
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();

cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
cart.checkout(100.0);

cart.setPaymentStrategy(new PayPalPayment("user@example.com"));
cart.checkout(50.0);

cart.setPaymentStrategy(new BitcoinPayment("1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"));
cart.checkout(75.0);
```

### Observer Pattern

Define a one-to-many dependency so that when one object changes state, all dependents are notified.

```java
import java.util.ArrayList;
import java.util.List;

// Observer interface
interface Observer {
    void update(String message);
}

// Subject interface
interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

// Concrete subject
class NewsAgency implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String news;

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(news);
        }
    }

    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
}

// Concrete observers
class EmailSubscriber implements Observer {
    private String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println("Email to " + email + ": " + message);
    }
}

class SMSSubscriber implements Observer {
    private String phoneNumber;

    public SMSSubscriber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void update(String message) {
        System.out.println("SMS to " + phoneNumber + ": " + message);
    }
}

// Usage
NewsAgency agency = new NewsAgency();

Observer emailSub = new EmailSubscriber("user@example.com");
Observer smsSub = new SMSSubscriber("123-456-7890");

agency.attach(emailSub);
agency.attach(smsSub);

agency.setNews("Breaking news: Java 21 released!");
// Output:
// Email to user@example.com: Breaking news: Java 21 released!
// SMS to 123-456-7890: Breaking news: Java 21 released!

agency.detach(smsSub);
agency.setNews("Update: New features in Java 21");
// Output:
// Email to user@example.com: Update: New features in Java 21
```

### Command Pattern

Encapsulate a request as an object.

```java
// Command interface
interface Command {
    void execute();
    void undo();
}

// Receiver
class Light {
    public void turnOn() {
        System.out.println("Light is ON");
    }

    public void turnOff() {
        System.out.println("Light is OFF");
    }
}

// Concrete commands
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }

    @Override
    public void undo() {
        light.turnOff();
    }
}

class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }

    @Override
    public void undo() {
        light.turnOn();
    }
}

// Invoker
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }

    public void pressUndo() {
        command.undo();
    }
}

// Usage
Light livingRoomLight = new Light();

Command lightOn = new LightOnCommand(livingRoomLight);
Command lightOff = new LightOffCommand(livingRoomLight);

RemoteControl remote = new RemoteControl();

remote.setCommand(lightOn);
remote.pressButton();  // Light is ON

remote.setCommand(lightOff);
remote.pressButton();  // Light is OFF

remote.pressUndo();    // Light is ON (undo off)
```

---

## Summary

| Pattern | Type | Purpose | Use When |
|---------|------|---------|----------|
| Singleton | Creational | Single instance | One instance needed globally |
| Factory | Creational | Create objects | Object creation logic complex |
| Builder | Creational | Construct complex objects | Many constructor parameters |
| Adapter | Structural | Interface compatibility | Incompatible interfaces |
| Decorator | Structural | Add responsibilities | Extend functionality dynamically |
| Facade | Structural | Simplified interface | Complex subsystem |
| Strategy | Behavioral | Interchangeable algorithms | Multiple algorithms for same purpose |
| Observer | Behavioral | Notify dependents | One-to-many dependency |
| Command | Behavioral | Encapsulate request | Parameterize objects with operations |

## Next Topic

Continue to [Collections Framework](./05-collections-framework.md) to learn about Java's collection classes and data structures.

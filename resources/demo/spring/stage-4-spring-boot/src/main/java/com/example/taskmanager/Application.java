package com.example.taskmanager;

import com.example.taskmanager.ui.ConsoleUI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application Entry Point.
 *
 * =====================================================
 * COMPARE THIS TO STAGE 3!
 * =====================================================
 *
 * STAGE 3:
 *     @Configuration
 *     @ComponentScan("com.example.taskmanager")
 *     @PropertySource("classpath:db.properties")
 *     public class AppConfig { }
 *
 *     public class Main {
 *         public static void main(String[] args) {
 *             ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
 *             ConsoleUI ui = context.getBean(ConsoleUI.class);
 *             ui.run();
 *         }
 *     }
 *
 * STAGE 4:
 *     @SpringBootApplication
 *     public class Application {
 *         public static void main(String[] args) {
 *             SpringApplication.run(Application.class, args);
 *         }
 *     }
 *
 * Spring Boot auto-configures:
 * - Component scanning
 * - Property loading
 * - DataSource
 * - JdbcTemplate
 * - Transaction management
 * - And much more!
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * CommandLineRunner runs after Spring Boot starts.
     * This is how we run our console UI in a Spring Boot app.
     */
    @Bean
    public CommandLineRunner commandLineRunner(ConsoleUI consoleUI) {
        return args -> {
            consoleUI.run();
        };
    }
}

package com.example.taskmanager.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring Configuration Class.
 *
 * This replaces XML configuration with Java-based configuration.
 *
 * @Configuration - Marks this as a Spring configuration class
 * @ComponentScan - Tells Spring where to look for @Component classes
 * @PropertySource - Loads external properties file
 */
@Configuration
@ComponentScan("com.example.taskmanager")
@PropertySource("classpath:db.properties")
public class AppConfig {
    /*
     * With @ComponentScan, Spring automatically finds and registers:
     * - @Component
     * - @Service
     * - @Repository
     * - @Controller
     *
     * No need to manually define beans for most classes!
     *
     * You CAN define beans explicitly here if needed:
     *
     * @Bean
     * public SomeExternalLibrary externalLib() {
     *     return new SomeExternalLibrary();
     * }
     */
}

package com.lanarimarco.modernization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for Modernized RPGLE Programs
 *
 * This application serves as the backend for modernized legacy RPGLE programs,
 * providing REST API endpoints to replace traditional green-screen interfaces.
 *
 * @author Modernization Team
 * @version 1.0.0
 */
@SpringBootApplication
public class ModernizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModernizationApplication.class, args);
    }
}

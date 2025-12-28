package com.lanarimarco.modernization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for RPGLE Modernization Project
 *
 * This application consolidates converted RPGLE programs into a modern
 * Java Spring Boot backend with REST APIs.
 */
@SpringBootApplication
public class ModernizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModernizationApplication.class, args);
    }
}

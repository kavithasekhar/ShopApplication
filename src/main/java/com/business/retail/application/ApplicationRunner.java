package com.business.retail.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class that starts the REST API service application
 * 
 * @author Kavitha
 *
 */
@SpringBootApplication
public class ApplicationRunner {
	public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }
}

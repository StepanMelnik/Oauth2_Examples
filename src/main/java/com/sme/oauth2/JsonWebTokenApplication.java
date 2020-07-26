package com.sme.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot application to authorize a request based on io.jsonwebtoken library.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.sme.oauth2.jsonwebtoken")
public class JsonWebTokenApplication
{
    /**
     * The main entry point to start application.
     * 
     * @param args The list of arguments.
     */
    public static void main(String[] args)
    {
        SpringApplication.run(JsonWebTokenApplication.class, "--spring.profiles.active=jsonwebtoken");
    }
}

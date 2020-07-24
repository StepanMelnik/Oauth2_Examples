package com.sme.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot application to authorize a request via GiHub OAuth2 client.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.sme.oauth2.github")
public class SocialGithubOAuth2Application
{
    /**
     * The main entry point to start application.
     * 
     * @param args The list of arguments.
     */
    public static void main(String[] args)
    {
        SpringApplication.run(SocialGithubOAuth2Application.class, args);
    }
}

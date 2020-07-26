package com.sme.oauth2.github.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Security configuration.
 */
@Configuration
public class SocialGithubConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler("/");

        // CSOFF
        // @formatter:off
        http.antMatcher("/**")
            .authorizeRequests(a ->
            {
                try
                {
                    a.antMatchers("/", "/logout").permitAll()
                        .anyRequest()
                        .authenticated()
                        .and().logout()
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .logoutSuccessUrl("/?logout")
                            .deleteCookies("JSESSIONID")
                            .permitAll()
                                .and()
                                .csrf()
                                .disable();
                                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                }
                catch (Exception e)
                {
                    throw new RuntimeException("Auth exception", e);
                }
            })
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .oauth2Login(o -> o
                .failureHandler((request, response, exception) -> {
                    request.getSession().setAttribute("error.message", exception.getMessage());
                    handler.onAuthenticationFailure(request, response, exception);
                }));
        // @formatter:on
        // CSON
    }
}

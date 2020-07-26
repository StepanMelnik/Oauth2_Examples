package com.sme.oauth2.jsonwebtoken.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sme.oauth2.jsonwebtoken.filter.JsonWebTokenFilter;
import com.sme.oauth2.jsonwebtoken.service.JsonWebTokenUserDetailsService;

/**
 * Security configuration.
 */
@SuppressWarnings("deprecation")
@EnableWebSecurity
public class JsonWebTokenConfig extends WebSecurityConfigurerAdapter
{
    private final JsonWebTokenUserDetailsService jsonWebTokenUserDetailsService;
    private final JsonWebTokenFilter jsonWebTokenFilter;

    public JsonWebTokenConfig(JsonWebTokenUserDetailsService jsonWebTokenUserDetailsService, JsonWebTokenFilter jsonWebTokenFilter)
    {
        this.jsonWebTokenUserDetailsService = jsonWebTokenUserDetailsService;
        this.jsonWebTokenFilter = jsonWebTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/", "/authenticate")
                .permitAll()
                .anyRequest()   // all other requests should be authenticated
                .authenticated()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // The config is stateless, no logout needed
        //  .and()
        //  .logout()
        //  .invalidateHttpSession(true)
        //  .clearAuthentication(true)
        //  .logoutSuccessUrl("/?logout")
        //  .deleteCookies("JSESSIONID");

        http.addFilterBefore(jsonWebTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(jsonWebTokenUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}

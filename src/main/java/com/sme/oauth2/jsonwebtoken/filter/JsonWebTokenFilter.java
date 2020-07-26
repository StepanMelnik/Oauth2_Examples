package com.sme.oauth2.jsonwebtoken.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sme.oauth2.jsonwebtoken.service.JsonWebTokenService;
import com.sme.oauth2.jsonwebtoken.service.JsonWebTokenUserDetailsService;

/**
 * The filter to process Authorization header.
 */
@Component
public class JsonWebTokenFilter extends OncePerRequestFilter
{
    private static final String AUTH_PREFIX = "Basic ";
    private final JsonWebTokenUserDetailsService jsonWebTokenUserDetailsService;
    private final JsonWebTokenService jsonWebTokenService;

    public JsonWebTokenFilter(JsonWebTokenUserDetailsService jsonWebTokenUserDetailsService, JsonWebTokenService jsonWebTokenService)
    {
        this.jsonWebTokenUserDetailsService = jsonWebTokenUserDetailsService;
        this.jsonWebTokenService = jsonWebTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        final Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader("Authorization"));

        Optional<String> username = Optional.empty();
        Optional<String> jwt = Optional.empty();

        if (authorizationHeader.isPresent() && authorizationHeader.get().startsWith(AUTH_PREFIX))
        {
            jwt = Optional.of(authorizationHeader.get().substring(AUTH_PREFIX.length()));
            username = Optional.of(jsonWebTokenService.extractUsername(jwt.get()));
        }

        if (username.isPresent())
        {
            UserDetails userDetails = this.jsonWebTokenUserDetailsService.loadUserByUsername(username.get());

            if (jsonWebTokenService.validateToken(jwt.get(), userDetails))
            {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}

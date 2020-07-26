package com.sme.oauth2.jsonwebtoken.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The service to work with JsonWebToken.
 */
@Service
@ConfigurationProperties(prefix = "jsonwebtoken", ignoreUnknownFields = true)
public class JsonWebTokenService
{
    private String secretKey;
    private long expirationInMs;

    /**
     * Set secrete key from properties.
     * 
     * @param secretKey The signing key to generate/fetch json web token.
     */
    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    /**
     * Set expiration from properties.
     * 
     * @param expirationInMs The expiration value in milliseconds.
     */
    public void setExpirationInMs(long expirationInMs)
    {
        this.expirationInMs = expirationInMs;
    }

    /**
     * Extract user name from token.
     * 
     * @param token The given token;
     * @return Returns extracted user name.
     */
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token.
     * 
     * @param token The given token;
     * @return Returns extracted expiration date.
     */
    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a clam by the given function.
     * 
     * @param <T> The type of extracted claim;
     * @param token The given token;
     * @param claimsResolver The function to resolve a claim;
     * @return Returns extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validate token.
     * 
     * @param token The given token;
     * @param userDetails The given user details;
     * @return Returns if a token is valid or not.
     */
    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Generate a token.
     * 
     * @param userDetails The given user details;
     * @return Returns a generated token.
     */
    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

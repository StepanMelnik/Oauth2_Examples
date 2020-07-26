package com.sme.oauth2.jsonwebtoken.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sme.oauth2.jsonwebtoken.model.AuthenticationRequest;
import com.sme.oauth2.jsonwebtoken.model.AuthenticationResponse;
import com.sme.oauth2.jsonwebtoken.service.JsonWebTokenService;
import com.sme.oauth2.jsonwebtoken.service.JsonWebTokenUserDetailsService;

/**
 * A controller to work with json web token.
 */
@RestController
public class JsonWebTokenController
{
    private final AuthenticationManager authenticationManager;
    private final JsonWebTokenUserDetailsService jsonWebTokenUserDetailsService;
    private final JsonWebTokenService jsonWebTokenService;

    public JsonWebTokenController(AuthenticationManager authenticationManager, JsonWebTokenUserDetailsService jsonWebTokenUserDetailsService, JsonWebTokenService jsonWebTokenService)
    {
        this.authenticationManager = authenticationManager;
        this.jsonWebTokenUserDetailsService = jsonWebTokenUserDetailsService;
        this.jsonWebTokenService = jsonWebTokenService;
    }

    @RequestMapping("")
    public String root()
    {
        return "Hello not securable JsonWebToken";
    }

    @RequestMapping("/secure/hello")
    public String hello()
    {
        return "Hello securable JsonWebToken";
    }

    /**
     * The mapping to create authentication token.
     * 
     * @param authenticationRequest The given request body;
     * @return Returns a response with generated web token.
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
    {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = jsonWebTokenUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jsonWebTokenService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

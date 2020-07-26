package com.sme.oauth2.jsonwebtoken.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sme.oauth2.jsonwebtoken.model.AuthUser;

/**
 * InMemory user details service.
 */
@Service
@ConfigurationProperties(prefix = "jsonwebtoken", ignoreUnknownFields = true)
public class JsonWebTokenUserDetailsService implements UserDetailsService
{
    private List<AuthUser> users = new ArrayList<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AuthUser authUser = users.stream()
                .filter(au -> au.getUsername().equals(username))
                .findAny()
                .orElseThrow(() -> new UsernameNotFoundException("User is not found with given username = " + username));

        return new User(authUser.getUsername(), authUser.getPassword(), new ArrayList<GrantedAuthority>());
    }

    public void setUsers(List<AuthUser> users)
    {
        this.users = users;
    }
}

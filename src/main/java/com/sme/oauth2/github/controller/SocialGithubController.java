package com.sme.oauth2.github.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to work with web application.
 */
@Controller
public class SocialGithubController
{
    /**
     * Get information about a logged-in user.
     * 
     * @param principal The logged in user;
     * @return Returns a map of user properties.
     */
    @GetMapping("/user")
    @ResponseBody
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal)
    {
        return Collections.unmodifiableMap(principal.getAttributes());
    }
}

package com.sme.oauth2.jsonwebtoken.model;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents authentication response.
 */
public class AuthenticationResponse
{
    private final String jwt;

    public AuthenticationResponse(String jwt)
    {
        this.jwt = jwt;
    }

    public String getJwt()
    {
        return jwt;
    }

    @Override
    public int hashCode()
    {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        return reflectionEquals(this, obj);
    }

    @Override
    public String toString()
    {
        return reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

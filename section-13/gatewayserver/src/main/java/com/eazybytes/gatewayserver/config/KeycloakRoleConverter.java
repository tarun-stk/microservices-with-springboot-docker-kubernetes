package com.eazybytes.gatewayserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
//This class main purpose is to convert the Jwt to extract roles from the access token
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
//        getClaims()  -> retrieves all payload details from access token
//        get("realm_access");  -> will retrieve all related roles assigned to clientCredentials
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
//        if no roles assigned then return new empty list
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
//        retrieving all roles based on key roles
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
//                appending all roles with "ROLE_" as spring security internally stores roles in this way
                .stream().map(roleName -> "ROLE_" + roleName)
//                convert Object to SimpleGrantedAuthority object
                .map(SimpleGrantedAuthority::new)
//                collecting into a list
                .collect(Collectors.toList());
//        finally return
        return returnValue;
    }
}

/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.security;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import de.faktorzehn.ipm.core.cdi.InMemory;

/**
 * An {@code AuthenticationProvider} that authenticates any user. It uses an
 * {@link UserDetailsService} to obtain the user's details which are set as the principal object in
 * the {@code Authentication} it returns.
 * <p>
 * For IPM, the {@code InMemoryIpmUserDetailsService} should be set as the user details service.
 * That way the principal is an {@code IpmUserDetails} instance and the
 * {@code SecurityContextCurrentUserProducer} can read it from the {@code SecurityContext}.
 */
@InMemory
class InMemoryAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails pricipal = userDetailsService.loadUserByUsername(authentication.getName());
        Collection<? extends GrantedAuthority> authorities = pricipal.getAuthorities();
        Object credentials = authentication.getCredentials();
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(pricipal, credentials,
                authorities);
        result.setDetails(authentication.getDetails());
        return result;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
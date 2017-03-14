/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import java.util.Collection;

import javax.annotation.Nullable;

import org.linkki.util.cdi.qualifier.InMemory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

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

    @Nullable
    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(@Nullable Authentication authentication) throws AuthenticationException {
        if (userDetailsService != null && authentication != null) {
            UserDetails principal = userDetailsService.loadUserByUsername(authentication.getName());
            Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
            Object credentials = authentication.getCredentials();
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal, credentials,
                    authorities);
            result.setDetails(authentication.getDetails());
            return result;
        } else {
            throw new IllegalStateException("Can't authenticate without UserDetailsService");
        }

    }

    @Override
    public boolean supports(@Nullable Class<?> authentication) {
        return true;
    }
}
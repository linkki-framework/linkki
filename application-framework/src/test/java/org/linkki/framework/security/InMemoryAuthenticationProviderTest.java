/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryAuthenticationProviderTest {

    @SuppressWarnings("null")
    @Mock
    UserDetailsService userDetailsService;

    @SuppressWarnings("null")
    @Mock
    UserDetails userDetails;

    @SuppressWarnings("null")
    @Mock
    Authentication inputAuthentication;

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER");

    @Before
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setUpMocks() {
        when(inputAuthentication.getName()).thenReturn("user");
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn((Collection)authorities);
    }

    @Test
    public void testAuthenticate() {
        InMemoryAuthenticationProvider authenticationProvider = new InMemoryAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);

        Authentication auth = authenticationProvider.authenticate(inputAuthentication);
        assertThat(auth.isAuthenticated(), is(true));
        assertThat(auth.getAuthorities(), hasSize(1));
        assertThat(auth.getAuthorities(), contains(authorities.get(0)));
        assertThat(auth.getPrincipal(), is(sameInstance(userDetails)));
    }

}

/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.linkki.util.cdi.BeanInstantiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

/**
 * Authentication using Active Directory.
 * 
 * Configuration via {@link ConfigResolver} using {@link #CONFIG_AD_URL}, {@link #CONFIG_AD_DOMAIN}
 * and {@link #CONFIG_AD_SEARCH_FILTER}.
 */
@Configuration
@EnableWebSecurity
public class ActiveDirectorySecurityConfig extends AbstractSecurityConfig {

    public static final String CONFIG_AD_URL = "ad_url";
    public static final String CONFIG_AD_DOMAIN = "ad_domain";
    public static final String CONFIG_AD_SEARCH_FILTER = "ad_searchFilter";

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        String url = ConfigResolver.getPropertyValue(CONFIG_AD_URL);
        String domain = ConfigResolver.getPropertyValue(CONFIG_AD_DOMAIN);

        ActiveDirectoryLdapAuthenticationProvider adAuthProvider = new ActiveDirectoryLdapAuthenticationProvider(
                domain, url);

        String searchFilter = ConfigResolver.getPropertyValue(CONFIG_AD_SEARCH_FILTER);
        adAuthProvider.setSearchFilter(searchFilter);
        adAuthProvider.setUserDetailsContextMapper(ipmUserDetailsMapper());

        auth.authenticationProvider(adAuthProvider);
    }

    @Bean
    public UserDetailsContextMapper ipmUserDetailsMapper() {
        return BeanInstantiator.getCDIInstance(UserDetailsContextMapper.class);
    }

}

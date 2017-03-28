/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.linkki.util.cdi.BeanInstantiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

/**
 * Authentication using Active Directory.
 *
 * Configuration via {@link ConfigResolver} using {@link #CONFIG_AD_URL}, {@link #CONFIG_AD_DOMAIN}
 * and {@link #CONFIG_AD_SEARCH_FILTER}.
 */
@Configuration
@EnableWebSecurity
public class ActiveDirectorySecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String CONFIG_AD_URL = "ad_url";
    public static final String CONFIG_AD_DOMAIN = "ad_domain";
    public static final String CONFIG_AD_SEARCH_FILTER = "ad_searchFilter";
    public static final String CONFIG_AD_USER = "ad_user";
    public static final String CONFIG_AD_PASSWORD = "ad_password";

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        String url = ConfigResolver.getPropertyValue(CONFIG_AD_URL);
        String domain = ConfigResolver.getPropertyValue(CONFIG_AD_DOMAIN);

        ActiveDirectoryLdapAuthenticationProvider adAuthProvider = new ActiveDirectoryLdapAuthenticationProvider(domain,
                url);

        String searchFilter = ConfigResolver.getPropertyValue(CONFIG_AD_SEARCH_FILTER);
        adAuthProvider.setSearchFilter(searchFilter);
        adAuthProvider.setUserDetailsContextMapper(ipmUserDetailsMapper());

        if (auth != null) {
            auth.authenticationProvider(adAuthProvider);
        }
    }

    // because... spring :(
    @SuppressWarnings("deprecation")
    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
                ConfigResolver.getPropertyValue(CONFIG_AD_URL));
        // for a more advanced usecase we can provide our own AuthenticationSource
        contextSource.setUserDn(ConfigResolver.getPropertyValue(CONFIG_AD_USER));
        contextSource.setPassword(ConfigResolver.getPropertyValue(CONFIG_AD_PASSWORD));

        DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource,
                ConfigResolver.getPropertyValue(KerberosUserDetailsServiceProducer.GROUPS_SEARCH_BASE));
        authoritiesPopulator.setIgnorePartialResultException(true);

        // deprecated but default is true and thats NOT what the
        // ActiveDirectoryLdapAuthenticationProvider#loadUserAuthorities does :(
        // and per default there is no prefix too
        authoritiesPopulator.setConvertToUpperCase(false);
        authoritiesPopulator.setRolePrefix(StringUtils.EMPTY);

        LdapUserDetailsService ldapService = new LdapUserDetailsService(
                new FilterBasedLdapUserSearch(
                        ConfigResolver.getPropertyValue(KerberosUserDetailsServiceProducer.SEARCH_BASE),
                        ConfigResolver.getPropertyValue(CONFIG_AD_SEARCH_FILTER),
                        contextSource),
                authoritiesPopulator);

        SpringUtil.afterPropertiesSet(contextSource);

        ldapService.setUserDetailsMapper(ipmUserDetailsMapper());
        return ldapService;
    }

    @Bean
    public UserDetailsContextMapper ipmUserDetailsMapper() {
        return BeanInstantiator.getCDIInstance(UserDetailsContextMapper.class);
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

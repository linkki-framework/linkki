/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import static org.linkki.framework.security.SpringUtil.afterPropertiesSet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.linkki.framework.state.ApplicationConfig;
import org.linkki.util.cdi.BeanInstantiator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.kerberos.client.config.SunJaasKrb5LoginConfig;
import org.springframework.security.kerberos.client.ldap.KerberosLdapContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

@ApplicationScoped
public class KerberosUserDetailsServiceProducer {

    public static final String KEYTAB_LOCATION = "kerberos_keyTabLocation";
    public static final String SERVICE_PRINCIPAL = "kerberos_servicePrinciple";
    public static final String SEARCH_BASE = "ad_searchBase";
    public static final String GROUPS_SEARCH_BASE = "ad_groupSearchBase";

    @Produces
    public UserDetailsService produce() {
        String url = ConfigResolver.getPropertyValue(ActiveDirectorySecurityConfig.CONFIG_AD_URL);
        String searchFilter = ConfigResolver.getPropertyValue(ActiveDirectorySecurityConfig.CONFIG_AD_SEARCH_FILTER);
        String keyTabLocation = ConfigResolver.getPropertyValue(KEYTAB_LOCATION);
        String servicePrincipal = ConfigResolver.getPropertyValue(SERVICE_PRINCIPAL);
        String searchBase = ConfigResolver.getPropertyValue(SEARCH_BASE);
        String groupSearchBase = ConfigResolver.getPropertyValue(GROUPS_SEARCH_BASE);

        KerberosLdapContextSource contextSource = new KerberosLdapContextSource(url);
        SunJaasKrb5LoginConfig loginConfig = new SunJaasKrb5LoginConfig();
        loginConfig.setKeyTabLocation(new FileSystemResource(keyTabLocation));
        loginConfig.setServicePrincipal(servicePrincipal);
        loginConfig.setDebug(ApplicationConfig.DEBUG);
        loginConfig.setIsInitiator(true);
        afterPropertiesSet(loginConfig);
        contextSource.setLoginConfig(loginConfig);
        afterPropertiesSet(contextSource);
        contextSource.afterPropertiesSet();

        DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource,
                groupSearchBase);
        authoritiesPopulator.setIgnorePartialResultException(true);
        LdapUserDetailsService ldapService = new LdapUserDetailsService(
                new FilterBasedLdapUserSearch(searchBase, searchFilter, contextSource), authoritiesPopulator);
        ldapService.setUserDetailsMapper(ipmUserDetailsMapper());
        return ldapService;
    }

    public UserDetailsContextMapper ipmUserDetailsMapper() {
        return BeanInstantiator.getCDIInstance(UserDetailsContextMapper.class);
    }

}

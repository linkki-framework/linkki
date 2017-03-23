/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import static org.linkki.framework.security.SpringUtil.afterPropertiesSet;

import javax.annotation.Nullable;
import javax.enterprise.util.AnnotationLiteral;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.linkki.framework.state.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Authentication using Kerberos.
 *
 * Configuration via {@link ConfigResolver}.
 */
@Configuration
@EnableWebSecurity
public class KerberosSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String CONFIG_PRINCIPLE_NAME = "kerberos_servicePrinciple";
    public static final String CONFIG_KEYTAB = "kerberos_keyTabLocation";

    /** The {@code @Kerberos} annotation. */
    private static final AnnotationLiteral<Kerberos> KERBEROS_ANNOTATION = new AnnotationLiteral<Kerberos>() {
        private static final long serialVersionUID = 1L;
    };


    @Override
    protected void configure(@Nullable HttpSecurity http) throws Exception {
        if (http != null) {
            http.addFilterBefore(spnegoAuthenticationProcessingFilter(authenticationManagerBean()),
                                 BasicAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(spnegoEntryPoint());
        }
    }

    @Override
    public void configure(@Nullable AuthenticationManagerBuilder auth) {
        if (auth != null) {
            auth.authenticationProvider(kerberosAuthenticationProvider());
            auth.authenticationProvider(kerberosServiceAuthenticationProvider());
        }
    }

    @Bean
    public SpnegoEntryPoint spnegoEntryPoint() {
        return new SpnegoEntryPoint("/login");
    }

    @Bean
    public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter(
            AuthenticationManager authenticationManager) {
        SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
        KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
        provider.setTicketValidator(sunJaasKerberosTicketValidator());
        provider.setUserDetailsService(ipmUserDetailsService());
        afterPropertiesSet(provider);
        return provider;
    }

    @Bean
    public KerberosAuthenticationProvider kerberosAuthenticationProvider() {
        KerberosAuthenticationProvider provider = new KerberosAuthenticationProvider();
        SunJaasKerberosClient client = new SunJaasKerberosClient();
        client.setDebug(ApplicationConfig.DEBUG);
        provider.setKerberosClient(client);
        provider.setUserDetailsService(ipmUserDetailsService());
        return provider;
    }

    /**
     * Create the user details service, uses {@link KerberosUserDetailsServiceProducer} if not
     * overwritten by injection configuration.
     *
     * @return The {@link UserDetailsService} to get user information like groups
     */
    @Bean
    public UserDetailsService ipmUserDetailsService() {
        return BeanProvider.getContextualReference(UserDetailsService.class, KERBEROS_ANNOTATION);
    }

    @Bean
    public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
        SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
        String servicePrincipal = ConfigResolver.getPropertyValue(CONFIG_PRINCIPLE_NAME);
        String keyTabLocation = ConfigResolver.getPropertyValue(CONFIG_KEYTAB);
        ticketValidator.setServicePrincipal(servicePrincipal);
        ticketValidator.setKeyTabLocation(new FileSystemResource(keyTabLocation));
        ticketValidator.setDebug(ApplicationConfig.DEBUG);
        afterPropertiesSet(ticketValidator);
        return ticketValidator;
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

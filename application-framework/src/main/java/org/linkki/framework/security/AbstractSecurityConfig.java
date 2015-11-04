/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import org.linkki.framework.state.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Authentication using Active Directory
 */
@Configuration
@EnableWebSecurity
public abstract class AbstractSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow access to stylesheet for login view
        web.debug(ApplicationConfig.DEBUG).ignoring().antMatchers("/VAADIN/themes/**").and().ignoring()
                .antMatchers("/PolicyBHBWebserviceImpl/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        // disable csrf (cross site request forgery) protection because it is handled by vaadin
        .csrf().disable()
                // Configure access to our web application
                .authorizeRequests().anyRequest().authenticated()
                // ...except the login page (and the Vaadin resources excluded above)
                .and().formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/main")
                .usernameParameter("username").passwordParameter("password").permitAll();
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

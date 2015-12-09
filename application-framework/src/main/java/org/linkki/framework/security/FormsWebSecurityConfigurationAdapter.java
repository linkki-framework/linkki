/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import org.linkki.framework.state.ApplicationConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(2)
public class FormsWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow access to stylesheet for login view
        web.debug(ApplicationConfig.DEBUG).ignoring().antMatchers("/VAADIN/themes/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
        // Configure access to our web application
        .authorizeRequests().anyRequest().authenticated()
                // ...except the login page (and the Vaadin resources excluded above)
                .and().formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/main")
                .failureUrl("/login").usernameParameter("username").passwordParameter("password").permitAll().and()
                // Logout configuration
                .logout().permitAll();

    }

}
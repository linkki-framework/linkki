/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public enum AuthenticationProvider {

    IN_MEMORY {
        @Override
        public Class<InMemorySecurityConfig> getSecurityConfigurer() {
            return InMemorySecurityConfig.class;
        }
    },
    ACTIVE_DIRECTORY {
        @Override
        public Class<ActiveDirectorySecurityConfig> getSecurityConfigurer() {
            return ActiveDirectorySecurityConfig.class;
        }
    },
    KERBEROS {
        @Override
        public Class<KerberosSecurityConfig> getSecurityConfigurer() {
            return KerberosSecurityConfig.class;
        }
    };

    public static final String PROPERTY_SECURITY_MODE = "authentication_provider";

    public static final AuthenticationProvider getCurrent() {
        String value = ConfigResolver.getPropertyValue(PROPERTY_SECURITY_MODE, IN_MEMORY.name());
        return AuthenticationProvider.valueOf(value);
    }

    public abstract Class<? extends WebSecurityConfigurerAdapter> getSecurityConfigurer();

}

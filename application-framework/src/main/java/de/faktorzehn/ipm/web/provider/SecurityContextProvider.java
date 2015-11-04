/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.provider;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import de.faktorzehn.ipm.core.cdi.IpmProvider;

/**
 * Provider for the current {@link SecurityContext} obtained from the current session and using
 * {@link SecurityContextHolder} as a fallback.
 *
 * @see SecurityContextHolder#getContext()
 */
public class SecurityContextProvider implements IpmProvider<SecurityContext> {

    @Override
    public SecurityContext get() {
        return SecurityContextHolder.getContext();
    }
}

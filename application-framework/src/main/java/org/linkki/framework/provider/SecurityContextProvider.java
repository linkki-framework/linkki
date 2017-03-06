/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.provider;

import org.linkki.util.cdi.IpmProvider;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provider for the current {@link SecurityContext} obtained from the current session and using
 * {@link SecurityContextHolder} as a fall-back.
 *
 * @see SecurityContextHolder#getContext()
 */
public class SecurityContextProvider implements IpmProvider<SecurityContext> {

    @Override
    public SecurityContext get() {
        return SecurityContextHolder.getContext();
    }
}

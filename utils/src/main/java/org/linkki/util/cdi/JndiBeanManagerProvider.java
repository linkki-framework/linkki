/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.util.cdi;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * A provider for {@link BeanManager} that uses JNDI to obtain the BeanManager.
 */
public class JndiBeanManagerProvider implements IpmProvider<BeanManager> {

    public static final String BEAN_MANAGER_JNDI_NAME = "java:comp/BeanManager";

    @Override
    public BeanManager get() {
        try {
            InitialContext initialContext = new InitialContext();
            Object beanManager = initialContext.lookup(BEAN_MANAGER_JNDI_NAME);
            if (!(beanManager instanceof BeanManager)) {
                throw new IllegalStateException("Lookup of BeanManager via JNDI failed, lookup returned " + beanManager);
            } else {
                return (BeanManager)beanManager;
            }
        } catch (NamingException e) {
            throw new IllegalStateException("Lookup of BeanManager via JNDI failed", e);
        }
    }

}

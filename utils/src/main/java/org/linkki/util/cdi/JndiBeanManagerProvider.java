/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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
                throw new IllegalStateException(
                        "Lookup of BeanManager via JNDI failed, lookup returned " + beanManager);
            } else {
                return (BeanManager)beanManager;
            }
        } catch (NamingException e) {
            throw new IllegalStateException("Lookup of BeanManager via JNDI failed", e);
        }
    }

}

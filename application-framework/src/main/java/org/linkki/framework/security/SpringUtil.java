/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.security;

import org.springframework.beans.factory.InitializingBean;

public class SpringUtil {

    private SpringUtil() {
        // do nothing
    }

    public static void afterPropertiesSet(InitializingBean bean) {
        // CSOFF: IllegalCatchCheck
        try {
            bean.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // CSON: IllegalCatchCheck
    }

}

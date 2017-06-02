/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.messages;

import org.linkki.core.nls.NlsService;

public class Messages {
    private static final String BUNDLE_NAME = "org/linkki/framework/ui/messages/messages"; //$NON-NLS-1$

    private Messages() {
        // do not instantiate
    }

    public static String getString(String key) {
        return NlsService.get().getString(BUNDLE_NAME, key, '!' + key + '!');

    }

}

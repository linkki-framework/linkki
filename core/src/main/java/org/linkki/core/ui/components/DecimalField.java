/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import java.util.Locale;

public class DecimalField extends DoubleField {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public DecimalField(Locale locale) {
        this("", locale);
    }

    public DecimalField(String pattern, Locale locale) {
        super(pattern, locale);
        setConverter(new DecimalFieldConverter(createFormat()));
    }

}

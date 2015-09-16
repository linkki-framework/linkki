/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.components;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalField extends NumberField {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public DecimalField() {
        super(NumberFormat.getNumberInstance(Locale.GERMANY));
        init();
    }

    public DecimalField(String format) {
        super(new DecimalFormat(format));
        init();
    }

    private void init() {
        setConverter(new DecimalFieldConverter(getFormat()));
    }

}

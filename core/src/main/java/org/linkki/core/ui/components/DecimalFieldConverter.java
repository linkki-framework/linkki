/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import java.text.NumberFormat;
import java.util.Locale;

import org.faktorips.values.Decimal;

public class DecimalFieldConverter extends AbstractNumberFieldConverter<Decimal> {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2694562525960273214L;

    public DecimalFieldConverter(NumberFormat format) {
        super(format);
    }

    @Override
    public Class<Decimal> getModelType() {
        return Decimal.class;
    }

    @Override
    protected Decimal convertToModel(Number value) {
        return Decimal.valueOf(value.doubleValue());
    }

    @Override
    public String convertToPresentation(Decimal value, Class<? extends String> targetType, Locale locale)
            throws ConversionException {

        if (value == null || value.isNull()) {
            return "";
        } else {
            return super.convertToPresentation(value, targetType, locale);
        }
    }
}

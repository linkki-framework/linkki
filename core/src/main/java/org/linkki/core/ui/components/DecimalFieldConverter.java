/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.components;

import java.text.NumberFormat;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    @Nonnull
    protected Decimal getNullValue() {
        return Decimal.NULL;
    }

    @Override
    public String convertToPresentation(@Nullable Decimal value,
            @Nullable Class<? extends String> targetType,
            @Nullable Locale locale)
            throws ConversionException {

        if (value == null || value.isNull()) {
            return "";
        } else {
            return super.convertToPresentation(value, targetType, locale);
        }
    }
}

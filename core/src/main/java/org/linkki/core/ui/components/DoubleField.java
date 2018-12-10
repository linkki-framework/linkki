/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.core.ui.components;

import static java.util.Objects.requireNonNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DoubleField extends NumberField {

    public static final String DEFAULT_DOUBLE_FORMAT = "#,##0.00##";

    private static final long serialVersionUID = 7642987884125435087L;

    private final Locale locale;

    private final String pattern;

    public DoubleField(Locale locale) {
        this(DEFAULT_DOUBLE_FORMAT, locale);
    }

    public DoubleField(String pattern, Locale locale) {
        super();
        this.pattern = requireNonNull(pattern, "pattern must not be null");
        this.locale = requireNonNull(locale, "locale must not be null");
        setConverter(createConverter());
    }

    private DoubleFieldConverter createConverter() {
        return new DoubleFieldConverter(createFormat());
    }

    protected final DecimalFormat createFormat() {
        return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
    }

}

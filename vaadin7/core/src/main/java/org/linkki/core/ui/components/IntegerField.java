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
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class IntegerField extends NumberField {

    private static final long serialVersionUID = 1L;

    private final String pattern;

    private final Locale locale;

    public IntegerField(Locale locale) {
        this("", locale);
    }

    public IntegerField(String pattern, Locale locale) {
        super();
        this.pattern = requireNonNull(pattern, "pattern must not be null");
        this.locale = requireNonNull(locale, "locale must not be null");
        init();
    }

    private void init() {
        setConverter(new IntegerFieldConverter(createFormat()));
    }

    private NumberFormat createFormat() {
        if (StringUtils.isEmpty(pattern)) {
            return NumberFormat.getIntegerInstance(locale);
        } else {
            return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
        }
    }

}

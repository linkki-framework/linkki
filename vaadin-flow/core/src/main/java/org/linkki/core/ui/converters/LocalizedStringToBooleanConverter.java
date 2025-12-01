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
package org.linkki.core.ui.converters;

import java.util.Locale;

import org.linkki.core.ui.nls.NlsText;

import com.vaadin.flow.data.converter.StringToBooleanConverter;

/**
 * A localized converter between {@link String} and {@link Boolean}.
 * <p>
 * This implementation returns locale-specific representations for the boolean values:
 * <ul>
 * <li>German: "Ja / Nein"</li>
 * <li>English: "Yes / No"</li>
 * </ul>
 */
public class LocalizedStringToBooleanConverter extends StringToBooleanConverter {

    public LocalizedStringToBooleanConverter() {
        super("");
    }

    @Override
    protected String getTrueString(Locale locale) {
        return NlsText.getString("LocalizedStringToBooleanConverter.True", locale);
    }

    @Override
    protected String getFalseString(Locale locale) {
        return NlsText.getString("LocalizedStringToBooleanConverter.False", locale);
    }
}

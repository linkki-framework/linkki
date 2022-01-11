/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.vaadin.component.base;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linkki.util.DateFormats;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;

@JsModule(value = "./styles/date-picker-pattern.js")
@NpmPackage(value = "luxon", version = "1.27.0")
public class LinkkiDatePicker extends DatePicker {

    private static final long serialVersionUID = 1L;

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);

        String pattern = DateFormats.getPattern(locale);
        String shortPattern = pattern.replace("dd", "d").replace("MM", "M").replace("yyyy", "yy");
        String patternWithoutSeparators = pattern.replaceAll("[^a-zA-Z]", "").replace("yyyy", "yy");
        setPatterns(pattern, shortPattern, patternWithoutSeparators);
    }

    /**
     * Sets the date patterns for parsing. The first pattern is also used for formatting.
     */
    private void setPatterns(String... patterns) {
        String patternArray = "[" + Stream.of(patterns)
                .map(pattern -> '"' + pattern + '"')
                .collect(Collectors.joining(","))
                + "]";

        // Execute on attach to support hiding/unhiding the component.
        addAttachListener(e -> this.getElement().executeJs("window.setDatePickerPatterns(this," + patternArray + ")"));
    }
}

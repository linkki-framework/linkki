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
package org.linkki.core.ui.section.annotations.adapters;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.util.UiUtil;
import org.linkki.util.DateFormatRegistry;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;

public class DateFieldBindingDefinitionTest {

    private static final String CUSTOM_DATE_FORMAT = "yy.dd.MM";

    @UIDateField(position = 0, label = "")
    public UIDateField defaultAnnotation() {
        try {
            return getClass().getMethod("defaultAnnotation", new Class<?>[] {}).getAnnotation(UIDateField.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIDateField(position = 0, label = "", dateFormat = CUSTOM_DATE_FORMAT)
    public UIDateField customAnnotation() {
        try {
            return getClass().getMethod("customAnnotation", new Class<?>[] {}).getAnnotation(UIDateField.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNewComponent_DefaultDateFormatIsUsed() {
        // Precondition
        assertThat(UiUtil.getUiLocale(), is(Locale.GERMAN));

        DateFieldBindingDefinition adapter = new DateFieldBindingDefinition(defaultAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(DateField.class)));
        DateField dateField = (DateField)component;
        assertThat(dateField.getDateFormat(), is(DateFormatRegistry.PATTERN_DE));
    }

    @Test
    public void testNewComponent_CustomDateFormatIsUsed() {

        DateFieldBindingDefinition adapter = new DateFieldBindingDefinition(customAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(DateField.class)));
        DateField dateField = (DateField)component;
        assertThat(dateField.getDateFormat(), is(CUSTOM_DATE_FORMAT));
    }

}

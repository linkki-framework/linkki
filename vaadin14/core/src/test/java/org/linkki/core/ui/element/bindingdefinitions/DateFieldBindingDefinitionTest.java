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
package org.linkki.core.ui.element.bindingdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.element.annotation.UIDateField;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;

import edu.umd.cs.findbugs.annotations.NonNull;

public class DateFieldBindingDefinitionTest {

    @BeforeEach
    private void setUp() {
        UI ui = new UI();
        ui.setLocale(Locale.ENGLISH);
        UI.setCurrent(ui);
    }

    @AfterEach
    private void tearDown() {
        UI.setCurrent(null);
    }

    private UIDateField getAnnotation(String name) {
        try {
            @NonNull

            UIDateField annotation = getClass().getMethod(name, new Class<?>[] {})
                    .getAnnotation(UIDateField.class);
            return annotation;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIDateField(position = 0, label = "")
    public UIDateField defaultAnnotation() {
        return getAnnotation("defaultAnnotation");
    }

    @Test
    public void testNewComponent() {
        UI ui = new UI();
        ui.setLocale(Locale.ENGLISH);
        DateFieldBindingDefinition adapter = new DateFieldBindingDefinition(defaultAnnotation());

        Component component = adapter.newComponent();

        assertThat(component, is(instanceOf(DatePicker.class)));
    }

}

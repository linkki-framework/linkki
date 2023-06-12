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

package org.linkki.core.vaadin.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.TextArea;

@ExtendWith(KaribuUIExtension.class)
class ComponentFactoryTest {

    @Test
    void testNewDateField_BelowRange() {
        DatePicker dateField = ComponentFactory.newDateField();

        dateField.setValue(LocalDate.of(999, 1, 1));

        assertThat(dateField.isInvalid()).isTrue();
    }

    @Test
    void testNewDateField_InRangeLowerBound() {
        DatePicker dateField = ComponentFactory.newDateField();

        dateField.setValue(LocalDate.of(1000, 1, 1));

        assertThat(dateField.isInvalid()).isFalse();
    }

    @Test
    void testNewDateField_InRangeUpperBound() {
        DatePicker dateField = ComponentFactory.newDateField();

        dateField.setValue(LocalDate.of(9999, 12, 31));

        assertThat(dateField.isInvalid()).isFalse();
    }

    @Test
    void testNewDateField_AboveRange() {
        DatePicker dateField = ComponentFactory.newDateField();

        dateField.setValue(LocalDate.of(10000, 1, 1));

        assertThat(dateField.isInvalid()).isTrue();
    }

    @Test
    void testNewDateField_Default() {
        DatePicker dateField = ComponentFactory.newDateField();

        assertThat(dateField.getElement().getProperty("autoselect")).isEqualTo("true");
        assertThat(dateField.isAutoOpen()).isFalse();
    }

    @Test
    void testNewDateField_CustomAutoFeatures() {
        DatePicker dateField = ComponentFactory.newDateField(true, false);

        assertThat(dateField.getElement().getProperty("autoselect")).isEqualTo("false");
        assertThat(dateField.isAutoOpen()).isTrue();
    }

    @Test
    void testNewDateField_I18n() {
        UI.getCurrent().setLocale(Locale.GERMAN);
        DatePicker dateField = ComponentFactory.newDateField();

        assertThat(dateField.getI18n().getToday()).isEqualTo("Heute");
    }

    @Test
    void testNewTextArea() {
        TextArea textArea = ComponentFactory.newTextArea(250, "500px", "100px");

        assertThat(textArea.getMaxLength()).isEqualTo(250);
        assertThat(textArea.getWidth()).isEqualTo("500px");
        assertThat(textArea.getHeight()).isEqualTo("100px");
    }

    @Test
    void testNewTextArea_EmptyHeight() {
        TextArea textArea = ComponentFactory.newTextArea(250, "500px", "");

        assertThat(textArea.getHeight()).isNull();
    }

    @Test
    void testNewDateTimeField_BelowRange() {
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(60);

        dateTimeField.setValue(LocalDateTime.of(999, 1, 1, 0, 0));

        assertThat(dateTimeField.isInvalid()).isTrue();
    }

    @Test
    void testNewDateTimeField_InRangeLowerBound() {
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(60);

        dateTimeField.setValue(LocalDateTime.of(1000, 1, 1, 0, 0));

        assertThat(dateTimeField.isInvalid()).isFalse();
    }

    @Test
    void testNewDateTimeField_InRangeUpperBound() {
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(60);

        dateTimeField.setValue(LocalDateTime.of(9999, 12, 31, 23, 59, 59));

        assertThat(dateTimeField.isInvalid()).isFalse();
    }

    @Test
    void testNewDateTimeField_AboveRange() {
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(60);

        dateTimeField.setValue(LocalDateTime.of(10000, 1, 1, 0, 0));

        assertThat(dateTimeField.isInvalid()).isTrue();
    }


    @Test
    void testNewDateTimeField_Default() {
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(60);

        assertThat(Duration.ofMinutes(60)).isEqualTo(dateTimeField.getStep());
        assertThat(dateTimeField.isAutoOpen()).isFalse();
        assertThat(dateTimeField.getElement().getProperty("autoselect")).isEqualTo("true");
    }

    @Test
    void testNewDateTimeField_CustomAttributes() {
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(120, true, false);

        assertThat(Duration.ofMinutes(120)).isEqualTo(dateTimeField.getStep());
        assertThat(dateTimeField.isAutoOpen()).isTrue();
        assertThat(dateTimeField.getElement().getProperty("autoselect")).isEqualTo("false");
    }

    @Test
    void testNewDateTimeField_ThrowsExceptionForInvalidStep() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ComponentFactory.newDateTimeField(42));
    }

    @Test
    void testNewDateTimeField_I18n() {
        UI.getCurrent().setLocale(Locale.GERMAN);
        DateTimePicker dateTimeField = ComponentFactory.newDateTimeField(60);

        assertThat(dateTimeField.getDatePickerI18n().getToday()).isEqualTo("Heute");
    }

}

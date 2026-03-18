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

package org.linkki.core.ui.aspects.annotation;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.linkki.core.ui.creation.VaadinUiCreator.createComponent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.ui.test.KaribuUIExtension;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.shared.HasClearButton;

@ExtendWith(KaribuUIExtension.class)
class BindClearButtonIntegrationTest {

    @Test
    void testBindClearButton() {
        @UICssLayout
        class ClearButtonPmo {

            @BindClearButton
            @UITextField(position = 0)
            public String getText() {
                return "This is a text.";
            }

            @BindClearButton
            @UIDateField(position = 10)
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @BindClearButton
            @UIDateTimeField(position = 20)
            public LocalDateTime getDateTime() {
                return LocalDateTime.now();
            }
        }
        var pmo = new ClearButtonPmo();

        var component = createComponent(pmo, new BindingContext());

        var hasClearButtonFields = component.getChildren().filter(HasClearButton.class::isInstance).toList();
        var dateTimeField = _get(component, DateTimePicker.class);
        assertThat(hasClearButtonFields)
                .map(Component::getId)
                .containsExactly(Optional.of("text"), Optional.of("date"));
        assertThat(hasClearButtonFields)
                .map(HasClearButton.class::cast)
                .allMatch(HasClearButton::isClearButtonVisible);
        assertThat(dateTimeField.getId()).isEqualTo(Optional.of("dateTime"));
        assertThat(dateTimeField.getChildren())
                .map(HasClearButton.class::cast)
                .allMatch(HasClearButton::isClearButtonVisible);
    }

    @Test
    void testBindClearButton_NoClearButton() {
        @UICssLayout
        class NoClearButtonPmo {

            @UITextField(position = 0)
            public String getText() {
                return "This is a text.";
            }

            @UIDateField(position = 10)
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @UIDateTimeField(position = 20)
            public LocalDateTime getDateTime() {
                return LocalDateTime.now();
            }
        }

        var component = createComponent(new NoClearButtonPmo(), new BindingContext());

        var hasClearButtonFields = component.getChildren().filter(HasClearButton.class::isInstance).toList();
        var dateTimeField = _get(component, DateTimePicker.class);
        assertThat(hasClearButtonFields).map(Component::getId)
                .containsExactly(Optional.of("text"), Optional.of("date"));
        assertThat(hasClearButtonFields)
                .map(HasClearButton.class::cast)
                .noneMatch(HasClearButton::isClearButtonVisible);
        assertThat(dateTimeField.getId()).isEqualTo(Optional.of("dateTime"));
        assertThat(dateTimeField.getChildren())
                .map(HasClearButton.class::cast)
                .noneMatch(HasClearButton::isClearButtonVisible);
    }

    @Test
    void testBindClearButton_PrimitiveTypes() {
        @UICssLayout
        class ClearButtonWithPrimitivePmo {
            @BindClearButton
            @UIIntegerField(position = 0)
            public int getIntegerField() {
                return 42;
            }
        }

        assertThatThrownBy(() -> createComponent(new ClearButtonWithPrimitivePmo(), new BindingContext()))
                .hasMessageContaining("Getter method for the property 'integerField' returns a primitive type 'int' and @BindClearButton can only be used with non-primitive types.");
    }

    @Test
    void testBindClearButton_FieldDoesNotSupportClearButton() {
        @UICssLayout
        class NoClearButtonSupportPmo {

            @BindClearButton
            @UICheckBox(position = 0)
            public Boolean getCheckBox() {
                return true;
            }
        }

        assertThatThrownBy(() -> createComponent(new NoClearButtonSupportPmo(), new BindingContext()))
                .hasMessageContaining("@BindClearButton cannot be used with Checkbox as it does not support clear buttons.");
    }

}

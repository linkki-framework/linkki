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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.ui.aspects.annotation.BindAutoFocus.BindAutoFocusAspectDefinition;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

class BindAutoFocusAspectDefinitionTest {

    PropertyDispatcher dispatcher = new PropertyDispatcherFactory()
            .createDispatcherChain(new Object(),
                                   BoundProperty.empty(),
                                   PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

    @Test
    void testSetAutoFocus_TextArea() {
        var component = new TextArea();
        assertThat(component.isAutofocus(), is(false));

        apply(component);

        assertThat(component.isAutofocus(), is(true));
    }

    @Test
    void testSetAutoFocus_TextField() {
        var component = new TextField();
        assertThat(component.isAutofocus(), is(false));

        apply(component);

        assertThat(component.isAutofocus(), is(true));
    }

    @Test
    void testSetAutoFocus_ComboBox() {
        var component = new ComboBox<>();
        assertThat(component.isAutofocus(), is(false));

        apply(component);

        assertThat(component.isAutofocus(), is(true));
    }

    @Test
    void testSetAutoFocus_Checkbox() {
        var component = new Checkbox();
        assertThat(component.isAutofocus(), is(false));

        apply(component);

        assertThat(component.isAutofocus(), is(true));
    }

    @Test
    void testSetAutoFocus_DatePicker() {
        var component = new DatePicker();
        assertThat(component.getElement().getProperty("autofocus"), nullValue());

        apply(component);

        assertThat(component.getElement().getProperty("autofocus"), is("true"));
    }

    @Test
    void testSetAutoFocus_DateTimePicker() {
        var component = new DateTimePicker();
        assertThat(component.getChildren().findFirst().get().getElement().getProperty("autofocus"), nullValue());

        apply(component);

        assertThat(component.getChildren().findFirst().get().getElement().getProperty("autofocus"), is("true"));
    }

    @Test
    void testSetAutoFocus_RadioButton() {
        // RadioButtonGroup does not support property autofocus
        var component = new RadioButtonGroup<>();
        assertThat(component.getElement().getProperty("autoFocus"), nullValue());

        apply(component);

        assertThat(component.getElement().getProperty("autoFocus"), nullValue());
    }

    @Test
    void testSetAutoFocus_Button() {
        var component = new Button();
        assertThat(component.isAutofocus(), is(false));

        apply(component);

        // Button does not implement HasValue
        assertThat(component.isAutofocus(), is(false));
    }

    void apply(Component component) {
        new BindAutoFocusAspectDefinition().createUiUpdater(dispatcher, new NoLabelComponentWrapper(component)).apply();
    }

}

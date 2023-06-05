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

package org.linkki.samples.playground.bugs.lin2867;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;

public class FocusringBug extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public FocusringBug() {
        setPadding(false);
        setSpacing(false);

        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(new Span("Stepping through controls via TAB. Focusrings must be completely visible."));

        add(new TextField());
        add(new TextArea());
        add(new PasswordField());
        add(new DatePicker());
        add(new ComboBox<>());
        add(new Checkbox());

        RadioButtonGroup<Object> radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("Option 1", "Option 2");
        add(radioGroup);

        add(new Button("Button 1"));

        Button primaryButton = new Button("Primary Button");
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout horizontalLayout = new HorizontalLayout(primaryButton, new Button("Button 3"));
        horizontalLayout.setPadding(false);
        add(horizontalLayout);


        TextField textFieldWithError = new TextField();
        textFieldWithError.setInvalid(true);
        textFieldWithError.getElement().setAttribute("severity", "error");
        add(textFieldWithError);

        TextField textFieldWithWarning = new TextField();
        textFieldWithWarning.setInvalid(true);
        textFieldWithWarning.getElement().setAttribute("severity", "warning");
        add(textFieldWithWarning);

        TextField textFieldWithInfo = new TextField();
        textFieldWithInfo.setInvalid(true);
        textFieldWithInfo.getElement().setAttribute("severity", "info");
        add(textFieldWithInfo);

    }

}

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

package org.linkki.samples.playground.bugs.lin2915;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class OverflowIssues extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public OverflowIssues() {
        vaadinBugTertiaryInlineButton();
        vaadinBugCheckbox();
        requiredIndicatorWithEmptyLabel();
    }

    // Vaadin Bug https://github.com/vaadin/flow-components/issues/2872
    private void vaadinBugTertiaryInlineButton() {
        add(new Label("Icon Outlines should be rectangular:"));

        Div flexbox = new Div();
        flexbox.getStyle().set("display", "flex");
        flexbox.getStyle().set("flex-flow", "row nowrap");
        add(flexbox);

        TextField tf = new TextField();
        tf.setWidthFull();

        Button b1 = new Button();
        b1.setIcon(VaadinIcon.USER.create());
        b1.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        b1.getStyle().set("outline", "auto");

        Button b2 = new Button();
        b2.setIcon(VaadinIcon.USER.create());
        b2.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        b2.getStyle().set("outline", "auto");

        flexbox.add(tf, b1, b2);
    }

    // Vaadin Bug https://github.com/vaadin/flow-components/issues/2873
    private void vaadinBugCheckbox() {
        add(new Label("Checkbox Outline should be rectangular:"));

        HorizontalLayout wrapper = new HorizontalLayout();

        Checkbox cb = new Checkbox();
        cb.setLabel("I am a checkbox");
        cb.getStyle().set("outline", "auto");
        wrapper.add(cb);

        add(wrapper);
    }

    private void requiredIndicatorWithEmptyLabel() {
        add(new Label("This layout must not have a scrollbar:"));

        FormLayout formLayout = new FormLayout();

        FormItem formItem = formLayout.addFormItem(new Label("Ich bin ein Label ohne Label"), "");
        formItem.getStyle().set("overflow", "auto");

        add(formLayout);
    }

}

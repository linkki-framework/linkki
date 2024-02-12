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

package org.linkki.core.vaadin.component.base;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

/**
 * FormItem that only has one component as label. The label shares the required and invalid state
 * with the component.
 */
public class LabelComponentFormItem extends FormItem {

    private static final long serialVersionUID = 1L;

    private final NativeLabel label;
    private final Component component;

    public LabelComponentFormItem(Component component, String label) {
        this(component, new NativeLabel(requireNonNull(label, "label must not be null")));
    }

    /**
     * Constructs a new {@link LabelComponentFormItem} which inherits required status indicator from
     * the component wrapped in it.
     * 
     * @param component The field component
     * @param label The label
     */
    public LabelComponentFormItem(Component component, NativeLabel label) {
        this.component = requireNonNull(component, "component must not be null");
        this.label = requireNonNull(label, "label must not be null");

        add(component);
        addToLabel(label);

        if (component instanceof HasValue) {
            synchronizePropertyFromField(component, "readonly", HasValue::isReadOnly);

            if (component instanceof RadioButtonGroup<?>) {
                component.getElement()
                        .addPropertyChangeListener("disabled", e -> getElement()
                                .setAttribute("readonly", ((RadioButtonGroup<?>)component).isReadOnly()));
            }
        }
    }

    private void synchronizePropertyFromField(Component cmpt,
            String attribute,
            Predicate<HasValue<?, ?>> valueOfField) {
        HasValue<?, ?> field = (HasValue<?, ?>)cmpt;
        getElement().setAttribute(attribute, valueOfField.test(field));

        cmpt.getElement()
                .addPropertyChangeListener(attribute, e -> getElement()
                        .setAttribute(attribute, valueOfField.test(field)));
    }

    public Component getComponent() {
        return component;
    }

    public NativeLabel getLabel() {
        return label;
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    @Override
    public String toString() {
        return label.getText() + "(" + getComponent().getClass().getSimpleName() + ")";
    }
}
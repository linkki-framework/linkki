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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;

/**
 * {@link FormLayout} whose items has only one component as label.
 * 
 * @see LabelComponentFormItem
 */
@CssImport(value = "./styles/linkki-form-item.css", themeFor = "vaadin-form-item")
public class LinkkiFormLayout extends FormLayout {

    private static final long serialVersionUID = 1L;

    @Override
    public LabelComponentFormItem addFormItem(Component field, String label) {
        return addFormItem(field, new Label(label));
    }

    @Override
    public LabelComponentFormItem addFormItem(Component field, Component label) {
        LabelComponentFormItem formItem = new LabelComponentFormItem(field, label);
        add(formItem);
        return formItem;
    }

    /**
     * FormItem that only has one component as label. The label shares the required and invalid state
     * with the component.
     */
    public static class LabelComponentFormItem extends FormItem {

        private static final long serialVersionUID = 1L;

        /**
         * Constructs a new {@link LabelComponentFormItem} which inherits required status indicator from
         * the component wrapped in it.
         * 
         * @param component The field component
         * @param label The label
         */
        public LabelComponentFormItem(Component component, Component label) {
            add(component);
            addToLabel(label);

            if (component instanceof HasValue) {
                HasValue<?, ?> field = (HasValue<?, ?>)component;
                String requiredAttribute = "required";
                getElement().setAttribute(requiredAttribute, field.isRequiredIndicatorVisible());

                component.getElement()
                        .addPropertyChangeListener(requiredAttribute, e -> getElement()
                                .setAttribute(requiredAttribute, field.isRequiredIndicatorVisible()));

                if (component instanceof HasValidation) {
                    component.getElement().addPropertyChangeListener("invalid", "change",
                                                                     e -> getElement()
                                                                             .setAttribute("invalid",
                                                                                           ((HasValidation)component)
                                                                                                   .isInvalid()));
                }
            }
        }
    }
}

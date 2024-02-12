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

package org.linkki.samples.playground.ts.messages;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindLabel;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@UIVerticalLayout
public class RequiredValidationPmo {
    public static final String PROPERTY_TEXT_FIELD = "textField";
    public static final String WITH_BINDING_MANAGER = "Required validation with binding manager";
    public static final String WITHOUT_BINDING_MANAGER = "Required validation without binding manager";
    @ModelObject
    private final RequiredTextFieldModelObject modelObject;
    private final String label;

    public RequiredValidationPmo(String label) {
        this.modelObject = new RequiredTextFieldModelObject();
        this.label = label;
    }

    @BindLabel
    @UITextField(position = 10, modelAttribute = PROPERTY_TEXT_FIELD, required = RequiredType.REQUIRED)
    public void textField() {
        // model binding
    }

    public String getTextFieldLabel() {
        return label;
    }

    public static Component createComponent() {

        var requiredValidationPmo = new RequiredValidationPmo(WITH_BINDING_MANAGER);
        var bindingManager = new DefaultBindingManager(requiredValidationPmo::validate);
        Component requiredValidationWithBindingManager = VaadinUiCreator
                .createComponent(requiredValidationPmo,
                                 bindingManager.getContext(RequiredValidationPmo.class));

        Component requiredValidationWithoutBindingManager = VaadinUiCreator
                .createComponent(new RequiredValidationPmo(WITHOUT_BINDING_MANAGER),
                                 new BindingContext());
        return new VerticalLayout(requiredValidationWithoutBindingManager,
                requiredValidationWithBindingManager);
    }

    private MessageList validate() {
        if (modelObject.getTextField() == null || modelObject.getTextField().isEmpty()) {
            return new MessageList(Message
                    .builder("Input is required", Severity.ERROR)
                    .invalidObjectWithProperties(this, PROPERTY_TEXT_FIELD)
                    .create());
        } else {
            return new MessageList();
        }
    }

    static class RequiredTextFieldModelObject {
        private String textField = "Initial content";

        public String getTextField() {
            return textField;
        }

        public void setTextField(String textField) {
            this.textField = textField;
        }
    }
}

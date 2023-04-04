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

package org.linkki.samples.playground.ts.messages;

import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

@UISection
public class ButtonValidationPmo {

    private static final String PROPERTY_BUTTON = "action";

    @CheckForNull
    private Severity severity = null;

    @UIComboBox(position = 10, label = "Message severity", content = AvailableValuesType.ENUM_VALUES_INCL_NULL)
    public Severity getComboBoxValue() {
        return severity;
    }

    public void setComboBoxValue(Severity severity) {
        this.severity = severity;
    }

    @UIButton(position = 20, caption = "Button with validation")
    @BindStyleNames("button-validation")
    public void action() {
        // do nothing
    }

    @NonNull
    public MessageList validate() {
        var messages = new MessageList();
        if (severity == null) {
            return messages;
        } else if (severity == Severity.INFO) {
            // for documentation
            Object invalidObject = this;
            // tag::message-builder[]
            var message = Message.builder(getValidationMessage("Info validation message"), Severity.INFO)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_BUTTON)
                    .create();
            // end::message-builder[]
            messages.add(message);
        } else if (severity == Severity.WARNING) {
            messages.add(Message.builder(getValidationMessage("Warning validation message"), Severity.WARNING)
                    .invalidObjectWithProperties(this, PROPERTY_BUTTON)
                    .create());
        } else if (severity == Severity.ERROR) {
            messages.add(Message.builder(getValidationMessage("Error validation message"), Severity.ERROR)
                    .invalidObjectWithProperties(this, PROPERTY_BUTTON)
                    .create());
        } else {
            throw new IllegalStateException();
        }
        return messages;
    }

    private String getValidationMessage(String baseMessage) {
        return baseMessage;
    }

    public static Component createComponent() {
        var pmo = new ButtonValidationPmo();
        ValidationService validationService = pmo::validate;
        var bindingManager = new DefaultBindingManager(validationService);
        return VaadinUiCreator.createComponent(pmo, bindingManager.getContext(pmo.getClass()));
    }
}

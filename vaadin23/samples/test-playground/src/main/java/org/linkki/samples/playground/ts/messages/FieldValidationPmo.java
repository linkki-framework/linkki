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

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

@UISection
public class FieldValidationPmo {

    private static final String PROPERTY_COMBO_BOX_VALUE = "comboBoxValue";
    private static final List<Severity> COMBOBOX_VALUES = Arrays.asList(null, Severity.INFO, Severity.WARNING,
                                                                        Severity.ERROR);

    @CheckForNull
    private Severity severity = null;
    private boolean showLongValidationMessage;

    @UIComboBox(position = 10, label = "Message severity", content = AvailableValuesType.DYNAMIC)
    public Severity getComboBoxValue() {
        return severity;
    }

    public void setComboBoxValue(Severity severity) {
        this.severity = severity;
    }

    public List<Severity> getComboBoxValueAvailableValues() {
        return COMBOBOX_VALUES;
    }

    @UICheckBox(position = 15, caption = "Show long validation message to test line break")
    public boolean isShowLongValidationMessage() {
        return showLongValidationMessage;
    }

    public void setShowLongValidationMessage(boolean showLongValidationMessage) {
        this.showLongValidationMessage = showLongValidationMessage;
    }

    @NonNull
    public MessageList validate() {
        MessageList messages = new MessageList();
        if (severity == null) {
            return messages;
        }
        if (severity.compareTo(Severity.INFO) >= 0) {
            // for documentation
            Object invalidObject = this;
            // tag::message-builder[]
            Message message = Message.builder(getValidationMessage("Info validation message"), Severity.INFO)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_COMBO_BOX_VALUE)
                    .create();
            // end::message-builder[]
            messages.add(message);
        }

        if (severity.compareTo(Severity.WARNING) >= 0) {
            messages.add(Message.builder(getValidationMessage("Warning validation message"), Severity.WARNING)
                    .invalidObjectWithProperties(this, PROPERTY_COMBO_BOX_VALUE)
                    .create());
        }

        if (severity.compareTo(Severity.ERROR) >= 0) {
            messages.add(Message.builder(getValidationMessage("Error validation message"), Severity.ERROR)
                    .invalidObjectWithProperties(this, PROPERTY_COMBO_BOX_VALUE)
                    .create());
        }
        return messages;
    }

    private String getValidationMessage(String baseMessage) {
        return isShowLongValidationMessage() ? baseMessage + " in a very long form with loooooooong words."
                + " This message should test the line break behavior. Also, the message should be wrapped on top of the buttons."
                + " Of course, the message only wraps if the size of the screen is small enough."
                : baseMessage;
    }

    public static Component createComponent() {
        FieldValidationPmo pmo = new FieldValidationPmo();
        ValidationService validationService = pmo::validate;
        DefaultBindingManager bindingManager = new DefaultBindingManager(validationService);
        return VaadinUiCreator.createComponent(pmo, bindingManager.getContext(pmo.getClass()));
    }
}
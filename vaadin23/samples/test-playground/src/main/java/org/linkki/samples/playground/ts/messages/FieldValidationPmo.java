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
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.validation.message.BindMessages;

import com.vaadin.flow.component.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

@UISection
public class FieldValidationPmo {

    private static final String PROPERTY_COMBO_BOX_VALUE = "comboBoxValue";
    private static final String PROPERTY_READ_ONLY_TEXT_FIELD = "readOnlyTextField";
    private static final List<Severity> COMBOBOX_VALUES = Arrays.asList(null, Severity.INFO, Severity.WARNING,
            Severity.ERROR);

    @CheckForNull
    private Severity severity = null;
    private boolean showLongValidationMessage;
    private boolean isReadOnly;
    private String readOnlyText;

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

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextField(position = 20, label = "No validation when read-only")
    public String getReadOnlyTextField() {
        return readOnlyText;
    }

    public void setReadOnlyTextField(String readOnlyText) {
        this.readOnlyText = readOnlyText;
    }

    public boolean isReadOnlyTextFieldReadOnly() {
        return isReadOnly;
    }

    @UICheckBox(position = 25, caption = "read-only")
    public boolean isReadOnlyCheckBox() {
        return isReadOnly;
    }

    public void setReadOnlyCheckBox(boolean readOnlyField) {
        this.isReadOnly = readOnlyField;
    }

    @BindMessages
    @UITextField(position = 30, label = "Only interested in errors")
    public String getAllErrorTextField() {
        return readOnlyText;
    }

    public void setAllErrorTextField(String t) {
        readOnlyText = t;
    }

    public MessageList getAllErrorTextFieldMessages(MessageList messages) {
        return messages.stream()
                .filter(m -> Severity.ERROR == m.getSeverity())
                .collect(MessageList.collector());
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
                    .invalidObjectWithProperties(invalidObject, PROPERTY_COMBO_BOX_VALUE)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_READ_ONLY_TEXT_FIELD)
                    .create();
            // end::message-builder[]
            messages.add(message);
        } else if (severity == Severity.WARNING) {
            messages.add(Message.builder(getValidationMessage("Warning validation message"), Severity.WARNING)
                    .invalidObjectWithProperties(this, PROPERTY_COMBO_BOX_VALUE)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_TEXT_FIELD)
                    .create());
        } else if (severity == Severity.ERROR) {
            messages.add(Message.builder(getValidationMessage("Error validation message"), Severity.ERROR)
                    .invalidObjectWithProperties(this, PROPERTY_COMBO_BOX_VALUE)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_TEXT_FIELD)
                    .create());
        } else {
            throw new IllegalStateException();
        }
        return messages;
    }

    private String getValidationMessage(String baseMessage) {
        return isShowLongValidationMessage() ? (baseMessage + " in a very long form with loooooooong words."
                + " This message should test the line break behavior. Also, the message should be wrapped "
                + "on top of the buttons. Of course, the message only wraps if the size of the screen "
                + "is small enough.")
                : baseMessage;
    }

    public static Component createComponent() {
        var pmo = new FieldValidationPmo();
        ValidationService validationService = pmo::validate;
        var bindingManager = new DefaultBindingManager(validationService);
        return VaadinUiCreator.createComponent(pmo, bindingManager.getContext(pmo.getClass()));
    }
}

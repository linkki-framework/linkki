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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.annotation.BindMessages;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

import com.vaadin.flow.component.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

@UIVerticalLayout
public class FieldValidationPmo {

    public static final String PROPERTY_SEVERITY = "severity";
    public static final String PROPERTY_FIELDS_READ_ONLY = "fieldsReadOnly";

    public static final String PROPERTY_TEXT_FIELD = "textField";
    public static final String PROPERTY_COMBOBOX = "comboBox";
    public static final String PROPERTY_DATE_TIME_FIELD = "dateTimeField";
    public static final String PROPERTY_ALL_ERRORS_TEXT_FIELD = "allErrorsTextField";

    private static final List<Severity> SEVERITY_VALUES = Arrays.asList(null, Severity.INFO, Severity.WARNING,
                                                                        Severity.ERROR);
    private static final List<String> COMBOBOX_VALUES = Arrays.asList(null, "Sample", "Test");

    @CheckForNull
    private Severity severity = null;
    private boolean showLongValidationMessage = false;
    private boolean fieldsReadOnly = false;

    private String textFieldValue = StringUtils.EMPTY;
    private String comboBoxValue = "Sample";
    private LocalDateTime dateTimeFieldValue = LocalDateTime.now();

    @UIComboBox(position = 10, label = "Message severity", content = AvailableValuesType.DYNAMIC)
    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public List<Severity> getSeverityAvailableValues() {
        return SEVERITY_VALUES;
    }

    @UICheckBox(position = 15, caption = "Show long validation message to test line break")
    public boolean isShowLongValidationMessage() {
        return showLongValidationMessage;
    }

    public void setShowLongValidationMessage(boolean showLongValidationMessage) {
        this.showLongValidationMessage = showLongValidationMessage;
    }

    @UICheckBox(position = 16, caption = "read-only")
    public boolean getFieldsReadOnly() {
        return fieldsReadOnly;
    }

    public void setFieldsReadOnly(boolean fieldsReadOnly) {
        this.fieldsReadOnly = fieldsReadOnly;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextField(position = 20, label = "Red border when invalid and read-only")
    public String getTextField() {
        return textFieldValue;
    }

    public void setTextField(String textFieldValue) {
        this.textFieldValue = textFieldValue;
    }

    public boolean isTextFieldReadOnly() {
        return fieldsReadOnly;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIComboBox(position = 21, label = "Red border when invalid and read-only", content = AvailableValuesType.DYNAMIC,
            width = "18em")
    public String getComboBox() {
        return comboBoxValue;
    }

    public void setComboBox(String comboBoxValue) {
        this.comboBoxValue = comboBoxValue;
    }

    public List<String> getComboBoxAvailableValues() {
        return COMBOBOX_VALUES;
    }

    public boolean isComboBoxReadOnly() {
        return fieldsReadOnly;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDateTimeField(position = 22, label = "Red border when invalid and read-only")
    public LocalDateTime getDateTimeField() {
        return dateTimeFieldValue;
    }

    public void setDateTimeField(LocalDateTime dateTimeFieldValue) {
        this.dateTimeFieldValue = dateTimeFieldValue;
    }

    public boolean isDateTimeFieldReadOnly() {
        return fieldsReadOnly;
    }

    // tag::bind-messages[]
    @BindMessages
    @UITextField(position = 30, label = "Interested in all errors")
    public String getAllErrorsTextField() {
        return textFieldValue;
    }

    public void setAllErrorsTextField(String t) {
        textFieldValue = t;
    }

    public MessageList getAllErrorsTextFieldMessages(MessageList messages) {
        return messages.stream()
                .filter(m -> Severity.ERROR == m.getSeverity())
                .collect(MessageList.collector());
    }
    // end::bind-messages[]

    @NonNull
    public MessageList validate() {
        var messages = new MessageList();
        if (severity == Severity.INFO) {
            // for documentation
            Object invalidObject = this;
            // tag::message-builder[]
            var message = Message.builder(getValidationMessage("Info validation message"), Severity.INFO)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_SEVERITY)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_TEXT_FIELD)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_COMBOBOX)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_DATE_TIME_FIELD)
                    .create();
            // end::message-builder[]
            messages.add(message);
        } else if (severity == Severity.WARNING) {
            messages.add(Message.builder(getValidationMessage("Warning validation message"), Severity.WARNING)
                    .invalidObjectWithProperties(this, PROPERTY_SEVERITY)
                    .invalidObjectWithProperties(this, PROPERTY_TEXT_FIELD)
                    .invalidObjectWithProperties(this, PROPERTY_COMBOBOX)
                    .invalidObjectWithProperties(this, PROPERTY_DATE_TIME_FIELD)
                    .create());
        } else if (severity == Severity.ERROR) {
            messages.add(Message.builder(getValidationMessage("Error validation message"), Severity.ERROR)
                    .invalidObjectWithProperties(this, PROPERTY_SEVERITY)
                    .invalidObjectWithProperties(this, PROPERTY_TEXT_FIELD)
                    .invalidObjectWithProperties(this, PROPERTY_COMBOBOX)
                    .invalidObjectWithProperties(this, PROPERTY_DATE_TIME_FIELD)
                    .create());
        }
        if (comboBoxValue == null) {
            messages.add(Message.builder("Must not be empty", Severity.ERROR)
                    .invalidObjectWithProperties(this, "requiredComboBox")
                    .create());
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

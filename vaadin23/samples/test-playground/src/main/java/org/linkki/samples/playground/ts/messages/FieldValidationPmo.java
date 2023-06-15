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
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

import com.vaadin.flow.component.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

@UIVerticalLayout
public class FieldValidationPmo {

    public static final String PROPERTY_COMBO_BOX_VALUE = "comboBoxValue";
    public static final String PROPERTY_READ_ONLY_COMBO_BOX = "readOnlyComboBox";
    public static final String PROPERTY_READ_ONLY_TEXT_FIELD = "readOnlyTextField";
    public static final String PROPERTY_READ_ONLY_DATE_TIME_FIELD = "readOnlyDateTimeField";
    public static final String PROPERTY_ONLY_ERROR_TEXT_FIELD = "onlyErrorTextField";
    public static final String PROPERTY_MULTISELECT = "multiSelect";

    public static final String PROPERTY_READ_ONLY_CHECKBOX = "readOnlyCheckBox";

    private static final List<Severity> COMBOBOX_VALUES = Arrays.asList(null, Severity.INFO, Severity.WARNING,
                                                                        Severity.ERROR);
    private static final List<String> COMBOBOX_EXAMPLE = Arrays.asList(null, "Sample", "Test");

    private static final List<String> MULTISELECT_VALUES = Arrays.asList("Red", "Green", "Blue");

    @CheckForNull
    private Severity severity = null;
    private boolean showLongValidationMessage = false;
    private boolean isReadOnly = false;
    private String readOnlyText = StringUtils.EMPTY;
    private String comboBoxText = StringUtils.EMPTY;
    private Set<String> multiSelectValues = Collections.emptySet();

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

    @UICheckBox(position = 16, caption = "read-only")
    public boolean isReadOnlyCheckBox() {
        return isReadOnly;
    }

    public void setReadOnlyCheckBox(boolean readOnlyField) {
        this.isReadOnly = readOnlyField;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UITextField(position = 20, label = "Red border when invalid and read-only")
    public String getReadOnlyTextField() {
        return readOnlyText;
    }

    public void setReadOnlyTextField(String readOnlyText) {
        this.readOnlyText = readOnlyText;
    }

    public boolean isReadOnlyTextFieldReadOnly() {
        return isReadOnly;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIComboBox(position = 21, label = "Red border when invalid and read-only", content = AvailableValuesType.DYNAMIC,
            width = "18em")
    public String getReadOnlyComboBox() {
        return comboBoxText;
    }

    public void setReadOnlyComboBox(String comboBoxText) {
        this.comboBoxText = comboBoxText;
    }

    public List<String> getReadOnlyComboBoxAvailableValues() {
        return COMBOBOX_EXAMPLE;
    }

    public boolean isReadOnlyComboBoxReadOnly() {
        return isReadOnly;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIMultiSelect(position = 22, label = "Red border when invalid and read-only", width = "18em")
    public Set<String> getMultiSelect() {
        return multiSelectValues;
    }

    public void setMultiSelect(Set<String> multiSelectValues) {
        this.multiSelectValues = multiSelectValues;
    }

    public List<String> getMultiSelectAvailableValues() {
        return MULTISELECT_VALUES;
    }

    public boolean isMultiSelectReadOnly() {
        return isReadOnly;
    }

    @BindReadOnly(ReadOnlyType.DYNAMIC)
    @UIDateTimeField(position = 23, label = "Red border when invalid and read-only")
    public LocalDateTime getReadOnlyDateTimeField() {
        return LocalDateTime.now();
    }

    public void setReadOnlyDateTimeField(LocalDateTime localDateTime) {
        // ignore
    }

    public boolean isReadOnlyDateTimeFieldReadOnly() {
        return isReadOnly;
    }

    // tag::bind-messages[]
    @BindMessages
    @UITextField(position = 30, label = "Only interested in errors")
    public String getOnlyErrorTextField() {
        return readOnlyText;
    }

    public void setOnlyErrorTextField(String t) {
        readOnlyText = t;
    }

    public MessageList getOnlyErrorTextFieldMessages(MessageList messages) {
        return messages.stream()
                .filter(m -> Severity.ERROR == m.getSeverity())
                .collect(MessageList.collector());
    }
    // end::bind-messages[]

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
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_COMBO_BOX)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_READ_ONLY_TEXT_FIELD)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_MULTISELECT)
                    .invalidObjectWithProperties(invalidObject, PROPERTY_READ_ONLY_DATE_TIME_FIELD)
                    .create();
            // end::message-builder[]
            messages.add(message);
        } else if (severity == Severity.WARNING) {
            messages.add(Message.builder(getValidationMessage("Warning validation message"), Severity.WARNING)
                    .invalidObjectWithProperties(this, PROPERTY_COMBO_BOX_VALUE)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_COMBO_BOX)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_TEXT_FIELD)
                    .invalidObjectWithProperties(this, PROPERTY_MULTISELECT)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_DATE_TIME_FIELD)
                    .create());
        } else if (severity == Severity.ERROR) {
            messages.add(Message.builder(getValidationMessage("Error validation message"), Severity.ERROR)
                    .invalidObjectWithProperties(this, PROPERTY_COMBO_BOX_VALUE)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_COMBO_BOX)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_TEXT_FIELD)
                    .invalidObjectWithProperties(this, PROPERTY_MULTISELECT)
                    .invalidObjectWithProperties(this, PROPERTY_READ_ONLY_DATE_TIME_FIELD)
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

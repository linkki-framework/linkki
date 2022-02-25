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

package org.linkki.samples.playground.dialogs;

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.NonNull;

@UISection
public class ValidationDialogPmo {

    private static final List<Severity> COMBOBOX_VALUES = Arrays.asList(Severity.INFO, Severity.WARNING,
                                                                        Severity.ERROR);

    private Severity severity = Severity.INFO;
    private boolean showLongValidationMessage;
    private boolean showTextArea;

    @UIComboBox(position = 10, label = "Message severity", required = RequiredType.REQUIRED, content = AvailableValuesType.DYNAMIC)
    public Severity getComboBoxValue() {
        return this.severity;
    }

    public void setComboBoxValue(Severity severity) {
        this.severity = severity;
    }

    public List<Severity> getComboBoxValueAvailableValues() {
        return COMBOBOX_VALUES;
    }

    @UICheckBox(position = 15, caption = "Show long validation message")
    public boolean isShowLongValidationMessage() {
        return showLongValidationMessage;
    }

    public void setShowLongValidationMessage(boolean showLongValidationMessage) {
        this.showLongValidationMessage = showLongValidationMessage;
    }

    @UICheckBox(position = 20, caption = "Show text area")
    public boolean isShowTextArea() {
        return showTextArea;
    }

    public void setShowTextArea(boolean showTextArea) {
        this.showTextArea = showTextArea;
    }

    @UITextArea(position = 30, label = "Text Area", visible = VisibleType.DYNAMIC, height = "100em")
    public String getTextAreaValue() {
        return "A very high text area to test scrolling behavior of messages in dialog";
    }

    public boolean isTextAreaValueVisible() {
        return showTextArea;
    }

    @NonNull
    public MessageList validate() {
        MessageList messages = new MessageList();
        if (severity.compareTo(Severity.INFO) >= 0) {
            messages.add(Message.newInfo("info", getValidationMessage("Info validation message")));
        }

        if (severity.compareTo(Severity.WARNING) >= 0) {
            messages.add(Message.newWarning("warining", getValidationMessage("Warning validation message")));
        }

        if (severity.compareTo(Severity.ERROR) >= 0) {
            messages.add(Message.newError("error", getValidationMessage("Error validation message")));
        }
        return messages;
    }

    private String getValidationMessage(String baseMessage) {
        return isShowLongValidationMessage() ? baseMessage + " in a very long form with loooooooong words."
                + " This message should test the line break behavior. Also, the message should be wrapped on top of the buttons."
                + " Of course, the message only wraps if the size of the screen is small enough."
                : baseMessage;
    }

    @UISection(caption = "Validation in dialog")
    public static class ButtonSectionPmo {

        @UIButton(position = 0, label = "Opens a dialog with PMO and validation", caption = "open dialog")
        public void button() {
            ValidationDialogPmo validationDialogPmo = new ValidationDialogPmo();
            new PmoBasedDialogFactory(validationDialogPmo::validate)
                    .openOkCancelDialog("Validation Dialog Pmo", Handler.NOP_HANDLER, validationDialogPmo);
        }
    }
}
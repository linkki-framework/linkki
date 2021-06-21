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

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.component.MessageUiComponents;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.util.handler.Handler;

public class ValidationConfirmationDialog {

    @UISection(caption = "Validation confirmation dialog")
    public static class ButtonSectionPmo {

        @UIButton(position = 0, label = "Opens a dialog with validation messages", caption = "open dialog")
        public void button() {
            ConfirmationDialog.open("Validation Messages",
                                    MessageUiComponents.createMessageTable(this::messages,
                                                                           new BindingContext()),
                                    Handler.NOP_HANDLER);
        }

        private MessageList messages() {
            return new MessageList(Message.builder("Error message 1", Severity.ERROR).create(),
                    Message.builder("Error message 2", Severity.ERROR).create(),
                    Message.builder("Warning message with a very, very, very, very, very, very, very, very, very, very, very, very long message text!",
                                    Severity.WARNING)
                            .create(),
                    Message.builder("Info message", Severity.INFO).create());
        }

    }

}

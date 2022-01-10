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

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.framework.ui.component.MessageUiComponents;

import com.vaadin.flow.component.Component;

public class MessageTableSection {

    public static Component create() {
        return MessageUiComponents.createMessageTable(MessageTableSection::messages, new BindingContext());
    }

    private static MessageList messages() {
        return new MessageList(
                Message.builder("An error message", Severity.ERROR).create(),
                Message.builder("A warning message", Severity.WARNING).create(),
                Message.builder("An info message", Severity.INFO).create());
    }

}

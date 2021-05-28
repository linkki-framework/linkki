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

package org.linkki.framework.ui.component;

import static java.util.stream.Collectors.toList;

import java.util.function.Supplier;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;

import com.vaadin.flow.component.dependency.CssImport;

/**
 * PMO for a {@link MessageList} to be displayed in a table.
 */
@CssImport("./styles/linkki-messages.css")
public class MessageTablePmo extends SimpleTablePmo<Message, MessageRowPmo> {

    protected MessageTablePmo(Supplier<MessageList> messageList) {
        super(() -> messageList.get().stream().collect(toList()));
    }

    @Override
    protected MessageRowPmo createRow(Message message) {
        return new MessageRowPmo(message);
    }

}

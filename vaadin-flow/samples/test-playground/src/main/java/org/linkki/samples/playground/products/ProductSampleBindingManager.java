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
package org.linkki.samples.playground.products;

import java.util.function.Consumer;

import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.util.Consumers;

public class ProductSampleBindingManager extends DefaultBindingManager {

    private Consumer<MessageList> messagesHandler = Consumers.nopConsumer();

    public ProductSampleBindingManager(ValidationService validationService) {
        super(validationService);
    }

    public void setMessagesHandler(Consumer<MessageList> messagesHandler) {
        this.messagesHandler = messagesHandler;
    }

    @Override
    protected void updateMessages(MessageList messages) {
        super.updateMessages(messages);
        messagesHandler.accept(messages);
    }
}

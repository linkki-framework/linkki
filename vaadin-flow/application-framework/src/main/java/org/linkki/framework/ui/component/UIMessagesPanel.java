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
package org.linkki.framework.ui.component;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.annotation.LinkkiMessages;
import org.linkki.core.binding.descriptor.messagehandler.annotation.MessageHandlerCreator;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.uicreation.ComponentDefinitionCreator;

/**
 * A {@link MessagesPanel} that displays all messages of the binding context after each validation.
 * 
 * @since 2.10.0
 */
@Retention(RUNTIME)
@Target(TYPE)
@LinkkiBoundProperty(BoundPropertyCreator.EmptyPropertyCreator.class)
@LinkkiComponent(UIMessagesPanel.MessagesPanelComponentDefinitionCreator.class)
@LinkkiMessages(UIMessagesPanel.MessagesPanelMessageHandlerCreator.class)
public @interface UIMessagesPanel {

    class MessagesPanelComponentDefinitionCreator implements ComponentDefinitionCreator<Annotation> {
        @Override
        public LinkkiComponentDefinition create(Annotation annotation, AnnotatedElement annotatedElement) {
            return pmo -> new MessagesPanel();
        }
    }

    class MessagesPanelMessageHandlerCreator implements MessageHandlerCreator<Annotation> {
        @Override
        public LinkkiMessageHandler create(Annotation annotation, AnnotatedElement annotatedElement) {
            return (ml, cw, pd) -> {
                var messagesPanel = (MessagesPanel)cw.getComponent();
                messagesPanel.showMessages(ml);
                return new MessageList();
            };
        }
    }
}

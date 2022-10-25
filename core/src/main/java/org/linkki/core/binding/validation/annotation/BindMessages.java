/*
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 */

package org.linkki.core.binding.validation.annotation;

import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.annotation.LinkkiMessages;
import org.linkki.core.binding.descriptor.messagehandler.annotation.MessageHandlerCreator;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.annotation.BindMessages.BindMessagesHandlerCreator;
import org.linkki.core.binding.validation.handler.BindValidationMessagesHandler;
import org.linkki.core.binding.validation.message.MessageList;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * When a PMO property method is annotated with this annotation, a method named
 * <b>get[PropertyName]Messages(MessageList)</b> is called when the binding processes the validation
 * messages. The {@link MessageList} passed as a parameter to this method contains all messages from the
 * {@link ValidationService}. The method must select the messages that are relevant to the bound
 * selecting messages about the bound object and the property will be overridden.
 * <p>
 * It is not recommended to return other messages which are not part of the input messages because they
 * will not be recognized by other message handlers. Consider changing your validation service instead
 * or create a feature request with your use case.
 */
@Retention(RUNTIME)
@Target(METHOD)
@LinkkiMessages(BindMessagesHandlerCreator.class)
public @interface BindMessages {

    // tag::message-handler-creator[]
    class BindMessagesHandlerCreator implements MessageHandlerCreator<BindMessages> {

        @Override
        public LinkkiMessageHandler create(BindMessages annotation, AnnotatedElement annotatedElement) {
            Method method = (Method)annotatedElement;
            return new BindValidationMessagesHandler(method.getDeclaringClass(),
                    BoundPropertyAnnotationReader.getBoundProperty(method).getPmoProperty());
        }
    }
    // end::message-handler-creator[]
}
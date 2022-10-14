/*
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 *
 * All Rights Reserved - Alle Rechte vorbehalten.
 */

package org.linkki.core.ui.validation.message;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.messagehandler.DefaultMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.annotation.LinkkiMessages;
import org.linkki.core.binding.descriptor.messagehandler.annotation.MessageHandlerCreator;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.validation.message.BindMessages.BindMessagesHandlerCreator;

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

    class BindMessagesHandlerCreator implements MessageHandlerCreator<BindMessages> {

        @Override
        public LinkkiMessageHandler create(BindMessages annotation, AnnotatedElement annotatedElement) {
            Method method = (Method)annotatedElement;
            return new BindValidationMessagesHandler(method.getDeclaringClass(),
                    BoundPropertyAnnotationReader.getBoundProperty(method).getPmoProperty());
        }
    }

    class BindValidationMessagesHandler extends DefaultMessageHandler {

        private static final String NAME = "Messages";
        private static final Map<CacheKey, Method> CACHE = new ConcurrentHashMap<>();

        private final Method validationMethod;

        public BindValidationMessagesHandler(Class<?> pmoClass, String property) {
            validationMethod = CACHE.computeIfAbsent(new CacheKey(pmoClass, property), this::getValidationMethod);
        }

        private Method getValidationMethod(CacheKey key) {
            String validationMethodName = "get"
                    + StringUtils.capitalize(key.propertyName + StringUtils.capitalize(NAME));
            try {
                return key.pmoClass.getMethod(validationMethodName, MessageList.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new LinkkiBindingException(String.format("Cannot find method %s(MessagesList) in %s",
                        validationMethodName, key.pmoClass.getName()),
                        e);
            }
        }

        @Override
        protected MessageList getRelevantMessages(MessageList messages, PropertyDispatcher propertyDispatcher) {
            try {
                return (MessageList)validationMethod.invoke(propertyDispatcher.getBoundObject(), messages);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new LinkkiBindingException(String.format("Cannot invoke method %s(MessagesList) on %s",
                        validationMethod.getName(),
                        propertyDispatcher.getBoundObject()),
                        e);
            }
        }

        private static class CacheKey {

            private final Class<?> pmoClass;
            private final String propertyName;

            public CacheKey(Class<?> pmoClass, String modelObjectName) {
                this.pmoClass = pmoClass;
                this.propertyName = modelObjectName;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
                result = prime * result + ((pmoClass == null) ? 0 : pmoClass.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                CacheKey cacheKey = (CacheKey)o;
                return Objects.equals(pmoClass, cacheKey.pmoClass)
                        && Objects.equals(propertyName, cacheKey.propertyName);
            }
        }
    }
}

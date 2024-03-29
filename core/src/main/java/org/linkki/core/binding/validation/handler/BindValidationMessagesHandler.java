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

package org.linkki.core.binding.validation.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.message.MessageList;

// tag::message-handler[]
public class BindValidationMessagesHandler extends DefaultMessageHandler {

    private static final String NAME = "messages";
    private static final Map<CacheKey, Method> CACHE = new ConcurrentHashMap<>();

    private final Method validationMethod;

    public BindValidationMessagesHandler(Class<?> pmoClass, String property) {
        this.validationMethod = CACHE.computeIfAbsent(new CacheKey(pmoClass, property), this::getValidationMethod);
    }

    private Method getValidationMethod(CacheKey key) {
        String validationMethodName = "get" + StringUtils.capitalize(key.propertyName + StringUtils.capitalize(NAME));
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
            if (propertyDispatcher.getBoundObject() instanceof Class<?>) {
                // The bound object is most likely a column from a grid.
                return new MessageList();
            }
            return (MessageList)validationMethod.invoke(propertyDispatcher.getBoundObject(), messages);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new LinkkiBindingException(String.format("Cannot invoke method %s(MessagesList) on %s",
                                                           validationMethod.getName(),
                                                           propertyDispatcher.getBoundObject()),
                    e);
        }
    }

    // end::message-handler[]
    private record CacheKey(Class<?> pmoClass, String propertyName) {
    }
    // tag::message-handler[]
}
// end::message-handler[]

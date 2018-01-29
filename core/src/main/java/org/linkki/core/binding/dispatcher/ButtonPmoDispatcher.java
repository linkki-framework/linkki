/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.binding.dispatcher;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.linkki.core.message.MessageList;

/**
 * @deprecated Temporary solution. Should be replaced with a regular
 *             {@link BindingAnnotationDispatcher}
 */
@Deprecated
public class ButtonPmoDispatcher extends AbstractPropertyDispatcherDecorator {

    private static final String ON_CLICK_METHOD_NAME = "onClick";

    public ButtonPmoDispatcher(PropertyDispatcher wrappedDispatcher) {
        super(wrappedDispatcher);
    }

    @Override
    public void invoke() {
        Object boundObject = getBoundObject();
        try {
            MethodUtils.invokeExactMethod(boundObject, ON_CLICK_METHOD_NAME);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getValueClass() {
        return Void.class;
    }

    @Override
    @CheckForNull
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(@Nullable Object value) {
        // do nothing
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public MessageList getMessages(MessageList messageList) {
        return new MessageList();
    }

}

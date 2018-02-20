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

import static java.util.Objects.requireNonNull;

import javax.annotation.CheckForNull;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.message.MessageList;


/**
 * Base class for all decorating {@link PropertyDispatcher property dispatchers}. Forwards all calls
 * to the dispatcher it was created with.
 * <p>
 * Subclasses can override methods and provide their own data.
 */
public abstract class AbstractPropertyDispatcherDecorator implements PropertyDispatcher {

    private final PropertyDispatcher wrappedDispatcher;

    public AbstractPropertyDispatcherDecorator(PropertyDispatcher wrappedDispatcher) {
        this.wrappedDispatcher = requireNonNull(wrappedDispatcher, "wrappedDispatcher must not be null");
    }

    @Override
    public Class<?> getValueClass() {
        return wrappedDispatcher.getValueClass();
    }

    @Override
    public MessageList getMessages(MessageList messageList) {
        return getWrappedDispatcher().getMessages(messageList);
    }

    protected PropertyDispatcher getWrappedDispatcher() {
        return wrappedDispatcher;
    }

    @Override
    public String getProperty() {
        return getWrappedDispatcher().getProperty();
    }

    @Override
    @CheckForNull
    public Object getBoundObject() {
        return getWrappedDispatcher().getBoundObject();
    }

    @CheckForNull
    @Override
    public <T> T pull(Aspect<T> aspect) {
        return getWrappedDispatcher().pull(aspect);
    }

    @Override
    public <T> void push(Aspect<T> aspect) {
        getWrappedDispatcher().push(aspect);
    }

    @Override
    public <T> boolean isPushable(Aspect<T> aspect) {
        return getWrappedDispatcher().isPushable(aspect);
    }

    @Override
    public String toString() {
        Object boundObject = getBoundObject();
        return getClass().getSimpleName() + "["
                + (boundObject != null ? boundObject.getClass().getSimpleName() : "<no object>") + "#" + getProperty()
                + "]\n\t-> " + wrappedDispatcher;
    }
}

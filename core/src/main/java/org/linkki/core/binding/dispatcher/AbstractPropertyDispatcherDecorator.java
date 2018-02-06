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

    @Override
    public void invoke() {
        getWrappedDispatcher().invoke();
    }

    protected PropertyDispatcher getWrappedDispatcher() {
        return wrappedDispatcher;
    }

    @Override
    public String toString() {
        return "PropertyDispatcherDecorator[wrappedDispatcher=" + wrappedDispatcher + "]";
    }

    @Override
    public String getProperty() {
        return getWrappedDispatcher().getProperty();
    }

    @Override
    public Object getBoundObject() {
        return getWrappedDispatcher().getBoundObject();
    }

    @CheckForNull
    @Override
    public <T> T getAspectValue(Aspect<T> aspect) {
        return getWrappedDispatcher().getAspectValue(aspect);
    }

    @Override
    public <T> void setAspectValue(Aspect<T> aspect) {
        getWrappedDispatcher().setAspectValue(aspect);
    }

    @Override
    public <T> boolean isWritable(Aspect<T> aspect) {
        return getWrappedDispatcher().isWritable(aspect);
    }
}

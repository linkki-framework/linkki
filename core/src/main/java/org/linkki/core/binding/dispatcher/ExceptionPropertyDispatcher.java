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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.message.MessageList;

/**
 * {@link PropertyDispatcher} that throws exception on every method call except
 * {@link #isReadOnly()} which returns <code>true</code> and {@link #getMessages(MessageList)},
 * which returns an empty {@link MessageList}.
 *
 * Serves as a last resort fall-back to simplify exception creation in other dispatchers.
 */
public final class ExceptionPropertyDispatcher implements PropertyDispatcher {

    private final List<Object> objects = new ArrayList<>();
    private String property;

    /**
     * @param property The name of the property
     * @param objects used for error messages
     */
    public ExceptionPropertyDispatcher(String property, Object... objects) {
        this.property = requireNonNull(property, "property must not be null");
        this.objects.addAll(Arrays.asList(objects));
    }

    /**
     * {@inheritDoc}
     * <p>
     * If there is no value class this dispatcher returns {@link Void#TYPE} because a exception is
     * not useful in this case.
     */
    @Override
    public Class<?> getValueClass() {
        return Void.TYPE;
    }

    private String getExceptionText(String action) {
        return MessageFormat.format("Cannot {0} property \"{1}\" in any of {2}", action, property, objects);
    }

    @Override
    @CheckForNull
    public Object getValue() {
        throw new IllegalArgumentException(getExceptionText("read"));
    }

    @Override
    public void setValue(@Nullable Object value) {
        throw new IllegalArgumentException(getExceptionText("write"));
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        throw new IllegalArgumentException(getExceptionText("get enabled state for"));
    }

    @Override
    public boolean isVisible() {
        throw new IllegalArgumentException(getExceptionText("get visibility for"));
    }

    @Override
    public boolean isRequired() {
        throw new IllegalArgumentException(getExceptionText("get required state for"));
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        return new MessageList();
    }

    @Override
    public void invoke() {
        throw new IllegalArgumentException(
                MessageFormat.format("Cannot invoke \"{0}\" on any of {1}", property, objects));
    }

    @Override
    public String toString() {
        return "ExceptionPropertyDispatcher[" + property + "]";
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public Object getBoundObject() {
        if (objects.size() > 0) {
            return objects.get(0);
        } else {
            throw new IllegalStateException("ExceptionPropertyDispatcher has no presentation model object");
        }
    }

    @Override
    public <T> T getAspectValue(Aspect<T> aspect) {
        throw new IllegalStateException(getExceptionText("find aspect \"" + aspect.getName() + "\" method for"));
    }
}

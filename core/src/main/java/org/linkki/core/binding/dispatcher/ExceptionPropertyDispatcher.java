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
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.CheckForNull;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.message.MessageList;

/**
 * {@link PropertyDispatcher} that throws exception for aspect except for
 * {@link #getMessages(MessageList)}, which returns an empty {@link MessageList}. In case of
 * {@link #isPushable(Aspect)}, <code>false</code> is returned.
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
     * If there is no value class this dispatcher returns {@link Void#TYPE} because a exception is not
     * useful in this case.
     */
    @Override
    public Class<?> getValueClass() {
        return Void.TYPE;
    }

    private String getExceptionText(String action) {
        return MessageFormat.format("Cannot {0} property \"{1}\" in any of {2}", action, property,
                                    objects.stream().map(Object::getClass).collect(toList()));
    }

    /**
     * Returns an empty {@link MessageList} for all properties.
     */
    @Override
    public MessageList getMessages(MessageList messageList) {
        return new MessageList();
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    @CheckForNull
    public Object getBoundObject() {
        if (objects.size() > 0) {
            return objects.get(0);
        } else {
            throw new IllegalStateException("ExceptionPropertyDispatcher has no presentation model object");
        }
    }

    @Override
    @CheckForNull
    public <T> T pull(Aspect<T> aspect) {
        throw new IllegalStateException(getExceptionText("read aspect \"" + aspect.getName() + "\" method for"));
    }

    @Override
    public <T> void push(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            throw new IllegalArgumentException(getExceptionText("write"));
        } else {
            throw new IllegalArgumentException(
                    MessageFormat.format("Cannot invoke \"{0}\" on any of {1}", property, objects));
        }
    }

    @Override
    public <T> boolean isPushable(Aspect<T> aspect) {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "["
                + objects.stream().map(Object::getClass).map(Class::getSimpleName).collect(joining(",")) + "#"
                + getProperty()
                + "]";
    }

}

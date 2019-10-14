/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.linkki.core.binding.dispatcher.fallback;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.validation.message.MessageList;

import edu.umd.cs.findbugs.annotations.CheckForNull;

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
     * If there is no value class this dispatcher returns {@link Void#TYPE} because an exception is not
     * useful in this case.
     */
    @Override
    public Class<?> getValueClass() {
        return Void.TYPE;
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
            throw new IllegalStateException(
                    "ExceptionPropertyDispatcher has no presentation model object. "
                            + "The bound object should be found in a previous dispatcher in the dispatcher chain before reaching ExceptionPropertyDisptacher.");
        }
    }

    @Override

    public <T> T pull(Aspect<T> aspect) {
        throw new IllegalStateException(missingMethodMessage(getPropertyAspectName("is/get", aspect), objects));
    }

    @Override
    public <T> void push(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            throw new IllegalArgumentException(
                    missingMethodMessage("set" + StringUtils.capitalize(getProperty()), objects));
        } else {
            throw new IllegalArgumentException(missingMethodMessage("void " + property + "()", objects));
        }
    }

    private String getPropertyAspectName(String prefix, Aspect<?> aspect) {
        return StringUtils
                .uncapitalize(prefix + StringUtils.capitalize(getProperty())
                        + StringUtils.capitalize(aspect.getName()));
    }

    /***
     * Creates a message that indicates a missing Method in certain classes
     * 
     * @param methodSignature the method signature of the missing method
     * @param objects in whose class the method is missing
     * @return the message that indicates missing methods
     */
    public static String missingMethodMessage(String methodSignature, List<Object> objects) {
        return MessageFormat.format("Cannot find method \"{0}\"  in any of the classes {1}",
                                    methodSignature,
                                    objects.stream().map(t -> t == null ? null : t.getClass().toGenericString())
                                            .collect(toList()));
    }

    @Override
    public <T> boolean isPushable(Aspect<T> aspect) {
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "["
                + objects.stream().map((Object c) -> (c != null) ? c.getClass().getSimpleName() : "null")
                        .collect(joining(","))
                + "#"
                + getProperty()
                + "]";
    }
}

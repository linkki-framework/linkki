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

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.message.MessageList;

/**
 * Provides field information for an arbitrary property through an unified interface.
 * <p>
 * For each aspect that can be bound to a field, a getter method exists. The aspects are:
 * <ul>
 * <li>{@linkplain #getValue() Value}</li>
 * <li>{@linkplain #isEnabled() Enabled state}</li>
 * <li>{@linkplain #isVisible() Visibility}</li>
 * <li>{@linkplain #isRequired() Mandatory state}</li>
 * <li>{@linkplain #isReadOnly() Read-only state}</li>
 * <li>{@linkplain #getMessages(MessageList) ErrorMessages/Warnings}</li>
 * </ul>
 * In contrast to the other aspects the value of a property can also be set.
 */
public interface PropertyDispatcher {

    /**
     * @return the name of the property
     */
    public String getProperty();

    /**
     * @return the model object containing the property.
     */
    @Nullable
    public Object getBoundObject();

    /**
     * Retrieves the value class for the property. i.e. the return type of the getter method.
     *
     * @return the value class of the property
     * @throws IllegalArgumentException if the property is not available.
     */
    public Class<?> getValueClass();

    /**
     * Retrieves the value for the property.
     *
     * @return the value of the property
     * @throws IllegalArgumentException if the property is not available.
     */
    @CheckForNull
    public Object getValue();

    /**
     * Sets the property to the argument value.
     * 
     * @param value the property's new value(may be <code>null</code>)
     *
     * @throws IllegalArgumentException if the property is not available.
     */
    public void setValue(@Nullable Object value);

    /**
     * Retrieves the read-only state for the property.
     *
     * @return whether the property is read-only
     * @throws IllegalArgumentException if the property is not available.
     */
    public boolean isReadOnly();

    /**
     * Retrieves the enabled state for the property.
     *
     * @return whether the property is enabled
     * @throws IllegalArgumentException if the property is not available.
     */
    public boolean isEnabled();

    /**
     * Retrieves the visibility for the property.
     *
     * @return whether the property is visible
     * @throws IllegalArgumentException if the property is not available.
     */
    public boolean isVisible();

    /**
     * Retrieves the mandatory state for the property.
     *
     * @return whether the property is mandatory
     * @throws IllegalArgumentException if the property is not available.
     */
    public boolean isRequired();

    /**
     * Retrieves the validation messages for the property.
     *
     * @param messageList the existing messages from previous model object validation
     * @return the list of messages
     */
    public MessageList getMessages(MessageList messageList);

    /**
     * Invokes the property with the given name, i.e. invokes the method of that name.
     */
    public void invoke();

    /**
     * @return the caption of a control, i.e. a button's text.
     */
    @CheckForNull
    public String getCaption();

    /**
     * Returns the value for the given {@link Aspect} according to this dispatcher.
     * <p>
     * The given {@link Aspect} may have a {@link Aspect#getStaticValue() static value}. It is up to the
     * decision of this dispatcher to use this value or to provide another value instead.
     * <p>
     * Note: This method may return <code>null</code> if the aspect is designed to accept
     * <code>null</code> value.
     * 
     * @param aspect the {@link Aspect} that is requested from this dispatcher
     * @return value of the given {@link Aspect} according to this dispatcher
     */
    @CheckForNull
    <T> T getAspectValue(Aspect<T> aspect);

}

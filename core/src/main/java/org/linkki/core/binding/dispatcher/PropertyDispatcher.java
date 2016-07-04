/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.faktorips.runtime.MessageList;

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
 * <li>{@linkplain #getAvailableValues() List of available values}</li>
 * <li>{@linkplain #getMessages(MessageList) ErrorMessages/Warnings}</li>
 * </ul>
 * In contrast to the other aspects the value of a property can also be set.
 *
 * @author widmaier
 */
public interface PropertyDispatcher {

    /**
     * @return the name of the property
     */
    @Nonnull
    public String getProperty();

    /**
     * @return the model object containing the property.
     */
    @Nonnull
    public Object getBoundObject();

    /**
     * Retrieves the value class for the property. i.e. the return type of the getter method.
     *
     * @return the value class of the property
     * @throws IllegalArgumentException if the property is not available.
     */
    @Nonnull
    public Class<?> getValueClass();

    /**
     * Retrieves the value for the property.
     *
     * @return the value of the property
     * @throws IllegalArgumentException if the property is not available.
     */
    public Object getValue();

    /**
     * Sets the property to the argument value.
     * 
     * @param value the property's new value
     *
     * @throws IllegalArgumentException if the property is not available.
     */
    public void setValue(Object value);

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
     * Retrieves the list of values for the property, e.g. all options in a dropdown control
     *
     * @return the list of available values
     * @throws IllegalArgumentException if the property is not available.
     */
    @Nonnull
    public Collection<?> getAvailableValues();

    /**
     * Retrieves the validation messages for the property.
     *
     * @param messageList the existing messages from previous model object validation
     * @return the list of messages
     */
    @Nonnull
    public MessageList getMessages(MessageList messageList);

    /**
     * Invokes the property with the given name, i.e. invokes the method of that name.
     */
    public void invoke();

    /**
     * @return the caption of a control, i.e. a button's text.
     */
    public String getCaption();
}

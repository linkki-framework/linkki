/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import java.util.Collection;

import org.faktorips.runtime.MessageList;

/**
 * Provides field information for arbitrary properties through an unified interface.
 * <p>
 * Instead of many getValueX() methods, one for each property, a single {@link #getValue(String)} is
 * provided. For each aspect that can be bound to a field, such a method exists. The aspects are:
 * <ul>
 * <li>Value</li>
 * <li>Enabled state</li>
 * <li>Visibility</li>
 * <li>Mandatory state</li>
 * <li>List of available values</li>
 * <li>ErrorMessages/Warnings</li>
 * </ul>
 * In contrast to the other aspects the value of a property can also be set.
 *
 * @author widmaier
 */
public interface PropertyDispatcher {

    /**
     * Called to inform this dispatcher about an imminent update of the UI. Dispatchers are expected
     * to execute operations and cache the result in order to be able to answer many subsequent
     * calls to its getters in a timely fashion.
     * <p>
     * For example validate an object structure and cache the results. Answer calls to
     * {@link #getMessages(String)} using the cached validation result.
     */
    public void prepareUpdateUI();

    /**
     * Retrieves the value class for the given property. i.e. the return type of the getter method.
     *
     * @param property the name of the property
     * @return the value class of the given property
     * @throws IllegalArgumentException if the given property is not available.
     */
    public Class<?> getValueClass(String property);

    /**
     * Retrieves the value for the given property.
     *
     * @param property the name of the property
     * @return the value of the given property
     * @throws IllegalArgumentException if the given property is not available.
     */
    public Object getValue(String property);

    /**
     * Sets the given property to the argument value.
     *
     * @param property the name of the property
     * @param value the property's new value
     * @throws IllegalArgumentException if the given property is not available.
     */
    public void setValue(String property, Object value);

    /**
     * Retrieves the read-only state for the given property.
     *
     * @param property the name of the property
     * @return whether the given property is read-only
     * @throws IllegalArgumentException if the given property is not available.
     */
    public boolean isReadonly(String property);

    /**
     * Retrieves the enabled state for the given property.
     *
     * @param property the name of the property
     * @return whether the given property is enabled
     * @throws IllegalArgumentException if the given property is not available.
     */
    public boolean isEnabled(String property);

    /**
     * Retrieves the visibility for the given property.
     *
     * @param property the name of the property
     * @return whether the given property is visible
     * @throws IllegalArgumentException if the given property is not available.
     */
    public boolean isVisible(String property);

    /**
     * Retrieves the mandatory state for the given property.
     *
     * @param property the name of the property
     * @return whether the given property is mandatory
     * @throws IllegalArgumentException if the given property is not available.
     */
    public boolean isRequired(String property);

    /**
     * Retrieves the list of values for the given property, e.g. all options in a dropdown control
     *
     * @param property the name of the property
     * @return the list of available values
     * @throws IllegalArgumentException if the given property is not available.
     */
    public Collection<?> getAvailableValues(String property);

    /**
     * Retrieves the validation messages for the given property.
     * <p>
     * Note that this method does <em>not</em> throw an exception if a property does not exist. It
     * simply returns an empty message list in all cases.
     *
     * @param property the name of the property
     * @return the list of messages
     */
    public MessageList getMessages(String property);

    /**
     * Invokes the property with the given name, i.e. invokes the method of that name.
     * 
     * @param property the name of the property (i.e. the method) to invoke
     */
    public void invoke(String property);
}

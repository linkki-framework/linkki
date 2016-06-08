/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.aspect;

import org.linkki.core.binding.dispatcher.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

/**
 * Influences the behavior of properties. There are multiple aspects that can be influenced. They
 * are whether a property is
 * <ul>
 * <li>writable</li>
 * <li>visible</li>
 * <li>displaying validation messages</li>
 * </ul>
 * <p>
 * {@link BehaviorDependentDispatcher} uses instances of this class to influence the data as well as
 * the data flow from/to other {@link PropertyDispatcher dispatchers}.
 * <p>
 * Instances of this class are injectable. This means they are:
 * <ul>
 * <li>self contained and thus have no references to decorators or other
 * {@link InjectablePropertyBehavior behaviors}</li>
 * <li>generally applicable and thus can be used in any context by any UI- or model element</li>
 * </ul>
 * <p>
 * Return values follow a logical AND (or veto logic). If {@link #isVisible(Object, String)} returns
 * <code>false</code>, that property will be hidden, no matter what other
 * {@link InjectablePropertyBehavior behaviors} say. This means a behavior should return
 * <code>true</code> for all properties be default, unless it wants to restrict the behavior. In
 * this case <code>false</code> should be returned.
 *
 * @author widmaier
 */
public interface InjectablePropertyBehavior {

    /**
     * Indicates whether the property of the given object should be writable (values can be entered
     * in the UI-Field and are written to the PMO/model).
     *
     * @return <code>true</code> if the property should be written, <code>false</code> if writing
     *         data is prohibited.
     */
    boolean isWritable(Object boundObject, String property);

    /**
     * Indicates whether the property of the given object should be visible.
     *
     * @return <code>true</code> if the property should be displayed, <code>false</code> if the
     *         property should be hidden.
     */
    boolean isVisible(Object boundObject, String property);

    /**
     * Indicates whether the property of the given object should display validation messages, i.e.
     * input errors.
     *
     * @return <code>true</code> if messages should be displayed, else <code>false</code>.
     */
    boolean isShowValidationMessages(Object boundObject, String property);

}

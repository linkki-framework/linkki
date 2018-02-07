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
package org.linkki.core.binding.behavior;

import javax.annotation.Nullable;

/**
 * Influences the behavior of properties. There are multiple aspects that can be influenced. They
 * are whether a property is
 * <ul>
 * <li>writable</li>
 * <li>visible</li>
 * <li>displaying validation messages</li>
 * </ul>
 * <p>
 * Return values follow a logical AND (or veto logic). If {@link #isVisible(Object, String)} returns
 * <code>false</code>, that property will be hidden, no matter what other {@link PropertyBehavior
 * behaviors} say. This means a behavior should return <code>true</code> for all properties by
 * default, unless it wants to restrict the behavior. In this case <code>false</code> should be
 * returned.
 */
public interface PropertyBehavior {

    /**
     * Indicates whether the property of the given object should be writable (values can be entered in
     * the UI-Field and are written to the PMO/model).
     * 
     * @param boundObject the object the property refers to
     * @param property property of which the write access is determined by this method
     * @return <code>true</code> if the property should be writable, <code>false</code> if writing data
     *         is prohibited.
     */
    default boolean isWritable(Object boundObject, String property) {
        return true;
    }

    /**
     * Indicates whether the property of the given object should be visible.
     *
     * @param boundObject the object the property refers to
     * @param property property of which the visiblity is determined by this method
     * @return <code>true</code> if the property should be displayed, <code>false</code> if the property
     *         should be hidden.
     */
    default boolean isVisible(Object boundObject, String property) {
        return true;
    }

    /**
     * Indicates whether the property of the given object should display validation messages, i.e. input
     * errors.
     *
     * @param boundObject the object the property refers to
     * @param property property of which the validation message display behavior is determined by this
     *            method
     * @return <code>true</code> if messages should be displayed, else <code>false</code>.
     */
    default boolean isShowValidationMessages(@Nullable Object boundObject, String property) {
        return true;
    }

}

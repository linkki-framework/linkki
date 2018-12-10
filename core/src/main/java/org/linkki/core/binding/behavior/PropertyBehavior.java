/*
 * Copyright Faktor Zehn GmbH.
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

import static org.linkki.util.BooleanSuppliers.negate;

import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

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
     * @param property property of which the visibility is determined by this method
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
    default boolean isShowValidationMessages(Object boundObject, String property) {
        return true;
    }

    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)}.
     * <p>
     * The writable state of the behavior is determined by the given supplier independent of the bound
     * object and property passed to {@link #isWritable(Object, String)}.
     * 
     * @param writableStateSupplier a supplier for the current writable state
     * @return a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)} with a
     *         redirection to the given supplier
     * 
     * @see PropertyBehavior#writable(BiPredicate)
     * @see PropertyBehavior#readOnly(BooleanSupplier)
     * @see PropertyBehavior#readOnly()
     */
    public static PropertyBehavior writable(BooleanSupplier writableStateSupplier) {
        return new PropertyBehavior() {
            @Override
            public boolean isWritable(Object boundObject, String property) {
                return writableStateSupplier.getAsBoolean();
            }
        };
    }

    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)}.
     * <p>
     * The writable state of the behavior is determined by the given predicate, passing it the bound
     * object and property parameters of {@link #isWritable(Object, String)}.
     * 
     * @param writablePredicate a method deciding for a given bound object/property pair whether the
     *            property should be writable
     * @return a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)} with a
     *         redirection to the given predicate
     * 
     * @see PropertyBehavior#writable(BooleanSupplier)
     * @see PropertyBehavior#readOnly(BiPredicate)
     * @see PropertyBehavior#readOnly()
     */
    public static PropertyBehavior writable(BiPredicate<Object, String> writablePredicate) {
        return new PropertyBehavior() {
            @Override
            public boolean isWritable(Object boundObject, String property) {
                return writablePredicate.test(boundObject, property);
            }
        };
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)}.
     * <p>
     * The property is always read-only, meaning it is not writable.
     * 
     * @return a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)} so
     *         that it always returns <code>false</code>
     * 
     * @see PropertyBehavior#readOnly(BiPredicate)
     * @see PropertyBehavior#readOnly(BooleanSupplier)
     * @see PropertyBehavior#writable(BooleanSupplier)
     */
    public static PropertyBehavior readOnly() {
        return writable(() -> false);
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)}.
     * <p>
     * The writable state of the behavior is determined by the given supplier independent of the bound
     * object and property passed to {@link #isWritable(Object, String)}, negating its return value to
     * better fit the read-only semantic.
     * <p>
     * A property is considered to be read-only exactly if it's not writable.
     * 
     * @param readOnlyStateSupplier a supplier for the current read-only state
     * @return a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)} with a
     *         redirection to the negation of given supplier
     * 
     * @see PropertyBehavior#readOnly(BiPredicate)
     * @see PropertyBehavior#readOnly()
     * @see PropertyBehavior#writable(BooleanSupplier)
     */
    public static PropertyBehavior readOnly(BooleanSupplier readOnlyStateSupplier) {
        return writable(negate(readOnlyStateSupplier));
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)}.
     * <p>
     * The writable state of the behavior is determined by the given predicate, passing it the bound
     * object and property passed to {@link #isWritable(Object, String)}, negating its return value to
     * better fit the read-only semantic.
     * <p>
     * A property is considered to be read-only exactly if it's not writable.
     * 
     * @param readOnlyPredicate a method deciding for a given bound object/property pair whether the
     *            property should be read-only
     * @return a new {@link PropertyBehavior} that implements {@link #isWritable(Object, String)} with a
     *         redirection to the negation of given predicate
     * 
     * @see PropertyBehavior#readOnly(BooleanSupplier)
     * @see PropertyBehavior#readOnly()
     * @see PropertyBehavior#writable(BiPredicate)
     */
    public static PropertyBehavior readOnly(BiPredicate<Object, String> readOnlyPredicate) {
        return writable(readOnlyPredicate.negate());
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isVisible(Object, String)}.
     * <p>
     * The visible state of the behavior is determined by the given supplier independent of the bound
     * object and property passed to {@link #isVisible(Object, String)}.
     * 
     * @param visibleStateSupplier a supplier for the current visible state
     * @return a new {@link PropertyBehavior} that implements {@link #isVisible(Object, String)} with a
     *         redirection to the given supplier
     * 
     * @see PropertyBehavior#visible(BiPredicate)
     */
    public static PropertyBehavior visible(BooleanSupplier visibleStateSupplier) {
        return new PropertyBehavior() {
            @Override
            public boolean isVisible(Object boundObject, String property) {
                return visibleStateSupplier.getAsBoolean();
            }
        };
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements {@link #isVisible(Object, String)}.
     * <p>
     * The visible state of the behavior is determined by the given predicate, passing it the bound
     * object and property passed to {@link #isVisible(Object, String)}.
     * 
     * @param visiblePredicate a method deciding for a given bound object/property pair whether the
     *            property should be visible
     * @return a new {@link PropertyBehavior} that implements {@link #isVisible(Object, String)} with a
     *         redirection to the given predicate
     * 
     * @see PropertyBehavior#visible(BooleanSupplier)
     */
    public static PropertyBehavior visible(BiPredicate<Object, String> visiblePredicate) {
        return new PropertyBehavior() {
            @Override
            public boolean isVisible(Object boundObject, String property) {
                return visiblePredicate.test(boundObject, property);
            }
        };
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements
     * {@link #isShowValidationMessages(Object, String)}.
     * <p>
     * The visible state of the behavior is determined by the given supplier independent of the bound
     * object and property passed to {@link #isShowValidationMessages(Object, String)}.
     * 
     * @param showValidationMessagesStateSupplier a supplier for the current validation-message state
     * @return a new {@link PropertyBehavior} that implements
     *         {@link #isShowValidationMessages(Object, String)} with a redirection to the given
     *         supplier
     * 
     * @see PropertyBehavior#showValidationMessages(BiPredicate)
     */
    public static PropertyBehavior showValidationMessages(BooleanSupplier showValidationMessagesStateSupplier) {
        return new PropertyBehavior() {
            @Override
            public boolean isShowValidationMessages(Object boundObject, String property) {
                return showValidationMessagesStateSupplier.getAsBoolean();
            }
        };
    }


    /**
     * Creates a new {@link PropertyBehavior} that implements
     * {@link #isShowValidationMessages(Object, String)}.
     * <p>
     * The visible state of the behavior is determined by the given predicate, passing it the bound
     * object and property passed to {@link #isShowValidationMessages(Object, String)}.
     * 
     * @param showValidationMessagesPredicate a method deciding for a given bound object/property pair
     *            whether messages for the property should be shown
     * @return a new {@link PropertyBehavior} that implements
     *         {@link #isShowValidationMessages(Object, String)} with a redirection to the given
     *         predicate
     * 
     * @see PropertyBehavior#showValidationMessages(BooleanSupplier)
     */
    public static PropertyBehavior showValidationMessages(BiPredicate<Object, String> showValidationMessagesPredicate) {
        return new PropertyBehavior() {
            @Override
            public boolean isShowValidationMessages(Object boundObject, String property) {
                return showValidationMessagesPredicate.test(boundObject, property);
            }
        };
    }

}

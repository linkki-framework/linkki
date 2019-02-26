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
package org.linkki.core.binding;

import org.linkki.core.message.MessageList;

/**
 * Common interface for bindings handled by the {@link BindingContext}. A binding has a PMO and an UI
 * control and is able to update this control when the {@link BindingContext} calls
 * {@link #updateFromPmo()}.
 *
 */
public interface Binding {

    /**
     * Returns the component that is updated by this binding.
     * 
     * @return The component that updated by this binding
     */
    Object getBoundComponent();

    /**
     * Returns the presentation model object that is bound to the component by this binding.
     * 
     * @return The presentation model object that is bound to the component by this binding
     */
    Object getPmo();

    /**
     * Called by the {@link BindingContext} and trigger control updating. This includes the update of
     * the value, the states (read-only, enabled, visible) and if supported the list of available
     * values.
     * 
     * This method does not update the validation message.
     * 
     * @see #displayMessages(MessageList)
     */
    void updateFromPmo();

    /**
     * Retrieves those messages from the given list that are relevant for this binding and displays them
     * directly at the bound component. An error message will mark the component property.
     * 
     * @param messages a list of messages
     * @return those messages from the given list that are displayed; an empty list if no messages are
     *         displayed.
     */
    MessageList displayMessages(MessageList messages);

}
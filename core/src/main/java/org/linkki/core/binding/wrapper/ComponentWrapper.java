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

package org.linkki.core.binding.wrapper;

import java.io.Serializable;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.uielement.UiElementCreator;

/**
 * This interface provides the most common API for a component in linkki. Besides the actual UI
 * component, additional components such as a label may be wrapped together for binding. It's encouraged
 * to use a specific implementation rather than using the base class.
 */
public interface ComponentWrapper extends Serializable {

    /**
     * Specifies the ID of the component that is wrapped by this {@link ComponentWrapper}. The ID might
     * be specified by the framework and could be used to identify the component.
     * <p>
     * The {@link UiElementCreator} sets this ID to the {@link BoundProperty#getPmoProperty() name of
     * the presentation model property} bound to this component.
     * 
     * @param id the ID of the component
     */
    void setId(String id);

    /**
     * Text which describes a component. Can be either a component itself, or be a part of another
     * component.
     * 
     * @param labelText the text which is shown to the left of the component
     */
    void setLabel(String labelText);

    /**
     * Delegates to the components method. Defines if a component can be edited.
     * 
     * @param enabled true if a component is editable, otherwise false
     */
    void setEnabled(boolean enabled);

    /**
     * Delegates to the components method. It defines if a component can be seen by users or not. If a
     * label is present, it has to change its visibility too.
     * 
     * @param visible if a component is visible to the user or not
     */
    void setVisible(boolean visible);

    /**
     * Delegates to an components method.
     *
     * @param text the components description
     */
    void setTooltip(String text);

    /**
     * Gets the unwrapped UI component.
     * 
     * @return the unwrapped component
     */
    Object getComponent();

    /**
     * Returns the {@link WrapperType} this {@link ComponentWrapper} is responsible for. The
     * {@link WrapperType} is used to determine whether a specific {@link LinkkiAspectDefinition}
     * supports this {@link ComponentWrapper} or not. If it is not applicable it is ignored silently.
     * <p>
     * As {@link WrapperType wrapper types} are hierarchical this type should be the most specific type
     * this wrapper represents. A specific {@link LinkkiAspectDefinition} might be applicable for a more
     * a more wider type. For example: a {@link LinkkiAspectDefinition} that supports
     * {@link WrapperType#COMPONENT} also supports {@link ComponentWrapper} of type
     * {@link WrapperType#FIELD} because {@link WrapperType#FIELD} is a subtype of
     * {@link WrapperType#COMPONENT}.
     * 
     * @return the {@link WrapperType} of this {@link ComponentWrapper}
     */
    WrapperType getType();

    /**
     * Specify a list of messages that should be displayed at the component. The component might get
     * highlighted or show the messages next to the component or by using an overlay.
     * <p>
     * The component might ignore the messages if it is not able to show messages.
     * 
     * @param messagesForProperty the messages that should be bound to the component
     */
    void setValidationMessages(MessageList messagesForProperty);

}

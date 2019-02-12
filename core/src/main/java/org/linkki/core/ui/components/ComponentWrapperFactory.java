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

package org.linkki.core.ui.components;

/**
 * Creates UI framework specific {@link ComponentWrapper ComponentWrappers}.
 */
public interface ComponentWrapperFactory {

    /**
     * Returns whether the class represents a UI component in this UIFramework.
     * 
     * @return whether the class represents a UI component in this UIFramework
     */
    boolean isUiComponent(Class<?> clazz);

    /**
     * Creates a {@link ComponentWrapper}.
     * 
     * @param component a component
     * @return a wrapper for the component
     */
    ComponentWrapper createComponentWrapper(Object component);

}

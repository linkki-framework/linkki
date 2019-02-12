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

package org.linkki.core.ui;

import java.util.Locale;
import java.util.stream.Stream;

import org.linkki.core.ui.components.ComponentWrapperFactory;

/**
 * Service interface for the abstraction of UI framework specific code. Instances should be accessed via
 * {@link UiFramework}'s methods.
 */
public interface UiFrameworkExtension {

    /**
     * Returns the locale defined for the UI session, may differ from the System locale.
     * 
     * @return the locale defined for the UI session
     */
    Locale getLocale();

    /**
     * Returns the {@link ComponentWrapperFactory} for this UI framework.
     * 
     * @return the {@link ComponentWrapperFactory}
     */
    ComponentWrapperFactory getComponentWrapperFactory();

    /**
     * Returns a {@link Stream} of all child components, if the given UI component has child components.
     * If the parameter is no UI component, {@link Stream#empty()} is returned
     * 
     * @param uiComponent with child components
     * @return all child components
     */
    Stream<?> getChildComponents(Object uiComponent);

}

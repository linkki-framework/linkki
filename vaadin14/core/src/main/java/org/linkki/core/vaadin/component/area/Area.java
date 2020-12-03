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
package org.linkki.core.vaadin.component.area;

import org.linkki.core.vaadin.component.page.Page;

import com.vaadin.flow.component.HasComponents;

/**
 * An area is a container component displayed in the UI that consists of other areas or pages.
 * 
 * @see Page
 */
public interface Area extends HasComponents {

    /** Creates the content (children) of this area. */
    void createContent();

    /**
     * Updates the content (children) of this area. In most cases it is enough to update the UI content
     * from the underlying PMOs by just calling <code>reloadBindings()</code>. If child components are
     * created dynamically, you have to add or remove child components.
     */
    void updateContent();

    /**
     * Reload the data bindings for the content (children) of this area. No children may be added or
     * removed.
     */
    void reloadBindings();
}

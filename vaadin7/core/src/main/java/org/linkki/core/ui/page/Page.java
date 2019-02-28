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
package org.linkki.core.ui.page;

import com.vaadin.ui.ComponentContainer;

/**
 * A page is a container component displayed in the UI that consists of sections.
 */
public interface Page extends ComponentContainer {

    /** Creates the content (sections) of this page. */
    void createContent();

    /**
     * Reloads the data bindings of the content (sections) displayed on this page. No sections are
     * removed/added.
     */
    void reloadBindings();
}

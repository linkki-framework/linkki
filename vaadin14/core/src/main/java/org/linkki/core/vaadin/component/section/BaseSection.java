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
package org.linkki.core.vaadin.component.section;

import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;

import com.vaadin.flow.component.formlayout.FormLayout.FormItem;

/**
 * Base class for sections that are supported by the {@link PmoBasedSectionFactory}.
 */
public abstract class BaseSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new section with the given caption that is not closable.
     * 
     * @param caption the caption
     */
    public BaseSection(String caption) {
        super(caption);
    }

    /**
     * Creates a new section with the given caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public BaseSection(String caption, boolean closeable) {
        super(caption, closeable);
    }

    /**
     * Adds the given Component to the content of this section.
     * 
     * @param component the component to add
     */
    public abstract void addContent(FormItem component);

}

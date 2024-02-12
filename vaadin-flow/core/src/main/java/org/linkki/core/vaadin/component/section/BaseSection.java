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
package org.linkki.core.vaadin.component.section;

import com.vaadin.flow.component.Component;

/**
 * Base class for sections. Child components can be added using {@link #addContent(Component)}
 */
public class BaseSection extends LinkkiSection {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new section with the given caption and closable state.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public BaseSection(String caption, boolean closeable) {
        this(caption, closeable, 1);
    }

    /**
     * Creates a new section with the given caption and closable state.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param columns number of columns in which the content components are displayed
     */
    public BaseSection(String caption, boolean closeable, int columns) {
        super(caption, closeable, columns);
    }

    /**
     * Adds the {@link Component} to the section's content
     */
    public void addContent(Component component) {
        getContentWrapper().add(component);
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Use {@link #getContentWrapper()} instead.
     */
    @Deprecated(since = "2.0.0")
    @Override
    public Component getSectionContent() {
        return getContentWrapper();
    }
}

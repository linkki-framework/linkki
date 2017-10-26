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
package org.linkki.core.ui.section;

import java.util.Optional;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * Base class for sections that are supported by the {@link PmoBasedSectionFactory}.
 */
public abstract class BaseSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new section with the given caption that is not closeable.
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
     * Creates a new section with the given caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editButton If present the section has an edit button in the header.
     */
    public BaseSection(String caption, boolean closeable, Optional<Button> editButton) {
        super(caption, closeable, editButton);
    }

    /**
     * Adds the given label / component pair to the section.
     * 
     * @param propertyName the property name that may be used to place the label and the component
     *            in the correct place.
     * @param label the label that should be placed beside the component.
     * @param component the component that should be added to the section
     * 
     */
    public abstract void add(String propertyName, Label label, Component component);

}

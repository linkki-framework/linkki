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

import java.util.Optional;

import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;

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
     * @deprecated Use {@link #BaseSection(String, boolean)} and a call to
     *             {@link #addHeaderButton(Button)} instead.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     * @param editButton If present the section has an edit button in the header.
     */
    @Deprecated
    public BaseSection(String caption, boolean closeable, Optional<Button> editButton) {
        super(caption, closeable);
        editButton.ifPresent(this::addHeaderButton);
    }

    /**
     * Adds the given label / component pair to the section.
     * @param label the label that should be placed beside the component.
     * @param component the component that should be added to the section
     * 
     */
    public abstract void add(Span label, Component component);

}

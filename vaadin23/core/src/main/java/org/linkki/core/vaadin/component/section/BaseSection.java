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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;

/**
 * Base class for sections that are supported by the {@link PmoBasedSectionFactory}.
 */
public class BaseSection extends AbstractSection {

    private static final long serialVersionUID = 1L;

    private final FlexLayout content;

    /**
     * Creates a new section with the given caption and closable state.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public BaseSection(String caption, boolean closeable) {
        super(caption, closeable);
        setWidthFull();
        content = createContent();
        add(content);
    }

    private FlexLayout createContent() {
        FlexLayout layout = new FlexLayout();
        layout.setWidthFull();
        layout.setFlexDirection(FlexDirection.COLUMN);
        layout.setFlexWrap(FlexWrap.WRAP);
        layout.setAlignItems(Alignment.BASELINE);
        return layout;
    }

    /**
     * Adds the {@link Component}, ignoring {@code propertyName} and {@code label}, as the
     * {@link FormLayout} uses the {@link Component}'s caption.
     */
    public void addContent(Component component) {
        content.add(component);
    }

    @Override
    public FlexLayout getSectionContent() {
        return content;
    }

}

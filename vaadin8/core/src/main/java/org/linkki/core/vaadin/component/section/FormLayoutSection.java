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

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

public class FormLayoutSection extends BaseSection {

    private static final long serialVersionUID = 1L;

    private final FormLayout content;

    /**
     * Creates a new section with the given caption, closable state, edit button and number of columns.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public FormLayoutSection(String caption, boolean closeable) {
        super(caption, closeable);
        setWidth("100%");
        content = createContent();
        addComponent(content);
    }

    private FormLayout createContent() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setMargin(new MarginInfo(true, true, true, true));
        formLayout.setSpacing(true);
        return formLayout;
    }

    /**
     * Adds the {@link Component}, ignoring {@code propertyName} and {@code label}, as the
     * {@link FormLayout} uses the {@link Component}'s caption.
     */
    @Override
    public void add(String propertyName, Label label, Component component) {
        content.addComponent(component);
    }


    @Override
    public FormLayout getSectionContent() {
        return content;
    }

}

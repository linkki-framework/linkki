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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.Span;

public class FormLayoutSection extends BaseSection {

    private static final long serialVersionUID = 1L;

    private final FormLayout content;

    private final int columns;

    /**
     * Creates a new section with the given caption and closable state.
     * 
     * @param caption the caption
     * @param columns number of columns that should be used by default
     * @param closeable <code>true</code> if the section can be closed and opened.
     */
    public FormLayoutSection(String caption, int columns, boolean closeable) {
        super(caption, closeable);
        this.columns = columns;
        setWidth("100%");
        content = createContent();
        add(content);
    }

    private FormLayout createContent() {
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new ResponsiveStep("0", columns, LabelsPosition.ASIDE));
        formLayout.setWidth("100%");
        // TODO LIN-2049
        // formLayout.setMargin(new MarginInfo(true, true, true, true));
        // formLayout.setSpacing(true);
        return formLayout;
    }

    /**
     * Adds the {@link Component}, ignoring {@code propertyName} and {@code label}, as the
     * {@link FormLayout} uses the {@link Component}'s caption.
     */
    @Override
    public void add(String propertyName, Span label, Component component) {
        content.addFormItem(component, label);
    }


    @Override
    public FormLayout getSectionContent() {
        return content;
    }

}

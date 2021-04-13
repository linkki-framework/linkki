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

import org.linkki.core.vaadin.component.base.LinkkiFormLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;

public class FormLayoutSection extends BaseSection {

    private static final long serialVersionUID = 1L;

    private final LinkkiFormLayout content;

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
        setWidthFull();
        content = createContent();
        add(content);
    }

    private LinkkiFormLayout createContent() {
        LinkkiFormLayout formLayout = new LinkkiFormLayout();
        formLayout.setResponsiveSteps(new ResponsiveStep("0", columns, LabelsPosition.ASIDE));
        formLayout.setWidthFull();
        return formLayout;
    }

    /**
     * Adds the {@link Component}, ignoring {@code propertyName} and {@code label}, as the
     * {@link FormLayout} uses the {@link Component}'s caption.
     */
    @Override
    public void addContent(FormItem component) {
        content.add(component);
    }

    @Override
    public FormLayout getSectionContent() {
        return content;
    }

}

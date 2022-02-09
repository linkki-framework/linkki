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

package org.linkki.core.ui.layout;

import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Defines a {@link VerticalLayout} with the given default horizontal alignment.
 */
public class VerticalLayoutComponentDefinition implements LinkkiComponentDefinition {

    private final HorizontalAlignment horizontalAlignment;

    private final boolean spacing;

    private final boolean padding;

    /**
     * Creates a new {@link VerticalLayoutComponentDefinition} with the given horizontal alignment.
     * Vertically, all components are middle aligned. The state of spacing and padding can be applied.
     * When activ then predefined style will apply to the layout. By default padding is activated and
     * spacing is activated.
     */
    public VerticalLayoutComponentDefinition(HorizontalAlignment horizontalAlignment, boolean spacing,
            boolean padding) {
        this.horizontalAlignment = horizontalAlignment;
        this.spacing = spacing;
        this.padding = padding;
    }

    @Override
    public Object createComponent(Object pmo) {
        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(horizontalAlignment.getAlignment());
        layout.setPadding(padding);
        layout.setSpacing(spacing);
        return layout;
    }

}

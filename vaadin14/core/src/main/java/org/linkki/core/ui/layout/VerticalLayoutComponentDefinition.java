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

import com.vaadin.shared.ui.AlignmentInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

/**
 * Defines a {@link VerticalLayout} with the given default horizontal alignment.
 */
public class VerticalLayoutComponentDefinition implements LinkkiComponentDefinition {

    private final HorizontalAlignment horizontalAlignment;

    /**
     * Creates a new {@link VerticalLayoutComponentDefinition} with the given horizontal alignment.
     * Vertically, all components are middle aligned.
     */
    public VerticalLayoutComponentDefinition(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    @Override
    public Object createComponent(Object pmo) {
        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultComponentAlignment(new Alignment(
                new AlignmentInfo(horizontalAlignment.getAlignmentInfo(), AlignmentInfo.MIDDLE).getBitMask()));
        return layout;
    }

}

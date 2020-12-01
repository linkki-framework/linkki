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
import com.vaadin.ui.HorizontalLayout;

/**
 * Defines a {@link HorizontalLayout} with the given default vertical alignment.
 */
public class HorizontalLayoutComponentDefinition implements LinkkiComponentDefinition {

    private final VerticalAlignment verticalAlignment;

    /**
     * Creates a new {@link HorizontalLayoutComponentDefinition} with the given vertical alignment.
     * <p>
     * For use cases where input fields do not have captions, {@link VerticalAlignment#MIDDLE} yields
     * the most consistent looking result. However, captions above input fields would create the effect
     * that input fields themselves are lower than those components without a caption, e.g. buttons. In
     * this case, consider {@link VerticalAlignment#BOTTOM} to make the controls better aligned.
     */
    public HorizontalLayoutComponentDefinition(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    @Override
    public Object createComponent(Object pmo) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(new Alignment(
                new AlignmentInfo(AlignmentInfo.LEFT, verticalAlignment.getAlignmentInfo()).getBitMask()));
        return layout;
    }

}

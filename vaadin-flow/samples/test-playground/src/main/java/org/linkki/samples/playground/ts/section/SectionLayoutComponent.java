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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SectionLayoutComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public SectionLayoutComponent() {
        setPadding(false);

        Component section = VaadinUiCreator.createComponent(new UiSectionLayoutFormComponentsPmo(),
                                                            new BindingContext(
                                                                    UiSectionLayoutFormComponentsPmo.class
                                                                            .getSimpleName()));
        ComponentStyles.setFormItemLabelWidth(section, "200px");
        add(section);

        add(VaadinUiCreator.createComponent(new UiSectionLayoutHorizontalComponentsPmo(),
                                            new BindingContext(
                                                    UiSectionLayoutHorizontalComponentsPmo.class.getSimpleName())));

        add(VaadinUiCreator.createComponent(new UiSectionLayoutVerticalComponentsPmo(),
                                            new BindingContext(
                                                    UiSectionLayoutVerticalComponentsPmo.class.getSimpleName())));
    }

}

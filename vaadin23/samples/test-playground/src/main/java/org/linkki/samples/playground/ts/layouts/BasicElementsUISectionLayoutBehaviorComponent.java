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

package org.linkki.samples.playground.ts.layouts;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

public class BasicElementsUISectionLayoutBehaviorComponent extends LinkkiTabLayout {

    private static final long serialVersionUID = 1L;

    public static final String FORM = SectionLayout.FORM.name();
    private static final String HORIZONTAL = SectionLayout.HORIZONTAL.name();
    private static final String VERTICAL = SectionLayout.VERTICAL.name();

    private static final String DESCRIPTION = "@UISection with SectionLayout.";

    public BasicElementsUISectionLayoutBehaviorComponent() {
        setWidthFull();
        BindingContext bindingContext = new BindingContext();

        addTabSheet(LinkkiTabSheet.builder(FORM)
                .description(DESCRIPTION + FORM)
                .content(() -> VaadinUiCreator.createComponent(new BasicElementsLayoutBehaviorUiSectionPmo(),
                                                               bindingContext))
                .build());
        addTabSheet(LinkkiTabSheet.builder(HORIZONTAL)
                .description(DESCRIPTION + HORIZONTAL)
                .content(() -> VaadinUiCreator.createComponent(new BasicElementsLayoutBehaviorUiSectionHorizontalPmo(),
                                                               bindingContext))
                .build());
        addTabSheet(LinkkiTabSheet.builder(VERTICAL)
                .description(DESCRIPTION + VERTICAL)
                .content(() -> VaadinUiCreator.createComponent(new BasicElementsLayoutBehaviorUiSectionVerticalPmo(),
                                                               bindingContext))
                .build());
    }

}

/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.ts.layouts;

import java.io.Serial;

import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.Component;

public class BasicElementsLayoutBehaviorUiSectionComponent extends LinkkiTabLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String FORM = SectionLayout.FORM.name();
    private static final String HORIZONTAL = SectionLayout.HORIZONTAL.name();
    private static final String VERTICAL = SectionLayout.VERTICAL.name();

    private static final String DESCRIPTION = "@UISection with SectionLayout.";

    public BasicElementsLayoutBehaviorUiSectionComponent() {
        setWidthFull();

        addTabSheet(LinkkiTabSheet.builder(FORM)
                .description(DESCRIPTION + FORM)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionPmo()))
                .build());
        addTabSheet(LinkkiTabSheet.builder(HORIZONTAL)
                .description(DESCRIPTION + HORIZONTAL)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionHorizontalPmo()))
                .build());
        addTabSheet(LinkkiTabSheet.builder(VERTICAL)
                .description(DESCRIPTION + VERTICAL)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionVerticalPmo()))
                .build());
    }

    private Component createSheetContent(AbstractBasicElementsLayoutBehaviorPmo pmo) {
        var bindingContext = new DefaultBindingManager(pmo::validate).getContext(pmo.getClass());
        var component = VaadinUiCreator.createComponent(pmo, bindingContext);
        component.getElement().getStyle().setPadding("var(--lumo-space-m)");
        return component;
    }

}

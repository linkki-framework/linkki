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

import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionHorizontalPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionVerticalPmo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SectionCardStyleComponent {
    public static final String FORM = SectionLayout.FORM.name();
    private static final String HORIZONTAL = SectionLayout.HORIZONTAL.name();
    private static final String VERTICAL = SectionLayout.VERTICAL.name();

    private static final String DESCRIPTION = "@UISection with SectionLayout.";

    public static Component create() {
        Component tabLayoutWithCardLikeSections = createTabLayoutWithCardLikeSections();
        return new VerticalLayout(VaadinUiCreator.createComponent(new SimpleSectionPmo(), new BindingContext()),
                tabLayoutWithCardLikeSections);
    }

    private static Component createTabLayoutWithCardLikeSections() {
        LinkkiTabLayout tabLayoutWithCardLikeSections = new LinkkiTabLayout();
        tabLayoutWithCardLikeSections.setWidthFull();

        tabLayoutWithCardLikeSections.addTabSheet(LinkkiTabSheet.builder(FORM)
                .description(DESCRIPTION + FORM)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionPmo(),
                                                  new GridSectionLayoutPmo()))
                .build());
        tabLayoutWithCardLikeSections.addTabSheet(LinkkiTabSheet.builder(HORIZONTAL)
                .description(DESCRIPTION + HORIZONTAL)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionHorizontalPmo(),
                                                  new GridSectionLayoutPmo()))
                .build());
        tabLayoutWithCardLikeSections.addTabSheet(LinkkiTabSheet.builder(VERTICAL)
                .description(DESCRIPTION + VERTICAL)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionVerticalPmo(),
                                                  new GridSectionLayoutPmo()))
                .build());
        ComponentStyles.setCardLikeSections(tabLayoutWithCardLikeSections);
        return tabLayoutWithCardLikeSections;
    }

    private static Component createSheetContent(Object... pmos) {
        var bindingContext = new BindingContext();
        var wrapperLayout = new VerticalLayout();
        List.of(pmos).stream().map(pmo -> VaadinUiCreator.createComponent(pmo, bindingContext))
                .forEachOrdered(wrapperLayout::add);
        return wrapperLayout;
    }

    @UISection(caption = "Simple section")
    public static class SimpleSectionPmo {

        private String value;

        @UITextField(position = 0)
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

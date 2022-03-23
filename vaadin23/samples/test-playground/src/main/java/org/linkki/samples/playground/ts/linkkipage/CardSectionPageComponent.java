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

package org.linkki.samples.playground.ts.linkkipage;

import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionHorizontalPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionVerticalPmo;
import org.linkki.samples.playground.ts.section.GridSectionLayoutPmo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;

public class CardSectionPageComponent {

    private static final String FORM = SectionLayout.FORM.name();
    private static final String HORIZONTAL = SectionLayout.HORIZONTAL.name();
    private static final String VERTICAL = SectionLayout.VERTICAL.name();

    private static final String DESCRIPTION = "@UISection with SectionLayout.";

    public static Component create() {
        Component tabLayoutWithCardLikeSections = createTabLayoutWithCardLikeSections();
        return new VerticalLayout(VaadinUiCreator.createComponent(new SimpleSectionPmo(), new BindingContext()),
                tabLayoutWithCardLikeSections);
    }

    private static Component createTabLayoutWithCardLikeSections() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.setWidthFull();

        tabLayout.addTabSheet(LinkkiTabSheet.builder(FORM)
                .description(DESCRIPTION + FORM)
                .content(() -> new SimpleSectionsPage(new BasicElementsLayoutBehaviorUiSectionPmo(),
                        new GridSectionPlusPmo()))
                .build());
        tabLayout.addTabSheet(LinkkiTabSheet.builder(HORIZONTAL)
                .description(DESCRIPTION + HORIZONTAL)
                .content(() -> new SimpleSectionsPage(new BasicElementsLayoutBehaviorUiSectionHorizontalPmo(),
                        new GridSectionPlusPmo()))
                .build());
        tabLayout.addTabSheet(LinkkiTabSheet.builder(VERTICAL)
                .description(DESCRIPTION + VERTICAL)
                .content(() -> new SimpleSectionsPage(new BasicElementsLayoutBehaviorUiSectionVerticalPmo(),
                        new GridSectionPlusPmo()))
                .build());
        return tabLayout;
    }

    @UISection(caption = "Section not in LinkkkiPage")
    public static class SimpleSectionPmo {

        private String value;

        @UIButton(position = 0, caption = "Toggle card theme globally")
        public void toggleTheme() {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();
            if (themeList.contains(LinkkiTheme.VARIANT_CARD_SECTION_PAGES)) {
                themeList.remove(LinkkiTheme.VARIANT_CARD_SECTION_PAGES);
            } else {
                themeList.add(LinkkiTheme.VARIANT_CARD_SECTION_PAGES);
            }
        }

        @UITextField(position = 10)
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @UISection(caption = "Grid section plus")
    public static class GridSectionPlusPmo extends GridSectionLayoutPmo {

        @SectionHeader
        @UIButton(position = -1, showIcon = true, icon = VaadinIcon.PLUS)
        public void plus() {
            // does nothing
        }
    }

    private static class SimpleSectionsPage extends AbstractPage {

        private static final long serialVersionUID = 1L;
        private final BindingManager bindingManager;

        public SimpleSectionsPage(Object... pmos) {
            bindingManager = new DefaultBindingManager();
            List.of(pmos).forEach(this::addSection);
        }

        @Override
        public void createContent() {
            // does nothing
        }

        @Override
        protected BindingManager getBindingManager() {
            return bindingManager;
        }

    }
}

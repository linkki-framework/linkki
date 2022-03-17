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

package org.linkki.samples.playground.products;

import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "products", layout = ProductsSampleAppLayout.class)
@PageTitle("F10 Products Sample Details")
public class ProductsSampleView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final ProductsSampleOverviewPage overviewPage;
    private final ProductsSampleDetailsComponent detailsComponent;

    private boolean cardLikeSections = false;

    public ProductsSampleView() {
        LinkkiTabLayout tabLayout = LinkkiTabLayout.newSidebarLayout();

        overviewPage = new ProductsSampleOverviewPage();
        detailsComponent = new ProductsSampleDetailsComponent();

        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("overview")
                                       .caption(VaadinIcon.INFO_CIRCLE.create())
                                       .content(() -> new HeadlinePageLayout("Overview",
                                               overviewPage))
                                       .build(), //
                               LinkkiTabSheet.builder("userdetails")
                                       .caption(VaadinIcon.USER.create())
                                       .content(() -> detailsComponent)
                                       .build());

        Button cardSectionToggleButton = new Button();
        cardSectionToggleButton.getStyle().set("margin", "0");
        cardSectionToggleButton.getStyle().set("background", "none");
        cardSectionToggleButton.getStyle().set("padding", "0.25rem 1rem");
        cardSectionToggleButton.getStyle().set("position", "absolute");
        cardSectionToggleButton.getStyle().set("bottom", "var(--lumo-space-m)");
        updateCardLikeClass(cardSectionToggleButton);
        cardSectionToggleButton.addClickListener(e -> {
            cardLikeSections = !cardLikeSections;
            updateCardLikeClass(e.getSource());
        });

        tabLayout.getTabsComponent().add(cardSectionToggleButton);
        add(tabLayout);

        setPadding(false);
        setSizeFull();
    }

    private void updateCardLikeClass(Button button) {
        if (cardLikeSections) {
            button.setIcon(VaadinIcon.GRID_BIG.create());
            ComponentStyles.setCardLikeSections(overviewPage);
            ComponentStyles.setCardLikeSections(detailsComponent.getTabLayout());
        } else {
            button.setIcon(VaadinIcon.GRID_BIG_O.create());
            overviewPage.removeClassName(LinkkiSection.CLASS_SECTION_STYLE_CARD);
            detailsComponent.getTabLayout().removeClassName(LinkkiSection.CLASS_SECTION_STYLE_CARD);
        }

    }
}

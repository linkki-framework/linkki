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

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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
    private boolean secondaryCaption = false;

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

        var styleTogglesWrapper = new Div(createStyleToggleButton(LinkkiSection.CLASS_SECTION_STYLE_CARD,
                                                                  () -> cardLikeSections,
                                                                  b -> cardLikeSections = b,
                                                                  VaadinIcon.GRID_BIG, VaadinIcon.GRID_BIG_O),
                createStyleToggleButton(LinkkiSection.CLASS_SECTION_SECONDARY_CAPTION, () -> secondaryCaption,
                                        b -> secondaryCaption = b, VaadinIcon.LEVEL_UP, VaadinIcon.LEVEL_DOWN));

        styleTogglesWrapper.getStyle().set("position", "absolute");
        styleTogglesWrapper.getStyle().set("bottom", "var(--lumo-space-m)");
        styleTogglesWrapper.getStyle().set("margin", "0");
        styleTogglesWrapper.getStyle().set("display", "flex");
        styleTogglesWrapper.getStyle().set("flex-wrap", "wrap");
        styleTogglesWrapper.getStyle().set("flex-direction", "column");

        tabLayout.getTabsComponent().add(styleTogglesWrapper);
        add(tabLayout);

        setPadding(false);
        setSizeFull();
    }

    private Button createStyleToggleButton(String className,
            BooleanSupplier hasClassNameSupplier,
            Consumer<Boolean> setHasClassName,
            VaadinIcon hasClassNameIcon,
            VaadinIcon hasNoClassNameIcon) {
        var button = new Button();
        button.getStyle().set("background", "none");
        button.getStyle().set("padding", "0.25rem 1rem");

        updateClass(button, className, hasClassNameSupplier.getAsBoolean(), hasClassNameIcon, hasNoClassNameIcon);
        button.addClickListener(e -> {
            setHasClassName.accept(!hasClassNameSupplier.getAsBoolean());
            updateClass(e.getSource(), className, hasClassNameSupplier.getAsBoolean(), hasClassNameIcon,
                        hasNoClassNameIcon);
        });

        return button;
    }

    private void updateClass(Button button,
            String className,
            boolean hasClassName,
            VaadinIcon hasClassNameIcon,
            VaadinIcon hasNoClassNameIcon) {
        if (hasClassName) {
            button.setIcon(hasClassNameIcon.create());
            overviewPage.addClassName(className);
            detailsComponent.getTabLayout().addClassName(className);
        } else {
            button.setIcon(hasNoClassNameIcon.create());
            overviewPage.removeClassName(className);
            detailsComponent.getTabLayout().removeClassName(className);
        }
    }
}

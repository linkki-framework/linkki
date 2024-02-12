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

package org.linkki.samples.playground.products;

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;

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

        add(tabLayout);

        setPadding(false);
        setSizeFull();
    }
}

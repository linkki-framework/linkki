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

import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.component.Headline;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.tabs.Tabs.Orientation;

/**
 * Sample layout based on the IPM layout.
 */
public class ProductsSampleDetailsComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public ProductsSampleDetailsComponent() {

        setPadding(false); // headline border should reach sidebar
        setSpacing(false); // content should scroll underneath headline border
        setHeightFull();

        add(new Headline("Details"));

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(false); // because the split panel pads
        content.getStyle().set("overflow", "auto");
        add(content);

        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);

        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("tab1")
                                       .caption("Just Sections")
                                       .content(() -> new VerticalLayout(
                                               ProductsSampleUtils.createSampleSectionVertical(),
                                               ProductsSampleUtils.createSampleSectionVertical(),
                                               ProductsSampleUtils.createSampleSectionHorizontal(),
                                               ProductsSampleUtils.createSampleSectionVertical(),
                                               ProductsSampleUtils.createSampleSectionVertical()))
                                       .build(),
                               LinkkiTabSheet.builder("tab3")
                                       .caption("Tables and Sections")
                                       .content(() -> new VerticalLayout(
                                               ProductsSampleUtils.createSampleSectionVertical(),
                                               ProductsSampleUtils.createSampleSectionHorizontal(),
                                               ProductsSampleUtils.createSampleTableSection(5),
                                               ProductsSampleUtils.createSampleTableSection(10),
                                               ProductsSampleUtils.createSampleSectionVertical()))
                                       .build());

        Accordion accordion = new Accordion();

        accordion.add("Section Vertical", new VerticalLayout(ProductsSampleUtils.createSampleSectionVertical()));
        accordion.add("Section Horizontal", new VerticalLayout(ProductsSampleUtils.createSampleSectionHorizontal()))
                .addThemeVariants(DetailsVariant.SMALL, DetailsVariant.FILLED);
        accordion.add("Table", new VerticalLayout(ProductsSampleUtils.createSampleTableSection(5)))
                .addThemeVariants(DetailsVariant.SMALL, DetailsVariant.REVERSE);
        accordion
                .add("Table and Sections",
                     new VerticalLayout(ProductsSampleUtils.createSampleSectionVertical(),
                             ProductsSampleUtils.createSampleTableSection(5),
                             ProductsSampleUtils.createSampleSectionVertical()))
                .addThemeVariants(DetailsVariant.SMALL, DetailsVariant.REVERSE, DetailsVariant.FILLED);

        SplitLayout splitLayout = new SplitLayout(tabLayout, accordion);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        splitLayout.setSplitterPosition(75);
        splitLayout.setSizeFull();

        content.add(splitLayout);
    }

}

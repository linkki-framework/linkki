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

import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.component.Headline;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

        setHeightFull();
        setSpacing(false);

        add(createHeadline());

        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);

        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("tab1")
                                       .caption("Just Sections")
                                       .content(() -> new VerticalLayout(
                                               ProductsSampleComponents.createVerticalSection(),
                                               ProductsSampleComponents.createVerticalSection(),
                                               ProductsSampleComponents.createHorizontalSection(),
                                               ProductsSampleComponents.createVerticalSection(),
                                               ProductsSampleComponents.createVerticalSection()))
                                       .build(),
                               LinkkiTabSheet.builder("tab3")
                                       .caption("Tables and Sections")
                                       .content(() -> new VerticalLayout(
                                               ProductsSampleComponents.createVerticalSection(),
                                               ProductsSampleComponents.createHorizontalSection(),
                                               ProductsSampleComponents.createTableSection(5),
                                               ProductsSampleComponents.createTableSection(10),
                                               ProductsSampleComponents.createVerticalSection()))
                                       .build());

        // TODO Property API and solution in LIN-2249
        tabLayout.getElement().getStyle().set("--linkki-form-item-label-width", "15em");

        Accordion accordion = new Accordion();
        accordion.add(createPanel("Tool", VaadinIcon.LIST, ProductsSampleComponents.createVerticalSection()));
        accordion.add(createPanel("Table", VaadinIcon.TABLE, ProductsSampleComponents.createTableSection(5)));
        VerticalLayout toolsArea = new VerticalLayout(accordion);
        accordion.setSizeFull();

        SplitLayout splitLayout = new SplitLayout(tabLayout, toolsArea);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        splitLayout.setSplitterPosition(75);
        splitLayout.setSizeFull();

        add(splitLayout);
    }

    private AccordionPanel createPanel(String caption, VaadinIcon icon, Component content) {
        LinkkiText toolCaption = new LinkkiText(caption, icon);
        AccordionPanel accordionPanel = new AccordionPanel(toolCaption, content);
        accordionPanel.addThemeVariants(DetailsVariant.REVERSE);
        return accordionPanel;
    }

    private Headline createHeadline() {
        Headline headline = new Headline("Details");
        headline.getContent().add(new Span("additional label"));
        headline.getContent()
                .add(new HorizontalLayout(createMenuBar("Header Button 1"),
                        createMenuBar("Header Button 2"),
                        createMenuBar("Header Button 3", "Item 1", "Item 2", "Item 3")));
        return headline;
    }

    private MenuBar createMenuBar(String caption, String... submenus) {
        MenuBar menuBar = new MenuBar();
        MenuItem item = menuBar.addItem(caption);
        for (String subMenuCaption : submenus) {
            item.getSubMenu().addItem(subMenuCaption);
        }
        return menuBar;
    }

}

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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.component.Headline;

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
 * Layout to display detailed content of a policy or offer.
 */
public class ProductsSampleDetailsComponent extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final LinkkiTabLayout tabLayout;

    public ProductsSampleDetailsComponent() {

        setHeightFull();
        setSpacing(false);

        add(createHeadline());

        tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);

        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("tab1")
                                       .caption("Just Sections")
                                       .content(() -> new DefaultBindingManagerPage(
                                               new ProductsSamplePmo.VerticalSamplePmo(),
                                               new ProductsSamplePmo.VerticalSamplePmo(),
                                               new ProductsSamplePmo.HorizontalSamplePmo(),
                                               new ProductsSamplePmo.VerticalSamplePmo(),
                                               new ProductsSamplePmo.VerticalSamplePmo()))
                                       .build(),
                               LinkkiTabSheet.builder("tab3")
                                       .caption("Tables and Sections")
                                       .content(() -> new DefaultBindingManagerPage(
                                               new ProductsSamplePmo.VerticalSamplePmo(),
                                               new ProductsSamplePmo.HorizontalSamplePmo(),
                                               new ProductsSampleTablePmo(5, 0),
                                               new ProductsSampleTablePmo(10, 0),
                                               new ProductsSamplePmo.VerticalSamplePmo()))
                                       .build());

        ComponentStyles.setFormItemLabelWidth(tabLayout, "15em");

        Accordion accordion = new Accordion();
        accordion.add(createPanel("Tool", VaadinIcon.LIST, new ProductsSamplePmo.VerticalSamplePmo("")));
        accordion.add(createPanel("Table", VaadinIcon.TABLE, new AccordionTablePmo()));
        VerticalLayout toolsArea = new VerticalLayout(accordion);
        accordion.setSizeFull();

        SplitLayout splitLayout = new SplitLayout(tabLayout, toolsArea);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        splitLayout.setSplitterPosition(75);
        splitLayout.setSizeFull();

        add(splitLayout);
    }

    private static Headline createHeadline() {
        Headline headline = new Headline("Details");
        headline.getContent().add(new Span("additional label"));
        headline.getContent()
                .add(new HorizontalLayout(createMenuBar("Header Button 1"),
                        createMenuBar("Header Button 2"),
                        createMenuBar("Header Button 3", "Item 1", "Item 2", "Item 3")));
        return headline;
    }

    private static MenuBar createMenuBar(String caption, String... submenus) {
        MenuBar menuBar = new MenuBar();
        MenuItem item = menuBar.addItem(caption);
        for (String subMenuCaption : submenus) {
            item.getSubMenu().addItem(subMenuCaption);
        }
        return menuBar;
    }

    private AccordionPanel createPanel(String caption, VaadinIcon icon, Object pmo) {
        LinkkiText toolCaption = new LinkkiText(caption, icon);
        AccordionPanel accordionPanel = new AccordionPanel(toolCaption,
                VaadinUiCreator.createComponent(pmo, new BindingContext()));
        accordionPanel.addThemeVariants(DetailsVariant.LUMO_REVERSE);
        return accordionPanel;
    }

    LinkkiTabLayout getTabLayout() {
        return tabLayout;
    }

    @UISection
    private static class AccordionTablePmo implements ContainerPmo<AccordionRowPmo> {

        @Override
        public List<AccordionRowPmo> getItems() {
            return List.of(new AccordionRowPmo(Optional.empty(), LocalDate.now(), "process 2"),
                           new AccordionRowPmo(Optional.of(VaadinIcon.ARROW_RIGHT), LocalDate.now().minusDays(1),
                                   "process 1"),
                           new AccordionRowPmo(Optional.empty(), LocalDate.now().minusDays(2), "process 1"));
        }

        @Override
        public int getPageLength() {
            return 0;
        }
    }

    static class AccordionRowPmo {

        private final Optional<VaadinIcon> icon;
        private final LocalDate date;
        private final String text;

        public AccordionRowPmo(Optional<VaadinIcon> icon, LocalDate date, String text) {
            this.icon = icon;
            this.date = date;
            this.text = text;
        }

        @UITableColumn(width = 23)
        @BindIcon
        @UILabel(position = 10)
        public String getIcon() {
            return "";
        }

        public VaadinIcon getIconIcon() {
            return icon.orElse(null);
        }

        @UILabel(position = 20, label = "Date")
        public LocalDate getDate() {
            return date;
        }

        @UILabel(position = 30, label = "Text")
        public String getText() {
            return text;
        }
    }
}

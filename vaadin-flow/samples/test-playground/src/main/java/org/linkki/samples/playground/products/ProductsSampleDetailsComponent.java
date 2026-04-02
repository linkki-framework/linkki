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

import static org.linkki.core.ui.creation.VaadinUiCreator.createComponent;

import java.io.Serial;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIBadge;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UIMenuButton;
import org.linkki.core.ui.element.annotation.UIMenuList;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.component.HeadlinePmo;
import org.linkki.framework.ui.component.infotool.InfoTool;
import org.linkki.util.Sequence;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Layout to display detailed content of a policy or offer.
 */
public class ProductsSampleDetailsComponent extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductsSampleDetailsComponent() {
        setHeightFull();
        setSpacing(false);

        var modelObject = new ProductsSampleModelObject();
        var bindingManager = new ProductSampleBindingManager(modelObject::validate);

        add(VaadinUiCreator.createComponent(new HeadlinePmo("Details", new HeadlineAdditionalTitlePmo(),
                new HeadlineButtonsPmo()), bindingManager.getContext(HeadlinePmo.class)));

        var mainArea = createMainArea(bindingManager, modelObject);
        var infoTools = new ProductSampleInfoToolComponent(
                Sequence.of(
                            new InfoTool("tool", "Tool", createComponent(new ProductsSamplePmo.VerticalSamplePmo(
                                    ""), new BindingContext())),
                            new InfoTool("table-tool", "Table",
                                    createComponent(new AccordionTablePmo(), new BindingContext()))));

        var splitLayout = new SplitLayout(mainArea, infoTools);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        splitLayout.setSplitterPosition(75);
        splitLayout.addClassNames(LumoUtility.Width.FULL, LumoUtility.Flex.GROW, LumoUtility.Overflow.AUTO);

        add(splitLayout);
    }

    private Component createMainArea(ProductSampleBindingManager bindingManager,
            ProductsSampleModelObject modelObject) {
        var tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);
        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("tab1")
                                       .caption("Just Sections")
                                       .content(() -> new ProductSamplePage(bindingManager,
                                               new ProductsSamplePmo.VerticalSamplePmo(modelObject),
                                               new ProductsSamplePmo.VerticalSamplePmo(modelObject),
                                               new ProductsSamplePmo.HorizontalSamplePmo(modelObject),
                                               new ProductsSamplePmo.VerticalSamplePmo(modelObject),
                                               new ProductsSamplePmo.VerticalSamplePmo(modelObject)))
                                       .build(),
                               LinkkiTabSheet.builder("tab3")
                                       .caption("Tables and Sections")
                                       // attention: these are in the same binding context as tab1
                                       // as the same page class is used
                                       .content(() -> new ProductSamplePage(bindingManager,
                                               new ProductsSamplePmo.VerticalSamplePmo(modelObject),
                                               new ProductsSamplePmo.HorizontalSamplePmo(modelObject),
                                               new ProductsSampleTablePmo(5, 0),
                                               new ProductsSampleTablePmo(10, 0),
                                               new ProductsSamplePmo.VerticalSamplePmo(modelObject)))
                                       .build());
        ComponentStyles.setFormItemLabelWidth(tabLayout, "15em");

        var messagePanelLayout = new MessagesSplitLayout();
        messagePanelLayout.setContentComponent(tabLayout);
        bindingManager.setMessagesHandler(messagePanelLayout::displayMessages);

        return messagePanelLayout;
    }

    @BindStyleNames(LumoUtility.Display.FLEX)
    @UICssLayout
    private static class HeadlineAdditionalTitlePmo {

        @UIBadge(position = 0)
        public String getBadge() {
            return "badge";
        }
    }

    @UIHorizontalLayout
    private static class HeadlineButtonsPmo {

        @UIMenuButton(position = 0, icon = VaadinIcon.BUTTON, caption = "menu button")
        public void menuButton() {
            // does nothing
        }

        @UIButton(position = 1, caption = "button")
        public void button() {
            // does nothing
        }

        @UIMenuList(position = 2, caption = "menu list")
        public List<MenuItemDefinition> getMenuList() {
            return List.of(new MenuItemDefinition("item 1", null, Handler.NOP_HANDLER),
                           new MenuItemDefinition("item 2", null, Handler.NOP_HANDLER),
                           new MenuItemDefinition("item 3", null, Handler.NOP_HANDLER));
        }
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

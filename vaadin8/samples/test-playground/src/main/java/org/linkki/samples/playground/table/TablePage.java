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

package org.linkki.samples.playground.table;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.TableSection;
import org.linkki.samples.playground.table.dynamicfields.DynamicFieldComponent;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TablePage extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "Table";

    private static final long serialVersionUID = 1L;

    private final List<TableModelObject> items;

    private PlaygroundTablePmo selectableTableSectionPmo;

    private BindingContext bindingContext;

    public TablePage() {
        items = IntStream.range(1, 10)
                .mapToObj(TableModelObject::new)
                .collect(toList());
        addSectionsForSelectableTable();
        addComponent(new DynamicFieldComponent());
    }

    @SuppressWarnings("deprecation")
    private void addSectionsForSelectableTable() {
        addComponent(new Label("Validation error for names which do not end with index or dates in the past."));
        addComponent(new Label("Validation markers must be visible after sidebar sheet switched."));

        DefaultBindingManager bindingManager = new DefaultBindingManager(() -> validate());

        bindingContext = bindingManager.getContext("selectableTable");
        selectableTableSectionPmo = new PlaygroundTablePmo(
                () -> items,
                () -> items.add(new TableModelObject(items.size() + 1)),
                o -> items.remove(o));

        TableSection section = (TableSection)new PmoBasedSectionFactory()
                .createSection(selectableTableSectionPmo, bindingContext);
        addComponent(section);
        com.vaadin.v7.ui.Table table = section.getSectionContent();

        BindingContext comparisonBindingContext = new BindingContext("selectableTableComparison");
        addComponent(VaadinUiCreator.createComponent(
                                                     new SelectionComparisonSectionPmo(
                                                             () -> (PlaygroundRowPmo)table.getValue(),
                                                             () -> selectableTableSectionPmo.getSelection(),
                                                             comparisonBindingContext::modelChanged),
                                                     comparisonBindingContext));
    }

    MessageList validate() {
        MessageList messages = items.stream()
                .filter(e -> !e.getName().endsWith(String.valueOf(e.getIndex())))
                .map(e -> Message
                        .builder("Name does not end with index " + e.getIndex(), Severity.ERROR)
                        .invalidObjectWithProperties(e, TableModelObject.PROPERTY_NAME)
                        .create())
                .collect(MessageList.collector());

        messages.add(items.stream()
                .filter(e -> e.getDate().isBefore(LocalDate.now()))
                .map(e -> Message
                        .builder("Date must be at least today.", Severity.ERROR)
                        .invalidObjectWithProperties(e, TableModelObject.PROPERTY_DATE)
                        .create())
                .collect(MessageList.collector()));

        return messages;
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.TABLE;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}

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
package org.linkki.samples.playground.binding.components;

import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.UiUpdateObserver;
import org.linkki.core.ui.creation.table.GridComponentCreator;
import org.linkki.samples.playground.binding.model.Contact;
import org.linkki.samples.playground.binding.pmo.ContactRowPmo;
import org.linkki.samples.playground.binding.pmo.ContactTablePmo;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ContactsTableComponent extends Div implements UiUpdateObserver {

    private static final long serialVersionUID = 1L;

    private final List<Contact> contactStorage;
    private final BindingContext context;

    private Grid<?> table;
    private VerticalLayout noContentLabel;

    public ContactsTableComponent(List<Contact> contactStorage, Consumer<Contact> editAction,
            BindingContext bindingContext) {

        this.contactStorage = contactStorage;
        this.context = bindingContext;

        createContent(editAction);
        uiUpdated();
    }

    private void createContent(Consumer<Contact> editAction) {
        table = GridComponentCreator.createGrid(new ContactTablePmo(contactStorage, editAction, contactStorage::remove),
                                                context);
        table.setSelectionMode(SelectionMode.SINGLE);
        table.asSingleSelect();
        table.addItemClickListener(e -> ((ContactRowPmo)e.getItem()).edit());

        noContentLabel = new VerticalLayout(new Span("No contacts available"));
        noContentLabel.setWidth(null);
        noContentLabel.setMargin(true);
        add(noContentLabel, table);
    }

    @Override
    public void uiUpdated() {
        noContentLabel.setVisible(contactStorage.isEmpty());
        table.setVisible(!contactStorage.isEmpty());
    }
}

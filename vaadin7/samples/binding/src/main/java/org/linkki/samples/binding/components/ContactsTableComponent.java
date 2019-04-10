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
package org.linkki.samples.binding.components;

import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.UiUpdateObserver;
import org.linkki.core.ui.table.PmoBasedTableFactory;
import org.linkki.samples.binding.model.Contact;
import org.linkki.samples.binding.pmo.ContactTablePmo;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ContactsTableComponent extends Panel implements UiUpdateObserver {

    private static final long serialVersionUID = 1L;

    private final List<Contact> contactStorage;
    private final BindingContext context;

    private Table table;
    private VerticalLayout noContentLabel;

    public ContactsTableComponent(List<Contact> contactStorage, Consumer<Contact> editAction,
            BindingContext bindingContext) {

        this.contactStorage = contactStorage;
        this.context = bindingContext;

        setCaption("Contacts");
        createContent(editAction);
        uiUpdated();
    }

    private void createContent(Consumer<Contact> editAction) {
        table = new PmoBasedTableFactory(new ContactTablePmo(contactStorage, editAction, contactStorage::remove),
                context).createTable();

        noContentLabel = new VerticalLayout(new Label("No contacts available"));
        noContentLabel.setMargin(true);
    }

    @Override
    public void uiUpdated() {
        if (contactStorage.isEmpty()) {
            setContent(noContentLabel);
        } else {
            setContent(table);
        }
    }
}

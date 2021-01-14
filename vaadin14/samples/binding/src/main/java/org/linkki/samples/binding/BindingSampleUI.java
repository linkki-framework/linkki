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
package org.linkki.samples.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.samples.binding.components.ContactComponent;
import org.linkki.samples.binding.components.ContactsTableComponent;
import org.linkki.samples.binding.model.Contact;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;

@Route("")
public class BindingSampleUI extends Div {

    private static final long serialVersionUID = 42L;

    // can be switched with URL parameters:
    // http://localhost:8080/linkki-sample-binding-vaadin14/?editMode=READ_ONLY
    private EditMode editMode = EditMode.EDIT;

    private static final List<Contact> PERSON_STORAGE = new ArrayList<>();

    public BindingSampleUI() {
        editMode = Optional.ofNullable(VaadinServletRequest.getCurrent()
                .getParameter("editMode"))
                .flatMap(EditMode::read)
                .orElse(EditMode.EDIT);

        UI.getCurrent().getPage().setTitle("linkki Sample :: Bindings");

        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        PropertyBehaviorProvider behaviorProvider = PropertyBehaviorProvider
                .with(PropertyBehavior.readOnly(editMode::isReadOnly));
        BindingContext context = bindingManager.createContext("binding-sample", behaviorProvider);

        ContactComponent contactComponent = new ContactComponent(p -> save(p, PERSON_STORAGE), context);
        ContactsTableComponent contactsTable = new ContactsTableComponent(PERSON_STORAGE, contactComponent::editContact,
                context);
        // Make ContactsTableComponent call uiUpdated explicitly to switch between label or table
        bindingManager.addUiUpdateObserver(contactsTable);

        SplitLayout panel = new SplitLayout(contactComponent, contactsTable);
        panel.setOrientation(Orientation.HORIZONTAL);
        panel.setSplitterPosition(50);
        // TODO LIN-2088
        // HorizontalSplitPanel panel = new HorizontalSplitPanel(contactComponent, contactsTable);
        // panel.setLocked(true);

        add(panel);
    }

    private static void save(Contact contact, List<Contact> personStorage) {
        if (!personStorage.contains(contact)) {
            personStorage.add(contact);
        }
    }

    public enum EditMode {
        EDIT(false),
        READ_ONLY(true);

        private final boolean readOnly;

        public boolean isReadOnly() {
            return readOnly;
        }

        private EditMode(boolean readOnly) {
            this.readOnly = readOnly;
        }

        public static Optional<EditMode> read(String s) {
            try {
                return Optional.of(valueOf(s));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

}
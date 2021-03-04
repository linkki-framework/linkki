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
package org.linkki.samples.playground.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.samples.playground.binding.components.ContactComponent;
import org.linkki.samples.playground.binding.components.ContactsTableComponent;
import org.linkki.samples.playground.binding.model.Contact;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings({ "java:S2160", "java:S110" })
public class BindingSampleView extends VerticalLayout implements View {

    public static final String NAME = "binding";

    private static final long serialVersionUID = 42L;

    private static final List<Contact> PERSON_STORAGE = new ArrayList<>();

    @Override
    public void enter(ViewChangeEvent event) {
        EditMode editMode = Optional.ofNullable(event
                .getParameterMap().get("editMode"))
                .flatMap(EditMode::read)
                .orElse(EditMode.EDIT);

        Page.getCurrent().setTitle("linkki Sample :: Bindings");

        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        // tag::property-behavior[]
        PropertyBehaviorProvider behaviorProvider = PropertyBehaviorProvider
                .with(PropertyBehavior.readOnly(editMode::isReadOnly));
        // end::property-behavior[]
        // tag::property-behavior-binding-manager
        BindingContext context = bindingManager.createContext("binding-sample", behaviorProvider);
        // end::property-behavior-binding-manager

        ContactComponent contactComponent = new ContactComponent(p -> save(p, PERSON_STORAGE), context);
        ContactsTableComponent contactsTable = new ContactsTableComponent(PERSON_STORAGE, contactComponent::editContact,
                context);
        // Make ContactsTableComponent call uiUpdated explicitly to switch between label or table
        bindingManager.addUiUpdateObserver(contactsTable);

        HorizontalSplitPanel panel = new HorizontalSplitPanel(contactComponent, contactsTable);
        panel.setLocked(true);

        addComponent(panel);
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
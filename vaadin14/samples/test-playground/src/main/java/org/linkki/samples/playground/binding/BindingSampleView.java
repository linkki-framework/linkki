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

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.samples.playground.binding.components.ContactComponent;
import org.linkki.samples.playground.binding.components.ContactsTableComponent;
import org.linkki.samples.playground.binding.model.Contact;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "binding", layout = PlaygroundAppLayout.class)
@PageTitle("linkki Sample :: Bindings")
public class BindingSampleView extends Div implements HasUrlParameter<String> {

    private static final long serialVersionUID = 42L;

    private static final List<Contact> PERSON_STORAGE = new ArrayList<>();

    private static void save(Contact contact, List<Contact> personStorage) {
        if (!personStorage.contains(contact)) {
            personStorage.add(contact);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        removeAll();
        // can be switched with URL:
        // http://localhost:8080/linkki-sample-test-playground-vaadin14/binding/readOnly
        boolean readOnly = StringUtils.equals("readOnly", parameter);

        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        // tag::property-behavior[]
        PropertyBehaviorProvider behaviorProvider = PropertyBehaviorProvider
                .with(PropertyBehavior.readOnly(() -> readOnly));
        // end::property-behavior[]
        // tag::property-behavior-binding-manager
        BindingContext context = bindingManager.createContext("binding-sample", behaviorProvider);
        // end::property-behavior-binding-manager

        ContactComponent contactComponent = new ContactComponent(p -> save(p, PERSON_STORAGE), context);
        ContactsTableComponent contactsTable = new ContactsTableComponent(PERSON_STORAGE, contactComponent::editContact,
                context);
        // Make ContactsTableComponent call uiUpdated explicitly to switch between label or table
        bindingManager.addUiUpdateObserver(contactsTable);

        SplitLayout panel = new SplitLayout(contactComponent, contactsTable);
        panel.setOrientation(Orientation.HORIZONTAL);
        panel.setSplitterPosition(50);

        add(panel);
    }

}
/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.samples.binding.components;

import java.util.function.Consumer;

import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.samples.binding.model.Address;
import org.linkki.samples.binding.model.Person;
import org.linkki.samples.binding.pmo.AddressPmo;
import org.linkki.samples.binding.pmo.ButtonsSectionPmo;
import org.linkki.samples.binding.pmo.PersonSectionPmo;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ContactComponent extends Panel {

    private static final long serialVersionUID = 1L;

    private final PmoBasedSectionFactory sectionFactory = new DefaultPmoBasedSectionFactory();

    private final BindingContext context;

    private final PersonSectionPmo personPmo;
    private final AddressPmo addressPmo;
    private final ButtonsSectionPmo buttonsSectionPmo;

    private final Consumer<Person> persistAction;

    public ContactComponent(Consumer<Person> persistAction, BindingContext bindingContext) {

        this.context = bindingContext;
        this.persistAction = persistAction;

        Person person = new Person();
        this.personPmo = new PersonSectionPmo(person);
        this.addressPmo = new AddressPmo(person.getAddress());

        this.buttonsSectionPmo = new ButtonsSectionPmo(this::canSave, this::save, this::reset);

        setCaption("Create / Edit Contact");
        createContent();
    }

    public void editContact(Person person) {
        resetPmos(person);
    }

    private void createContent() {

        AbstractSection personSection = sectionFactory.createSection(personPmo, context);

        AddressFields addressFields = new AddressFields();
        AddressComponent addressComponent = new AddressComponent(addressFields);

        // tag::manual-binding[]
        new Binder(addressFields, addressPmo).setupBindings(context);
        // end::manual-binding[]

        AbstractSection buttonsSection = sectionFactory.createSection(buttonsSectionPmo, context);

        setContent(new VerticalLayout(personSection, addressComponent, buttonsSection));
    }

    private boolean canSave() {
        return personPmo.isInputValid() && addressPmo.isInputValid();
    }

    private void save() {

        Address address = addressPmo.getAddress();
        Person person = personPmo.getPerson();
        person.setAddress(address);

        persistAction.accept(person);

        reset();
    }

    private void reset() {
        resetPmos(new Person());
    }

    private void resetPmos(Person person) {

        personPmo.reset(person);
        addressPmo.reset(person.getAddress());

        context.updateUI();
    }

}

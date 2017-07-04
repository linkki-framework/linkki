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
import org.linkki.samples.binding.pmo.PersonPmo;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ContactComponent extends Panel {

    private static final long serialVersionUID = 1L;

    private final PmoBasedSectionFactory sectionFactory = new DefaultPmoBasedSectionFactory();

    private final BindingContext context;

    private final PersonPmo personPmo;
    private final AddressPmo addressPmo;
    private final ButtonsSectionPmo buttonsSectionPmo;

    private final Consumer<Person> persistAction;

    public ContactComponent(Consumer<Person> persistAction, BindingContext bindingContext) {

        this.context = bindingContext;
        this.persistAction = persistAction;

        Person person = new Person();
        this.personPmo = new PersonPmo(person);
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

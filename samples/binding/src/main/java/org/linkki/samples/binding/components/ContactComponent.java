package org.linkki.samples.binding.components;

import java.util.function.Consumer;

import org.linkki.core.binding.Binder;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.samples.binding.model.Address;
import org.linkki.samples.binding.model.Person;
import org.linkki.samples.binding.pmo.AddressPmo;
import org.linkki.samples.binding.pmo.PersonPmo;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ContactComponent extends Panel {

    private static final long serialVersionUID = 1L;

    private final BindingContext context;

    private final PersonPmo personPmo;
    private final AddressPmo addressPmo;

    public ContactComponent(PersonPmo personPmo, AddressPmo addressPmo, Consumer<Person> saveAction,
            BindingContext bindingContext) {

        this.personPmo = personPmo;
        this.addressPmo = addressPmo;
        this.context = bindingContext;

        setCaption("Create / Edit Contact");
        createContent(saveAction);
    }

    public void editContact(Person person) {
        refreshPmos(person);
    }

    private void createContent(Consumer<Person> saveAction) {

        AbstractSection personSection = new DefaultPmoBasedSectionFactory().createSection(personPmo, context);

        AddressFields addressFields = new AddressFields();
        AddressComponent addressComponent = new AddressComponent(addressFields);

        // tag::manual-binding[]
        new Binder(addressFields, addressPmo).setupBindings(context);
        // end::manual-binding[]

        Button saveBtn = new Button("Save", FontAwesome.SAVE);
        saveBtn.addClickListener(e -> save(saveAction));
        saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        setContent(new VerticalLayout(personSection, addressComponent, saveBtn));
    }

    private void save(Consumer<Person> saveAction) {

        Address address = personPmo.getPerson().getAddress();
        address.setStreet(addressPmo.getStreet());
        address.setZip(addressPmo.getZip());
        address.setCity(addressPmo.getCity());
        address.setCountry(addressPmo.getCountry());

        saveAction.accept(personPmo.getPerson());
        refreshPmos(new Person());
    }

    private void refreshPmos(Person person) {

        personPmo.refreshPerson(person);
        addressPmo.refreshAddress(person.getAddress());

        context.updateUI();
    }

}

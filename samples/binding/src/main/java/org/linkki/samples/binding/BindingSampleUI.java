package org.linkki.samples.binding;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingManager;
import org.linkki.core.binding.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.samples.binding.components.ContactComponent;
import org.linkki.samples.binding.components.ContactsTableComponent;
import org.linkki.samples.binding.model.Person;
import org.linkki.samples.binding.pmo.AddressPmo;
import org.linkki.samples.binding.pmo.PersonPmo;

@Theme(value = "valo")
public class BindingSampleUI extends UI {


    @Override
    protected void init(VaadinRequest request) {

        Page.getCurrent().setTitle("Linkki Sample :: Bindings");

        List<Person> personStorage = new ArrayList<>();

        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        BindingContext context = bindingManager.startNewContext("binding-sample");

        PersonPmo personPmo = new PersonPmo(new Person());
        AddressPmo addressPmo = new AddressPmo(personPmo.getPerson().getAddress());

        ContactComponent contactComponent = new ContactComponent(personPmo,
                                                                 addressPmo,
                                                                 p -> save(p, personStorage),
                                                                 context);

        ContactsTableComponent contactsTable = new ContactsTableComponent(personStorage,
                                                                          contactComponent::editContact,
                                                                          context);
        bindingManager.registerUiUpdateObserver(contactsTable);

        HorizontalSplitPanel panel = new HorizontalSplitPanel(contactComponent, contactsTable);
        panel.setLocked(true);

        setContent(panel);
    }


    private static void save(Person person, List<Person> personStorage) {
        if (!personStorage.contains(person)) {
            personStorage.add(person);
        }
    }

}

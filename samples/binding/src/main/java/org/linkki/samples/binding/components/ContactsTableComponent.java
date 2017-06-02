package org.linkki.samples.binding.components;

import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.UiUpdateObserver;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.core.ui.table.TableSection;
import org.linkki.samples.binding.model.Person;
import org.linkki.samples.binding.pmo.PersonRowPmo;
import org.linkki.samples.binding.pmo.PersonTablePmo;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class ContactsTableComponent extends Panel implements UiUpdateObserver {

    private static final long serialVersionUID = 1L;

    private final List<Person> personStorage;
    private final BindingContext context;

    private TableSection<PersonRowPmo> tableSection;
    private Label noContentLabel;

    public ContactsTableComponent(List<Person> personStorage, Consumer<Person> editAction,
            BindingContext bindingContext) {

        this.personStorage = personStorage;
        this.context = bindingContext;

        setCaption("Contacts");
        createContent(editAction);
        uiUpdated();
    }

    private void createContent(Consumer<Person> editAction) {

        DefaultPmoBasedSectionFactory sectionFactory = new DefaultPmoBasedSectionFactory();
        tableSection = sectionFactory
                .createTableSection(new PersonTablePmo(personStorage, editAction, personStorage::remove), context);

        noContentLabel = new Label("No contacts available");
    }

    @Override
    public void uiUpdated() {
        if (personStorage.isEmpty()) {
            setContent(noContentLabel);
        } else {
            setContent(tableSection);
        }
    }
}

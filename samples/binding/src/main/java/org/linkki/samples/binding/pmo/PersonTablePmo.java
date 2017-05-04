package org.linkki.samples.binding.pmo;

import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.SimpleItemSupplier;
import org.linkki.samples.binding.model.Person;

//tag::personTablePmo-class[]
@UISection
public class PersonTablePmo implements ContainerPmo<PersonRowPmo> {

    //end::personTablePmo-class[]
    private final SimpleItemSupplier<PersonRowPmo, Person> items;


    public PersonTablePmo(List<Person> persons,
                          Consumer<Person> editAction,
                          Consumer<Person> deleteAction) {

        items = new SimpleItemSupplier<>(() -> persons, p -> new PersonRowPmo(p, editAction, deleteAction));
    }


    //tag::personTablePmo-getItems[]
    @Override
    public List<PersonRowPmo> getItems() {
        return items.get();
    }
    //end::personTablePmo-getItems[]

    @Override
    public int getPageLength() {
        return Math.min(15, getItems().size());
    }
}

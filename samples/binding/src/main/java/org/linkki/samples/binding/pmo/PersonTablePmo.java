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
package org.linkki.samples.binding.pmo;

import java.util.List;
import java.util.function.Consumer;

import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.SimpleItemSupplier;
import org.linkki.samples.binding.model.Person;

// tag::personTablePmo-class[]
@UISection
public class PersonTablePmo implements ContainerPmo<PersonRowPmo> {

    // end::personTablePmo-class[]
    private final SimpleItemSupplier<PersonRowPmo, Person> items;

    // tag::item-supplier[]
    public PersonTablePmo(List<Person> persons, Consumer<Person> editAction, Consumer<Person> deleteAction) {
        items = new SimpleItemSupplier<>(() -> persons,
                                          p -> new PersonRowPmo(p, editAction, deleteAction));
    }
    // end::item-supplier[]

    // tag::personTablePmo-getItems[]
    @Override
    public List<PersonRowPmo> getItems() {
        return items.get();
    }
    // end::personTablePmo-getItems[]

    // tag::page-length[]
    @Override
    public int getPageLength() {
        return Math.min(ContainerPmo.DEFAULT_PAGE_LENGTH, getItems().size());
    }
    // end::page-length[]
}

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
package org.linkki.samples.binding.pmo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.core.ui.table.SimpleItemSupplier;
import org.linkki.core.ui.table.TableFooterPmo;
import org.linkki.samples.binding.model.Contact;

@SuppressWarnings("null")
// tag::contactTablePmo-class[]
public class ContactTablePmo implements ContainerPmo<ContactRowPmo> {

    // end::contactTablePmo-class[]
    private final SimpleItemSupplier<ContactRowPmo, Contact> items;

    // tag::item-supplier[]
    public ContactTablePmo(List<Contact> contacts, Consumer<Contact> editAction, Consumer<Contact> deleteAction) {
        items = new SimpleItemSupplier<>(() -> contacts,
                p -> new ContactRowPmo(p, editAction, deleteAction));
    }
    // end::item-supplier[]

    // tag::contactTablePmo-getItems[]
    @Override
    public List<ContactRowPmo> getItems() {
        return items.get();
    }
    // end::contactTablePmo-getItems[]

    // tag::page-length[]
    @Override
    public int getPageLength() {
        return Math.min(ContainerPmo.DEFAULT_PAGE_LENGTH, getItems().size());
    }
    // end::page-length[]

    @Override
    public @NonNull Optional<@NonNull TableFooterPmo> getFooterPmo() {
        long favorites = items.get().stream().filter(ContactRowPmo::isFavorite).count();
        return favorites == 0 ? Optional.empty()
                : Optional.of(s -> "favorite".equals(s) ? Long.toString(favorites) : "");
    }
}

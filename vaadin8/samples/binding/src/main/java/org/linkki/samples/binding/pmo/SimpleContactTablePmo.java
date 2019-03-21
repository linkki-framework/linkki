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
import java.util.function.Consumer;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.samples.binding.model.Contact;

// tag::simpleContactTablePmo[]
@UISection
public class SimpleContactTablePmo extends SimpleTablePmo<Contact, ContactRowPmo> {

    private final Consumer<Contact> editAction;
    private final Consumer<Contact> deleteAction;

    public SimpleContactTablePmo(List<Contact> Contacts, Consumer<Contact> editAction, Consumer<Contact> deleteAction) {
        super(Contacts);
        this.editAction = editAction;
        this.deleteAction = deleteAction;
    }

    @Override
    protected ContactRowPmo createRow(Contact Contact) {
        return new ContactRowPmo(Contact, editAction, deleteAction);
    }
}
// end::simpleContactTablePmo[]
/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.samples.appsample.pmo.table;

import java.util.function.Consumer;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.samples.appsample.model.Address;
import org.linkki.samples.appsample.pmo.dialog.AddressPmo;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

// tag::rowAfterExtraction[]
public class AddressRowPmo extends AddressPmo {

    // tag::deleteButton[]
    private Consumer<Address> deleteConsumer;

    // end::deleteButton[]
    public AddressRowPmo(Address address, Consumer<Address> deleteConsumer) {
        super(address);
        this.deleteConsumer = deleteConsumer;
    }

    @BindReadOnlyBehavior(value = ReadOnlyBehaviorType.WRITABLE)
    @UITableColumn(width = 50)
    // tag::deleteButton[]
    @UIButton(position = 60, captionType = CaptionType.NONE, showIcon = true, icon = VaadinIcon.TRASH, variants = {
            ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL })
    public void deleteButton() {
        deleteConsumer.accept(getAddress());
    }
    // end::deleteButton[]
}
// end::rowAfterExtraction[]

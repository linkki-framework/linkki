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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.framework.ui.dialogs.UIOpenDialogButton;
import org.linkki.samples.appsample.model.Address;
import org.linkki.samples.appsample.pmo.dialog.AddAddressDialogPmo;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection(caption = "Addresses")
public class AddressTablePmo extends SimpleTablePmo<Address, AddressRowPmo> {

    private final Consumer<Address> deleteAddress;
    // tag::createConsumer[]
    private final Consumer<Address> createConsumer;

    public AddressTablePmo(Supplier<List<? extends Address>> modelObjectsSupplier, Consumer<Address> deleteAddress,
            Consumer<Address> createConsumer) {
        super(modelObjectsSupplier);
        this.deleteAddress = deleteAddress;
        this.createConsumer = createConsumer;
    }
    // end::createConsumer[]

    @Override
    protected AddressRowPmo createRow(Address address) {
        return new AddressRowPmo(address, deleteAddress);
    }

    // tag::getAddAddressDialogPmo[]
    @SectionHeader
    @BindIcon(VaadinIcon.PLUS)
    @UIOpenDialogButton(position = 10, captionType = CaptionType.NONE)
    public DialogPmo getAddAddressDialogPmo() {
        return new AddAddressDialogPmo(createConsumer);
    }
    // end::getAddAddressDialogPmo[]

}

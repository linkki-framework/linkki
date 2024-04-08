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
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.appsample.model.Address;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

@UISection(caption = "Addresses")
public class AddressTablePmo extends SimpleTablePmo<Address, AddressRowPmo> {

    private Consumer<Address> deleteAddress;
    // tag::createHandler[]
    private Handler createHandler;

    public AddressTablePmo(Supplier<List<? extends Address>> modelObjectsSupplier, Consumer<Address> deleteAddress,
            Handler createHandler) {
        super(modelObjectsSupplier);
        this.deleteAddress = deleteAddress;
        this.createHandler = createHandler;
    }
    // end::createHandler[]

    @Override
    protected AddressRowPmo createRow(Address address) {
        return new AddressRowPmo(address, deleteAddress);
    }

    // tag::addButton[]
    @SectionHeader
    @UIButton(position = 10, captionType = CaptionType.NONE, showIcon = true, icon = VaadinIcon.PLUS, variants = {
            ButtonVariant.LUMO_CONTRAST })
    public void add() {
        // end::addButton[]
        createHandler.apply();
        // tag::addButton[]
    }
    // end::addButton[]

}

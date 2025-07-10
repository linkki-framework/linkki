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
package org.linkki.samples.appsample.pmo.dialog;

import java.util.function.Consumer;

import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.ips.messages.MessageConverter;
import org.linkki.samples.appsample.model.Address;
import org.linkki.util.handler.Handler;

// tag::addAddressDialogPmo[]
public class AddAddressDialogPmo implements DialogPmo {

    private final Consumer<Address> saveAddress;
    private final AddressPmo contentPmo;

    public AddAddressDialogPmo(Consumer<Address> saveAddress) {
        this.saveAddress = saveAddress;
        contentPmo = new AddressPmo(new Address());
    }

    @Override
    public String getCaption() {
        return "Add Address";
    }

    @Override
    public Handler getOkHandler() {
        return () -> saveAddress.accept(contentPmo.getAddress());
    }

    @Override
    public AddressPmo getContentPmo() {
        return contentPmo;
    }
    // end::addAddressDialogPmo[]

    // tag::validate[]
    @Override
    public MessageList validate() {
        return MessageConverter.convert(contentPmo.getAddress().validate());
    }
    // end::validate[]
    // tag::addAddressDialogPmo_end[]
}
// end::addAddressDialogPmo_end[]

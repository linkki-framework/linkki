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

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.appsample.model.Address;

// tag::addressPmo1[]
@UISection
public class AddressPmo {

    // tag::modelBinding[]
    private final Address address;

    // end::modelBinding[]
    public AddressPmo(Address address) {
        this.address = address;
    }

    // tag::modelBinding[]
    @ModelObject
    public Address getAddress() {
        return address;
    }

    // end::addressPmo1[]
    @UITextField(position = 10, label = "Street", modelAttribute = "street")
    public void street() {
        // model binding
    }

    @UITextField(position = 20, label = "Number", modelAttribute = "streetNumber")
    public void streetNumber() {
        // model binding
    }

    @UITextField(position = 30, label = "Postal Code", modelAttribute = "postalCode")
    public void postalCode() {
        // model binding
    }

    @UITextField(position = 40, label = "City", modelAttribute = "city")
    public void city() {
        // model binding
    }

    @UITextField(position = 50, label = "Country", modelAttribute = "country")
    public void country() {
        // model binding
    }
    // end::modelBinding[]
    // tag::addressPmo2[]
}
// end::addressPmo2[]
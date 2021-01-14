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

import org.linkki.core.pmo.ModelObject;
import org.linkki.samples.binding.model.Address;
import org.linkki.samples.binding.model.Country;
import org.linkki.samples.binding.service.CountryService;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class AddressPmo {

    private Address addressInEdit;

    public AddressPmo(Address address) {
        this.addressInEdit = copyAddress(address);
    }

    @ModelObject
    public Address getAddressInEdit() {
        return addressInEdit;
    }

    public List<Country> getCountryAvailableValues() {
        return CountryService.getCountries();
    }

    public void reset(Address newAddress) {
        this.addressInEdit = copyAddress(newAddress);
    }

    private static Address copyAddress(@CheckForNull Address addressToCopy) {
        if (addressToCopy != null) {
            return new Address(addressToCopy.getStreet(), addressToCopy.getZip(), addressToCopy.getCity(),
                    addressToCopy.getCountry());
        } else {
            return new Address(null, null, null, null);
        }
    }

    public boolean isInputValid() {
        return true;
    }
}

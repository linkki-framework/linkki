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

import org.linkki.samples.binding.model.Address;
import org.linkki.samples.binding.model.Country;
import org.linkki.samples.binding.service.CountryService;

public class AddressPmo {

    private Address address;

    private String street;
    private String zip;
    private String city;
    private Country country;

    public AddressPmo(Address address) {
        reset(address);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Country> getCountryAvailableValues() {
        return CountryService.getCountries();
    }

    public void reset(Address newAddress) {
        this.address = newAddress;

        if (newAddress != null) {
            this.street = newAddress.getStreet();
            this.zip = newAddress.getZip();
            this.city = newAddress.getCity();
            this.country = newAddress.getCountry();
        } else {
            this.street = null;
            this.zip = null;
            this.city = null;
            this.country = null;
        }
    }

    public boolean isInputValid() {
        return true;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address(street, zip, city, country);
        } else {
            address.setStreet(street);
            address.setZip(zip);
            address.setCountry(country);
            address.setCity(city);
        }

        return address;
    }
}

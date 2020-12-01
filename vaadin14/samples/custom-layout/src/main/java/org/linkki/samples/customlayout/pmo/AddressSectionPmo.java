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

package org.linkki.samples.customlayout.pmo;

import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "Address", layout = SectionLayout.CUSTOM)
public class AddressSectionPmo {

    private String givenName;

    private String surname;

    private String street;

    private String number;

    private String zip;

    private String city;

    @UITextField(position = 0, label = "Given Name")
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String giveName) {
        this.givenName = giveName;
    }

    @UITextField(position = 1, label = "Surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String sureName) {
        this.surname = sureName;
    }

    @UITextField(position = 2, label = "Street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @UITextField(position = 3, label = "Number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @UITextField(position = 4, label = "Zip")
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @UITextField(position = 5, label = "City")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
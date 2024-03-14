/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.samples.appsample.model;

import org.faktorips.runtime.MessageList;

/**
 * An object to hold information concerning the business partner's address
 */
public class Address {

    private static final String PROPERTY_STREET = "street";
    private static final String PROPERTY_STREET_NUMBER = "streetNumber";
    private static final String PROPERTY_POSTAL_CODE = "postalCode";
    private static final String PROPERTY_CITY = "city";
    private static final String PROPERTY_COUNTRY = "country";

    public static final String MSG_TEXT_STREET = //
            "The field Street must not be empty.";
    public static final String MSG_CODE_STREET = "error.checkStreet";
    public static final String MSG_TEXT_STREET_NUMBER = //
            "The field Street number must not be empty.";
    public static final String MSG_CODE_STREET_NUMBER = "error.checkNumber";
    public static final String MSG_TEXT_POSTAL_CODE = "The field Postal code must not be empty.";
    public static final String MSG_CODE_POSTAL_CODE = "error.checkCode";
    public static final String MSG_TEXT_CITY = "The field City must not be empty.";
    public static final String MSG_CODE_CITY = "error.checkCity";
    public static final String MSG_TEXT_COUNTRY = "The field Country must not be empty.";
    public static final String MSG_CODE_COUNTRY = "error.checkCountry";

    private String street;
    private String streetNumber;
    private String postalCode;
    private String city;
    private String country;

    public Address(String street, String streetNumber, String city, String country, String postalCode) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Address() {
        this.street = "";
        this.streetNumber = "";
        this.city = "";
        this.country = "";
        this.postalCode = "";
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public MessageList validateStreet() {
        return Validation.validateRequiredProperty(getStreet(), MSG_CODE_STREET, MSG_TEXT_STREET, this,
                                                   PROPERTY_STREET);
    }

    public MessageList validateStreetNumber() {
        return Validation.validateRequiredProperty(getStreetNumber(), MSG_CODE_STREET_NUMBER, MSG_TEXT_STREET_NUMBER,
                                                   this,
                                                   PROPERTY_STREET_NUMBER);
    }

    public MessageList validatePostalCode() {
        return Validation.validateRequiredProperty(getPostalCode(), MSG_CODE_POSTAL_CODE, MSG_TEXT_POSTAL_CODE, this,
                                                   PROPERTY_POSTAL_CODE);
    }

    public MessageList validateCity() {
        return Validation.validateRequiredProperty(getCity(), MSG_CODE_CITY, MSG_TEXT_CITY, this, PROPERTY_CITY);
    }

    public MessageList validateCountry() {
        return Validation.validateRequiredProperty(getCountry(), MSG_CODE_COUNTRY, MSG_TEXT_COUNTRY, this,
                                                   PROPERTY_COUNTRY);
    }

    public MessageList validate() {
        MessageList msgList = new MessageList();
        msgList.add(validateStreet());
        msgList.add(validateStreetNumber());
        msgList.add(validatePostalCode());
        msgList.add(validateCity());
        msgList.add(validateCountry());
        return msgList;
    }

}

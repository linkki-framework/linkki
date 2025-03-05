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
package org.linkki.samples.playground.search.service;

import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.time.LocalDate;

public class SampleSearchParameters {

    public static final String PROPERTY_PARTNER_NUMBER = "partnerNumber";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_GIVEN_NAME = "givenName";
    public static final String PROPERTY_DATE_OF_BIRTH = "dateOfBirth";
    public static final String PROPERTY_SECTOR = "sector";
    public static final String PROPERTY_CLASSIFICATION = "classification";
    public static final String PROPERTY_POSTAL_CODE = "postalCode";
    public static final String PROPERTY_CITY = "city";
    public static final String PROPERTY_STREET_AND_NUMBER = "streetAndNumber";
    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_PHONE = "phone";
    public static final String PROPERTY_IBAN = "iban";

    @CheckForNull
    private String partnerNumber;
    @CheckForNull
    private String name;
    @CheckForNull
    private String givenName;
    @CheckForNull
    private LocalDate dateOfBirth;
    @CheckForNull
    private String sector;
    @CheckForNull
    private String classification;
    @CheckForNull
    private String postalCode;
    @CheckForNull
    private String city;
    @CheckForNull
    private String streetAndNumber;
    @CheckForNull
    private String email;
    @CheckForNull
    private String phone;
    @CheckForNull
    private String iban;

    @CheckForNull
    public String getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(String partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    @CheckForNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @CheckForNull
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @CheckForNull
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @CheckForNull
    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @CheckForNull
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @CheckForNull
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @CheckForNull
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @CheckForNull
    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber;
    }

    @CheckForNull
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @CheckForNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @CheckForNull
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

}

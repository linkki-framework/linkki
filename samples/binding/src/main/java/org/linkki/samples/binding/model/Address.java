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
package org.linkki.samples.binding.model;

public class Address {

    public static final String PROPERTY_STREET = "street";
    public static final String PROPERTY_ZIP = "zip";
    public static final String PROPERTY_CITY = "city";
    public static final String PROPERTY_COUNTRY = "country";

    private String street;
    private String zip;
    private String city;
    private Country country;

    public Address(String street, String zip, String city, Country country) {
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.country = country;
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

    public String asSingleLineString() {
        StringBuilder builder = new StringBuilder(50);
        if (country != null) {
            builder.append(country.getIsoA3()).append(" - ");
        }
        if (zip != null) {
            builder.append(zip);
        }
        if (city != null) {
            appendSpaceIfNeeded(builder).append(city).append(',');
        }
        if (street != null) {
            appendSpaceIfNeeded(builder).append(street);
        }

        return builder.toString();
    }

    private static StringBuilder appendSpaceIfNeeded(StringBuilder builder) {

        if (builder.length() > 0) {
            builder.append(' ');
        }

        return builder;
    }
}

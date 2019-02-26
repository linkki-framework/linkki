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
package org.linkki.samples.binding.model;

public class Contact {

    public static final String PROPERTY_LASTNAME = "lastname";
    public static final String PROPERTY_COUNTRY_OF_BIRTH = "countryOfBirth";
    public static final String PROPERTY_NO_OF_CHILDREN = "noOfChildren";
    public static final String PROPERTY_NOTES_ON_CHILDREN = "notesOnChildren";

    private String firstname;
    private String lastname;
    private boolean favorite;

    private Address address;
    private Gender gender = Gender.NON_BINARY;
    private String countryOfBirth;

    private int noOfChildren;
    private String notesOnChildren;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder(30);
        if (firstname != null) {
            builder.append(firstname);
        }
        if (lastname != null) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(lastname);
        }

        return builder.toString();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public int getNoOfChildren() {
        return noOfChildren;
    }

    public void setNoOfChildren(int noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    public String getNotesOnChildren() {
        return notesOnChildren;
    }

    public void setNotesOnChildren(String notesOnChildren) {
        this.notesOnChildren = notesOnChildren;
    }

    public enum Gender {
        MALE,
        FEMALE,
        NON_BINARY
    }
}

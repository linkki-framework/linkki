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
package org.linkki.samples.playground.treetable.fixed;

public class Person {

    private String firstName;
    private String lastName;
    private String company;
    private String address;
    private String city;
    private String province;
    private String zipCode;
    private String phone1;
    private String phone2;
    private String email;
    private String web;

    private Person(final PersonBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.company = builder.company;
        this.address = builder.address;
        this.city = builder.city;
        this.province = builder.province;
        this.zipCode = builder.zipCode;
        this.phone1 = builder.phone1;
        this.phone2 = builder.phone2;
        this.email = builder.email;
        this.web = builder.web;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @SuppressWarnings("hiding")
    public static class PersonBuilder {
        private String firstName;
        private String lastName;
        private String company;
        private String address;
        private String city;
        private String province;
        private String zipCode;
        private String phone1;
        private String phone2;
        private String email;
        private String web;

        public Person build() {
            return new Person(this);
        }

        public PersonBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public PersonBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public PersonBuilder company(final String company) {
            this.company = company;
            return this;
        }

        public PersonBuilder address(final String address) {
            this.address = address;
            return this;
        }

        public PersonBuilder city(final String city) {
            this.city = city;
            return this;
        }

        public PersonBuilder province(final String province) {
            this.province = province;
            return this;
        }

        public PersonBuilder zipCode(final String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public PersonBuilder phone1(final String phone1) {
            this.phone1 = phone1;
            return this;
        }

        public PersonBuilder phone2(final String phone2) {
            this.phone2 = phone2;
            return this;
        }

        public PersonBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public PersonBuilder web(final String web) {
            this.web = web;
            return this;
        }
    }

}

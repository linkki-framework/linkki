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

package org.linkki.samples.treetable.fixed.pmo;

import org.linkki.samples.treetable.fixed.model.Person;

// tag::hierarchical-row-pmo[]
public class PersonRowPmo extends AbstractPersonRowPmo {

    private final Person person;

    public PersonRowPmo(Person person) {
        this.person = person;
    }

    // end::hierarchical-row-pmo[]

    @Override
    public String getAddress() {
        return person.getAddress() + "<br/>" + person.getCity() + " " + person.getZipCode();
    }

    @Override
    public String getLastName() {
        return person.getLastName();
    }

    @Override
    public String getFirstName() {
        return person.getFirstName();
    }

    @Override
    public String getCompany() {
        return person.getCompany();
    }

    @Override
    public String getPhone1() {
        return person.getPhone1();
    }

    @Override
    public String getPhone2() {
        return person.getPhone2();
    }

    @Override
    public String getEmail() {
        return person.getEmail();
    }

    @Override
    public String getWeb() {
        return person.getWeb();
    }

}

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
package org.linkki.doc;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

// tag::class[]
@UISection
public class ContractSectionPmo {

    private final Contract contract;


    public ContractSectionPmo(Contract contract) {
        this.contract = contract;
    }


    @ModelObject
    public Person getPolicyHolder() {
        return contract.getPolicyHolder();
    }

    @ModelObject(name = "IP")
    public Person getInsuredPerson() {
        return contract.getInsuredPerson();
    }


    @UITextField(position = 10, label = "First Name PH", modelAttribute = "firstname")
    public void firstNamePolicyHolder() {
    }

    @UITextField(position = 20, label = "First Name IP", modelAttribute = "firstname", modelObject = "IP")
    public void firstNameInsuredPerson() {
    }

}
// end::class[]

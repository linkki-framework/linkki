package org.linkki.doc;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

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

package org.linkki.doc;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

@UISection
public class PersonSectionPmo_fieldModelObject {

    // tag::modelobject[]
    @ModelObject
    private final Person person;
    // end::modelobject[]

    public PersonSectionPmo_fieldModelObject(Person person) {
        this.person = person;
    }

    // tag::pojo-binding[]
    @UITextField(position = 10, label = "First Name")
    public String getFirstName() {
        return person.getFirstname();
    }
    // end::pojo-binding[]

    // tag::model-binding[]
    @UITextField(position = 10, label = "First Name", modelAttribute = "firstname")
    public void firstName() {
    }
    // end::model-binding[]
}

package org.linkki.doc;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

@UISection
public class PersonPmo {

    private final Person person;

    public PersonPmo(Person person) {
        this.person = person;
    }

    // tag::modelobject[]
    @ModelObject
    public Person getPerson() {
        return person;
    }
    // end::modelobject[]

    // tag::pojo-binding[]
    @UITextField(position = 10, label = "Vorname")
    public String getVorname() {
        return getPerson().getFirstname();
    }
    // end::pojo-binding[]

    // tag::model-binding[]
    @UITextField(position = 10, label = "Vorname", modelAttribute = "firstname")
    public void vorname() {

    }
    // end::model-binding[]
}

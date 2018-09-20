package org.linkki.doc;

import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@UISection
public class PersonSectionPmo {

    private final Person person;

    public PersonSectionPmo(Person person) {
        this.person = person;
    }

    // tag::modelobject[]
    @ModelObject
    public Person getPerson() {
        return person;
    }
    // end::modelobject[]

    @SuppressFBWarnings("NM_CONFUSING")
    // tag::pojo-binding[]
    @UITextField(position = 10, label = "First Name")
    public String getFirstName() {
        return getPerson().getFirstname();
    }
    // end::pojo-binding[]

    // tag::model-binding[]
    @UITextField(position = 10, label = "First Name", modelAttribute = "firstname")
    public void firstName() {

    }
    // end::model-binding[]
}

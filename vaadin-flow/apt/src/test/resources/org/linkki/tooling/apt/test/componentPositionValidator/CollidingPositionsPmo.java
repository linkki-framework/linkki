package org.linkki.tooling.apt.test.componentPositionValidator;


import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.tooling.apt.test.Person;

@UISection
public class CollidingPositionsPmo {

    @ModelObject
    public Person getPerson() {
        return null;
    }

    @UILabel(position = 10, modelAttribute = Person.PROPERTY_FIRSTNAME)
    public void label1() {
        // model binding
    }

    @UILabel(position = 10, modelAttribute = Person.PROPERTY_LASTNAME)
    public void label2() {
        // model binding
    }
}

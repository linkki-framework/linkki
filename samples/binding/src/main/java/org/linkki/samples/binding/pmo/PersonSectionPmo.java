package org.linkki.samples.binding.pmo;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.components.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.samples.binding.model.Person;

// tag::personPmo-class[]
@UISection
public class PersonSectionPmo implements PresentationModelObject {

    // end::personPmo-class[]
    private Person person;

    public PersonSectionPmo(Person person) {
        reset(person);
    }

    // tag::personPmo-class[]
    @ModelObject
    public Person getPerson() {
        return person;
    }

    @UITextField(position = 10, label = "Firstname", required = RequiredType.REQUIRED, modelAttribute = "firstname")
    public void firstname() {
        /* model binding only */ }

    @UITextField(position = 20, label = "Lastname", required = RequiredType.REQUIRED, modelAttribute = "lastname")
    public void lastname() {
        /* model binding only */ }

    @UIComboBox(position = 30, label = "Gender", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, itemCaptionProvider = ToStringCaptionProvider.class)
    public void gender() {
        /* model binding only */ }

    // end::personPmo-class[]
    public void reset(Person newPerson) {
        this.person = newPerson;
    }

    public boolean isInputValid() {
        return !StringUtils.isEmpty(person.getFirstname()) && !StringUtils.isEmpty(person.getLastname());
    }
    // tag::personPmo-class[]
}
// end::personPmo-class[]

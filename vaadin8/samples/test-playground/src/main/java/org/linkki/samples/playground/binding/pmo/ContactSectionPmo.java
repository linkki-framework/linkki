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
package org.linkki.samples.playground.binding.pmo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.samples.playground.binding.annotation.AlignmentType;
import org.linkki.samples.playground.binding.annotation.UIRadioButtonGroup;
import org.linkki.samples.playground.binding.model.Contact;

// tag::contactPmo-class[]
@UISection
public class ContactSectionPmo {

    private Contact contact;
    // end::contactPmo-class[]

    public ContactSectionPmo(Contact contact) {
        this.contact = contact;
    }

    // tag::contactPmo-class[]
    @ModelObject
    public Contact getContact() {
        return contact;
    }

    @BindTooltip
    @UITextField(position = 10, label = "Firstname", required = RequiredType.REQUIRED, modelAttribute = Contact.PROPERTY_FIRSTNAME)
    public void firstname() {
        /* model binding only */
    }

    public String getFirstnameTooltip() {
        return "First name";
    }

    @BindTooltip
    @UITextField(position = 20, label = "Lastname", required = RequiredType.REQUIRED, modelAttribute = Contact.PROPERTY_LASTNAME)
    public void lastname() {
        /* model binding only */
    }

    public String getLastnameTooltip() {
        return "Last name";
    }

    // tag::radiobutton[]
    @UIRadioButtonGroup(position = 30, label = "Gender", buttonAlignment = AlignmentType.HORIZONTAL, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, //
            itemCaptionProvider = GenderCaptionProvider.class, modelAttribute = Contact.PROPERTY_GENDER)
    public void gender() {
        /* model binding only */
    }
    // end::radiobutton[]

    @UIComboBox(position = 40, label = "Country of Birth", //
            content = AvailableValuesType.DYNAMIC, itemCaptionProvider = ToStringCaptionProvider.class, //
            modelAttribute = Contact.PROPERTY_COUNTRY_OF_BIRTH)
    public void countryOfBirth() {
        /* model binding only */
    }

    public List<String> getCountryOfBirthAvailableValues() {
        return Arrays.asList(Locale.getAvailableLocales()).stream()
                .map(l -> l.getDisplayCountry(UiFramework.getLocale()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @UICheckBox(position = 50, caption = "Add to favorites", modelAttribute = Contact.PROPERTY_FAVORITE)
    public void favorite() {
        /* model binding only */
    }

    // end::contactPmo-class[]
    public void reset(Contact newContact) {
        this.contact = newContact;
    }

    public boolean isInputValid() {
        return !StringUtils.isEmpty(contact.getFirstname()) && !StringUtils.isEmpty(contact.getLastname());
    }
    // tag::contactPmo-class[]
}
// end::contactPmo-class[]
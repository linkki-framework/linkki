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
package org.linkki.samples.playground.binding.components;

import org.linkki.core.vaadin.component.section.FormLayoutSection;
import org.linkki.samples.playground.binding.model.Country;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;


// tag::declaration[]
public class AddressComponent extends FormLayoutSection {

    private static final long serialVersionUID = 1L;

    public AddressComponent(AddressFields fields) {
        super("", 1, false);
        setSizeFull();

        TextField street = fields.getStreetTxt();
        TextField zip = fields.getZipTxt();
        TextField city = fields.getCity();
        // end::declaration[]
        ComboBox<Country> country = fields.getCountryCb();

        street.setSizeFull();
        city.setSizeFull();
        country.setSizeFull();

        // tag::methods[]
        HorizontalLayout zipCity = new HorizontalLayout(zip, city);
        zipCity.setSpacing(false);

        getSectionContent().addFormItem(street, street.getLabel());
        getSectionContent().addFormItem(zipCity, "Zip / City");
        getSectionContent().addFormItem(country, country.getLabel());
        // end::methods[]

        zip.getStyle().set("padding-right", "10px");
        street.setLabel(null);
        country.setLabel(null);
    }

}

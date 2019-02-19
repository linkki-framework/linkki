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
package org.linkki.samples.binding.components;

import org.linkki.samples.binding.model.Country;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class AddressComponent extends FormLayout {

    private static final long serialVersionUID = 1L;

    public AddressComponent(AddressFields fields) {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, true, true));

        TextField street = fields.getStreetTxt();
        TextField zip = fields.getZipTxt();
        TextField city = fields.getCity();
        ComboBox<Country> country = fields.getCountryCb();

        street.setSizeFull();
        city.setSizeFull();
        country.setSizeFull();

        HorizontalLayout zipCity = new HorizontalLayout(zip, city);
        zipCity.setSizeFull();
        zipCity.setSpacing(true);
        zipCity.setCaption("Zip / City");
        zipCity.setExpandRatio(city, 1F);

        addComponents(street, zipCity, country);
    }

}

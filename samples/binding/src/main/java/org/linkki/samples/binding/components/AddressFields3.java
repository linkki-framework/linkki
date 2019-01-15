/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.samples.binding.components;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.samples.binding.annotation.BindValue;
import org.linkki.samples.binding.model.Address;
import org.linkki.samples.binding.model.Country;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public class AddressFields3 {

    @Bind(pmoProperty = "street", modelAttribute = Address.PROPERTY_STREET)
    private final TextField streetTxt;

    // tag::addressFields-custom-bind[]
    @BindValue(pmoProperty = "zip", modelAttribute = Address.PROPERTY_ZIP)
    private final TextField zipTxt;
    // end::addressFields-custom-bind[]

    /* uses the field's name as pmoProperty and modelAttribute */
    @Bind
    private final TextField city;

    @Bind(pmoProperty = "country", availableValues = AvailableValuesType.DYNAMIC, modelAttribute = Address.PROPERTY_COUNTRY)
    private final ComboBox countryCb;

    public AddressFields3() {
        streetTxt = createTextField("Street");
        zipTxt = createTextField(null);
        city = createTextField(null);
        countryCb = new ComboBox("Country") {

            private static final long serialVersionUID = 1L;

            @Override
            public String getItemCaption(Object itemId) {
                return ((Country)itemId).getName();
            }
        };
    }

    public TextField getStreetTxt() {
        return streetTxt;
    }

    public TextField getZipTxt() {
        return zipTxt;
    }

    public TextField getCityTxt() {
        return city;
    }

    public ComboBox getCountryCb() {
        return countryCb;
    }

    private static TextField createTextField(String caption) {
        TextField tf = new TextField(caption);
        tf.setNullRepresentation("");

        return tf;
    }
    // tag::addressFields-class[]
}
// end::addressFields-class[]

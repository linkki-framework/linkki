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
package org.linkki.samples.playground.binding.components;

import org.linkki.core.binding.Binder;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.bind.annotation.Bind;
import org.linkki.samples.playground.binding.annotation.BindValue;
import org.linkki.samples.playground.binding.model.Address;
import org.linkki.samples.playground.binding.model.Country;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Input fields for the address using {@link Bind @Bind} annotation on private fields and
 * {@link Binder}.
 */
// tag::addressFields-class[]
public class AddressFields {

    @Bind(pmoProperty = "street", modelAttribute = Address.PROPERTY_STREET)
    private final TextField streetTxt;

    /* uses the field's name as pmoProperty and modelAttribute */
    @Bind
    private final TextField city;

    /* name of pmoProperty is different from the field name */
    @Bind(pmoProperty = "country",
            availableValues = AvailableValuesType.DYNAMIC,
            modelAttribute = Address.PROPERTY_COUNTRY)
    private final ComboBox<Country> countryCb;
    // end::addressFields-class[]

    // tag::addressFields-custom-bind[]
    @BindValue(pmoProperty = "zip", modelAttribute = Address.PROPERTY_ZIP)
    private final TextField zipTxt;
    // end::addressFields-custom-bind[]

    public AddressFields() {
        streetTxt = createTextField("Street");
        zipTxt = createTextField(null);
        city = createTextField(null);
        countryCb = new ComboBox<>("Country");
        countryCb.setItemLabelGenerator(Country::getName);
    }

    public TextField getStreetTxt() {
        return streetTxt;
    }

    public TextField getZipTxt() {
        return zipTxt;
    }

    public TextField getCity() {
        return city;
    }

    public ComboBox<Country> getCountryCb() {
        return countryCb;
    }

    private static TextField createTextField(String caption) {
        TextField tf = new TextField(caption);
        return tf;
    }
    // tag::addressFields-class[]
}
// end::addressFields-class[]

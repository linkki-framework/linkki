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

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.bind.annotation.Bind;
import org.linkki.samples.playground.binding.annotation.BindValue;
import org.linkki.samples.playground.binding.model.Country;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Same as {@link AddressFields} but with {@link Bind} annotation on the getter methods.
 */
public class AddressFieldsWithBindOnGetters {

    private final TextField streetTxt;

    private final TextField zipTxt;

    private final TextField cityTxt;

    private final ComboBox<Country> countryCb;

    public AddressFieldsWithBindOnGetters() {
        streetTxt = createTextField("Street");
        zipTxt = createTextField(null);
        cityTxt = createTextField(null);
        countryCb = new ComboBox<>("Country");
        countryCb.setItemLabelGenerator(Country::getName);
    }

    @Bind(pmoProperty = "street")
    public TextField getStreetTxt() {
        return streetTxt;
    }

    /* uses the pmoProperty and modelAttribute "city" derived from the method name */
    @Bind
    public TextField getCity() {
        return cityTxt;
    }

    @Bind(pmoProperty = "country", availableValues = AvailableValuesType.DYNAMIC)
    public ComboBox<Country> getCountryCb() {
        return countryCb;
    }

    @BindValue(pmoProperty = "zip")
    public TextField getZipTxt() {
        return zipTxt;
    }

    private static TextField createTextField(String caption) {
        TextField tf = new TextField(caption);
        return tf;
    }
}

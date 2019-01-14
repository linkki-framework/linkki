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
import org.linkki.samples.binding.model.Country;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

public class AddressFields2 {

    private final TextField streetTxt;

    private final TextField zipTxt;

    private final TextField cityTxt;

    private final ComboBox countryCb;

    public AddressFields2() {
        streetTxt = createTextField("Street");
        zipTxt = createTextField(null);
        cityTxt = createTextField(null);
        countryCb = new ComboBox("Country") {
            private static final long serialVersionUID = 1L;

            @Override
            public String getItemCaption(Object itemId) {
                return ((Country)itemId).getName();
            }
        };
    }

    // tag::addressFields-methodBinding[]
    @Bind(pmoProperty = "street")
    public TextField getStreetTxt() {
        return streetTxt;
    }

    @Bind(pmoProperty = "zip")
    public TextField getZipTxt() {
        return zipTxt;
    }

    /* uses the pmoProperty and modelAttribute "city" derived from the method name */
    @Bind
    public TextField getCity() {
        return cityTxt;
    }

    @Bind(pmoProperty = "country", availableValues = AvailableValuesType.DYNAMIC)
    public ComboBox getCountryCb() {
        return countryCb;
    }
    // end::addressFields-methodBinding[]

    private static TextField createTextField(String caption) {
        TextField tf = new TextField(caption);
        tf.setNullRepresentation("");

        return tf;
    }
}

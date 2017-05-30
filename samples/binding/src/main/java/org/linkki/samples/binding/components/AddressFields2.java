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

    @Bind(pmoProperty = "city")
    public TextField getCityTxt() {
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

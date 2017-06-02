package org.linkki.samples.binding.components;

import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.samples.binding.model.Country;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

// tag::addressFields-class[]
public class AddressFields {

    @Bind(pmoProperty = "street")
    private final TextField streetTxt;

    @Bind(pmoProperty = "zip")
    private final TextField zipTxt;

    @Bind(pmoProperty = "city")
    private final TextField cityTxt;

    @Bind(pmoProperty = "country", availableValues = AvailableValuesType.DYNAMIC)
    private final ComboBox countryCb;

    // end::addressFields-class[]

    public AddressFields() {
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

    public TextField getStreetTxt() {
        return streetTxt;
    }

    public TextField getZipTxt() {
        return zipTxt;
    }

    public TextField getCityTxt() {
        return cityTxt;
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

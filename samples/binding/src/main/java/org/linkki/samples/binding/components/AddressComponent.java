package org.linkki.samples.binding.components;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.samples.binding.model.Country;
import org.linkki.samples.binding.pmo.AddressPmo;

public class AddressComponent extends FormLayout {

    public AddressComponent(AddressFields fields) {
        setSizeFull();
        setSpacing(true);
        setMargin(new MarginInfo(false, true, true, true));

        TextField street = fields.getStreetTxt();
        TextField zip = fields.getZipTxt();
        TextField city = fields.getCityTxt();
        ComboBox country = fields.getCountryCb();

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

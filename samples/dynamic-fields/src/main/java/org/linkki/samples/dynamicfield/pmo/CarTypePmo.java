package org.linkki.samples.dynamicfield.pmo;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.samples.dynamicfield.model.NewCar;

import java.io.Serializable;

@UISection
public class CarTypePmo implements PresentationModelObject, Serializable {

    private static final long serialVersionUID = 2629334795264678052L;


    private final NewCar car;


    public CarTypePmo(NewCar car) {
        this.car = car;
    }


    @ModelObject
    public NewCar getCar() {
        return car;
    }


    @UIComboBox(position = 10, label = "Car Type", modelAttribute = NewCar.PROPERTY_CAR_TYPE, content = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
    public void carType() {
        /* model binding */
    }
}

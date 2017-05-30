package org.linkki.samples.dynamicfield.pmo;

import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITableColumn;
import org.linkki.samples.dynamicfield.model.Car;

public class CarRowPmo extends CarPmo {

    private static final long serialVersionUID = 3750767083500409415L;


    public CarRowPmo(Car car) {
        super(car);
    }


    @UITableColumn
    @UIComboBox(position = 25, label = "Car Type", modelAttribute = Car.PROPERTY_CAR_TYPE, enabled = EnabledType.DISABLED)
    public void carType() {
        /* model binding */
    }
}

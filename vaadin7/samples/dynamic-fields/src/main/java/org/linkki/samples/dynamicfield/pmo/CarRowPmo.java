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
package org.linkki.samples.dynamicfield.pmo;

import org.linkki.core.defaults.uielement.aspects.types.EnabledType;
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

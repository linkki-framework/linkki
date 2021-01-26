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
package org.linkki.samples.playground.table.dynamicfields;

import java.io.Serializable;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class CarTypeSectionPmo implements PresentationModelObject, Serializable {

    private static final long serialVersionUID = 2629334795264678052L;


    private final NewCar car;


    public CarTypeSectionPmo(NewCar car) {
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

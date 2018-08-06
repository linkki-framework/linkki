/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.samples.dynamicfield.pmo;

import java.util.List;

import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.samples.dynamicfield.model.Car;
import org.linkki.util.handler.Handler;

@UISection
public class NewCarSectionPmo extends CarPmo {

    private static final long serialVersionUID = 6118622651214201674L;


    private final List<Car> carStorage;
    private final Handler afterOkAction;


    public NewCarSectionPmo(Car car, List<Car> carStorage, Handler afterOkAction) {
        super(car);
        this.carStorage = carStorage;
        this.afterOkAction = afterOkAction;
    }


    @UIButton(position = 40, caption = "OK", enabled = EnabledType.DYNAMIC)
    public void ok() {
        carStorage.add(getCar());
        afterOkAction.apply();
    }


    public boolean isOkEnabled() {
        Car model = getCar();
        return model.getMake() != null && model.getModel() != null && model.getRetention() != null;
    }

}

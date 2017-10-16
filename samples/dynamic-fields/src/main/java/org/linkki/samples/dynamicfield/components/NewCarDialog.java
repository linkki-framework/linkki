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
package org.linkki.samples.dynamicfield.components;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.samples.dynamicfield.model.Car;
import org.linkki.samples.dynamicfield.model.CarType;
import org.linkki.samples.dynamicfield.model.NewCar;
import org.linkki.samples.dynamicfield.pmo.CarTypeSectionPmo;
import org.linkki.samples.dynamicfield.pmo.NewCarSectionPmo;
import org.linkki.util.handler.Handler;

import java.util.List;

public class NewCarDialog extends Window {

    private static final long serialVersionUID = -6187152700037744469L;


    private final List<Car> carStorage;

    private final PmoBasedSectionFactory sectionFactory;
    private final Handler afterSaveAction;


    public NewCarDialog(List<Car> carStorage, Handler afterSaveAction) {
        this.carStorage = carStorage;
        this.afterSaveAction = afterSaveAction;

        setWidth(600, Unit.PIXELS);
        setHeightUndefined();
        setResizable(false);

        VerticalLayout layout = new VerticalLayout();


        sectionFactory = new DefaultPmoBasedSectionFactory();

        NewCar car = new NewCar();

        layout.addComponent(sectionFactory.createSection(new CarTypeSectionPmo(car),
                                                         new BindingContext("car-type",
                                                                 PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER,
                                                                 () -> addNewCarSection(layout, car))));
        setContent(layout);

        center();
        UI.getCurrent().addWindow(this);
    }

    private void addNewCarSection(VerticalLayout layout, NewCar car) {

        if (layout.getComponentCount() > 1) {
            layout.removeComponent(layout.getComponent(1));
        }

        // reset retention if the carType == premium
        if (CarType.PREMIUM == car.getCarType()) {
            car.setRetention(null);
        }

        layout.addComponent(sectionFactory.createSection(new NewCarSectionPmo(car, this.carStorage, this::close),
                                                         new BindingContext("new-car",
                                                                 PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER,
                                                                 afterSaveAction)));
        center();
    }


}

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
package org.linkki.samples.playground.table.dynamicfields;

import java.util.List;

import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CarDialog extends Dialog {

    private static final long serialVersionUID = -6187152700037744469L;

    private final List<Car> carStorage;
    private final Handler afterSaveAction;

    public CarDialog(List<Car> carStorage, Handler afterSaveAction) {
        this.carStorage = carStorage;
        this.afterSaveAction = afterSaveAction;

        setWidth("600px");
        // setHeightUndefined();
        setResizable(false);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setSpacing(false);

        var car = new Car();

        layout.add(VaadinUiCreator.createComponent(new CarTypeSectionPmo(car),
                                                   new BindingContextBuilder().name("car-type")
                                                           .afterUpdateHandler(() -> addCarSection(layout, car))
                                                           .build()));
        add(layout);

        open();
    }

    private void addCarSection(VerticalLayout layout, Car car) {

        if (layout.getComponentCount() > 1) {
            layout.remove(layout.getComponentAt(1));
        }

        // reset retention if the carType == premium
        if (CarType.PREMIUM == car.getCarType()) {
            car.setRetention(null);
        }

        layout.add(VaadinUiCreator.createComponent(new CarSectionPmo(car, this.carStorage, this::close),
                                                   new BindingContextBuilder().name("new-car")
                                                           .afterUpdateHandler(afterSaveAction)
                                                           .build()));
    }

}

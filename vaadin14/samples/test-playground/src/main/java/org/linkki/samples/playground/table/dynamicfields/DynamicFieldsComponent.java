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

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.AbstractSection;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.internal.CurrentInstance;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WrappedSession;

public class DynamicFieldsComponent extends Div {

    private static final long serialVersionUID = 7747620148317506269L;

    private static final String CAR_STORAGE_ATTRIBUTE = "linkki-sample::car-storage";

    public DynamicFieldsComponent() {

        List<Car> carStorage = getCarStorage();

        BindingContext bindingContext = new BindingContext();

        AbstractSection table = new PmoBasedSectionFactory()
                .createSection(new CarTablePmo(carStorage,
                        () -> new NewCarDialog(carStorage, bindingContext::modelChanged)),
                               bindingContext);
        add(table);

        setWidthFull();
    }

    // some fake persistent storage
    // store the cars in the session so it is available after a browser
    // refresh as long as we are in the same session
    private List<Car> getCarStorage() {
        List<Car> carStorage;

        WrappedSession session = CurrentInstance.get(VaadinSession.class).getSession();
        @SuppressWarnings("unchecked")
        List<Car> storage = (List<Car>)session.getAttribute(CAR_STORAGE_ATTRIBUTE);
        if (storage != null) {
            carStorage = storage;
        } else {
            carStorage = new ArrayList<>();
            addCars(carStorage);
            session.setAttribute(CAR_STORAGE_ATTRIBUTE, carStorage);
        }

        return carStorage;
    }


    private void addCars(List<Car> carStorage) {
        carStorage.add(createCar(CarType.STANDARD, "Audi", "A4", 200.0));
        carStorage.add(createCar(CarType.PREMIUM, "Porsche", "911", 5000.0));
        carStorage.add(createCar(CarType.STANDARD, "Mercedes-Benz", "GLA", 300.0));
        carStorage.add(createCar(CarType.PREMIUM, "Ferrari", "812 superfast", 10000.0));

    }

    private Car createCar(CarType carType, String make, String model, double retention) {
        Car car = new Car(carType);
        car.setMake(make);
        car.setModel(model);
        car.setRetention(retention);

        return car;
    }
}

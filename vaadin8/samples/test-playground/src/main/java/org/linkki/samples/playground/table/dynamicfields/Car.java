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

public class Car implements Serializable {

    public static final String PROPERTY_CAR_TYPE = "carType";
    public static final String PROPERTY_MAKE = "make";
    public static final String PROPERTY_MODEL = "model";
    public static final String PROPERTY_RETENTION = "retention";

    private static final long serialVersionUID = -4013282509907331553L;


    CarType carType;

    private String make;
    private String model;

    private Double retention;


    Car() {
        // default constructor for inheritance
    }

    public Car(CarType carType) {
        this.carType = carType;
    }


    public CarType getCarType() {
        return carType;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getRetention() {
        return retention;
    }

    public void setRetention(Double retention) {
        this.retention = retention;
    }
}

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

package org.linkki.samples.playground.ts.formelements;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.element.annotation.UITimeField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class TimeFieldPmo {

    private String textFieldOne;
    private LocalTime time = LocalTime.now();
    private String textFieldTwo;
    private LocalTime timeWithSecondPrecision = LocalTime.now();

    @UITextField(position = 10, label = "Field to test tabbing")
    public String getTextFieldOne() {
        return textFieldOne;
    }

    public void setTextFieldOne(String textFieldOne) {
        this.textFieldOne = textFieldOne;
    }

    @UITimeField(position = 20, label = "@UITimeField with 30 minute steps", step = 30L)
    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @UITextField(position = 30, label = "Field to test tabbing")
    public String getTextFieldTwo() {
        return textFieldTwo;
    }

    public void setTextFieldTwo(String textFieldTwo) {
        this.textFieldTwo = textFieldTwo;
    }

    @UITimeField(position = 40, label = "@UITimeField with 15 second steps", step = 15L, precision = ChronoUnit.SECONDS)
    public LocalTime getTimeWithSecondPrecision() {
        return timeWithSecondPrecision;
    }

    public void setTimeWithSecondPrecision(LocalTime timeWithSecondPrecision) {
        this.timeWithSecondPrecision = timeWithSecondPrecision;
    }

}

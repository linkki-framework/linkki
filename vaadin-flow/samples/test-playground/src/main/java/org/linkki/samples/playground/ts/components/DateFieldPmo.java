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

package org.linkki.samples.playground.ts.components;

import java.time.LocalDate;

import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class DateFieldPmo {

    private String textFieldOne;
    private LocalDate date = LocalDate.now();
    private String textFieldTwo;

    @UITextField(position = 10, label = "@UITextField")
    public String getTextFieldOne() {
        return textFieldOne;
    }

    public void setTextFieldOne(String textFieldOne) {
        this.textFieldOne = textFieldOne;
    }

    @UIDateField(position = 20, label = "@UIDateField")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @UITextField(position = 30, label = "@UITextField")
    public String getTextFieldTwo() {
        return textFieldTwo;
    }

    public void setTextFieldTwo(String textFieldTwo) {
        this.textFieldTwo = textFieldTwo;
    }
}

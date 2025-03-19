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

import java.time.LocalDateTime;

import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class DateTimeFieldPmo {

    private String textFieldOne;
    private LocalDateTime dateTime = LocalDateTime.now();
    private String textFieldTwo;

    @UITextField(position = 10, label = "Text field to test tabbing")
    public String getTextFieldOne() {
        return textFieldOne;
    }

    public void setTextFieldOne(String textFieldOne) {
        this.textFieldOne = textFieldOne;
    }

    @UIDateTimeField(position = 20, label = "@UIDateTimeField")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @UITextField(position = 30, label = "Text field to test tabbing")
    public String getTextFieldTwo() {
        return textFieldTwo;
    }

    public void setTextFieldTwo(String textFieldTwo) {
        this.textFieldTwo = textFieldTwo;
    }

}

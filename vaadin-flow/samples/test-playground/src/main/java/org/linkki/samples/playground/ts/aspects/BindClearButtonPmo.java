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
package org.linkki.samples.playground.ts.aspects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.linkki.core.ui.aspects.annotation.BindClearButton;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILongField;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.element.annotation.UITimeField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindClearButtonPmo {
    private String text = "This is a text.";
    private LocalDate dateField = LocalDate.of(2026, 1, 1);
    private LocalDateTime dateTimeField = LocalDateTime.of(2026, 1, 1, 12, 0);
    private LocalTime timeField = LocalTime.of(12, 0);
    private Integer integerField = 42;
    private Double doubleField = 3.14;
    private Long longField = 123456789L;

    @UITextField(position = 10, label = "TextField without @BindClearButton")
    public String getTextFieldWithout() {
        return text;
    }

    public void setTextFieldWithout(String text) {
        this.text = text;
    }

    @UITextField(position = 20, label = "TextField with @BindClearButton")
    @BindClearButton
    public String getTextField() {
        return text;
    }

    public void setTextField(String text) {
        this.text = text;
    }

    @UITextArea(position = 30, label = "TextArea with @BindClearButton")
    @BindClearButton
    public String getTextArea() {
        return text;
    }

    public void setTextArea(String text) {
        this.text = text;
    }

    @UIDateField(position = 50, label = "DateField with @BindClearButton")
    @BindClearButton
    public LocalDate getDateField() {
        return dateField;
    }

    public void setDateField(LocalDate dateField) {
        this.dateField = dateField;
    }

    @UIDateTimeField(position = 60, label = "DateTimeField with @BindClearButton")
    @BindClearButton
    public LocalDateTime getDateTimeField() {
        return dateTimeField;
    }

    public void setDateTimeField(LocalDateTime dateTimeField) {
        this.dateTimeField = dateTimeField;
    }

    @UITimeField(position = 70, label = "TimeField with @BindClearButton")
    @BindClearButton
    public LocalTime getTimeField() {
        return timeField;
    }

    public void setTimeField(LocalTime timeField) {
        this.timeField = timeField;
    }

    @UIIntegerField(position = 80, label = "IntegerField with @BindClearButton")
    @BindClearButton
    public Integer getIntegerField() {
        return integerField;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    @UIDoubleField(position = 90, label = "DoubleField with @BindClearButton")
    @BindClearButton
    public Double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }

    @UILongField(position = 100, label = "LongField with @BindClearButton")
    @BindClearButton
    public Long getLongField() {
        return longField;
    }

    public void setLongField(Long longField) {
        this.longField = longField;
    }

}

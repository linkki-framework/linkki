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
import java.util.List;
import java.util.Set;

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindClearButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILongField;
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.element.annotation.UITimeField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindClearButtonPmo {

    private String textField = "TextField with clear button";
    private String textFieldWithout = "TextField without clear button";
    private String textArea = "TextArea with clear button";
    private String comboBox = "Option 1";
    private LocalDate dateField = LocalDate.of(2026, 1, 1);
    private LocalDateTime dateTimeField = LocalDateTime.of(2026, 1, 1, 12, 0);
    private LocalTime timeField = LocalTime.of(12, 0);
    private int integerField = 42;
    private double doubleField = 3.14;
    private long longField = 123456789L;
    private Set<SampleEnum> multiSelect = Set.of(SampleEnum.VALUE1);

    @UITextField(position = 10, label = "TextField without @BindClearButton")
    public String getTextFieldWithout() {
        return textFieldWithout;
    }

    public void setTextFieldWithout(String textFieldWithout) {
        this.textFieldWithout = textFieldWithout;
    }

    @UITextField(position = 20, label = "TextField with @BindClearButton")
    @BindClearButton
    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    @UITextArea(position = 30, label = "TextArea with @BindClearButton")
    @BindClearButton
    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    @UIComboBox(position = 40, label = "ComboBox with @BindClearButton", content = AvailableValuesType.DYNAMIC)
    @BindClearButton
    public String getComboBox() {
        return comboBox;
    }

    public void setComboBox(String comboBox) {
        this.comboBox = comboBox;
    }

    public List<String> getComboBoxAvailableValues() {
        return List.of("Option 1", "Option 2", "Option 3");
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
    public int getIntegerField() {
        return integerField;
    }

    public void setIntegerField(int integerField) {
        this.integerField = integerField;
    }

    @UIDoubleField(position = 90, label = "DoubleField with @BindClearButton")
    @BindClearButton
    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }

    @UILongField(position = 100, label = "LongField with @BindClearButton")
    @BindClearButton
    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    @UIMultiSelect(position = 120, label = "MultiSelect with @BindClearButton")
    @BindClearButton
    public Set<SampleEnum> getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(Set<SampleEnum> multiSelect) {
        this.multiSelect = multiSelect;
    }

    public List<SampleEnum> getMultiSelectAvailableValues() {
        return List.of(SampleEnum.values());
    }

    public enum SampleEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
}

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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;

@UISection
public class MultiSelectPmo {

    public enum Day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    private Set<Day> days = new HashSet<>();

    @UIMultiSelect(position = 0, label = "Days")
    public Set<Day> getDays() {
        return days;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    @UIMultiSelect(position = 10, label = "Selected Days")
    public Set<Day> getSelectedDays() {
        return getDays();
    }

    @UIMultiSelect(position = 20, label = "Days (auto expand)",
            autoExpand = MultiSelectComboBox.AutoExpandMode.VERTICAL)
    public Set<Day> getAutoExpandDays() {
        return getDays();
    }

    public void setAutoExpandDays(Set<Day> days) {
        setDays(days);
    }

    @UIMultiSelect(position = 30, label = "Days (selected items on top)", selectedItemsOnTop = true)
    public Set<Day> getDaysOnTop() {
        return getDays();
    }

    public void setDaysOnTop(Set<Day> days) {
        setDays(days);
    }

    public Collection<Day> getDaysAvailableValues() {
        return Arrays.asList(Day.values());
    }

    public Collection<Day> getSelectedDaysAvailableValues() {
        return getDaysAvailableValues();
    }

    public Collection<Day> getAutoExpandDaysAvailableValues() {
        return getDaysAvailableValues();
    }

    public Collection<Day> getDaysOnTopAvailableValues() {
        return getDaysAvailableValues();
    }

}

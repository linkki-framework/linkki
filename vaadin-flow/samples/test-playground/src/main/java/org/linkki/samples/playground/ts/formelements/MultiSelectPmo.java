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
import java.util.Set;

import org.apache.commons.compress.utils.Sets;
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class MultiSelectPmo {

    public enum Day {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY;
    }

    private Set<Day> days = Sets.newHashSet();

    @UIMultiSelect(position = 0, label = "Days")
    public Set<Day> getDays() {
        return days;
    }

    @UIMultiSelect(position = 10, label = "Selected Days")
    public Set<Day> getSelectedDays() {
        return getDays();
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    public Collection<Day> getDaysAvailableValues() {
        return Arrays.asList(Day.values());
    }

    public Collection<Day> getSelectedDaysAvailableValues() {
        return getDaysAvailableValues();
    }

}

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

import static org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.AlignmentType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.element.annotation.UICheckboxes;
import org.linkki.core.ui.layout.annotation.UISection;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@UISection
public class CheckboxesPmo {

    private Set<CheckboxesValues> checkboxesValues;

    @UICheckboxes(position = 0, label = "Enabled Checkboxes",
            itemCaptionProvider = CaptionProvider.class)
    public Set<CheckboxesValues> getCheckboxesValues() {
        return checkboxesValues;
    }

    public void setCheckboxesValues(Set<CheckboxesValues> checkboxesValues) {
        this.checkboxesValues = checkboxesValues;
    }

    public Set<CheckboxesValues> getCheckboxesValuesAvailableValues() {
        return Set.of(CheckboxesValues.values());
    }

    @UICheckboxes(position = 10, label = "Disabled Checkboxes", enabled = EnabledType.DISABLED,
            itemCaptionProvider = CaptionProvider.class)
    public Set<CheckboxesValues> getDisabledCheckboxesValues() {
        return checkboxesValues;
    }

    public void setDisabledCheckboxesValues(Set<CheckboxesValues> checkboxesValues) {
        this.checkboxesValues = checkboxesValues;
    }

    public Set<CheckboxesValues> getDisabledCheckboxesValuesAvailableValues() {
        return Set.of(CheckboxesValues.values());
    }

    @UICheckboxes(position = 20, label = "Required Checkboxes", required = RequiredType.REQUIRED,
            itemCaptionProvider = CaptionProvider.class)
    public Set<CheckboxesValues> getRequiredCheckboxesValues() {
        return checkboxesValues;
    }

    public void setRequiredCheckboxesValues(Set<CheckboxesValues> checkboxesValues) {
        this.checkboxesValues = checkboxesValues;
    }

    public Set<CheckboxesValues> getRequiredCheckboxesValuesAvailableValues() {
        return Set.of(CheckboxesValues.values());
    }

    @BindReadOnly(ReadOnlyType.ALWAYS)
    @UICheckboxes(position = 30, label = "ReadOnly Checkboxes",
            itemCaptionProvider = CaptionProvider.class)
    public Set<CheckboxesValues> getReadOnlyCheckboxesValues() {
        return checkboxesValues;
    }

    public void setReadOnlyCheckboxesValues(Set<CheckboxesValues> checkboxesValues) {
        this.checkboxesValues = checkboxesValues;
    }

    public Set<CheckboxesValues> getReadOnlyCheckboxesValuesAvailableValues() {
        return Set.of(CheckboxesValues.values());
    }

    @UICheckboxes(position = 40, label = "Horizontal Checkboxes", checkboxesAlignment = AlignmentType.HORIZONTAL,
            itemCaptionProvider = CaptionProvider.class)
    public Set<CheckboxesValues> getHorizontalCheckboxesValues() {
        return checkboxesValues;
    }

    public void setHorizontalCheckboxesValues(Set<CheckboxesValues> checkboxesValues) {
        this.checkboxesValues = checkboxesValues;
    }

    public Set<CheckboxesValues> getHorizontalCheckboxesValuesAvailableValues() {
        return Set.of(CheckboxesValues.values());
    }

    public enum CheckboxesValues {
        FIRST_CHECKBOX,
        SECOND_CHECKBOX,
        THIRD_CHECKBOX;
    }

    public static class CaptionProvider implements ItemCaptionProvider<CheckboxesValues> {

        @Override
        public String getCaption(@CheckForNull CheckboxesValues value) {
            assert value != null;
            return StringUtils.capitalize(value.name().toLowerCase());
        }

    }

}

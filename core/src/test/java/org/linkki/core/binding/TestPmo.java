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
package org.linkki.core.binding;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.aspect.definition.TestComponentClickAspectDefinition;
import org.linkki.core.ui.section.annotations.ModelObject;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class TestPmo {

    public static final String PROPERTY_VALUE = "value";
    public static final String PROPERTY_ENUM_VALUE = "enumValue";

    private String value = StringUtils.EMPTY;
    private String valueTooltip = "abc";
    @CheckForNull
    private Object modelObject;

    private boolean enabled = true;
    private boolean visible = true;
    private boolean required = false;

    private List<?> availableValues = new ArrayList<>();

    @CheckForNull
    private TestEnum enumValue;

    private boolean clicked;

    public TestPmo() {
        this(null);
    }

    public TestPmo(@CheckForNull Object modelObject) {
        this.modelObject = modelObject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValueEnabled() {
        return enabled;
    }

    public void setValueEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isValueVisible() {
        return visible;
    }

    public String getValueTooltip() {
        return valueTooltip;
    }

    public void setValueTooltip(String valueTooltip) {
        this.valueTooltip = valueTooltip;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @CheckForNull
    @ModelObject
    public Object getModelObject() {
        return modelObject;
    }

    public void setModelObject(Object modelObject) {
        this.modelObject = modelObject;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isValueRequired() {
        return required;
    }

    public void setEnumValueAvailableValues(List<?> availableValues) {
        this.availableValues = availableValues;
    }

    public List<?> getEnumValueAvailableValues() {
        return availableValues;
    }

    public void setEnumValue(TestEnum enumValue) {
        this.enumValue = enumValue;
    }

    @CheckForNull
    public TestEnum getEnumValue() {
        return enumValue;
    }

    public String getEnumValueTooltip() {
        return "xyz";
    }

    public boolean isEnumValueEnabled() {
        return true;
    }

    public boolean isEnumValueRequired() {
        return false;
    }

    public boolean isEnumValueVisible() {
        return true;
    }

    @CheckForNull
    public TestEnum getReadonlyEnumValue() {
        return enumValue;
    }

    public boolean isReadonlyEnumValueEnabled() {
        return true;
    }

    public boolean isReadonlyEnumValueRequired() {
        return true;
    }

    public boolean isReadonlyEnumValueVisible() {
        return true;
    }

    public List<?> getReadonlyEnumValueAvailableValues() {
        return availableValues;
    }

    public String getValueDisabledInvisibleNotRequired() {
        return value;
    }

    public void setValueDisabledInvisibleNotRequired(String value) {
        this.value = value;
    }

    public boolean isEnabledValueDisabledInvisibleNotRequired() {
        return false;
    }

    public boolean isVisibleValueDisabledInvisibleNotRequired() {
        return true;
    }

    public boolean isMandatoryValueDisabledInvisibleNotRequired() {
        return true;
    }

    /**
     * @see TestComponentClickAspectDefinition
     */
    public void click() {
        this.clicked = true;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void resetClicked() {
        this.clicked = false;
    }

}
package de.faktorzehn.ipm.web.binding;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.faktorzehn.ipm.web.PresentationModelObject;

public class TestPMO implements PresentationModelObject {
    private String value = StringUtils.EMPTY;

    private Object modelObject;

    private boolean enabled = true;
    private boolean visible = true;

    private boolean required = false;

    private List<?> availableValues = new ArrayList<Object>();

    private TestEnum enumValue;

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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
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

    public List<?> getAvailableValuesEnumValue() {
        return availableValues;
    }

    public void setEnumValue(TestEnum enumValue) {
        this.enumValue = enumValue;
    }

    public TestEnum getEnumValue() {
        return enumValue;
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

}
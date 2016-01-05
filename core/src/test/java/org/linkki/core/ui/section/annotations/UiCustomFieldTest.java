/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.TestBindingContext;

import com.vaadin.ui.OptionGroup;

public class UiCustomFieldTest {

    private TestModelObject modelObject = new TestModelObject();

    private TestPmo pmo = new TestPmo(modelObject);

    private BindingContext bindingContext = TestBindingContext.create();

    private OptionGroup createCustomField() {
        return (OptionGroup)TestUi.componentBoundTo(pmo, bindingContext);
    }

    @Test
    public void testAvailableValues() {
        OptionGroup optionGroup = createCustomField();

        assertThat(optionGroup.getItemIds(), contains(pmo.availableValues.toArray()));
    }

    @Test
    public void testGetValue() {
        OptionGroup optionGroup = createCustomField();

        assertThat(optionGroup.getValue(), is(modelObject.getProperty()));

        TestValue newValue = new TestValue("newValue");
        modelObject.property = newValue;
        bindingContext.updateUI();

        assertThat(optionGroup.getValue(), is(newValue));
    }

    @Test
    public void testSetValue() {
        OptionGroup optionGroup = createCustomField();

        TestValue newValue = new TestValue("b");
        optionGroup.setValue(newValue);

        assertThat(modelObject.getProperty(), is(newValue));
    }

    @Test
    public void testEnabled() {
        pmo.enabled = false;
        OptionGroup optionGroup = createCustomField();

        assertThat(optionGroup.isEnabled(), is(false));

        pmo.enabled = true;
        bindingContext.updateUI();

        assertThat(optionGroup.isEnabled(), is(true));
    }

    @Test
    public void testVisible() {
        pmo.visible = false;
        OptionGroup optionGroup = createCustomField();

        assertThat(optionGroup.isVisible(), is(false));

        pmo.visible = true;
        bindingContext.updateUI();

        assertThat(optionGroup.isVisible(), is(true));
    }

    @Test
    public void testRequired() {
        pmo.required = false;
        OptionGroup optionGroup = createCustomField();

        assertThat(optionGroup.isRequired(), is(false));

        pmo.required = true;
        bindingContext.updateUI();

        assertThat(optionGroup.isRequired(), is(true));
    }

    public static class TestModelObject {

        private TestValue property = new TestValue("abc123");

        public TestValue getProperty() {
            return property;
        }

        public void setProperty(TestValue value) {
            this.property = value;
        }

    }

    @UISection
    public static class TestPmo implements PresentationModelObject {

        private final Object modelObject;

        private boolean enabled = false;

        private boolean visible = false;

        private boolean required = false;

        private List<TestValue> availableValues = Arrays.asList(new TestValue("a"), new TestValue("b"),
                                                                new TestValue("c"));

        public TestPmo(Object modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UICustomField(position = 7, label = "TheLabel", enabled = EnabledType.DYNAMIC, modelAttribute = "property", visible = VisibleType.DYNAMIC, required = RequiredType.DYNAMIC, content = AvailableValuesType.DYNAMIC, uiControl = OptionGroup.class)
        public void property() {
            // data binding
        }

        @Override
        public Object getModelObject() {
            return modelObject;
        }

        public boolean isPropertyEnabled() {
            return enabled;
        }

        public boolean isPropertyVisible() {
            return visible;
        }

        public boolean isPropertyRequired() {
            return required;
        }

        public List<TestValue> getPropertyAvailableValues() {
            return availableValues;
        }

    }

    public static class TestValue {

        private String value;

        public TestValue(String value) {
            this.value = value;
        }

        public String getId() {
            return value;
        }

        public String getName() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TestValue other = (TestValue)obj;
            if (value == null) {
                if (other.value != null) {
                    return false;
                }
            } else if (!value.equals(other.value)) {
                return false;
            }
            return true;
        }

    }
}

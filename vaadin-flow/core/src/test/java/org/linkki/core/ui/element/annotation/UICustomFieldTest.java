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
package org.linkki.core.ui.element.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UICustomFieldTest {

    private final TestModelObject modelObject = new TestModelObject();

    private final TestPmo pmo = new TestPmo(modelObject);

    private final BindingContext bindingContext = new BindingContext();

    @SuppressWarnings("unchecked")
    private RadioButtonGroup<TestValue> createCustomField() {
        return (RadioButtonGroup<TestValue>)TestUiUtil.createFirstComponentOf(pmo, bindingContext);
    }

    @Test
    void testAvailableValues() {
        RadioButtonGroup<TestValue> optionGroup = createCustomField();
        assertThat(getAllowedValues(optionGroup), contains(pmo.availableValues.toArray()));
    }

    @Test
    void testAvailableValues_NotApplicable() {
        Div section = TestUiUtil.createSectionWith(pmo);
        TextField textField = (TextField)TestUiUtil.getComponentAtIndex(1, section);

        // the real test is that this text field could be created, just check the value to check
        // anything
        assertThat(textField.getValue(), is("test"));
    }

    @Test
    void testGetValue() {
        RadioButtonGroup<TestValue> optionGroup = createCustomField();

        assertThat(optionGroup.getValue(), is(modelObject.getProperty()));

        TestValue newValue = new TestValue("newValue");
        modelObject.property = newValue;
        bindingContext.modelChanged();

        assertThat(optionGroup.getValue(), is(newValue));
    }

    @Test
    void testSetValue() {
        RadioButtonGroup<TestValue> optionGroup = createCustomField();

        TestValue newValue = new TestValue("b");
        TestUiUtil.setUserOriginatedValue(optionGroup, newValue);

        assertThat(modelObject.getProperty(), is(newValue));
    }

    @Test
    void testEnabled() {
        pmo.enabled = false;
        RadioButtonGroup<TestValue> optionGroup = createCustomField();

        assertThat(optionGroup.isEnabled(), is(false));

        pmo.enabled = true;
        bindingContext.modelChanged();

        assertThat(optionGroup.isEnabled(), is(true));
    }

    @Test
    void testVisible() {
        pmo.visible = false;
        RadioButtonGroup<TestValue> optionGroup = createCustomField();

        assertThat(optionGroup.isVisible(), is(false));

        pmo.visible = true;
        bindingContext.modelChanged();

        assertThat(optionGroup.isVisible(), is(true));
    }

    @Test
    void testRequired() {
        pmo.required = false;
        RadioButtonGroup<TestValue> optionGroup = createCustomField();

        assertThat(optionGroup.isRequiredIndicatorVisible(), is(false));

        pmo.required = true;
        bindingContext.modelChanged();

        assertThat(optionGroup.isRequiredIndicatorVisible(), is(true));
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

    private static List<TestValue> getAllowedValues(RadioButtonGroup<TestValue> comboBox) {
        return comboBox.getListDataView().getItems()
                .collect(Collectors.toList());
    }

    @UISection
    public static class TestPmo {

        private final TestModelObject modelObject;

        private boolean enabled = false;

        private boolean visible = false;

        private boolean required = false;

        private final List<TestValue> availableValues = Arrays.asList(new TestValue("a"), new TestValue("b"),
                                                                      new TestValue("c"));

        public TestPmo(TestModelObject modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UICustomField(position = 7,
                label = "TheLabel",
                enabled = EnabledType.DYNAMIC,
                modelAttribute = "property",
                visible = VisibleType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                content = AvailableValuesType.DYNAMIC,
                uiControl = RadioButtonGroup.class)
        public void property() {
            // data binding
        }

        @UICustomField(position = 10, label = "", uiControl = TextField.class)
        public String getNoSelectField() {
            return "test";
        }

        @ModelObject
        public TestModelObject getModelObject() {
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

        @CheckForNull
        private final String value;

        public TestValue(String value) {
            this.value = value;
        }

        @CheckForNull
        public String getId() {
            return value;
        }

        @CheckForNull
        public String getName() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value != null) ? value.hashCode() : 0);
            return result;
        }

        @Override
        public boolean equals(@CheckForNull Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            TestValue other = (TestValue)obj;
            if (value != null) {
                if (!value.equals(other.value)) {
                    return false;
                }
            } else {
                if (other.value != null) {
                    return false;
                }
            }
            return true;
        }

    }
}

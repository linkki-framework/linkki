/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ButtonBinding;
import org.linkki.core.binding.ElementBinding;
import org.linkki.core.binding.FieldBinding;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

/**
 * TODO Add test with messages
 */
@SuppressWarnings("null")
public class PmoBasedSectionFactoryTest {

    private FieldBinding<?> textFieldBinding;
    private FieldBinding<?> comboBinding1;
    private FieldBinding<?> comboBinding2;
    private FieldBinding<?> disabledInvisibleBinding;
    private ButtonBinding buttonBinding;
    private BindingContext bindingContext;
    private TestPmoWithAnnotations pmo;
    private BaseSection section;

    @Before
    public void setUp() {
        bindingContext = TestBindingContext.create();
        pmo = new TestPmoWithAnnotations();
        section = new DefaultPmoBasedSectionFactory().createBaseSection(pmo, bindingContext);

        Collection<ElementBinding> bindings = bindingContext.getElementBindings();
        assertEquals(5, bindings.size());
        for (Binding binding : bindings) {
            if (binding instanceof FieldBinding) {
                FieldBinding<?> fieldBinding = (FieldBinding<?>)binding;
                String property = fieldBinding.getPropertyDispatcher().getProperty();
                switch (property) {
                    case "xyz":
                        textFieldBinding = fieldBinding;
                        break;
                    case "staticEnumAttr":
                        comboBinding1 = fieldBinding;
                        break;
                    case "dynamicEnumAttr":
                        comboBinding2 = fieldBinding;
                        break;
                    case "disabledInvisible":
                        disabledInvisibleBinding = fieldBinding;
                        break;
                    default:
                        throw new IllegalStateException("Unknown property: " + property);
                }
            } else if (binding instanceof ButtonBinding) {
                buttonBinding = (ButtonBinding)binding;
            }
        }
    }

    @Test
    public void testCreateSection_DefaultState() {
        assertEquals("123", textFieldBinding.getValue());
        assertEquals(TestEnum.THREE, comboBinding1.getValue());
        assertEquals(TestEnum.TWO, comboBinding2.getValue());
        assertNull(disabledInvisibleBinding.getValue());

        assertTrue(textFieldBinding.isEnabled());
        assertTrue(comboBinding1.isEnabled());
        assertTrue(comboBinding2.isEnabled());
        assertFalse(disabledInvisibleBinding.isEnabled());
        assertTrue(buttonBinding.isEnabled());

        assertTrue(textFieldBinding.isVisible());
        assertTrue(comboBinding1.isVisible());
        assertTrue(comboBinding2.isVisible());
        assertFalse(disabledInvisibleBinding.isVisible());
        assertTrue(buttonBinding.isVisible());

        assertTrue(textFieldBinding.isRequired());
        assertFalse(comboBinding1.isRequired());
        assertFalse(comboBinding2.isRequired());
        assertFalse(disabledInvisibleBinding.isRequired());

        assertEquals(4, comboBinding1.getAvailableValues().size());
        assertEquals(2, comboBinding2.getAvailableValues().size());
    }

    @Test
    public void testCreateSection_Dependencies() {
        pmo.setXyz("abc");
        pmo.setStaticEnumAttr(TestEnum.TWO);

        assertEquals("abc", textFieldBinding.getValue());
        assertEquals(TestEnum.TWO, comboBinding1.getValue());

        assertFalse(textFieldBinding.isEnabled());
        assertTrue(comboBinding2.isEnabled());

        assertTrue(textFieldBinding.isVisible());

        // annotation defines field as always visible, thus the isVisible() methods is ignored
        assertFalse(pmo.isDynamicEnumAttrVisible());
        assertTrue(comboBinding2.isVisible());
    }

    @Test
    public void testCreateSection_NoAvailableValues() {
        assertThat(textFieldBinding.getAvailableValues(), is(empty()));
    }

    @Test
    public void testCreateSection_ButtonBinding() {
        Panel panel = (Panel)section.getComponent(1);
        GridLayout layout = (GridLayout)panel.getContent();
        Button button = (Button)layout.getComponent(1, 4);
        assertThat(pmo.clickCount, is(0));
        button.click();
        assertThat(pmo.clickCount, is(1));
    }

    @UISection
    public class TestPmoWithAnnotations {

        private String xyz = "123";
        private TestEnum staticEnumAttr = TestEnum.THREE;

        public boolean buttonVisible = true;
        public boolean buttonEnabled = true;
        public int clickCount = 0;

        @UITextField(position = 1, enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED)
        public void xyz() {
            // nothing to do
        }

        @UIComboBox(position = 2, enabled = EnabledType.ENABLED)
        public void staticEnumAttr() {
            // nothing to do
        }

        @UIComboBox(position = 3, enabled = EnabledType.ENABLED, visible = VisibleType.VISIBLE, content = AvailableValuesType.DYNAMIC, required = RequiredType.NOT_REQUIRED)
        public void dynamicEnumAttr() {
            // nothing to do
        }

        @UITextField(position = 4, enabled = EnabledType.DISABLED, visible = VisibleType.INVISIBLE, required = RequiredType.NOT_REQUIRED)
        public void disabledInvisible() {
            // nothing to do
        }

        @UIButton(position = 5, enabled = EnabledType.DYNAMIC, visible = VisibleType.DYNAMIC)
        public void button() {
            clickCount++;
        }

        public boolean isButtonVisible() {
            return buttonVisible;
        }

        public boolean isButtonEnabled() {
            return buttonEnabled;
        }

        public String getXyz() {
            return xyz;
        }

        public void setXyz(String xyz) {
            this.xyz = xyz;
        }

        public boolean isXyzEnabled() {
            return getStaticEnumAttr() == TestEnum.THREE;
        }

        public boolean isXyzRequired() {
            return false;
        }

        public TestEnum getStaticEnumAttr() {
            return staticEnumAttr;
        }

        public void setStaticEnumAttr(TestEnum attr) {
            staticEnumAttr = attr;
        }

        public TestEnum getDynamicEnumAttr() {
            return TestEnum.TWO;
        }

        public boolean isDynamicEnumAttrVisible() {
            return getStaticEnumAttr() == TestEnum.THREE;
        }

        public List<TestEnum> getDynamicEnumAttrAvailableValues() {
            return Arrays.asList(new TestEnum[] { TestEnum.TWO, TestEnum.THREE });
        }

        public String getDisabledInvisible() {
            return null;
        }

        @ModelObject
        public Object getModelObject() {
            return StringUtils.EMPTY;
        }

    }

}

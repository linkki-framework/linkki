/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.ui.section;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.BindingContext;
import de.faktorzehn.ipm.web.binding.FieldBinding;
import de.faktorzehn.ipm.web.binding.TestEnum;
import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIComboBox;
import de.faktorzehn.ipm.web.ui.section.annotations.UISection;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextField;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;

/**
 * TODO Add test with messages
 *
 * @author widmaier
 */
public class PmoBasedSectionFactoryTest {

    private FieldBinding<?> textFieldBinding;
    private FieldBinding<?> comboBinding1;
    private FieldBinding<?> comboBinding2;
    private FieldBinding<?> disabledInvisibleBinding;
    private BindingContext bindingContext;
    private PMOWithAnnotations pmo;

    @Before
    public void setUp() {
        bindingContext = new BindingContext("testContext");
        pmo = new PMOWithAnnotations();
        new DefaultPmoBasedSectionFactory().createSection(pmo, bindingContext);

        List<FieldBinding<?>> bindings = bindingContext.getFieldBindings();
        assertEquals(4, bindings.size());
        textFieldBinding = bindings.get(0);
        comboBinding1 = bindings.get(1);
        comboBinding2 = bindings.get(2);
        disabledInvisibleBinding = bindings.get(3);
    }

    @Test
    public void testCreateSection_defaultState() {
        assertEquals("123", textFieldBinding.getValue());
        assertEquals(TestEnum.THREE, comboBinding1.getValue());
        assertEquals(TestEnum.TWO, comboBinding2.getValue());
        assertNull(disabledInvisibleBinding.getValue());

        assertTrue(textFieldBinding.isEnabled());
        assertTrue(comboBinding1.isEnabled());
        assertTrue(comboBinding2.isEnabled());
        assertFalse(disabledInvisibleBinding.isEnabled());

        assertTrue(textFieldBinding.isVisible());
        assertTrue(comboBinding1.isVisible());
        assertTrue(comboBinding2.isVisible());
        assertFalse(disabledInvisibleBinding.isVisible());

        assertTrue(textFieldBinding.isRequired());
        assertFalse(comboBinding1.isRequired());
        assertFalse(comboBinding2.isRequired());
        assertFalse(disabledInvisibleBinding.isRequired());

        assertEquals(3, comboBinding1.getAvailableValues().size());
        assertEquals(2, comboBinding2.getAvailableValues().size());
    }

    @Test
    public void testCreateSection_dependencies() {
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

    @Test(expected = IllegalStateException.class)
    public void testCreateSection_exceptionIllegalAvailableValues() {
        assertNull(textFieldBinding.getAvailableValues());
    }

    @UISection
    public class PMOWithAnnotations implements PresentationModelObject {

        private String xyz = "123";
        private TestEnum staticEnumAttr = TestEnum.THREE;

        @UITextField(position = 1, enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED)
        public void xyz() {
            // nothing to do
        }

        @UIComboBox(position = 2, enabled = EnabledType.ENABLED, lov = AvailableValuesType.STATIC)
        public void staticEnumAttr() {
            // nothing to do
        }

        @UIComboBox(position = 3, enabled = EnabledType.ENABLED, visible = VisibleType.VISIBLE, lov = AvailableValuesType.DYNAMIC, required = RequiredType.NOT_REQUIRED)
        public void dynamicEnumAttr() {
            // nothing to do
        }

        @UITextField(position = 4, enabled = EnabledType.DISABLED, visible = VisibleType.INVISIBLE, required = RequiredType.NOT_REQUIRED)
        public void disabledInvisible() {
            // nothing to do
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

        @Override
        public Object getModelObject() {
            return "";
        }

    }

}

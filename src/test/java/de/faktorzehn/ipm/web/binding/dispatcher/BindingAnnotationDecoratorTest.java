/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.web.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.faktorzehn.ipm.web.binding.TestEnum;
import de.faktorzehn.ipm.web.ui.section.annotations.AvailableValuesType;
import de.faktorzehn.ipm.web.ui.section.annotations.EnabledType;
import de.faktorzehn.ipm.web.ui.section.annotations.RequiredType;
import de.faktorzehn.ipm.web.ui.section.annotations.UIComboBox;
import de.faktorzehn.ipm.web.ui.section.annotations.UITextField;
import de.faktorzehn.ipm.web.ui.section.annotations.VisibleType;

@RunWith(MockitoJUnitRunner.class)
public class BindingAnnotationDecoratorTest {

    @Mock
    private PropertyDispatcher mockDispatcher;
    private BindingAnnotationDecorator decorator;
    private ObjectWithAnnotations annotatedObject;

    @Before
    public void setUp() {
        annotatedObject = new ObjectWithAnnotations();
        decorator = new BindingAnnotationDecorator(mockDispatcher, annotatedObject.getClass());
    }

    @Test
    public void testGetValue() {
        decorator.getValue("xyz");

        verify(mockDispatcher).getValue("xyz");
    }

    @Test
    public void testSetValue() {
        decorator.setValue("xyz", "value");

        verify(mockDispatcher).setValue("xyz", "value");
    }

    @Test
    public void testIsEnabledDynamic() {
        decorator.isEnabled("xyz");

        verify(mockDispatcher).isEnabled("xyz");
    }

    @Test
    public void testIsEnabledDisabled() {
        assertFalse(decorator.isEnabled("disabledInvisible"));

        verify(mockDispatcher, never()).isEnabled(anyString());
    }

    @Test
    public void testIsEnabledEnabled() {
        assertTrue(decorator.isEnabled("staticEnumAttr"));

        verify(mockDispatcher, never()).isEnabled("staticEnumAttr");
    }

    @Test
    public void testIsEnabled_missingFieldAnnotation() {
        decorator.isEnabled("notAnnotatedProperty");

        verify(mockDispatcher).isEnabled("notAnnotatedProperty");
    }

    @Test
    public void testIsVisibleDynamic() {
        decorator.isVisible("xyz");

        verify(mockDispatcher).isVisible("xyz");
    }

    @Test
    public void testIsVisibleInvisible() {
        assertFalse(decorator.isVisible("disabledInvisible"));

        verify(mockDispatcher, never()).isVisible(anyString());
    }

    @Test
    public void testIsVisibleVisible() {
        assertTrue(decorator.isVisible("dynamicEnumAttr"));

        verify(mockDispatcher, never()).isVisible("staticEnumAttr");
    }

    @Test
    public void testIsVisible_missingFieldAnnotation() {
        decorator.isVisible("notAnnotatedProperty");

        verify(mockDispatcher).isVisible("notAnnotatedProperty");
    }

    @Test
    public void testIsRequiredDynamic() {
        decorator.isRequired("staticEnumAttr");

        verify(mockDispatcher).isRequired("staticEnumAttr");
    }

    @Test
    public void testIsRequiredNotRequired() {
        assertFalse(decorator.isRequired("dynamicEnumAttr"));

        verify(mockDispatcher, never()).isRequired(anyString());
    }

    @Test
    public void testIsRequiredRequired() {
        assertTrue(decorator.isRequired("xyz"));

        verify(mockDispatcher, never()).isRequired("xyz");
    }

    @Test
    public void testIsRequiredRequiredIfEnabled() {
        when(mockDispatcher.isEnabled("requiredIfEnabled")).thenReturn(false);
        assertFalse(decorator.isRequired("requiredIfEnabled"));

        verify(mockDispatcher, never()).isRequired("requiredIfEnabled");
    }

    @Test
    public void testIsRequiredRequiredIfEnabled2() {
        when(mockDispatcher.isEnabled("requiredIfEnabled")).thenReturn(true);
        assertTrue(decorator.isRequired("requiredIfEnabled"));

        verify(mockDispatcher, never()).isRequired("requiredIfEnabled");
    }

    @Test
    public void testIsRequired_missingFieldAnnotation() {
        decorator.isRequired("notAnnotatedProperty");

        verify(mockDispatcher).isRequired("notAnnotatedProperty");
    }

    @Test
    public void testGetAvailableValues() {
        doReturn(TestEnum.class).when(mockDispatcher).getValueClass("staticEnumAttr");
        Collection<?> availableValues = decorator.getAvailableValues("staticEnumAttr");
        assertEquals(3, availableValues.size());

        verify(mockDispatcher, never()).getAvailableValues("staticEnumAttr");
    }

    @Test
    public void testGetAvailableValues2() {
        doReturn(Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE })).when(mockDispatcher)
                .getAvailableValues("dynamicEnumAttr");
        Collection<?> availableValues = decorator.getAvailableValues("dynamicEnumAttr");
        assertEquals(2, availableValues.size());

        verify(mockDispatcher, never()).getValueClass("dynamicEnumAttr");
    }

    @Test
    public void testGetAvailableValues_missingFieldAnnotation() {
        decorator.getAvailableValues("notAnnotatedProperty");

        verify(mockDispatcher).getAvailableValues("notAnnotatedProperty");
    }

    public class ObjectWithAnnotations {

        @UITextField(position = 1, modelAttribute = "xyz", label = "XYZ:", enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED, visible = VisibleType.DYNAMIC)
        public void xyz() {
            // nothing to do
        }

        @UIComboBox(position = 2, modelAttribute = "staticEnumAttr", label = "Bla", lov = AvailableValuesType.STATIC, enabled = EnabledType.ENABLED, required = RequiredType.DYNAMIC)
        public void staticEnumAttr() {
            // nothing to do
        }

        @UIComboBox(position = 3, modelAttribute = "dynamicEnumAttr", lov = AvailableValuesType.DYNAMIC, visible = VisibleType.VISIBLE, required = RequiredType.NOT_REQUIRED)
        public void dynamicEnumAttr() {
            // nothing to do
        }

        public Object getDynamicEnumAttr() {
            return null;
        }

        @UITextField(position = 4, modelAttribute = "disabledInvisible", visible = VisibleType.INVISIBLE, enabled = EnabledType.DISABLED, required = RequiredType.NOT_REQUIRED)
        public void disabledInvisible() {
            // nothing to do
        }

        @UITextField(position = 5, modelAttribute = "requiredIfEnabled", enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED_IF_ENABLED)
        public void requiredIfEnabled() {
            // nothing to do
        }

    }
}

/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BindingAnnotationDispatcherTest {

    private static final String STATIC_ENUM_ATTR_EXCL_NULL = "staticEnumAttrExclNull";
    private static final String REQUIRED_IF_ENABLED = "requiredIfEnabled";
    private static final String DYNAMIC_ENUM_ATTR = "dynamicEnumAttr";
    private static final String STATIC_ENUM_ATTR = "staticEnumAttr";
    private static final String DISABLED_INVISIBLE = "disabledInvisible";
    private static final String XYZ = "xyz";
    @Mock
    private PropertyDispatcher mockDispatcher;
    private ObjectWithAnnotations annotatedObject;
    private Map<String, BindingAnnotationDispatcher> decorators;

    @Before
    public void setUp() {
        annotatedObject = new ObjectWithAnnotations();
        UIAnnotationReader uiAnnotationReader = new UIAnnotationReader(annotatedObject.getClass());
        decorators = uiAnnotationReader.getUiElements().stream().collect(Collectors
                .toMap(e -> e.getPropertyName(), e -> new BindingAnnotationDispatcher(mockDispatcher, e)));
    }

    @Test
    public void testGetValue() {
        decorators.get(XYZ).getValue();

        verify(mockDispatcher).getValue();
    }

    @Test
    public void testSetValue() {
        decorators.get(XYZ).setValue("value");

        verify(mockDispatcher).setValue("value");
    }

    @Test
    public void testIsEnabledDynamic() {
        decorators.get(XYZ).isEnabled();

        verify(mockDispatcher).isEnabled();
    }

    @Test
    public void testIsEnabledDisabled() {
        assertFalse(decorators.get(DISABLED_INVISIBLE).isEnabled());

        verify(mockDispatcher, never()).isEnabled();
    }

    @Test
    public void testIsEnabledEnabled() {
        assertTrue(decorators.get(STATIC_ENUM_ATTR).isEnabled());

        verify(mockDispatcher, never()).isEnabled();
    }

    @Test
    public void testIsEnabled_missingFieldAnnotation() {
        decorators.get(XYZ).isEnabled();

        verify(mockDispatcher).isEnabled();
    }

    @Test
    public void testIsVisibleDynamic() {
        decorators.get(XYZ).isVisible();

        verify(mockDispatcher).isVisible();
    }

    @Test
    public void testIsVisibleInvisible() {
        assertFalse(decorators.get(DISABLED_INVISIBLE).isVisible());

        verify(mockDispatcher, never()).isVisible();
    }

    @Test
    public void testIsVisibleVisible() {
        assertTrue(decorators.get(DYNAMIC_ENUM_ATTR).isVisible());

        verify(mockDispatcher, never()).isVisible();
    }

    @Test
    public void testIsVisible_missingFieldAnnotation() {
        decorators.get(XYZ).isVisible();

        verify(mockDispatcher).isVisible();
    }

    @Test
    public void testIsRequiredDynamic() {
        decorators.get(STATIC_ENUM_ATTR).isRequired();

        verify(mockDispatcher).isRequired();
    }

    @Test
    public void testIsRequiredNotRequired() {
        assertFalse(decorators.get(DYNAMIC_ENUM_ATTR).isRequired());

        verify(mockDispatcher, never()).isRequired();
    }

    @Test
    public void testIsRequiredRequired() {
        assertTrue(decorators.get(XYZ).isRequired());

        verify(mockDispatcher, never()).isRequired();
    }

    @Test
    public void testIsRequiredRequiredIfEnabled() {
        when(mockDispatcher.isEnabled()).thenReturn(false);
        assertFalse(decorators.get(REQUIRED_IF_ENABLED).isRequired());

        verify(mockDispatcher, never()).isRequired();
    }

    @Test
    public void testIsRequiredRequiredIfEnabled2() {
        when(mockDispatcher.isEnabled()).thenReturn(true);
        assertTrue(decorators.get(REQUIRED_IF_ENABLED).isRequired());

        verify(mockDispatcher, never()).isRequired();
    }

    @Test
    public void testGetAvailableValues_inklNull() {
        doReturn(TestEnum.class).when(mockDispatcher).getValueClass();
        Collection<?> availableValues = decorators.get(STATIC_ENUM_ATTR).getAvailableValues();

        assertEquals(4, availableValues.size());
        verify(mockDispatcher, never()).getAvailableValues();
    }

    @Test
    public void testGetAvailableValues_ExclNull() {
        doReturn(TestEnum.class).when(mockDispatcher).getValueClass();
        Collection<?> availableValues = decorators.get(STATIC_ENUM_ATTR_EXCL_NULL).getAvailableValues();

        assertEquals(3, availableValues.size());
        verify(mockDispatcher, never()).getAvailableValues();
    }

    @Test
    public void testGetAvailableValues2() {
        doReturn(Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE })).when(mockDispatcher)
                .getAvailableValues();
        Collection<?> availableValues = decorators.get(DYNAMIC_ENUM_ATTR).getAvailableValues();
        assertEquals(2, availableValues.size());

        verify(mockDispatcher, never()).getValueClass();
    }

    @Test
    public void testGetAvailableValues_missingFieldAnnotation() {
        doReturn(Void.class).when(mockDispatcher).getValueClass();
        decorators.get(XYZ).getAvailableValues();

        verify(mockDispatcher).getAvailableValues();
    }

    public class ObjectWithAnnotations {

        @UITextField(position = 1, modelAttribute = XYZ, label = "XYZ:", enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED, visible = VisibleType.DYNAMIC)
        public void xyz() {
            // nothing to do
        }

        @UIComboBox(position = 2, modelAttribute = STATIC_ENUM_ATTR, label = "Bla", content = AvailableValuesType.ENUM_VALUES_INCL_NULL, enabled = EnabledType.ENABLED, required = RequiredType.DYNAMIC)
        public void staticEnumAttr() {
            // nothing to do
        }

        @UIComboBox(position = 7, modelAttribute = STATIC_ENUM_ATTR_EXCL_NULL, label = "Bla", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, enabled = EnabledType.ENABLED, required = RequiredType.DYNAMIC)
        public void staticEnumAttrExclNull() {
            // nothing to do
        }

        @UIComboBox(position = 3, modelAttribute = DYNAMIC_ENUM_ATTR, content = AvailableValuesType.DYNAMIC, visible = VisibleType.VISIBLE, required = RequiredType.NOT_REQUIRED)
        public void dynamicEnumAttr() {
            // nothing to do
        }

        public Object getDynamicEnumAttr() {
            return null;
        }

        @UITextField(position = 4, modelAttribute = DISABLED_INVISIBLE, visible = VisibleType.INVISIBLE, enabled = EnabledType.DISABLED, required = RequiredType.NOT_REQUIRED)
        public void disabledInvisible() {
            // nothing to do
        }

        @UITextField(position = 5, modelAttribute = REQUIRED_IF_ENABLED, enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED_IF_ENABLED)
        public void requiredIfEnabled() {
            // nothing to do
        }

    }
}

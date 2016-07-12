/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 *
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding.dispatcher;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
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
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.BindAnnotationDescriptor;
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
    private PropertyDispatcher uiAnnotationFallbackDispatcher;
    private Map<String, BindingAnnotationDispatcher> uiAnnotationDispatchers;
    private TestObjectWithUIAnnotations objectWithUIAnnotations;

    @Mock
    private PropertyDispatcher bindAnnotationFallbackDispatcher;
    private BindingAnnotationDispatcher bindAnnotationDispatcher;
    private TestObjectWithBindAnnotation objectWithBindAnnotation;

    @Before
    public void setUp() {
        objectWithUIAnnotations = new TestObjectWithUIAnnotations();
        UIAnnotationReader uiAnnotationReader = new UIAnnotationReader(objectWithUIAnnotations.getClass());
        uiAnnotationDispatchers = uiAnnotationReader.getUiElements().stream()
                .collect(Collectors.toMap(e -> e.getModelPropertyName(),
                                          e -> new BindingAnnotationDispatcher(uiAnnotationFallbackDispatcher, e)));

        objectWithBindAnnotation = new TestObjectWithBindAnnotation();
        bindAnnotationDispatcher = new BindingAnnotationDispatcher(bindAnnotationFallbackDispatcher,
                objectWithBindAnnotation.bindAnnotationDescriptor());
    }

    @Test
    public void testGetValue() {
        uiAnnotationDispatchers.get(XYZ).getValue();
        verify(uiAnnotationFallbackDispatcher).getValue();
    }

    @Test
    public void testSetValue() {
        uiAnnotationDispatchers.get(XYZ).setValue("value");
        verify(uiAnnotationFallbackDispatcher).setValue("value");
    }

    @Test
    public void testIsEnabled_Dynamic() {
        uiAnnotationDispatchers.get(XYZ).isEnabled();
        verify(uiAnnotationFallbackDispatcher).isEnabled();
    }

    @Test
    public void testIsEnabled_Disabled() {
        assertFalse(uiAnnotationDispatchers.get(DISABLED_INVISIBLE).isEnabled());
        verify(uiAnnotationFallbackDispatcher, never()).isEnabled();

        assertFalse(bindAnnotationDispatcher.isEnabled());
        verify(bindAnnotationFallbackDispatcher, never()).isEnabled();
    }

    @Test
    public void testIsEnabled_Enabled() {
        assertTrue(uiAnnotationDispatchers.get(STATIC_ENUM_ATTR).isEnabled());
        verify(uiAnnotationFallbackDispatcher, never()).isEnabled();
    }

    @Test
    public void testIsEnabled_MissingFieldAnnotation() {
        uiAnnotationDispatchers.get(XYZ).isEnabled();
        verify(uiAnnotationFallbackDispatcher).isEnabled();
    }

    @Test
    public void testIsVisible_Dynamic() {
        uiAnnotationDispatchers.get(XYZ).isVisible();
        verify(uiAnnotationFallbackDispatcher).isVisible();
    }

    @Test
    public void testIsVisible_Invisible() {
        assertFalse(uiAnnotationDispatchers.get(DISABLED_INVISIBLE).isVisible());
        verify(uiAnnotationFallbackDispatcher, never()).isVisible();

        assertFalse(bindAnnotationDispatcher.isVisible());
        verify(bindAnnotationFallbackDispatcher, never()).isVisible();
    }

    @Test
    public void testIsVisible_Visible() {
        assertTrue(uiAnnotationDispatchers.get(DYNAMIC_ENUM_ATTR).isVisible());
        verify(uiAnnotationFallbackDispatcher, never()).isVisible();
    }

    @Test
    public void testIsVisible_MissingFieldAnnotation() {
        uiAnnotationDispatchers.get(XYZ).isVisible();
        verify(uiAnnotationFallbackDispatcher).isVisible();
    }

    @Test
    public void testIsRequired_Dynamic() {
        uiAnnotationDispatchers.get(STATIC_ENUM_ATTR).isRequired();
        verify(uiAnnotationFallbackDispatcher).isRequired();
    }

    @Test
    public void testIsRequired_NotRequired() {
        assertFalse(uiAnnotationDispatchers.get(DYNAMIC_ENUM_ATTR).isRequired());
        verify(uiAnnotationFallbackDispatcher, never()).isRequired();
    }

    @Test
    public void testIsRequired_Required() {
        assertTrue(uiAnnotationDispatchers.get(XYZ).isRequired());
        verify(uiAnnotationFallbackDispatcher, never()).isRequired();

        assertTrue(bindAnnotationDispatcher.isRequired());
        verify(bindAnnotationFallbackDispatcher, never()).isRequired();
    }

    @Test
    public void testIsRequired_RequiredIfEnabled() {
        when(uiAnnotationFallbackDispatcher.isEnabled()).thenReturn(false);

        assertFalse(uiAnnotationDispatchers.get(REQUIRED_IF_ENABLED).isRequired());
        verify(uiAnnotationFallbackDispatcher, never()).isRequired();
    }

    @Test
    public void testIsRequired_RequiredIfEnabled2() {
        when(uiAnnotationFallbackDispatcher.isEnabled()).thenReturn(true);

        assertTrue(uiAnnotationDispatchers.get(REQUIRED_IF_ENABLED).isRequired());
        verify(uiAnnotationFallbackDispatcher, never()).isRequired();
    }

    @Test
    public void testGetAvailableValues_IncludingNull() {
        doReturn(TestEnum.class).when(uiAnnotationFallbackDispatcher).getValueClass();
        Collection<?> availableValues = uiAnnotationDispatchers.get(STATIC_ENUM_ATTR).getAvailableValues();

        assertEquals(4, availableValues.size());
        verify(uiAnnotationFallbackDispatcher, never()).getAvailableValues();
    }

    @Test
    public void testGetAvailableValues_ExcludingNull() {
        doReturn(TestEnum.class).when(uiAnnotationFallbackDispatcher).getValueClass();
        Collection<?> availableValues = uiAnnotationDispatchers.get(STATIC_ENUM_ATTR_EXCL_NULL).getAvailableValues();

        assertEquals(3, availableValues.size());
        verify(uiAnnotationFallbackDispatcher, never()).getAvailableValues();
    }

    @Test
    public void testGetAvailableValues() {
        doReturn(Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE })).when(uiAnnotationFallbackDispatcher)
                .getAvailableValues();
        Collection<?> availableValues = uiAnnotationDispatchers.get(DYNAMIC_ENUM_ATTR).getAvailableValues();
        assertEquals(2, availableValues.size());

        verify(uiAnnotationFallbackDispatcher, never()).getValueClass();
    }

    @Test
    public void testGetAvailableValues_AnnotationWithoutAvailableValues() {
        assertThat(uiAnnotationDispatchers.get(XYZ).getAvailableValues(), is(empty()));
        assertThat(bindAnnotationDispatcher.getAvailableValues(), is(empty()));
    }

    public class TestObjectWithBindAnnotation {

        @Bind(pmoProperty = "", enabled = EnabledType.DISABLED, visible = VisibleType.INVISIBLE, required = RequiredType.REQUIRED, availableValues = AvailableValuesType.NO_VALUES)
        public Bind bindAnnotation() {
            try {
                return getClass().getMethod("bindAnnotation", new Class<?>[] {}).getAnnotation(Bind.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(e);
            }
        }

        public BindAnnotationDescriptor bindAnnotationDescriptor() {
            return new BindAnnotationDescriptor(bindAnnotation());
        }
    }

    public class TestObjectWithUIAnnotations {

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

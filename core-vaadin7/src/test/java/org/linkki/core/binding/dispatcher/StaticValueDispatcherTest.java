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
package org.linkki.core.binding.dispatcher;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.section.annotations.aspect.AvailableValuesAspectDefinition;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

// TODO LIN-1247 replace vaadin annotations with test annotations and move back to core
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("null")
public class StaticValueDispatcherTest {

    private static final String STATIC_ENUM_ATTR_EXCL_NULL = "staticEnumAttrExclNull";
    private static final String REQUIRED_IF_ENABLED = "requiredIfEnabled";
    private static final String DYNAMIC_ENUM_ATTR = "dynamicEnumAttr";
    private static final String STATIC_ENUM_ATTR = "staticEnumAttr";
    private static final String DISABLED_INVISIBLE = "disabledInvisible";
    private static final String XYZ = "xyz";

    @Mock
    private PropertyDispatcher uiAnnotationFallbackDispatcher;
    private Map<String, @NonNull StaticValueDispatcher> uiAnnotationDispatchers;
    private TestObjectWithUIAnnotations objectWithUIAnnotations;

    @Mock
    private PropertyDispatcher bindAnnotationFallbackDispatcher;

    @Before
    public void setUp() {
        objectWithUIAnnotations = new TestObjectWithUIAnnotations();
        UIAnnotationReader uiAnnotationReader = new UIAnnotationReader(objectWithUIAnnotations.getClass());
        uiAnnotationDispatchers = uiAnnotationReader.getUiElements()
                .collect(Collectors.toMap(e -> e.getPmoPropertyName(),
                                          e -> new StaticValueDispatcher(uiAnnotationFallbackDispatcher)));
    }

    @Test
    public void testGetValue() {
        Aspect<Object> newDynamic = Aspect.of("");
        pull(XYZ, newDynamic);
        verify(uiAnnotationFallbackDispatcher).pull(newDynamic);
    }

    @Test
    public void testPull_static() {
        Aspect<ArrayList<Object>> staticAspect = Aspect.of(AvailableValuesAspectDefinition.NAME,
                                                           new ArrayList<>());
        pull(STATIC_ENUM_ATTR, staticAspect);
        verify(uiAnnotationFallbackDispatcher, never()).pull(staticAspect);
    }


    @Test
    public void testPull_dynamic() {
        Aspect<ArrayList<Object>> dynamicAspect = Aspect.of(AvailableValuesAspectDefinition.NAME);
        pull(STATIC_ENUM_ATTR, dynamicAspect);
        verify(uiAnnotationFallbackDispatcher).pull(dynamicAspect);
    }

    private void pull(String property, Aspect<?> aspect) {
        @NonNull
        StaticValueDispatcher staticValueDispatcher = uiAnnotationDispatchers.get(property);
        staticValueDispatcher.pull(aspect);
    }

    public class TestObjectWithUIAnnotations {

        @UITextField(position = 1, modelAttribute = XYZ, label = "XYZ:", enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED, visible = VisibleType.DYNAMIC)
        public void xyz() {
            // nothing to do
        }

        @UIComboBox(position = 2, modelAttribute = STATIC_ENUM_ATTR, label = "Bla", enabled = EnabledType.ENABLED, required = RequiredType.DYNAMIC)
        public void staticEnumAttr() {
            // nothing to do
        }

        @UIComboBox(position = 7, modelAttribute = STATIC_ENUM_ATTR_EXCL_NULL, label = "Bla", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL, enabled = EnabledType.ENABLED, required = RequiredType.DYNAMIC)
        public void staticEnumAttrExclNull() {
            // nothing to do
        }

        @UIComboBox(position = 3, label = "", modelAttribute = DYNAMIC_ENUM_ATTR, content = AvailableValuesType.DYNAMIC, visible = VisibleType.VISIBLE, required = RequiredType.NOT_REQUIRED)
        public void dynamicEnumAttr() {
            // nothing to do
        }

        public Object getDynamicEnumAttr() {
            return new Object();
        }

        @UITextField(position = 4, label = "", modelAttribute = DISABLED_INVISIBLE, visible = VisibleType.INVISIBLE, enabled = EnabledType.DISABLED, required = RequiredType.NOT_REQUIRED)
        public void disabledInvisible() {
            // nothing to do
        }

        @UITextField(position = 5, label = "", modelAttribute = REQUIRED_IF_ENABLED, enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED_IF_ENABLED)
        public void requiredIfEnabled() {
            // nothing to do
        }

    }
}

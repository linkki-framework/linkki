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
package org.linkki.core.binding.dispatcher.staticvalue;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.descriptor.UIElementAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.ui.element.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.umd.cs.findbugs.annotations.NonNull;

@RunWith(MockitoJUnitRunner.class)
public class StaticValueDispatcherTest {

    private static final String DYNAMIC_ENUM_ATTR = "dynamicEnumAttr";
    private static final String STATIC_ENUM_ATTR = "staticEnumAttr";
    private static final String XYZ = "xyz";

    @Mock
    private PropertyDispatcher uiAnnotationFallbackDispatcher;
    private Map<String, StaticValueDispatcher> uiAnnotationDispatchers;
    private TestObjectWithUIAnnotations objectWithUIAnnotations;

    @Mock
    private PropertyDispatcher bindAnnotationFallbackDispatcher;

    @Before
    public void setUp() {
        objectWithUIAnnotations = new TestObjectWithUIAnnotations();
        UIElementAnnotationReader uiAnnotationReader = new UIElementAnnotationReader(
                objectWithUIAnnotations.getClass());
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
        Aspect<ArrayList<Object>> staticAspect = Aspect.of(EnabledAspectDefinition.NAME,
                                                           new ArrayList<>());
        pull(STATIC_ENUM_ATTR, staticAspect);
        verify(uiAnnotationFallbackDispatcher, never()).pull(staticAspect);
    }


    @Test
    public void testPull_dynamic() {
        Aspect<ArrayList<Object>> dynamicAspect = Aspect.of(EnabledAspectDefinition.NAME);
        pull(DYNAMIC_ENUM_ATTR, dynamicAspect);
        verify(uiAnnotationFallbackDispatcher).pull(dynamicAspect);
    }

    private void pull(String property, Aspect<?> aspect) {
        @NonNull
        StaticValueDispatcher staticValueDispatcher = uiAnnotationDispatchers.get(property);
        staticValueDispatcher.pull(aspect);
    }

    public class TestObjectWithUIAnnotations {

        @TestUIField(position = 1, modelAttribute = XYZ, label = "XYZ:")
        public void xyz() {
            // nothing to do
        }

        @TestUIField(position = 2, modelAttribute = STATIC_ENUM_ATTR, label = STATIC_ENUM_ATTR, enabled = EnabledType.ENABLED)
        public void staticEnumAttr() {
            // nothing to do
        }

        @TestUIField(position = 3, modelAttribute = DYNAMIC_ENUM_ATTR, label = DYNAMIC_ENUM_ATTR, enabled = EnabledType.DYNAMIC)
        public void dynamicEnumAttr() {
            // nothing to do
        }

    }
}

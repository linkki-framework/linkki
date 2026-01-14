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

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.flow.data.provider.KeyMapper;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.dom.Element;

import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

class BindComboBoxItemStyleAspectDefinitionTest {

    private static final String EXPECTED_LABEL = "expected label";
    private static final String EXPECTED_STYLE = "expected style";

    @Test
    void testCreateAspect_NoStyleNames() {
        var aspectDefinition = new BindComboBoxItemStyleAspectDefinition();

        Aspect<Function<Object, String>> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName()).isEqualTo(BindComboBoxItemStyleAspectDefinition.NAME);
        assertThat(createdAspect.isValuePresent())
                .as("Aspect should be dynamic")
                .isFalse();
    }

    @Test
    void testCreateAspect_SingleStyleName() {
        var aspectDefinition = new BindComboBoxItemStyleAspectDefinition("foo");

        Aspect<Function<Object, String>> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName()).isEqualTo(BindComboBoxItemStyleAspectDefinition.NAME);
        assertThat(createdAspect.getValue().apply("any object")).isEqualTo("foo");
    }

    @Test
    void testCreateAspect_MultipleStyleNames() {
        var aspectDefinition = new BindComboBoxItemStyleAspectDefinition("foo",
                "bar");

        Aspect<Function<Object, String>> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName()).isEqualTo(BindComboBoxItemStyleAspectDefinition.NAME);
        assertThat(createdAspect.getValue().apply("any object")).isEqualTo("foo bar");
    }

    @Test
    void testCreateComponentValueSetter_ComboBox() throws Exception {
        MockVaadin.setup();

        var styleName = "bar";
        var aspectDefinition = new BindComboBoxItemStyleAspectDefinition(styleName);
        var multiSelect = ComponentFactory.newComboBox();
        var componentWrapper = new NoLabelComponentWrapper(multiSelect);

        var componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);
        componentValueSetter.accept(o -> EXPECTED_STYLE);

        var renderer = getRenderer(multiSelect);
        var render = renderer.render(new Element("div"), new KeyMapper<>());
        var jsonObject = new ObjectNode(new JsonNodeFactory());
        render.getDataGenerator().get().generateData(EXPECTED_LABEL, jsonObject);

        assertThat(jsonObject
                .get(jsonObject.propertyNames().stream()
                        .filter(c -> c.endsWith("style"))
                        .findFirst()
                        .orElseThrow())
                .asString())
                        .isEqualTo(EXPECTED_STYLE);
        assertThat(jsonObject
                .get(jsonObject.propertyNames().stream()
                        .filter(c -> c.endsWith("label"))
                        .findFirst()
                        .orElseThrow())
                .asString())
                        .isEqualTo(EXPECTED_LABEL);

        MockVaadin.tearDown();
    }

    @Test
    void testCreateComponentValueSetter_MultiSelect() throws Exception {
        MockVaadin.setup();

        var styleName = "bar";
        var aspectDefinition = new BindComboBoxItemStyleAspectDefinition(styleName);
        var comboBox = ComponentFactory.newMultiSelect();
        var componentWrapper = new NoLabelComponentWrapper(comboBox);

        var componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);
        componentValueSetter.accept(o -> EXPECTED_STYLE);

        var renderer = getRenderer(comboBox);
        var render = renderer.render(new Element("div"), new KeyMapper<>());
        var jsonObject = new ObjectNode(new JsonNodeFactory());
        render.getDataGenerator().get().generateData(EXPECTED_LABEL, jsonObject);

        assertThat(jsonObject
                .get(jsonObject.propertyNames().stream()
                        .filter(c -> c.endsWith("style"))
                        .findFirst()
                        .orElseThrow())
                .asString())
                        .isEqualTo(EXPECTED_STYLE);
        assertThat(jsonObject
                .get(jsonObject.propertyNames().stream()
                        .filter(c -> c.endsWith("label"))
                        .findFirst()
                        .orElseThrow())
                .asString())
                        .isEqualTo(EXPECTED_LABEL);

        MockVaadin.tearDown();
    }

    @SuppressWarnings("rawtypes")
    private LitRenderer<Object> getRenderer(ComboBoxBase component)
            throws InvocationTargetException, IllegalAccessException {
        var getRenderManager = ReflectionUtils.findMethod(ComboBoxBase.class, "getRenderManager").get();
        getRenderManager.setAccessible(true);
        var renderManager = getRenderManager.invoke(component);

        var rendererField = ReflectionUtils
                .findFields(renderManager.getClass(), f -> f.getName().equals("renderer"),
                            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .getFirst();
        rendererField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var renderer = (LitRenderer<Object>)rendererField.get(renderManager);

        return renderer;
    }
}

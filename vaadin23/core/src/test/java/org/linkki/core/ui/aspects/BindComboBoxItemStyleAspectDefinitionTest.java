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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.mockito.ArgumentCaptor;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.KeyMapper;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Rendering;
import com.vaadin.flow.dom.Element;

import elemental.json.impl.JreJsonFactory;
import elemental.json.impl.JreJsonObject;

class BindComboBoxItemStyleAspectDefinitionTest {

    private static final String EXPECTED_LABEL = "expected label";
    private static final String EXPECTED_STYLE = "expected style";

    @Test
    void testCreateAspect_NoStyleNames() {
        BindComboBoxItemStyleAspectDefinition aspectDefinition = new BindComboBoxItemStyleAspectDefinition(
                new String[] {});

        Aspect<Function<Object, String>> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindComboBoxItemStyleAspectDefinition.NAME));
        // Retrieved dynamically
        assertThat(createdAspect.isValuePresent(), is(false));
    }

    @Test
    void testCreateAspect_SingleStyleName() {
        BindComboBoxItemStyleAspectDefinition aspectDefinition = new BindComboBoxItemStyleAspectDefinition("foo");

        Aspect<Function<Object, String>> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindComboBoxItemStyleAspectDefinition.NAME));
        assertThat(createdAspect.getValue().apply("any object"), is("foo"));
    }

    @Test
    void testCreateAspect_MultipleStyleNames() {
        BindComboBoxItemStyleAspectDefinition aspectDefinition = new BindComboBoxItemStyleAspectDefinition("foo",
                "bar");

        Aspect<Function<Object, String>> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindComboBoxItemStyleAspectDefinition.NAME));
        assertThat(createdAspect.getValue().apply("any object"), is("foo bar"));
    }

    @Test
    void testCreateComponentValueSetter() {
        MockVaadin.setup();

        String styleName = "bar";
        BindComboBoxItemStyleAspectDefinition aspectDefinition = new BindComboBoxItemStyleAspectDefinition(styleName);
        ComboBox<Object> comboBox = spy(ComponentFactory.newComboBox());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<LitRenderer<Object>> argumentCaptor = ArgumentCaptor.forClass(LitRenderer.class);
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox);

        Consumer<Function<Object, String>> componentValueSetter = aspectDefinition
                .createComponentValueSetter(componentWrapper);
        componentValueSetter.accept(o -> EXPECTED_STYLE);

        verify(comboBox).setRenderer(argumentCaptor.capture());
        LitRenderer<Object> renderer = argumentCaptor.getValue();
        Rendering<Object> render = renderer.render(new Element("div"), new KeyMapper<>());
        JreJsonObject jsonObject = new JreJsonObject(new JreJsonFactory());
        render.getDataGenerator().get().generateData(EXPECTED_LABEL, jsonObject);

        assertThat(jsonObject.get("lr_0_" + BindComboBoxItemStyleAspectDefinition.LABEL).asString(),
                   is(EXPECTED_LABEL));
        assertThat(jsonObject.get("lr_0_" + BindComboBoxItemStyleAspectDefinition.CLASS).asString(),
                   is(EXPECTED_STYLE));

        MockVaadin.tearDown();
    }

}

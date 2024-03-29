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
 *
 */

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.aspects.LabelAspectDefinition.NAME;

import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.types.LabelType;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;

import com.vaadin.flow.component.textfield.TextField;

/**
 * Tests {@link LabelAspectDefinition}.
 */
class LabelAspectDefinitionTest {

    private static final String LABEL = "Label";

    @Test
    void testCreateAspect_Dynamic() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition(LABEL, LabelType.DYNAMIC);

        verifyDynamicAspect(labelAspectDefinition.createAspect());
    }

    @Test
    void testCreateAspect_AutoDynamic() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition(StringUtils.EMPTY, LabelType.AUTO);

        verifyDynamicAspect(labelAspectDefinition.createAspect());
    }

    @Test
    void testCreateAspect_AutoStatic() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition(LABEL, LabelType.AUTO);

        verifyStaticAspect(labelAspectDefinition.createAspect());
    }

    @Test
    void testCreateAspect_Static() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition(LABEL, LabelType.STATIC);

        verifyStaticAspect(labelAspectDefinition.createAspect());
    }

    @Test
    void testCreateAspect_None() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition(LABEL, LabelType.NONE);

        assertThat(labelAspectDefinition.createAspect()).satisfies(aspect -> {
            assertThat(aspect.getName()).isEqualTo(NAME);
            assertThat(aspect.getValue()).isNull();
        });
    }

    @Test
    void testCreateAspect_NoLabelTypeSpecified() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition(LABEL);

        verifyStaticAspect(labelAspectDefinition.createAspect());
    }

    @Test
    void testCreateComponentValueSetter() {
        LabelAspectDefinition labelAspectDefinition = new LabelAspectDefinition("Other", LabelType.DYNAMIC);
        TextField component = new TextField();
        ComponentWrapper componentWrapper = new LabelComponentWrapper(component, WrapperType.FIELD);
        Consumer<String> componentValueSetter = labelAspectDefinition.createComponentValueSetter(componentWrapper);
        componentValueSetter.accept(LABEL);

        assertThat(component.getLabel()).isEqualTo(LABEL);
    }

    /**
     * Verifies an aspect which represents a dynamic label.
     *
     * @param aspect The aspect to be investigated
     */
    private static void verifyDynamicAspect(Aspect<String> aspect) {
        assertThat(aspect.getName()).isEqualTo(NAME);
        assertThat(aspect.isValuePresent()).isFalse();
    }

    /**
     * Verifies an aspect which represents a static label.
     *
     * @param aspect The aspect to be investigated
     */
    private static void verifyStaticAspect(Aspect<String> aspect) {
        assertThat(aspect.getName()).isEqualTo(NAME);
        assertThat(aspect.getValue()).isEqualTo(LABEL);
    }
}

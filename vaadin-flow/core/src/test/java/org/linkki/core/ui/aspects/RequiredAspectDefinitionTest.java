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

import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class RequiredAspectDefinitionTest {

    @Test
    void testCreateComponentValueSetter_NotRequired_NotHasValue() {
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.NOT_REQUIRED, new EnabledAspectDefinition(EnabledType.ENABLED));
        var component = ComponentFactory.newButton();
        var componentWrapper = new NoLabelComponentWrapper(component);

        var componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        assertThatNoException().isThrownBy(() -> componentValueSetter.accept(true));
    }

    @Test
    void testCreateComponentValueSetter_HasValue() {
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.REQUIRED, new EnabledAspectDefinition(EnabledType.ENABLED));
        var component = new TextField();
        var componentWrapper = new NoLabelComponentWrapper(component);
        assertThat(component.isRequiredIndicatorVisible()).describedAs("Required indicator is false by default")
                .isFalse();

        var componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);
        componentValueSetter.accept(true);

        assertThat(component.isRequiredIndicatorVisible()).describedAs("Component value setter sets the required indicator")
                .isTrue();
    }

    @Test
    void testCreateComponentValueSetter_ListBox() {
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.REQUIRED, new EnabledAspectDefinition(EnabledType.ENABLED));
        var component = new ListBox<>();
        var componentWrapper = new NoLabelComponentWrapper(component);

        var componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        assertThatNoException().isThrownBy(() -> componentValueSetter.accept(true));
    }

    @Test
    void testCreateAspect_Required() {
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.REQUIRED, new EnabledAspectDefinition(EnabledType.ENABLED));

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(RequiredAspectDefinition.NAME);
        assertThat(aspect.getValue()).isTrue();
    }

    @Test
    void testCreateAspect_NotRequired() {
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.NOT_REQUIRED, new EnabledAspectDefinition(EnabledType.ENABLED));

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(RequiredAspectDefinition.NAME);
        assertThat(aspect.getValue()).isFalse();
    }

    @Test
    void testCreateAspect_RequiredIfEnabled_Enabled() {
        var enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.ENABLED);
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.REQUIRED_IF_ENABLED, enabledAspectDefinition);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(enabledAspectDefinition.createAspect().getName());
        assertThat(aspect.getValue()).isEqualTo(enabledAspectDefinition.createAspect().getValue());
    }

    @Test
    void testCreateAspect_RequiredIfEnabled_Disabled() {
        var enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.DISABLED);
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.REQUIRED_IF_ENABLED, enabledAspectDefinition);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(enabledAspectDefinition.createAspect().getName());
        assertThat(aspect.getValue()).isEqualTo(enabledAspectDefinition.createAspect().getValue());
    }

    @Test
    void testCreateAspect_Dynamic() {
        var enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.DISABLED);
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.DYNAMIC, enabledAspectDefinition);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(RequiredAspectDefinition.NAME);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(aspect::getValue);
    }

    @Test
    void testHandleNullValue() {
        var enabledAspectDefinition = new EnabledAspectDefinition(EnabledType.DISABLED);
        var aspectDefinition = new RequiredAspectDefinition(RequiredType.DYNAMIC, enabledAspectDefinition);
        var component = new TextField();
        component.setRequiredIndicatorVisible(true);
        var componentWrapper = new NoLabelComponentWrapper(component);

        aspectDefinition.handleNullValue(aspectDefinition.createComponentValueSetter(componentWrapper), componentWrapper);

        assertThat(component.isRequiredIndicatorVisible()).as("The required indicator defaulted to false").isFalse();
    }
}

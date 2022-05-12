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

package org.linkki.core.binding;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.ObjectProperty;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;
import org.linkki.core.defaults.section.TestSectionBuilder;
import org.linkki.core.defaults.section.TestSectionPmo;

class BindingIntegrationTest {

    @Test
    void testBinding() {
        TestSectionPmo testSectionPmo = new TestSectionPmo();
        testSectionPmo.setId("TestId");
        TestModelObject modelObject = (TestModelObject)testSectionPmo.getModelObject();

        Message pmoValueMessage = Message.builder("foo", Severity.WARNING).code("bar")
                .invalidObjects(new ObjectProperty(testSectionPmo, TestSectionPmo.PROPERTY_VALUE)).create();
        Message modelPropMessage = Message.builder("LoremIpsum", Severity.INFO).code("baz")
                .invalidObjects(new ObjectProperty(modelObject, TestModelObject.PROPERTY_MODEL_PROP))
                .create();
        DefaultBindingManager bindingManager = new DefaultBindingManager(
                () -> new MessageList(pmoValueMessage, modelPropMessage));
        BindingContext bindingContext = bindingManager.getContext(TestSectionPmo.class);
        TestButtonPmo editButtonPmo = new TestButtonPmo();
        testSectionPmo.setEditButtonPmo(editButtonPmo);

        TestUiLayoutComponent section = TestSectionBuilder.createSection(testSectionPmo, bindingContext);

        assertThat(section.getChildren()).hasSize(3);

        List<String> childIds = section.getChildren().stream().map(TestUiComponent::getId).collect(Collectors.toList());
        assertThat(childIds).contains("editButton", TestPmo.PROPERTY_VALUE, TestModelObject.PROPERTY_MODEL_PROP);

        // UI->PMO
        TestUiComponent editButton = section.getChildren().get(0);
        assertThat(editButtonPmo.getClickCount()).isEqualTo(0);
        editButton.click();
        assertThat(editButtonPmo.getClickCount()).isEqualTo(1);

        // PMO->UI
        TestUiComponent valueComponent = section.getChildren().get(1);
        assertThat(valueComponent.isEnabled()).isTrue();
        testSectionPmo.setValueEnabled(false);
        bindingContext.modelChanged();
        assertThat(valueComponent.isEnabled()).isFalse();

        // PMO->UI for model property
        TestUiComponent modelPropComponent = section.getChildren().get(2);
        assertThat(modelPropComponent.isEnabled()).isTrue();
        testSectionPmo.setModelPropEnabled(false);
        bindingContext.modelChanged();
        assertThat(modelPropComponent.isEnabled()).isFalse();

        // MO->UI for model property
        assertThat(modelPropComponent.isVisible()).isTrue();
        modelObject.setModelPropVisible(false);
        bindingContext.modelChanged();
        assertThat(modelPropComponent.isVisible()).isFalse();

        // Aspect
        assertThat(valueComponent.getTooltipText()).isEqualTo("abc");
        testSectionPmo.setValueTooltip("foo");
        bindingContext.modelChanged();
        assertThat(valueComponent.getTooltipText()).isEqualTo("foo");

        // Messages
        assertThat(valueComponent.getValidationMessages()).hasSize(1);
        assertThat(valueComponent.getValidationMessages().getMessage(0))
                .isEqualTo(pmoValueMessage);
        assertThat(modelPropComponent.getValidationMessages()).hasSize(1);
        assertThat(modelPropComponent.getValidationMessages().getMessage(0))
                .isEqualTo(modelPropMessage);
    }

}

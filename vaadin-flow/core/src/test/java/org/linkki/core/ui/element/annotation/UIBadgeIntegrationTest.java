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

package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.vaadin.component.BadgeSeverity;

class UIBadgeIntegrationTest {

    @Test
    void testBadges() {
        var pmo = new TestPmo(new TestModelObject("Badge_Status"));

        var component = VaadinUiCreator.createComponent(pmo, new BindingContext());
        var badges = component.getChildren().toList();

        assertThat(badges)
                .map(b -> b.getElement().getTextRecursively())
                .containsExactly("Badge_Status", "Caption", "123", "DEFAULT");
        assertThat(badges.getFirst().getElement().getThemeList())
                .containsExactlyInAnyOrder("badge", BadgeVariant.SMALL, BadgeVariant.PRIMARY);
        assertThat(badges.get(1).getElement().getThemeList())
                .containsExactlyInAnyOrder("badge", BadgeSeverity.NONE.getVariantName(), BadgeVariant.PILL);
        assertThat(badges.get(2).getElement().getThemeList())
                .containsExactlyInAnyOrder("badge", BadgeSeverity.ERROR.getVariantName(), BadgeVariant.PILL);
        assertThat(badges.getLast().getElement().getThemeList())
                .containsExactlyInAnyOrder("badge", BadgeSeverity.WARNING.getVariantName(), BadgeVariant.PRIMARY,
                                           BadgeVariant.SMALL);
    }

    @Test
    void testBadges_ModelChanged() {
        var binding = new BindingContext();
        var modelObject = new TestModelObject("Init_Status");
        var pmo = new TestPmo(modelObject);

        var component = VaadinUiCreator.createComponent(pmo, binding);

        assertThat(component.getChildren().toList().getFirst().getElement().getTextRecursively())
                .isEqualTo("Init_Status");

        modelObject.setStatus("Update_Status");
        binding.modelChanged();

        assertThat(component.getChildren().toList().getFirst().getElement().getTextRecursively())
                .isEqualTo("Update_Status");
    }

    @UICssLayout
    static class TestPmo {

        private final TestModelObject modelObject;

        public TestPmo(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        @ModelObject
        public TestModelObject getModelObject() {
            return modelObject;
        }

        @UIBadge(position = 0, severity = BadgeSeverity.INFO, variants = { BadgeVariant.SMALL, BadgeVariant.PRIMARY })
        public void status() {
            // model binding
        }

        @UIBadge(position = 10)
        public String getMinimalBadge() {
            return "Caption";
        }

        @UIBadge(position = 20, severity = BadgeSeverity.ERROR)
        public int getIntegerBadge() {
            return 123;
        }

        @UIBadge(position = 30, severity = BadgeSeverity.WARNING,
                variants = { BadgeVariant.PRIMARY, BadgeVariant.SMALL })
        public TestEnum getEnumBadge() {
            return TestEnum.DEFAULT;
        }
    }

    static class TestModelObject {

        private String status;

        TestModelObject(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    enum TestEnum {
        DEFAULT
    }
}
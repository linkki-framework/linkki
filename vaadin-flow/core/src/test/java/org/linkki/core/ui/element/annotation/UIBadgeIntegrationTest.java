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
import org.linkki.core.ui.aspects.annotation.BindBadgeSeverity;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.vaadin.component.BadgeSeverity;
import org.linkki.core.vaadin.component.BadgeStyleVariant;

import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.icon.VaadinIcon;

class UIBadgeIntegrationTest {

    @Test
    void testBadges() {
        var pmo = new TestPmo(new TestModelObject("Badge_Status"));

        var component = VaadinUiCreator.createComponent(pmo, new BindingContext());
        var badges = component.getChildren().toList();

        assertThat(badges)
                .map(b -> b.getElement().getTextRecursively())
                .containsExactly("Badge_Status", "Caption", "123", "DEFAULT");
        assertThat(badges).allMatch(Badge.class::isInstance);
        assertThat(badges.getFirst().getElement().getThemeList())
                .containsExactlyInAnyOrder(BadgeStyleVariant.SMALL, BadgeStyleVariant.FILLED);
        assertThat(badges.get(1).getElement().getThemeList())
                .containsExactly(BadgeSeverity.NONE.getVariantName());
        assertThat(badges.get(2).getElement().getThemeList())
                .containsExactly(BadgeSeverity.ERROR.getVariantName());
        assertThat(badges.getLast().getElement().getThemeList())
                .containsExactlyInAnyOrder(BadgeSeverity.WARNING.getVariantName(),
                        BadgeStyleVariant.FILLED, BadgeStyleVariant.SMALL);
    }

    @Test
    void testBadge_NoStylesByDefault() {
        var component = VaadinUiCreator.createComponent(new NullValuePmo(), new BindingContext());

        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getElement().getThemeList()).containsExactly(BadgeSeverity.NONE.getVariantName());
    }

    @Test
    void testBadge_SquareStyle() {
        var component = VaadinUiCreator.createComponent(new SquarePmo(), new BindingContext());

        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getElement().getThemeList())
                .containsExactlyInAnyOrder(BadgeStyleVariant.SQUARE, BadgeSeverity.SUCCESS.getVariantName());
    }

    @Test
    void testBadge_DotStyle() {
        var component = VaadinUiCreator.createComponent(new DotPmo(), new BindingContext());

        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getElement().getThemeList())
                .containsExactlyInAnyOrder(BadgeStyleVariant.DOT, BadgeSeverity.ERROR.getVariantName());
    }

    @Test
    void testBadge_DynamicSeverity_KeepsStyles() {
        var binding = new BindingContext();
        var pmo = new DynamicSeverityWithStylePmo();
        var component = VaadinUiCreator.createComponent(pmo, binding);
        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        pmo.severity = BadgeSeverity.ERROR;
        binding.modelChanged();

        assertThat(badge.getElement().getThemeList())
                .containsExactlyInAnyOrder(BadgeStyleVariant.DOT, BadgeSeverity.ERROR.getVariantName());
    }

    @Test
    void testBadge_NoLinkkiSpecificClassName() {
        var component = VaadinUiCreator.createComponent(new NullValuePmo(), new BindingContext());

        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getClassNames()).isEmpty();
    }

    @Test
    void testBadge_WithIcon() {
        var component = VaadinUiCreator.createComponent(new IconPmo(), new BindingContext());

        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getText()).isEqualTo("With icon");
        assertThat(badge.getIcon()).isNotNull();
    }

    @Test
    void testBadge_NullValue() {
        var component = VaadinUiCreator.createComponent(new NullValuePmo(), new BindingContext());

        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getText()).isEmpty();
    }

    @Test
    void testBadge_DynamicSeverity() {
        var binding = new BindingContext();
        var pmo = new DynamicSeverityPmo();
        var component = VaadinUiCreator.createComponent(pmo, binding);
        var badge = (Badge)component.getChildren().findFirst().orElseThrow();

        assertThat(badge.getElement().getThemeList())
                .containsExactly(BadgeSeverity.SUCCESS.getVariantName());

        pmo.severity = BadgeSeverity.ERROR;
        binding.modelChanged();

        assertThat(badge.getElement().getThemeList())
                .containsExactly(BadgeSeverity.ERROR.getVariantName());
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

        @UIBadge(position = 0, severity = BadgeSeverity.INFO, styles = {BadgeStyleVariant.SMALL, BadgeStyleVariant.FILLED})
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
                styles = {BadgeStyleVariant.FILLED, BadgeStyleVariant.SMALL})
        public TestEnum getEnumBadge() {
            return TestEnum.DEFAULT;
        }
    }

    @UICssLayout
    static class IconPmo {

        @BindIcon(VaadinIcon.LOCK)
        @UIBadge(position = 0)
        public String getBadge() {
            return "With icon";
        }
    }

    @UICssLayout
    static class SquarePmo {

        @UIBadge(position = 0, severity = BadgeSeverity.SUCCESS, styles = BadgeStyleVariant.SQUARE)
        public String getBadge() {
            return "Square";
        }
    }

    @UICssLayout
    static class DotPmo {

        @UIBadge(position = 0, severity = BadgeSeverity.ERROR, styles = BadgeStyleVariant.DOT)
        public String getBadge() {
            return "Dot";
        }
    }

    @UICssLayout
    static class DynamicSeverityWithStylePmo {

        private BadgeSeverity severity = BadgeSeverity.SUCCESS;

        @BindBadgeSeverity
        @UIBadge(position = 0, styles = BadgeStyleVariant.DOT)
        public String getBadge() {
            return "Dynamic";
        }

        public BadgeSeverity getBadgeBadgeSeverity() {
            return severity;
        }
    }

    @UICssLayout
    static class NullValuePmo {

        @UIBadge(position = 0)
        public String getBadge() {
            return null;
        }
    }

    @UICssLayout
    static class DynamicSeverityPmo {

        private BadgeSeverity severity = BadgeSeverity.SUCCESS;

        @BindBadgeSeverity
        @UIBadge(position = 0)
        public String getBadge() {
            return "Dynamic";
        }

        public BadgeSeverity getBadgeBadgeSeverity() {
            return severity;
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
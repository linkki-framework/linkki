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

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UITimeFieldIntegrationTest.TimeFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils;

import com.vaadin.flow.component.timepicker.TimePicker;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UITimeFieldIntegrationTest extends FieldAnnotationIntegrationTest<TimePicker, TimeFieldTestPmo> {

    private static final LocalTime USER_INPUT_TIME = LocalTime.of(1, 10, 10);
    private static final LocalTime STATIC_TIME = LocalTime.of(2, 20, 20);

    UITimeFieldIntegrationTest() {
        super(TestModelObjectWithLocalTime::new, TimeFieldTestPmo::new);
    }

    @Test
    void testTimeFieldWithoutPrecision() {
        var modelObject = new TestModelObjectWithLocalTime();
        var timeFieldPmo = createFirstComponent(modelObject);

        assertThat(timeFieldPmo.getStep()).isEqualTo(Duration.ofMinutes(60));

        KaribuUtils.Fields.setValue(timeFieldPmo, USER_INPUT_TIME);
        assertThat(modelObject.getValue()).isEqualTo(USER_INPUT_TIME);
        KaribuUtils.Fields.setValue(timeFieldPmo, null);
        assertThat(modelObject.getValue()).isNull();
    }

    @Test
    void testTimeFieldWithSecondsPrecision() {
        var modelObject = getDefaultModelObject();
        var timeFieldPmo = getComponentById("valueSeconds");

        assertThat(timeFieldPmo.getStep()).isEqualTo(Duration.ofSeconds(30));

        KaribuUtils.Fields.setValue(timeFieldPmo, USER_INPUT_TIME);
        assertThat(modelObject.getValueSeconds()).isEqualTo(USER_INPUT_TIME);
        KaribuUtils.Fields.setValue(timeFieldPmo, null);
        assertThat(modelObject.getValueSeconds()).isNull();
    }

    @Test
    void testTimeFieldWithMinutesPrecision() {
        var modelObject = getDefaultModelObject();
        var timeFieldPmo = getComponentById("valueMinutes");

        assertThat(timeFieldPmo.getStep()).isEqualTo(Duration.ofMinutes(30));

        KaribuUtils.Fields.setValue(timeFieldPmo, USER_INPUT_TIME);
        assertThat(modelObject.getValueMinutes()).isEqualTo(USER_INPUT_TIME);
        KaribuUtils.Fields.setValue(timeFieldPmo, null);
        assertThat(modelObject.getValueMinutes()).isNull();
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        var timeField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();

        assertThat(timeField.isRequiredIndicatorVisible()).isTrue();
        KaribuUtils.Fields.setValue(timeField, USER_INPUT_TIME);
        assertThat(getDefaultModelObject().getValue()).isEqualTo(USER_INPUT_TIME);
        KaribuUtils.Fields.setValue(timeField, null);
        assertThat(getDefaultModelObject().getValue()).isNull();
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2)).isEqualTo("Foo");
    }

    @Override
    protected UITimeFieldIntegrationTest.TestModelObjectWithLocalTime getDefaultModelObject() {
        return (UITimeFieldIntegrationTest.TestModelObjectWithLocalTime)super.getDefaultModelObject();
    }

    @UISection
    protected static class TimeFieldTestPmo extends AnnotationTestPmo {

        public TimeFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UITimeField(position = 1,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                visible = VisibleType.DYNAMIC)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UITimeField(position = 2,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED,
                visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UITimeField(position = 3)
        public LocalTime getFoo() {
            return LocalTime.now();
        }

        @UITimeField(position = 4, step = 30, precision = ChronoUnit.SECONDS)
        public void valueSeconds() {
            // model binding
        }

        @UITimeField(position = 5, step = 30, precision = ChronoUnit.MINUTES)
        public void valueMinutes() {
            // model binding
        }
    }

    protected static class TestModelObjectWithLocalTime {

        @CheckForNull
        private LocalTime value = null;

        @CheckForNull
        private LocalTime valueSeconds = null;

        @CheckForNull
        private LocalTime valueMinutes = null;

        @CheckForNull
        public LocalTime getStaticValue() {
            return STATIC_TIME;
        }

        @CheckForNull
        public LocalTime getValue() {
            return value;
        }

        public void setValue(@CheckForNull LocalTime value) {
            this.value = value;
        }

        public LocalTime getValueSeconds() {
            return valueSeconds;
        }

        public void setValueSeconds(LocalTime valueSeconds) {
            this.valueSeconds = valueSeconds;
        }

        public LocalTime getValueMinutes() {
            return valueMinutes;
        }

        public void setValueMinutes(LocalTime valueMinutes) {
            this.valueMinutes = valueMinutes;
        }

    }
}

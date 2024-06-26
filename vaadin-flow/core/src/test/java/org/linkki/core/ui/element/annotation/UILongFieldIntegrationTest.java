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

import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UILongFieldIntegrationTest.LongFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.uiframework.UiFramework;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class UILongFieldIntegrationTest extends FieldAnnotationIntegrationTest<TextField, LongFieldTestPmo> {

    private static final String FANCY_FORMAT = "###,###.###";

    private static final int MAX_LENGTH = 8;

    private final NumberFormat formatter;

    UILongFieldIntegrationTest() {

        super(TestModelObjectWithObjectLong::new, LongFieldTestPmo::new);
        formatter = new DecimalFormat(FANCY_FORMAT, DecimalFormatSymbols.getInstance(UiFramework.getLocale()));

    }

    @Test
    void testSetValue_WithPrimitiveLongInModelObject() {

        TestModelObjectWithPrimitiveLong object = new TestModelObjectWithPrimitiveLong();
        TextField textField = createFirstComponent(object);

        assertThat(textField.getMaxLength()).isEqualTo(MAX_LENGTH);
        assertThat(textField.getValue()).isEqualTo(formatter.format(0));

        KaribuUtils.Fields.setValue(textField, formatter.format(1));
        assertThat(object.getValue()).isEqualTo(1L);

        object.setValue(2000000L);
        getBindingContext().modelChanged();
        assertThat(textField.getValue()).isEqualTo(formatter.format(2000000));

    }

    @Test
    void testSetValue_WithPrimitiveLongInModelObject_RevertsForNull() {

        TestModelObjectWithPrimitiveLong object = new TestModelObjectWithPrimitiveLong();
        TextField textField = createFirstComponent(object);
        KaribuUtils.Fields.setValue(textField, formatter.format(1));
        assertThat(textField.getValue()).isEqualTo(formatter.format(1));

        KaribuUtils.Fields.setValue(textField, "");

        assertThat(textField.getValue()).isEqualTo(formatter.format(1));

    }

    @Test
    void testSetValue_WithObjectLongInModelObject() {

        TestModelObjectWithObjectLong object = new TestModelObjectWithObjectLong();
        TextField textField = createFirstComponent(object);

        assertThat(textField.getValue()).isBlank();

        KaribuUtils.Fields.setValue(textField, formatter.format(1));
        assertThat(object.getValue()).isEqualTo(1L);

        object.setValue(100L);
        getBindingContext().modelChanged();
        assertThat(textField.getValue()).isEqualTo(formatter.format(100));

        KaribuUtils.Fields.setValue(textField, textField.getEmptyValue());
        assertThat(getDefaultModelObject().getValue()).isNull();
    }

    @Test
    @Override
    void testNullInputIfRequired() {

        TextField textField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(textField.isRequiredIndicatorVisible()).isTrue();

        KaribuUtils.Fields.setValue(textField, formatter.format(1));
        assertThat(getDefaultModelObject().getValue()).isEqualTo(1L);

        KaribuUtils.Fields.setValue(textField, textField.getEmptyValue());
        assertThat(getDefaultModelObject().getValue()).isNull();

    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2)).isEqualTo("Foo");
    }

    @Override
    protected TestModelObjectWithObjectLong getDefaultModelObject() {

        return (TestModelObjectWithObjectLong) super.getDefaultModelObject();

    }

    protected static class TestModelObjectWithObjectLong extends TestModelObject<Long> {

        @CheckForNull
        private Long value = null;

        @CheckForNull
        @Override
        public Long getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Long value) {
            this.value = value;
        }

    }

    protected static class TestModelObjectWithPrimitiveLong {

        private long value = 0L;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public long getStaticValue() {
            return value;
        }

    }


    @UISection
    protected static class LongFieldTestPmo extends AnnotationTestPmo {

        public LongFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @BindVisible
        @UILongField(
                position = 1,
                label = "",
                enabled = EnabledType.DYNAMIC,
                required = RequiredType.DYNAMIC,
                format = FANCY_FORMAT,
                maxLength = MAX_LENGTH
        )
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @BindVisible
        @UILongField(
                position = 2,
                label = TEST_LABEL,
                enabled = EnabledType.DISABLED,
                required = RequiredType.REQUIRED
        )
        public void staticValue() {
            // model binding
        }

        @UILongField(position = 3)
        public long getFoo() {
            return 42L;
        }

        public boolean isStaticValueVisible() {
            return false;
        }

        public boolean isVisible() {
            return true;
        }

    }
}

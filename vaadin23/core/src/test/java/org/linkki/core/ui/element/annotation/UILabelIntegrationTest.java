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
package org.linkki.core.ui.element.annotation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.ui.element.annotation.UILabelIntegrationTest.LabelTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.base.LinkkiText;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UILabelIntegrationTest extends ComponentAnnotationIntegrationTest<LinkkiText, LabelTestPmo> {

    private static final String STYLES = "blabla";

    UILabelIntegrationTest() {
        super(TestModelObjectWithString::new, LabelTestPmo::new);
    }

    @Test
    void testLabelFieldValue() {
        LinkkiText label = getDynamicComponent();

        assertThat(label.getClassName(), containsString(STYLES));
        assertThat(label.getText(), is(""));

        ((TestModelObjectWithString)getDefaultModelObject()).setValue("fdsa");
        modelChanged();
        // htmlContent = true
        assertThat(label.getText(), is("fdsa"));
    }

    @Test
    void testLabelFieldValue_Integer_UsesConverter() {
        setModelObjectSupplier(TestModelObjectWithInteger::new);
        LinkkiText label = getDynamicComponent();

        assertThat(label.getClassName(), containsString(STYLES));
        assertThat(label.getText(), is(""));

        ((TestModelObjectWithInteger)getDefaultModelObject()).setValue(123456);
        modelChanged();
        // htmlContent = true
        assertThat(label.getText(), is("123.456"));
    }

    @Test
    void testEnabled() {
        assertThat(getStaticComponent().getElement().isEnabled(), is(true));
        assertThat(getDynamicComponent().getElement().isEnabled(), is(true));
    }

    @Test
    void testLocalDateUsesConverter() {
        assertThat(getComponentById("localDate").getText(), is("06.05.1234"));
    }

    @Test
    void testLocalDateTimeUsesConverter() {
        assertThat(getComponentById("localDateTime").getText(), is("06.05.1234 07:08"));
    }

    @Test
    void testUnnamedEnumUsesToString() {
        assertThat(getComponentById("enum").getText(), is("FLOOR"));
    }

    @Test
    void testNamedEnumUsesGetName() {
        assertThat(getComponentById("namedEnum").getText(), is("name"));
    }

    @Test
    void testNamedObjectUsesGetName() {
        assertThat(getComponentById("namedObject").getText(), is("name"));
    }

    @Test
    void testNamedEnumtWithToStringCaptionProviderUsesToString() {
        assertThat(getComponentById("namedEnumWithToStringCaptionProvider").getText(), is("VALUE"));
    }

    @UISection
    protected static class LabelTestPmo extends AnnotationTestPmo {

        public LabelTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @UILabel(position = 1, visible = VisibleType.DYNAMIC, htmlContent = true, styleNames = STYLES)
        public void value() {
            // model binding
        }

        @Override
        @UILabel(position = 2, label = TEST_LABEL, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        // just have some further labels and check that the section could be created

        @UILabel(position = 3)
        public BigDecimal getOtherTyp() {
            return new BigDecimal("123");
        }

        @UILabel(position = 4)
        public int getInt() {
            return 1231234;
        }

        @UILabel(position = 5)
        public LocalDate getLocalDate() {
            return LocalDate.of(1234, 5, 6);
        }

        @UILabel(position = 6)
        public LocalDateTime getLocalDateTime() {
            return LocalDateTime.of(1234, 5, 6, 7, 8, 9);
        }

        @UILabel(position = 7)
        public RoundingMode getEnum() {
            return RoundingMode.FLOOR;
        }

        @UILabel(position = 8)
        public NamedEnum getNamedEnum() {
            return NamedEnum.VALUE;
        }

        @UILabel(position = 9)
        public NamedObject getNamedObject() {
            return new NamedObject();
        }

        @UILabel(position = 10, itemCaptionProvider = ToStringCaptionProvider.class)
        public NamedEnum getNamedEnumWithToStringCaptionProvider() {
            return NamedEnum.VALUE;
        }
    }

    protected static class TestModelObjectWithString extends TestModelObject<String> {

        @CheckForNull
        private String value = null;

        @CheckForNull
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull String value) {
            this.value = value;
        }
    }

    protected static class TestModelObjectWithInteger extends TestModelObject<Integer> {

        @CheckForNull
        private Integer value = null;

        @CheckForNull
        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public void setValue(@CheckForNull Integer value) {
            this.value = value;
        }
    }

    public static enum NamedEnum {
        VALUE;

        public String getName() {
            return "name";
        }
    }

    public static class NamedObject {

        public String getName() {
            return "name";
        }

        @Override
        public String toString() {
            return "toString";
        }
    }
}

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UITextFieldIntegrationTest.TextFieldTestPmo;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class UITextFieldIntegrationTest extends FieldAnnotationIntegrationTest<TextField, TextFieldTestPmo> {

    private static final String WIDTH = "42em";
    private static final int MAX_LENGTH = 8;

    UITextFieldIntegrationTest() {
        super(TestModelObjectWithString::new, TextFieldTestPmo::new);
    }

    @Test
    void testTextVieldValue() {
        TestModelObjectWithString modelObject = new TestModelObjectWithString();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getMaxLength(), is(MAX_LENGTH));
        assertThat(textField.getWidth(), is("42em"));
        assertThat(textField.getValue(), is(""));

        TestUiUtil.setUserOriginatedValue(textField, "asdf");
        assertThat(modelObject.getValue(), is("asdf"));

        modelObject.setValue("fdsa");
        getBindingContext().modelChanged();
        assertThat(textField.getValue(), is("fdsa"));

        // clearing textfield results in ""
        TestUiUtil.setUserOriginatedValue(textField, textField.getEmptyValue());
        assertThat(modelObject.getValue(), is(emptyString()));
    }

    @Test
    @Override
    void testNullInputIfRequired() {
        TextField textField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(textField.isRequiredIndicatorVisible(), is(true));

        TestUiUtil.setUserOriginatedValue(textField, "something");
        assertThat(getDefaultModelObject().getValue(), is("something"));

        // clearing textfield results in ""
        TestUiUtil.setUserOriginatedValue(textField, textField.getEmptyValue());
        assertThat(getDefaultModelObject().getValue(), is(emptyString()));
    }

    @Test
    void testDerivedLabel() {
        assertThat(TestUiUtil.getLabelOfComponentAt(getDefaultSection(), 2), is("Foo"));
    }

    @Override
    protected TestModelObjectWithString getDefaultModelObject() {
        return (TestModelObjectWithString)super.getDefaultModelObject();
    }

    @UISection
    protected static class TextFieldTestPmo extends AnnotationTestPmo {

        public TextFieldTestPmo(Object modelObject) {
            super(modelObject);
        }

        @Override
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        @UITextField(position = 1, label = "", enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, width = WIDTH, maxLength = MAX_LENGTH)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(TEST_TOOLTIP)
        @UITextField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

        @UITextField(position = 3)
        public String getFoo() {
            return "bar";
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
}

/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.UITextFieldIntegrationTest.TextFieldTestPmo;

import com.vaadin.ui.TextField;

public class UITextFieldIntegrationTest extends FieldAnnotationIntegrationTest<TextField, TextFieldTestPmo> {

    private static final int COLUMNS = 42;
    private static final int MAX_LENGTH = 8;

    public UITextFieldIntegrationTest() {
        super(TestModelObjectWithString::new, TextFieldTestPmo::new);
    }

    @Test
    public void testTextVieldValue() {
        TestModelObjectWithString modelObject = new TestModelObjectWithString();
        TextField textField = createFirstComponent(modelObject);

        assertThat(textField.getMaxLength(), is(MAX_LENGTH));
        assertThat(textField.getColumns(), is(COLUMNS));
        assertThat(textField.getValue(), is(nullValue()));

        textField.setValue("asdf");
        assertThat(modelObject.getValue(), is("asdf"));

        modelObject.setValue("fdsa");
        // updateUi(); not needed at the moment
        assertThat(textField.getValue(), is("fdsa"));

        textField.setValue(null);
        assertThat(modelObject.getValue(), is(nullValue()));
    }

    @Test
    @Override
    public void testNullInputIfRequired() {
        TextField textField = getDynamicComponent();
        getDefaultPmo().setRequired(true);
        modelChanged();
        assertThat(textField.isRequired(), is(true));

        textField.setValue("something");
        assertThat(getDefaultModelObject().getValue(), is("something"));

        textField.setValue(null);
        assertThat(getDefaultModelObject().getValue(), is(nullValue()));
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
        @BindTooltip(tooltipType = BindTooltipType.DYNAMIC)
        @UITextField(position = 1, noLabel = true, enabled = EnabledType.DYNAMIC, required = RequiredType.DYNAMIC, visible = VisibleType.DYNAMIC, columns = COLUMNS, maxLength = MAX_LENGTH)
        public void value() {
            // model binding
        }

        @Override
        @BindTooltip(text = TEST_TOOLTIP)
        @UITextField(position = 2, label = TEST_LABEL, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED, visible = VisibleType.INVISIBLE)
        public void staticValue() {
            // model binding
        }

    }

    protected static class TestModelObjectWithString extends TestModelObject<String> {

        @Nullable
        private String value = null;

        @SuppressWarnings("null")
        @CheckForNull
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(@Nullable String value) {
            this.value = value;
        }
    }
}

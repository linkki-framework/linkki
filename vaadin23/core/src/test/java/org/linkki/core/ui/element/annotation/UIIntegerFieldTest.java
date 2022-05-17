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
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.VaadinUIExtension;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@ExtendWith(VaadinUIExtension.class)
class UIIntegerFieldTest {

    @Test
    void testSetValue_WithPrimitiveIntegerInModelObject() {
        TextField textField = createIntegerTextField(new TestModelObjectWithPrimitiveInteger());
        Assertions.assertDoesNotThrow(() -> {
            TestUiUtil.setUserOriginatedValue(textField, "0");
        });
    }

    @Test
    void testSetValue_WithObjectIntegerInModelObject() {
        TextField textField = createIntegerTextField(new TestModelObjectWithObjectInteger());

        Assertions.assertDoesNotThrow(() -> {
            TestUiUtil.setUserOriginatedValue(textField, "");
            TestUiUtil.setUserOriginatedValue(textField, "0");
        });
    }

    @Test
    void testSetValue_WithThousandSeparatorOnPrimitiveInteger_DE() {
        UI.getCurrent().setLocale(Locale.GERMAN);
        TextField textField = createIntegerTextField(new TestModelObjectWithPrimitiveInteger());

        TestUiUtil.setUserOriginatedValue(textField, "98765432");

        assertThat(textField.getValue(), is("98.765.432"));
    }

    @Test
    void testSetValue_WithThousandSeparatorOnPrimitiveInteger_EN() {
        UI.getCurrent().setLocale(Locale.ENGLISH);
        TextField textField = createIntegerTextField(new TestModelObjectWithPrimitiveInteger());

        TestUiUtil.setUserOriginatedValue(textField, "98765432");

        assertThat(textField.getValue(), is("98,765,432"));
    }

    @Test
    void testSetValue_WithThousandSeparatorOnObjectInteger_DE() {
        UI.getCurrent().setLocale(Locale.GERMAN);
        TextField textField = createIntegerTextField(new TestModelObjectWithObjectInteger());

        TestUiUtil.setUserOriginatedValue(textField, "123456");

        assertThat(textField.getValue(), is("123.456"));
    }

    @Test
    void testSetValue_WithThousandSeparatorOnObjectInteger_EN() {
        UI.getCurrent().setLocale(Locale.ENGLISH);
        TextField textField = createIntegerTextField(new TestModelObjectWithObjectInteger());

        TestUiUtil.setUserOriginatedValue(textField, "123456");

        assertThat(textField.getValue(), is("123,456"));
    }

    protected static class TestModelObjectWithPrimitiveInteger {

        private int value = 0;

        public int getIntegerValue() {
            return value;
        }

        public void setIntegerValue(int i) {
            this.value = i;
        }
    }

    protected static class TestModelObjectWithObjectInteger {

        @CheckForNull
        private Integer value = null;

        @CheckForNull
        public Integer getIntegerValue() {
            return value;
        }

        public void setIntegerValue(Integer i) {
            this.value = i;
        }
    }

    @UISection
    protected static class TestPmo {

        private final Object modelObject;

        public TestPmo(Object modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UIIntegerField(position = 1, label = "", modelAttribute = "integerValue")
        public void integerValue() {
            // data binding
        }

        @ModelObject
        public Object getModelObject() {
            return modelObject;
        }
    }

    /**
     * Returns a {@code TextField} that is bound to the given model object using the IPM data binder.
     * The {@code TextField} is part of a mostly mocked UI so that a rudimentary Vaadin environment is
     * in place.
     * 
     * @param modelObject the model object to which the {@code TextField} is bound
     * @return a {@code TextField} that is bound to the model object
     */
    private TextField createIntegerTextField(Object modelObject) {
        TestPmo pmo = new TestPmo(modelObject);
        return (TextField)TestUiUtil.createFirstComponentOf(pmo);
    }
}

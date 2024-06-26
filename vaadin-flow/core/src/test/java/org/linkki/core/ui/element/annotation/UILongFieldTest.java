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

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.TextField;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.test.KaribuUtils;

import java.util.Locale;

@ExtendWith(KaribuUIExtension.class)
public class UILongFieldTest {

    @Test
    void testSetValue_WithPrimitiveLongInModelObject() {
        TextField textField = createLongTextField(new TestModelObjectWithPrimitiveLong());
        Assertions.assertDoesNotThrow(() -> {
            KaribuUtils.Fields.setValue(textField, "0");
        });
    }

    @Test
    void testSetValue_WithObjectLongInModelObject() {
        TextField textField = createLongTextField(new TestModelObjectWithObjectLong());

        Assertions.assertDoesNotThrow(() -> {
            KaribuUtils.Fields.setValue(textField, "");
            KaribuUtils.Fields.setValue(textField, "0");
        });
    }

    @Test
    void testSetValue_WithThousandSeparatorOnPrimitiveLong_DE() {
        UI.getCurrent().setLocale(Locale.GERMAN);
        TextField textField = createLongTextField(new TestModelObjectWithPrimitiveLong());

        KaribuUtils.Fields.setValue(textField, "10987654321");

        assertThat(textField.getValue()).isEqualTo("10.987.654.321");
    }

    @Test
    void testSetValue_WithThousandSeparatorOnPrimitiveLong_EN() {
        UI.getCurrent().setLocale(Locale.ENGLISH);
        TextField textField = createLongTextField(new TestModelObjectWithPrimitiveLong());

        KaribuUtils.Fields.setValue(textField, "10987654321");

        assertThat(textField.getValue()).isEqualTo("10,987,654,321");
    }

    @Test
    void testSetValue_WithThousandSeparatorOnObjectLong_DE() {
        UI.getCurrent().setLocale(Locale.GERMAN);
        TextField textField = createLongTextField(new TestModelObjectWithObjectLong());

        KaribuUtils.Fields.setValue(textField, "12345678910");

        assertThat(textField.getValue()).isEqualTo("12.345.678.910");
    }

    @Test
    void testSetValue_WithThousandSeparatorOnObjectLong_EN() {
        UI.getCurrent().setLocale(Locale.ENGLISH);
        TextField textField = createLongTextField(new TestModelObjectWithObjectLong());

        KaribuUtils.Fields.setValue(textField, "12345678910");

        assertThat(textField.getValue()).isEqualTo("12,345,678,910");
    }

    private TextField createLongTextField(Object modelObject) {
        LongFieldTestPmo longFieldTestPmo = new LongFieldTestPmo(modelObject);
        return (TextField) TestUiUtil.createFirstComponentOf(longFieldTestPmo);
    }

    protected static class TestModelObjectWithObjectLong {

        @CheckForNull
        private Long value = null; // Nicht sicher, ob hier null eingaben soll

        @CheckForNull
        public Long getValue() {
            return value;
        }

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

    }
    
    @UISection
    protected static class LongFieldTestPmo {

        private final Object modelObject;

        public LongFieldTestPmo(Object modelObject) {
            this.modelObject = modelObject;
        }

        @UILongField(
                position = 1,
                label = "",
                modelAttribute = "value"
        )
        public void value() {
            // data binding
        }

        @ModelObject
        public Object getModelObject() {
            return modelObject;
        }

    }
}

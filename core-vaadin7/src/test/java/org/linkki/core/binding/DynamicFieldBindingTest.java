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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Test;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.TestUiUtil;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextArea;
import org.linkki.core.ui.section.annotations.UITextField;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

// TODO LIN-1247 replace vaadin annotations with test annotations and move back to core
public class DynamicFieldBindingTest {

    private static final int POS = 1;


    @Test
    public void testDynamicField_shouldBindToTextField() {

        String value = "value";
        Pmo pmo = new Pmo(new Model(value, false));

        Component component = TestUiUtil.createFirstComponentOf(pmo);
        assertNotNull(component);
        assertTrue(component instanceof TextField);

        TextField txt = (TextField)component;
        assertThat(txt.getValue(), is(value));

        String newValue = "new value";
        txt.setValue(newValue);
        assertThat(pmo.model.paymentMethod, is(newValue));

        pmo.model.paymentMethod = null;
        assertThat(txt.getValue(), is(nullValue()));
    }

    @Test
    public void testDynamicField_shouldBindToComboBox() {
        String value = "semi-annual";
        Pmo pmo = new Pmo(new Model(value, true));

        Component component = TestUiUtil.createFirstComponentOf(pmo);
        assertNotNull(component);
        assertTrue(component instanceof ComboBox);

        ComboBox cb = (ComboBox)component;
        assertThat(cb.getContainerDataSource().getItemIds(), contains(pmo.getPaymentMethodAvailableValues().toArray()));
        assertThat(cb.getValue(), is(value));

        String newValue = "annual";
        cb.setValue(newValue);
        assertThat(pmo.model.paymentMethod, is(newValue));

        pmo.model.paymentMethod = null;
        assertThat(cb.getValue(), is(nullValue()));
    }

    @Test(expected = IllegalStateException.class)
    public void testDynamicField_missingMethod_shouldThrowIllegalStateException() {
        TestUiUtil.createFirstComponentOf(new PmoWithoutMethod());
    }

    @Test(expected = IllegalStateException.class)
    public void testDynamicField_illegalClass_shouldThrowIllegalStateException() {
        TestUiUtil.createFirstComponentOf(new PmoWithWrongClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testDynamicField_inconsistentPmoPropertyNames_shouldThrowIllegalStateException() {
        TestUiUtil.createFirstComponentOf(new PmoWith2MethodsAnnotated());
    }

    @Test(expected = IllegalStateException.class)
    public void testDynamicFiled_inconsistentLabelText_shouldThrowIllegalArgumentException() {
        TestUiUtil.createFirstComponentOf(new PmoWithInconsistentLabelText());
    }


    public static class Model {

        @Nullable
        private String paymentMethod;
        private boolean showComboBox;

        Model(String paymentMethod, boolean showComboBox) {
            this.paymentMethod = paymentMethod;
            this.showComboBox = showComboBox;
        }

        @Nullable
        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public boolean isShowComboBox() {
            return showComboBox;
        }

        public void setShowComboBox(boolean showComboBox) {
            this.showComboBox = showComboBox;
        }
    }

    @UISection
    public static class Pmo {

        private Model model;

        Pmo(Model model) {
            this.model = model;
        }

        @ModelObject
        public Model model() {
            return model;
        }

        @UITextField(position = POS, label = "", modelAttribute = "paymentMethod")
        @UIComboBox(position = POS, label = "", modelAttribute = "paymentMethod", content = AvailableValuesType.DYNAMIC)
        public void paymentMethod() {
            // model binding
        }

        public Class<?> getPaymentMethodComponentType() {
            if (model.isShowComboBox()) {
                return UIComboBox.class;
            }

            return UITextField.class;
        }

        public List<String> getPaymentMethodAvailableValues() {
            return Arrays.asList("annual", "semi-annual", "quarterly", "monthly");
        }
    }

    @UISection
    public static class PmoWithoutMethod {

        @UICheckBox(position = POS, caption = "label")
        @UIButton(position = POS, label = "label")
        public void method() {
            // model binding
        }
    }

    @UISection
    public static class PmoWithWrongClass {

        @UITextArea(position = POS, label = "")
        @UILabel(position = POS)
        public void component() {
            /* model binding */ }

        public Class<?> getComponentComponentType() {
            return String.class;
        }
    }

    @UISection
    public static class PmoWith2MethodsAnnotated {

        @UITextField(position = POS, label = "")
        public String getValueTxt() {
            return "";
        }

        @UIComboBox(position = POS, label = "")
        public String getValueCb() {
            return "";
        }
    }

    @UISection
    public static class PmoWithInconsistentLabelText {

        @UITextField(position = POS, label = "label-one")
        @UIComboBox(position = POS, label = "another label")
        public String getValue() {
            return "";
        }

    }
}

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
package org.linkki.core.ui.bind;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider.ToStringCaptionProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.TestUiUtil;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class DynamicFieldBindingTest {

    private static final int POS = 1;

    @Test
    public void testDynamicField_shouldBindToTextField() {
        String value = "value";
        Pmo pmo = new Pmo(new Model(value, false));
        BindingContext bindingContext = new BindingContext();

        Component component = TestUiUtil.createFirstComponentOf(pmo, bindingContext);
        assertNotNull(component);
        assertThat(component, is(instanceOf(TextField.class)));

        TextField txt = (TextField)component;
        assertThat(txt.getValue(), is(value));

        // TODO LIN-2051
        // String newValue = "new value";
        // TestUiUtil.setUserOriginatedValue(txt, newValue);
        // assertThat(pmo.model.paymentMethod, is(newValue));

        pmo.model.paymentMethod = null;

        bindingContext.modelChanged();
        // because of the ToStringConverter
        assertThat(txt.getValue(), is(""));
    }

    @Test
    public void testDynamicField_shouldBindToComboBox() {
        String value = "semi-annual";
        Pmo pmo = new Pmo(new Model(value, true));
        BindingContext bindingContext = new BindingContext();

        Component component = TestUiUtil.createFirstComponentOf(pmo, bindingContext);
        assertNotNull(component);
        assertThat(component, is(instanceOf(ComboBox.class)));

        @SuppressWarnings("unchecked")
        ComboBox<String> cb = (ComboBox<String>)component;
        // TODO LIN-2051
        // assertThat(TestUiUtil.getData(cb),
        // contains(pmo.getPaymentMethodAvailableValues().toArray()));
        // assertThat(cb.getValue(), is(value));


        // String newValue = "annual";
        // TestUiUtil.setUserOriginatedValue(cb, newValue);
        // assertThat(pmo.model.paymentMethod, is(newValue));

        pmo.model.paymentMethod = null;

        bindingContext.modelChanged();
        assertThat(cb.getValue(), is(nullValue()));
    }

    @Test
    public void testDynamicField_differentModelAttribute() {
        BindingContext bindingContext = new BindingContext();

        PmoWithDifferentModelAttributes pmoWithTextField = new PmoWithDifferentModelAttributes(new Model(),
                UITextField.class);
        Component textField = TestUiUtil.createFirstComponentOf(pmoWithTextField, bindingContext);
        assertNotNull(textField);
        assertThat(textField, is(instanceOf(TextField.class)));

        PmoWithDifferentModelAttributes pmoWithTextArea = new PmoWithDifferentModelAttributes(new Model(),
                UITextArea.class);
        Component textArea = TestUiUtil.createFirstComponentOf(pmoWithTextArea, bindingContext);
        assertNotNull(textArea);
        assertThat(textArea, is(instanceOf(TextArea.class)));
    }

    @Test
    public void testDynamicField_missingMethod_shouldThrowIllegalStateException() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            TestUiUtil.createFirstComponentOf(new PmoWithoutMethod());
        });
    }

    @Test
    public void testDynamicField_illegalClass_shouldThrowIllegalStateException() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            TestUiUtil.createFirstComponentOf(new PmoWithWrongClass());
        });
    }

    @Test
    public void testDynamicField_inconsistentPmoPropertyNames_shouldThrowIllegalStateException() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            TestUiUtil.createFirstComponentOf(new PmoWith2MethodsAnnotated());
        });
    }

    @Test
    public void testDynamicFiled_inconsistentLabelText_shouldThrowIllegalArgumentException() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            TestUiUtil.createFirstComponentOf(new PmoWithInconsistentLabelText());
        });
    }


    public static class Model {

        public static final String PROPERTY_PAYMENT_METHOD = "paymentMethod";
        public static final String PROPERTY_ADDRESS = "address";

        private String paymentMethod;
        private String address;
        private boolean showComboBox;

        Model() {
            this.paymentMethod = "";
            this.address = "";
            this.showComboBox = false;
        }

        Model(String paymentMethod, boolean showComboBox) {
            this.paymentMethod = paymentMethod;
            this.showComboBox = showComboBox;
            this.address = "";
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
        @UIComboBox(position = POS, label = "", modelAttribute = "paymentMethod", content = AvailableValuesType.DYNAMIC, itemCaptionProvider = ToStringCaptionProvider.class)
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

    public static class PmoWithDifferentModelAttributes {

        private Model model;
        private Class<?> componentType;

        PmoWithDifferentModelAttributes(Model model, Class<?> componentType) {
            this.model = model;
            this.componentType = componentType;
        }

        @ModelObject
        public Model getModel() {
            return model;
        }

        @UITextField(position = POS, label = "", modelAttribute = Model.PROPERTY_ADDRESS)
        @UITextArea(position = POS, label = "", modelAttribute = Model.PROPERTY_PAYMENT_METHOD)
        public void addressOrPaymentMethod() {
            // model binding
        }

        public Class<?> getAddressOrPaymentMethodComponentType() {
            return componentType;
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

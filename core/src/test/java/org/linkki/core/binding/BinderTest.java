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
package org.linkki.core.binding;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;

import com.vaadin.ui.Button;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BinderTest {

    private final BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);

    @Test
    public void testSetupBindings() {
        TestView view = new TestView();
        view.initFields();
        TestPmo pmo = new TestPmo();

        // Precondition
        assertThat(pmo.getClickCount(), is(0));

        Binder binder = new Binder(view, pmo);
        BindingContext bindingContext = bindingManager.startNewContext("");
        binder.setupBindings(bindingContext);
        assertThat(bindingContext.getBindings(), hasSize(4));

        // Binding pmo -> view
        assertThat(view.textField.getDescription(), is(TestPmo.TEST_TOOLTIP));
        assertThat(view.numberField.getDescription(), is(emptyString()));
        assertThat(view.button.getDescription(), is(emptyString()));
        assertThat(view.listSelect.getItemIds(), contains("a", "b"));

        pmo.setNumber(13);
        pmo.setText("foo");
        pmo.setToolTip("test tool tip");
        pmo.setSomeothertextValues("c");

        bindingContext.modelChanged();

        assertThat(view.numberField.getValue(), is("13"));
        assertThat(view.textField.getValue(), is("foo"));
        assertThat(view.textField.getDescription(), is(TestPmo.TEST_TOOLTIP));
        assertThat(view.numberField.getDescription(), is("test tool tip"));
        assertThat(view.button.getDescription(), is("test tool tip"));
        assertThat(view.listSelect.getItemIds(), contains("c"));


        // Binding view -> pmo
        view.numberField.setValue("42");
        view.textField.setValue("bar");
        view.button.click();
        view.button.click();
        assertThat(pmo.getNumber(), is(42));
        assertThat(pmo.getText(), is("bar"));
        assertThat(pmo.getClickCount(), is(2));
    }

    @Test
    public void testEnabledAndRequiredTypeBinding() {
        TestView view = new TestView();
        view.initFields();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        BindingContext bindingContext = bindingManager.startNewContext("");
        binder.setupBindings(bindingContext);

        assertThat(view.listSelect.isEnabled(), is(false));
        assertThat(view.listSelect.isRequired(), is(true));

        pmo.setNumberEnabled(false);
        bindingContext.modelChanged();
        assertThat(view.numberField.isEnabled(), is(false));
        assertThat(view.numberField.isRequired(), is(false));
        pmo.setNumberEnabled(true);
        bindingContext.modelChanged();
        assertThat(view.numberField.isEnabled(), is(true));
        assertThat(view.numberField.isRequired(), is(true));

        pmo.setTextRequired(true);
        bindingContext.modelChanged();
        assertThat(view.textField.isRequired(), is(true));
        pmo.setTextRequired(false);
        bindingContext.modelChanged();
        assertThat(view.textField.isRequired(), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testSetupBindings_ThrowsExceptionForNullField() {
        TestView testView = new TestView();
        testView.initNumberField();
        TestPmo pmo = new TestPmo();

        // Precondition
        assertThat(testView.textField, is(nullValue()));

        Binder binder = new Binder(testView, pmo);
        BindingContext ctx = bindingManager.startNewContext("");
        binder.setupBindings(ctx);
    }

    @Test(expected = NullPointerException.class)
    public void testSetupBindings_ThrowsExceptionForMethodReturningNull() {
        TestView testView = new TestView();
        testView.initTextField();
        TestPmo pmo = new TestPmo();

        // Precondition
        assertThat(testView.getNumberField(), is(nullValue()));

        Binder binder = new Binder(testView, pmo);
        BindingContext ctx = bindingManager.startNewContext("");
        binder.setupBindings(ctx);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedNonComponentField() {
        TestViewWithIllegalFieldAnnotation view = new TestViewWithIllegalFieldAnnotation();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingManager.startNewContext(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithParameters() {
        TestViewWithIllegalMethodParamters view = new TestViewWithIllegalMethodParamters();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingManager.startNewContext(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithNonComponentReturnType() {
        TestViewWithIllegalMethodReturnType view = new TestViewWithIllegalMethodReturnType();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingManager.startNewContext(""));
    }

    protected static class TestView {

        @SuppressWarnings("null")
        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT, required = RequiredType.DYNAMIC)
        @UIToolTip(text = TestPmo.TEST_TOOLTIP)
        private TextField textField;

        @Bind(pmoProperty = TestPmo.METHOD_ON_CLICK)
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        private Button button = new Button();

        @Bind(pmoProperty = TestPmo.PROPERTY_SOMEOTHERTEXT, availableValues = AvailableValuesType.DYNAMIC, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED)
        private ListSelect listSelect = new ListSelect();

        @SuppressWarnings("null")
        private IntegerField numberField;

        public void initFields() {
            initTextField();
            initNumberField();
        }

        public void initTextField() {
            textField = new TextField();
        }

        public void initNumberField() {
            numberField = new IntegerField(Locale.CHINESE);
        }

        @Bind(pmoProperty = TestPmo.PROPERTY_NUMBER, enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED_IF_ENABLED)
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        public IntegerField getNumberField() {
            return numberField;
        }
    }

    protected static class TestViewWithIllegalFieldAnnotation extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public Object illegalField = new Object();

    }

    protected static class TestViewWithIllegalMethodParamters extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public TextField illegalParamterMethod(String illegalParamter) {
            return new TextField(illegalParamter);
        }

    }

    protected static class TestViewWithIllegalMethodReturnType extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public Object illegalReturnTypeMethod() {
            return new Object();
        }

    }

    public static class TestPmo {

        public static final String PROPERTY_TEXT = "text";
        public static final String PROPERTY_SOMEOTHERTEXT = "someothertext";
        public static final String PROPERTY_NUMBER = "number";
        public static final String METHOD_ON_CLICK = "onClick";
        public static final String TEST_TOOLTIP = "test";

        private String text = "";
        private String someothertext = "";
        private int number = 0;
        private int clickCount = 0;
        private String toolTip = "";

        private List<String> someotherValues = Arrays.asList("a", "b");
        private boolean numberEnabled;
        private boolean numberRequired;
        private boolean textFieldRequired;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isTextRequired() {
            return textFieldRequired;
        }

        public void setTextRequired(boolean required) {
            this.textFieldRequired = required;
        }

        public String getSomeothertext() {
            return someothertext;
        }

        public List<String> getSomeothertextAvailableValues() {
            return someotherValues;
        }

        public void setSomeothertextValues(String... textValues) {
            this.someotherValues = Arrays.asList(textValues);
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getNumberToolTip() {
            return toolTip;
        }

        public boolean isNumberEnabled() {
            return numberEnabled;
        }

        public void setNumberEnabled(boolean enabled) {
            this.numberEnabled = enabled;
        }

        public boolean isNumberRequired() {
            return numberRequired;
        }

        public void setNumberRequired(boolean required) {
            this.numberRequired = required;
        }

        public void onClick() {
            clickCount++;
        }

        public String getOnClickToolTip() {
            return toolTip;
        }

        public int getClickCount() {
            return clickCount;
        }

        public void setToolTip(String toolTip) {
            this.toolTip = toolTip;
        }
    }

}

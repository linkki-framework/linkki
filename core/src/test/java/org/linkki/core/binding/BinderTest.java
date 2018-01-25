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

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.components.IntegerField;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;

import com.vaadin.ui.Button;
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
        assertThat(bindingContext.getElementBindings(), hasSize(3));

        // Binding pmo -> view
        assertThat(view.textField.getDescription(), is(TestPmo.TEST_TOOLTIP));
        assertThat(view.numberField.getDescription(), is(emptyString()));
        assertThat(view.button.getDescription(), is(emptyString()));

        pmo.setNumber(13);
        pmo.setText("foo");
        pmo.setToolTip("test tool tip");

        bindingContext.updateUI();

        assertThat(view.numberField.getValue(), is("13"));
        assertThat(view.textField.getValue(), is("foo"));
        assertThat(view.textField.getDescription(), is(TestPmo.TEST_TOOLTIP));
        assertThat(view.numberField.getDescription(), is("test tool tip"));
        assertThat(view.button.getDescription(), is("test tool tip"));


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
    public void testSetupBindings_CanBindPrivateMethods() {
        TestViewWithPrivateMethod testView = new TestViewWithPrivateMethod();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(testView, pmo);
        BindingContext ctx = bindingManager.startNewContext("");
        binder.setupBindings(ctx);

        assertThat(ctx.getElementBindings(), hasSize(1));

        // Binding pmo -> view
        pmo.setText("foo");
        assertThat(testView.textField.getValue(), is("foo"));

        // Binding view -> pmo
        testView.textField.setValue("bar");
        assertThat(pmo.getText(), is("bar"));
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
        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        @UIToolTip(text = TestPmo.TEST_TOOLTIP)
        private TextField textField;

        @Bind(pmoProperty = TestPmo.METHOD_ON_CLICK)
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        private Button button = new Button();

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

        @Bind(pmoProperty = TestPmo.PROPERTY_NUMBER)
        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        public IntegerField getNumberField() {
            return numberField;
        }
    }

    protected static class TestViewWithPrivateMethod extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        private TextField textField = new TextField();

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        private TextField getTextField() {
            return textField;
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
        public static final String PROPERTY_NUMBER = "number";
        public static final String METHOD_ON_CLICK = "onClick";
        public static final String TEST_TOOLTIP = "test";

        private String text = "";
        private int number = 0;
        private int clickCount = 0;
        private String toolTip = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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

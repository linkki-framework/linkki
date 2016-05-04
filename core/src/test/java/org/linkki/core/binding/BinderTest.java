/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Locale;

import org.faktorips.runtime.MessageList;
import org.junit.Test;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.components.IntegerField;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BinderTest {

    private PropertyBehaviorProvider behaviorProvider = () -> Collections.emptySet();
    private ValidationService validationService = () -> new MessageList();

    @Test
    public void testSetupBindings() {
        TestView view = new TestView();
        view.initFields();
        TestPmo pmo = new TestPmo();

        // Precondition
        assertThat(pmo.getClickCount(), is(0));

        Binder binder = new Binder(view, pmo);
        BindingContext ctx = new BindingContext("", validationService, behaviorProvider);
        binder.setupBindings(ctx);
        assertThat(ctx.getElementBindings(), hasSize(3));

        // Binding pmo -> view
        pmo.setNumber(13);
        pmo.setText("foo");
        assertThat(view.numberField.getValue(), is("13"));
        assertThat(view.textField.getValue(), is("foo"));

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
        BindingContext ctx = new BindingContext("", validationService, behaviorProvider);
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
        BindingContext ctx = new BindingContext("", validationService, behaviorProvider);
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
        BindingContext ctx = new BindingContext("", validationService, behaviorProvider);
        binder.setupBindings(ctx);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedNonComponentField() {
        TestViewWithIllegalFieldAnnotation view = new TestViewWithIllegalFieldAnnotation();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(new BindingContext("", validationService, behaviorProvider));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithParameters() {
        TestViewWithIllegalMethodParamters view = new TestViewWithIllegalMethodParamters();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(new BindingContext("", validationService, behaviorProvider));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithNonComponentReturnType() {
        TestViewWithIllegalMethodReturnType view = new TestViewWithIllegalMethodReturnType();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(new BindingContext("", validationService, behaviorProvider));
    }

    protected static class TestView extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        private TextField textField;

        @Bind(pmoProperty = TestPmo.METHOD_ON_CLICK)
        private Button button = new Button();

        private IntegerField numberField;

        public void initTextField() {
            textField = new TextField();
        }

        public void initNumberField() {
            numberField = new IntegerField(Locale.CHINESE);
        }

        public void initFields() {
            initTextField();
            initNumberField();
        }

        @Bind(pmoProperty = TestPmo.PROPERTY_NUMBER)
        public IntegerField getNumberField() {
            return numberField;
        }
    }

    protected static class TestViewWithPrivateMethod extends VerticalLayout {

        private static final long serialVersionUID = 1L;

        private TextField textField = new TextField();

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public TextField getTextField() {
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

        private String text = "";
        private int number = 0;
        private int clickCount = 0;

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

        public void onClick() {
            clickCount++;
        }

        public int getClickCount() {
            return clickCount;
        }

    }

}

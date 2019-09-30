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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.pmo.ModelObject;

public class BinderTest {


    private BindingContext bindingContext;

    @BeforeEach
    public void setUp() {
        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        bindingContext = bindingManager.getContext("");
    }

    @Test
    public void testSetupBindings() {
        TestView view = new TestView();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        // precondition
        assertThat(pmo.getClickCount(), is(0));
        assertThat(pmo.getModel().getClickCount(), is(0));

        assertThat(bindingContext.getBindings(), hasSize(2));

        // Binding pmo -> view with @TestBind
        assertThat(view.fieldForFieldBinding.getLabelText(), is(""));
        assertThat(view.fieldForFieldBinding.isEnabled(), is(true));
        assertThat(view.fieldForMethodBinding.getLabelText(), is("mod"));
        assertThat(view.fieldForMethodBinding.isEnabled(), is(false));
        // Binding pmo -> view with additional @BindTooltip
        assertThat(view.fieldForFieldBinding.getTooltipText(), is(TestPmo.PMO_ATTRIBUTE_TOOLTIP));
        assertThat(view.fieldForMethodBinding.getTooltipText(), is(TestPmo.MODEL_ATTRIBUTE_TOOLTIP));

        pmo.setModelAttributeEnabled(true);
        pmo.setModelAttributeTooltip(TestPmo.PMO_ATTRIBUTE_TOOLTIP);

        bindingContext.modelChanged();

        // updated
        assertThat(view.fieldForMethodBinding.isEnabled(), is(true));
        assertThat(view.fieldForMethodBinding.getTooltipText(), is(TestPmo.PMO_ATTRIBUTE_TOOLTIP));

        // Binding view -> pmo
        view.fieldForMethodBinding.setEnabled(false);
        view.fieldForFieldBinding.click();
        view.fieldForMethodBinding.click();
        view.fieldForMethodBinding.click();

        // still the same as the EnabledAspect is model to ui only
        assertThat(pmo.isModelAttributeEnabled(), is(true));
        // updated
        assertThat(pmo.getClickCount(), is(1));
        assertThat(pmo.getModel().getClickCount(), is(2));
    }


    @Test
    public void testSetupBindings_ThrowsExceptionForNullField() {
        TestView view = new TestView();
        view.fieldForFieldBinding = null;
        // precondition as textField is binded with the field
        assertThat(view.fieldForFieldBinding, is(nullValue()));

        Binder binder = new Binder(view, new TestPmo());
        Assertions.assertThrows(NullPointerException.class, () -> {
            binder.setupBindings(bindingContext);
        });
    }


    @Test
    public void testSetupBindings_ThrowsExceptionForMethodReturningNull() {
        TestView view = new TestView();
        view.fieldForMethodBinding = null;

        // precondition as numberField is binded with the getter method
        assertThat(view.methodForMethodBinding(), is(nullValue()));

        Binder binder = new Binder(view, new TestPmo());
        Assertions.assertThrows(NullPointerException.class, () -> {
            binder.setupBindings(bindingContext);
        });

    }

    @Test
    public void testSetupBindings_ThrowsExceptionForAnnotatedNonComponentField() {
        IllegalFieldAnnotationView view = new IllegalFieldAnnotationView();

        Binder binder = new Binder(view, new TestPmo());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            binder.setupBindings(bindingContext);
        });
    }

    @Test
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithParameters() {
        IllegalMethodParamtersView view = new IllegalMethodParamtersView();

        Binder binder = new Binder(view, new TestPmo());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            binder.setupBindings(bindingContext);
        });
    }

    @Test
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithNonComponentReturnType() {
        IllegalMethodReturnTypeView view = new IllegalMethodReturnTypeView();

        Binder binder = new Binder(view, new TestPmo());
        Assertions.assertThrows(IllegalStateException.class, () -> {
            binder.setupBindings(bindingContext);
        });
    }

    protected class TestView {

        @TestBind(pmoProperty = TestPmo.PROPERTY_PMO_ATTRIBUTE, enabled = EnabledType.ENABLED)
        @BindTooltip(TestPmo.PMO_ATTRIBUTE_TOOLTIP)
        private TestUiComponent fieldForFieldBinding = new TestUiComponent();

        private TestUiComponent fieldForMethodBinding = new TestUiComponent();

        @TestBind(pmoProperty = TestModel.PROPERTY_MODEL_ATTRIBUTE, enabled = EnabledType.DYNAMIC, label = "mod")
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        public TestUiComponent methodForMethodBinding() {
            return fieldForMethodBinding;
        }
    }

    public class TestPmo {
        public static final String PROPERTY_PMO_ATTRIBUTE = "pmoAttribute";

        public static final String PMO_ATTRIBUTE_TOOLTIP = "testPmo";
        public static final String MODEL_ATTRIBUTE_TOOLTIP = "testModel";

        @ModelObject
        private TestModel model = new TestModel();
        private boolean modelAttributeEnabled;
        private String modelAttributeTooltip = MODEL_ATTRIBUTE_TOOLTIP;
        private int clickCount;

        public void pmoAttributeOnClick() {
            clickCount++;
        }

        public int getClickCount() {
            return clickCount;
        }

        public boolean isModelAttributeEnabled() {
            return modelAttributeEnabled;
        }

        public void setModelAttributeEnabled(boolean enabled) {
            modelAttributeEnabled = enabled;
        }

        public String getModelAttributeTooltip() {
            return modelAttributeTooltip;
        }

        public void setModelAttributeTooltip(String modelAttributeTooltip) {
            this.modelAttributeTooltip = modelAttributeTooltip;
        }

        public TestModel getModel() {
            return model;
        }

        public void setModel(TestModel model) {
            this.model = model;
        }
    }

    public class TestModel {
        public static final String PROPERTY_MODEL_ATTRIBUTE = "modelAttribute";
        private int clickCount;

        public void modelAttributeOnClick() {
            clickCount++;
        }

        public int getClickCount() {
            return clickCount;
        }
    }

    protected class IllegalFieldAnnotationView {

        @TestBind(pmoProperty = TestPmo.PROPERTY_PMO_ATTRIBUTE)
        public Object illegalField = new Object();

    }

    protected class IllegalMethodParamtersView {

        /**
         * @param illegalParamter should lead to the method not being found
         */
        @TestBind(pmoProperty = TestPmo.PROPERTY_PMO_ATTRIBUTE)
        public TestUiComponent illegalParamterMethod(String illegalParamter) {
            return new TestUiComponent();
        }
    }

    protected class IllegalMethodReturnTypeView {

        @TestBind(pmoProperty = TestPmo.PROPERTY_PMO_ATTRIBUTE)
        public Object illegalReturnTypeMethod() {
            return new Object();
        }
    }
}

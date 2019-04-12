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
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.bind.annotation.Bind;
import org.linkki.core.ui.element.annotation.TestUiUtil;

import com.vaadin.ui.Button;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("synthetic-access")
public class BinderIntegrationTest {


    private BindingContext bindingContext;

    @Before
    public void setUp() {
        BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
        bindingContext = bindingManager.startNewContext("");
    }

    @Test
    public void testSetupBindings() {
        TestView view = new TestView();
        TestPmo pmo = new TestPmo();
        view.initFields();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        // Precondition
        assertThat(pmo.getClickCount(), is(0));

        assertThat(bindingContext.getBindings(), hasSize(4));

        // Binding pmo -> view
        assertThat(view.textField.getDescription(), is(TestPmo.TEST_TOOLTIP));
        assertThat(view.numberField.getDescription(), is(emptyString()));
        assertThat(view.button.getDescription(), is(emptyString()));
        assertThat(TestUiUtil.getData(view.listSelect), contains("a", "b"));

        pmo.setNumber(13);
        pmo.setText("foo");
        pmo.setTooltip("test tool tip");
        pmo.setSomeothertextValues("c");

        bindingContext.modelChanged();

        assertThat(view.numberField.getValue(), is("13"));
        assertThat(view.textField.getValue(), is("foo"));
        assertThat(view.textField.getDescription(), is(TestPmo.TEST_TOOLTIP));
        assertThat(view.numberField.getDescription(), is("test tool tip"));
        assertThat(view.button.getDescription(), is("test tool tip"));
        assertThat(TestUiUtil.getData(view.listSelect), contains("c"));


        // Binding view -> pmo
        TestUiUtil.setUserOriginatedValue(view.numberField, "42");
        TestUiUtil.setUserOriginatedValue(view.textField, "bar");
        view.button.click();
        view.button.click();
        assertThat(pmo.getNumber(), is(42));
        assertThat(pmo.getText(), is("bar"));
        assertThat(pmo.getClickCount(), is(2));
    }

    @Test
    public void testEnabledTypeBinding() {
        TestView view = new TestView();
        TestPmo pmo = new TestPmo();
        view.initFields();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.listSelect.isEnabled(), is(false));
        assertThat(view.listSelect.isRequiredIndicatorVisible(), is(true));

        pmo.setNumberEnabled(false);
        bindingContext.modelChanged();
        assertThat(view.numberField.isEnabled(), is(false));
        assertThat(view.numberField.isRequiredIndicatorVisible(), is(false));
        pmo.setNumberEnabled(true);
        bindingContext.modelChanged();
        assertThat(view.numberField.isEnabled(), is(true));
        assertThat(view.numberField.isRequiredIndicatorVisible(), is(true));

        pmo.setTextRequired(true);
        bindingContext.modelChanged();
        assertThat(view.textField.isRequiredIndicatorVisible(), is(true));
        pmo.setTextRequired(false);
        bindingContext.modelChanged();
        assertThat(view.textField.isRequiredIndicatorVisible(), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testSetupBindings_ThrowsExceptionForNullField() {
        TestView view = new TestView();

        // precondition as textField is binded with the field
        assertThat(view.textField, is(nullValue()));

        Binder binder = new Binder(view, new TestPmo());
        binder.setupBindings(bindingContext);
    }

    @Test(expected = NullPointerException.class)
    public void testSetupBindings_ThrowsExceptionForMethodReturningNull() {
        TestView view = new TestView();

        // precondition as numberField is binded with the getter method
        assertThat(view.getNumberField(), is(nullValue()));

        Binder binder = new Binder(view, new TestPmo());
        binder.setupBindings(bindingContext);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedNonComponentField() {
        IllegalFieldAnnotationView view = new IllegalFieldAnnotationView();

        Binder binder = new Binder(view, new TestPmo());
        binder.setupBindings(bindingContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithParameters() {
        IllegalMethodParamtersView view = new IllegalMethodParamtersView();

        Binder binder = new Binder(view, new TestPmo());
        binder.setupBindings(bindingContext);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetupBindings_ThrowsExceptionForAnnotatedMethodWithNonComponentReturnType() {
        IllegalMethodReturnTypeView view = new IllegalMethodReturnTypeView();

        Binder binder = new Binder(view, new TestPmo());
        binder.setupBindings(bindingContext);
    }

    @Test
    public void testSetUpBindings_modelBinding() {
        ModelBindingView view = new ModelBindingView();
        TestPmo pmo = new TestPmo();
        pmo.getTestModelObject().setModelProperty("1");
        pmo.setModelPropertyEnabled(false);

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.pmoPropertyIsModelAttribute.getValue(), is("1"));
        assertThat(view.pmoPropertyIsModelAttribute.isEnabled(), is(false));

        pmo.getTestModelObject().setModelProperty("11");
        pmo.setModelPropertyEnabled(true);
        bindingContext.modelChanged();
        assertThat(view.pmoPropertyIsModelAttribute.getValue(), is("11"));
        assertThat(view.pmoPropertyIsModelAttribute.isEnabled(), is(true));
    }

    /**
     * If both pmo property and model attribute are valid, the pmo property is used.
     */
    @Test
    public void testSetUpBindings_modelBinding_validPmoPropertyAndModelAttribute() {
        ModelBindingView view = new ModelBindingView();
        TestPmo pmo = new TestPmo();
        pmo.getTestModelObject().setModelProperty("not text");
        pmo.text = "text";

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.validPmoPropertyAndModelAttribute.getValue(), is("text"));

        pmo.text = "changed text";
        bindingContext.modelChanged();
        assertThat(view.validPmoPropertyAndModelAttribute.getValue(), is("changed text"));
    }

    @Test
    public void testSetUpBindings_modelBinding_invalidPmoProperty() {
        ModelBindingView view = new ModelBindingView();
        TestPmo pmo = new TestPmo();
        pmo.getTestModelObject().setModelProperty("1");

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.invalidPmoProperty.getValue(), is("1"));

        pmo.getTestModelObject().setModelProperty("11");
        bindingContext.modelChanged();
        assertThat(view.invalidPmoProperty.getValue(), is("11"));
    }

    /**
     * If the given model attribute is invalid, but a valid pmo property is provided, then the binding
     * falls back to the pmo property.
     */

    @Test
    public void testSetUpBindings_modelBinding_invalidModelAttribute() {
        ModelBindingView view = new ModelBindingView();
        TestPmo pmo = new TestPmo();

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.invalidModelAttribute.getValue(), is(pmo.text));

        pmo.text = "changed text";
        bindingContext.modelChanged();
        assertThat(view.invalidModelAttribute.getValue(), is("changed text"));
    }

    @Test
    public void testSetUpBindings_modelBinding_alternativeModelAttribute() {
        ModelBindingView view = new ModelBindingView();
        TestPmo pmo = new TestPmo();
        pmo.getTestModelObject2().setModelProperty("2");

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.alternativeModelObject.getValue(), is("2"));

        pmo.getTestModelObject().setModelProperty("22");
        bindingContext.modelChanged();
        assertThat(view.alternativeModelObject.getValue(), is("2"));

        pmo.getTestModelObject2().setModelProperty("22");
        bindingContext.modelChanged();
        assertThat(view.alternativeModelObject.getValue(), is("22"));
    }

    @Test
    public void testSetUpBindings_modelBinding_pmoPropertyModelAttributeCombined() {
        ModelBindingView view = new ModelBindingView();
        TestPmo pmo = new TestPmo();
        pmo.getTestModelObject().setModelProperty("1");
        pmo.setRequiredOnlyPropertyRequired(true);

        Binder binder = new Binder(view, pmo);
        binder.setupBindings(bindingContext);

        assertThat(view.pmoPropertyModelAttributeCombined.getValue(), is("1"));
        assertThat(view.pmoPropertyModelAttributeCombined.isRequiredIndicatorVisible(), is(true));

        pmo.getTestModelObject().setModelProperty("11");
        pmo.setRequiredOnlyPropertyRequired(false);
        bindingContext.modelChanged();
        assertThat(view.pmoPropertyModelAttributeCombined.getValue(), is("11"));
        assertThat(view.pmoPropertyModelAttributeCombined.isRequiredIndicatorVisible(), is(false));
    }

    @SuppressFBWarnings("NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    protected class TestView {

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT, required = RequiredType.DYNAMIC)
        @BindTooltip(TestPmo.TEST_TOOLTIP)
        private TextField textField;

        @Bind(pmoProperty = TestPmo.METHOD_ON_CLICK)
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        private Button button = new Button();

        @Bind(pmoProperty = TestPmo.PROPERTY_SOMEOTHERTEXT, availableValues = AvailableValuesType.DYNAMIC, enabled = EnabledType.DISABLED, required = RequiredType.REQUIRED)
        private ListSelect<String> listSelect = new ListSelect<>();


        private TextField numberField;

        public void initFields() {
            initTextField();
            initNumberField();
        }

        public void initTextField() {
            textField = new TextField();
        }

        public void initNumberField() {
            numberField = new TextField();
            numberField.setLocale(Locale.CHINESE);
        }

        @Bind(pmoProperty = TestPmo.PROPERTY_NUMBER, enabled = EnabledType.DYNAMIC, required = RequiredType.REQUIRED_IF_ENABLED)
        @BindTooltip(tooltipType = TooltipType.DYNAMIC)
        public TextField getNumberField() {
            return numberField;
        }
    }

    protected class ModelBindingView {

        @Bind(pmoProperty = TestModelObject.MODEL_PROPERTY, enabled = EnabledType.DYNAMIC)
        private final TextField pmoPropertyIsModelAttribute = new TextField();

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT, modelAttribute = TestModelObject.MODEL_PROPERTY)
        private final TextField validPmoPropertyAndModelAttribute = new TextField();

        @Bind(pmoProperty = "nonExistingProperty", modelAttribute = TestModelObject.MODEL_PROPERTY)
        private final TextField invalidPmoProperty = new TextField();

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT, modelAttribute = "nonExistingProperty")
        private final TextField invalidModelAttribute = new TextField();

        @Bind(pmoProperty = TestModelObject.MODEL_PROPERTY, modelObject = TestPmo.MODEL_OBJECT2)
        private final TextField alternativeModelObject = new TextField();

        @Bind(pmoProperty = TestPmo.PROPERTY_REQUIRED_ONLY_PROPERTY, //
                modelAttribute = TestModelObject.MODEL_PROPERTY, //
                required = RequiredType.DYNAMIC)
        private final TextField pmoPropertyModelAttributeCombined = new TextField();
    }

    protected class IllegalFieldAnnotationView {

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public Object illegalField = new Object();

    }

    protected class IllegalMethodParamtersView {

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public TextField illegalParamterMethod(String illegalParamter) {
            return new TextField(illegalParamter);
        }
    }

    protected class IllegalMethodReturnTypeView {

        @Bind(pmoProperty = TestPmo.PROPERTY_TEXT)
        public Object illegalReturnTypeMethod() {
            return new Object();
        }
    }

    public class TestModelObject {

        public static final String MODEL_PROPERTY = "modelProperty";

        private String modelProperty = "1";

        public String getModelProperty() {
            return modelProperty;
        }

        public void setModelProperty(String modelProperty) {
            this.modelProperty = modelProperty;
        }
    }

    public class TestPmo {

        public static final String MODEL_OBJECT2 = "modelObject2";

        public static final String PROPERTY_TEXT = "text";
        public static final String PROPERTY_SOMEOTHERTEXT = "someothertext";
        public static final String PROPERTY_NUMBER = "number";
        public static final String PROPERTY_REQUIRED_ONLY_PROPERTY = "requiredOnlyProperty";

        public static final String METHOD_ON_CLICK = "onClick";
        public static final String TEST_TOOLTIP = "test";

        private TestModelObject modelObject = new TestModelObject();
        private TestModelObject modelObject2 = new TestModelObject();

        private String text = "";
        private String someothertext = "";
        private int number = 0;
        private int clickCount = 0;
        private String tooltip = "";

        private List<String> someotherValues = Arrays.asList("a", "b");
        private boolean numberEnabled;
        private boolean numberRequired;
        private boolean textFieldRequired;

        private boolean modelPropertyEnabled;
        private boolean requiredOnlyPropertyRequired;

        @ModelObject
        public TestModelObject getTestModelObject() {
            return modelObject;
        }

        @ModelObject(name = MODEL_OBJECT2)
        public TestModelObject getTestModelObject2() {
            return modelObject2;
        }

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

        public void setSomeothertextValues(@NonNull String... textValues) {
            this.someotherValues = Arrays.asList(textValues);
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getNumberTooltip() {
            return tooltip;
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

        public String getOnClickTooltip() {
            return tooltip;
        }

        public int getClickCount() {
            return clickCount;
        }

        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

        public boolean isRequiredOnlyPropertyRequired() {
            return requiredOnlyPropertyRequired;
        }

        public void setRequiredOnlyPropertyRequired(boolean required) {
            this.requiredOnlyPropertyRequired = required;
        }

        public boolean isModelPropertyEnabled() {
            return modelPropertyEnabled;
        }

        public void setModelPropertyEnabled(boolean enabled) {
            this.modelPropertyEnabled = enabled;
        }
    }
}

package org.linkki.core.binding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.linkki.core.binding.NullModelObjectIntegrationTest.InheritedTestModelObject.INHERITED_PROPERTY;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UICssLayout;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;

import edu.umd.cs.findbugs.annotations.CheckForNull;

class NullModelObjectIntegrationTest {

    @Test
    void testNullModelObject_InitiallyNull_NoExceptionOnCreation() {
        var pmo = new ModelObjectAwarePmo(null, null);

        assertThatNoException().isThrownBy(() -> VaadinUiCreator.createComponent(pmo, new BindingContext()));
    }

    @Test
    void testNullModelObject_InitiallyNull() {
        var pmo = new ModelObjectAwarePmo(null, null);

        var component = VaadinUiCreator.createComponent(pmo, new BindingContext());

        var children = component.getChildren().toList();
        assertThat(children.size())
                .as("Child components are created")
                .isEqualTo(4);

        var component1 = (HasValue<?, ?>)children.get(0);
        assertThat(component1.getValue())
                .as("If the value aspect delegates to the model object, the value of a nullable component should be null")
                .isNull();
        assertThat(component1.isReadOnly())
                .as("If the value aspect delegates to the model object, the component should be read-only")
                .isTrue();

        assertThat(((TextField)children.get(1)).getValue())
                .as("If the value aspect delegates to the model object, the value of the field is cleared" +
                        "even if the converter does not permit null")
                .isEqualTo("");

        var component3 = (TextField)children.get(2);
        assertThat(component3.getValue())
                .as("If the value aspect does not use model binding, the value should be displayed")
                .isEqualTo("initial");
        assertThat(component3.isReadOnly())
                .as("If the value aspect does not use model binding, the component should be active")
                .isFalse();
        assertThat(component3.isEnabled())
                .as("If an additional aspect (enabled) depends on the model object, the value should default to false")
                .isFalse();

        assertThat(((TextField)children.get(3)).getValue())
                .as("If the value aspect delegates to the model object, the value of the field is cleared" +
                        "even if the converter does not permit null")
                .isEqualTo("");
    }

    @Test
    void testNullModelObject_NullOnUpdate() {
        var modelObject = new TestModelObject();
        modelObject.setText("initialText");
        modelObject.setNumber(12);
        var inheritedModelObject = new InheritedTestModelObject();
        inheritedModelObject.setInheritedProperty("initialText");
        var pmo = new ModelObjectAwarePmo(modelObject, inheritedModelObject);
        var bindingContext = new BindingContext();
        VaadinUiCreator.createComponent(pmo, bindingContext);

        pmo.setModelObject(null);
        pmo.setInheritedModelObject(null);

        assertThatNoException().isThrownBy(bindingContext::modelChanged);
    }

    public static abstract class GenericModelObjectPmo<MO extends TestModelObject> {

        public static final String INHERITED_MODEL_OBJECT = "inherited";

        private MO inheritedModelObject;

        public GenericModelObjectPmo(MO inheritedModelObject) {
            this.inheritedModelObject = inheritedModelObject;
        }

        @ModelObject(name = INHERITED_MODEL_OBJECT)
        @CheckForNull
        public MO getInheritedModelObject() {
            return inheritedModelObject;
        }

        public void setInheritedModelObject(@CheckForNull MO inheritedModelObject) {
            this.inheritedModelObject = inheritedModelObject;
        }
    }

    // The layout annotation does not matter for this test
    @UICssLayout
    public static class ModelObjectAwarePmo extends GenericModelObjectPmo<InheritedTestModelObject> {

        private TestModelObject modelObject;
        private String pmoValue;

        public ModelObjectAwarePmo(TestModelObject modelObject, InheritedTestModelObject inheritedModelObject) {
            super(inheritedModelObject);
            this.modelObject = modelObject;
            this.pmoValue = "initial";
        }

        @ModelObject
        @CheckForNull
        public TestModelObject getModelObject() {
            return modelObject;
        }

        public void setModelObject(@CheckForNull TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        @UIComboBox(position = 10, content = AvailableValuesType.DYNAMIC)
        public void text() {
            // delegates value aspect and available values aspect to model object
        }

        @UIIntegerField(position = 20)
        public void number() {
            // delegates value aspect, whose converter does not permit null, to model object
        }

        @UITextField(position = 30, enabled = EnabledType.DYNAMIC)
        public String getPmoValue() {
            return pmoValue;
        }

        public void setPmoValue(String pmoValue) {
            this.pmoValue = pmoValue;
        }

        @UITextField(position = 40, modelObject = INHERITED_MODEL_OBJECT, modelAttribute = INHERITED_PROPERTY)
        public void inheritedProperty() {
            // model binding
        }
    }

    public static class TestModelObject {

        private String text;
        private int number = 0;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getTextAvailableValues() {
            return List.of("1", "2", "3");
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isPmoValueEnabled() {
            return true;
        }
    }

    public static class InheritedTestModelObject extends TestModelObject {

        public static final String INHERITED_PROPERTY = "inheritedProperty";

        private String inheritedProperty;

        public String getInheritedProperty() {
            return inheritedProperty;
        }

        public void setInheritedProperty(String inheritedProperty) {
            this.inheritedProperty = inheritedProperty;
        }
    }
}

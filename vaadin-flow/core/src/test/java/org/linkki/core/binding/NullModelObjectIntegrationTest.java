package org.linkki.core.binding;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UICssLayout;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

class NullModelObjectIntegrationTest {

    @Test
    void testNullModelObject_InitiallyNull_NoExceptionOnCreation() {
        var pmo = new ModelObjectAwarePmo(null);

        assertThatNoException().isThrownBy(() -> VaadinUiCreator.createComponent(pmo, new BindingContext()));
    }

    @Test
    void testNullModelObject_InitiallyNull() {
        var pmo = new ModelObjectAwarePmo(null);

        var component = VaadinUiCreator.createComponent(pmo, new BindingContext());

        var children = component.getChildren().toList();
        assertThat(children.size())
                .as("Child components are created")
                .isEqualTo(3);

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
    }

    @Test
    void testNullModelObject_NullOnUpdate() {
        var modelObject = new TestModelObject();
        modelObject.setText("initialText");
        modelObject.setNumber(12);
        var pmo = new ModelObjectAwarePmo(modelObject);
        var bindingContext = new BindingContext();
        var component = VaadinUiCreator.createComponent(pmo, bindingContext);


    }

    // The layout annotation does not matter for this test
    @UICssLayout
    public static class ModelObjectAwarePmo {

        private TestModelObject modelObject;
        private String pmoValue;

        public ModelObjectAwarePmo(TestModelObject modelObject) {
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
}

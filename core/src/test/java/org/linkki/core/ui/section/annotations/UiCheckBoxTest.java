/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.junit.Test;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.UICheckBox;
import org.linkki.core.ui.section.annotations.UISection;

import com.vaadin.data.Buffered;
import com.vaadin.ui.CheckBox;

public class UiCheckBoxTest {

    public static class TestModelObjectWithPrimitiveBoolean {

        private boolean value = false;

        public boolean getBooleanValue() {
            return value;
        }

        public void setBooleanValue(boolean b) {
            this.value = b;
        }
    }

    public static class TestModelObjectWithObjectBoolean {

        private Boolean value = null;

        public Boolean getBooleanValue() {
            return value;
        }

        public void setBooleanValue(Boolean b) {
            this.value = b;
        }
    }

    @UISection
    public static class TestPmo implements PresentationModelObject {

        private final Object modelObject;

        public TestPmo(Object modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UICheckBox(position = 1, modelAttribute = "booleanValue")
        public void booleanValue() {
            // data binding
        }

        @Override
        public Object getModelObject() {
            return modelObject;
        }
    }

    /**
     * Returns a {@code CheckBox} that is bound to the given model object using the IPM data binder.
     * The {@code CheckBox} is part of a mostly mocked UI so that a rudimentary Vaadin environment
     * is in place.
     * 
     * @param modelObject the model object to which the {@code CheckBox} is bound
     * @return a {@code CheckBox} that is bound to the model object
     */
    private CheckBox createCheckbox(Object modelObject) {
        TestPmo pmo = new TestPmo(modelObject);
        return (CheckBox)TestUi.componentBoundTo(pmo);
    }

    @Test
    public void testSetValueWithPrimitiveBooleanInModelObject() {
        CheckBox checkBox = createCheckbox(new TestModelObjectWithPrimitiveBoolean());

        // No assertions needed, we just make sure no exception is thrown
        checkBox.setValue(Boolean.TRUE);
        checkBox.setValue(Boolean.FALSE);
        checkBox.setValue(true);
        checkBox.setValue(false);
    }

    @Test(expected = Buffered.SourceException.class)
    public void testSetValueWithPrimitiveBooleanInModelObjectFailsForNull() {
        CheckBox checkBox = createCheckbox(new TestModelObjectWithPrimitiveBoolean());
        checkBox.setValue(null);
    }

    @Test
    public void testSetValueWithObjectBooleanInModelObject() {
        CheckBox checkBox = createCheckbox(new TestModelObjectWithObjectBoolean());

        // No assertions needed, we just make sure no exception is thrown
        checkBox.setValue(null);
        checkBox.setValue(Boolean.TRUE);
        checkBox.setValue(Boolean.FALSE);
        checkBox.setValue(true);
        checkBox.setValue(false);
    }
}

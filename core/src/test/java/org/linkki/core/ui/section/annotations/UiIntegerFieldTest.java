/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import org.junit.Test;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.UIIntegerField;
import org.linkki.core.ui.section.annotations.UISection;

import com.vaadin.data.Buffered;
import com.vaadin.ui.TextField;

public class UiIntegerFieldTest {

    public static class TestModelObjectWithPrimitiveInteger {

        private int value = 0;

        public int getIntegerValue() {
            return value;
        }

        public void setIntegerValue(int i) {
            this.value = i;
        }
    }

    public static class TestModelObjectWithObjectInteger {

        private Integer value = null;

        public Integer getIntegerValue() {
            return value;
        }

        public void setIntegerValue(Integer i) {
            this.value = i;
        }
    }

    @UISection
    public static class TestPmo implements PresentationModelObject {

        private final Object modelObject;

        public TestPmo(Object modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UIIntegerField(position = 1, modelAttribute = "integerValue")
        public void integerValue() {
            // data binding
        }

        @Override
        public Object getModelObject() {
            return modelObject;
        }
    }

    /**
     * Returns a {@code TextField} that is bound to the given model object using the IPM data
     * binder. The {@code TextField} is part of a mostly mocked UI so that a rudimentary Vaadin
     * environment is in place.
     * 
     * @param modelObject the model object to which the {@code TextField} is bound
     * @return a {@code TextField} that is bound to the model object
     */
    private TextField createIntegerTextField(Object modelObject) {
        TestPmo pmo = new TestPmo(modelObject);
        return (TextField)TestUi.componentBoundTo(pmo);
    }

    @Test
    public void testSetValueWithPrimitiveIntegerInModelObject() {
        TextField textField = createIntegerTextField(new TestModelObjectWithPrimitiveInteger());

        // No assertions needed, we just make sure no exception is thrown
        textField.setValue("0");
    }

    @Test(expected = Buffered.SourceException.class)
    public void testSetValueWithPrimitiveIntegerInModelObjectFailsForNull() {
        TextField textField = createIntegerTextField(new TestModelObjectWithPrimitiveInteger());
        textField.setValue(null);
    }

    @Test
    public void testSetValueWithObjectIntegerInModelObject() {
        TextField textField = createIntegerTextField(new TestModelObjectWithObjectInteger());

        // No assertions needed, we just make sure no exception is thrown
        textField.setValue(null);
        textField.setValue("0");
    }
}

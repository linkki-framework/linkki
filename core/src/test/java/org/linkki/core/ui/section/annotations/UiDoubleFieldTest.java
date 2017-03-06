/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section.annotations;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.junit.Test;

import com.vaadin.data.Buffered;
import com.vaadin.ui.TextField;

public class UiDoubleFieldTest {

    protected static class TestModelObjectWithPrimitiveDouble {

        private double value = 0;

        public double getDoubleValue() {
            return value;
        }

        public void setDoubleValue(double d) {
            this.value = d;
        }
    }

    protected static class TestModelObjectWithObjectDouble {

        @Nullable
        private Double value = null;

        @CheckForNull
        public Double getDoubleValue() {
            return value;
        }

        public void setDoubleValue(Double d) {
            this.value = d;
        }
    }

    @UISection
    protected static class TestPmo {

        private final Object modelObject;

        public TestPmo(Object modelObject) {
            super();
            this.modelObject = modelObject;
        }

        @UIDoubleField(position = 1, modelAttribute = "doubleValue")
        public void doubleValue() {
            // data binding
        }

        @ModelObject
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
    private TextField createDoubleTextField(Object modelObject) {
        TestPmo pmo = new TestPmo(modelObject);
        return (TextField)TestUi.componentBoundTo(pmo);
    }

    @Test
    public void testSetValueWithPrimitiveDoubleInModelObject() {
        TextField textField = createDoubleTextField(new TestModelObjectWithPrimitiveDouble());

        // No assertions needed, we just make sure no exception is thrown
        textField.setValue("0.0");
    }

    @Test(expected = Buffered.SourceException.class)
    public void testSetValueWithPrimitiveDoubleInModelObjectFailsForNull() {
        TextField textField = createDoubleTextField(new TestModelObjectWithPrimitiveDouble());
        textField.setValue(null);
    }

    @Test
    public void testSetValueWithObjectDoubleInModelObject() {
        TextField textField = createDoubleTextField(new TestModelObjectWithObjectDouble());

        // No assertions needed, we just make sure no exception is thrown
        textField.setValue(null);
        textField.setValue("0.0");
    }
}

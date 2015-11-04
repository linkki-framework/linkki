/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.ui.section;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.SectionCreationContext;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SectionCreationContextTest {

    private TestPMO pmo;

    private TestModelObject modelObject;

    @Mock
    private BindingContext bindingContext;

    @Mock
    private PropertyBehaviorProvider propertyBehaviorProvider;

    private SectionCreationContext sectionCreationContext;

    @Before
    public void setUp() {
        modelObject = new TestModelObject();
        pmo = new TestPMO(modelObject);
        sectionCreationContext = new SectionCreationContext(pmo, bindingContext, propertyBehaviorProvider);
    }

    @Test
    public void testCreateDefaultDispatcher_getValueFromPmo() {
        PropertyDispatcher defaultDispatcher = sectionCreationContext.createDefaultDispatcher(pmo);
        pmo.setPmoProp("testValue");

        Object pmoProp = defaultDispatcher.getValue(TestPMO.PROPERTY_PMO_PROP);

        assertThat(pmoProp, is("testValue"));
    }

    @Test
    public void testCreateDefaultDispatcher_setValueToPmo() {
        PropertyDispatcher defaultDispatcher = sectionCreationContext.createDefaultDispatcher(pmo);

        defaultDispatcher.setValue(TestPMO.PROPERTY_PMO_PROP, "testSetValue");

        assertThat(pmo.getPmoProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDefaultDispatcher_getValueFromModelObject() {
        PropertyDispatcher defaultDispatcher = sectionCreationContext.createDefaultDispatcher(pmo);
        modelObject.setModelProp("testValue");

        Object modelProp = defaultDispatcher.getValue(TestModelObject.PROPERTY_MODEL_PROP);

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDefaultDispatcher_setValueToModelObject() {
        PropertyDispatcher defaultDispatcher = sectionCreationContext.createDefaultDispatcher(pmo);

        defaultDispatcher.setValue(TestModelObject.PROPERTY_MODEL_PROP, "testSetValue");

        assertThat(modelObject.getModelProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDefaultDispatcher_getValue_changeModelObject() {
        PropertyDispatcher defaultDispatcher = sectionCreationContext.createDefaultDispatcher(pmo);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);
        newModelObject.setModelProp("testNewValue");

        Object modelProp = defaultDispatcher.getValue(TestModelObject.PROPERTY_MODEL_PROP);

        assertThat(modelProp, is("testNewValue"));
    }

    @Test
    public void testCreateDefaultDispatcher_setValue_changeModelObject() {
        PropertyDispatcher defaultDispatcher = sectionCreationContext.createDefaultDispatcher(pmo);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);

        defaultDispatcher.setValue(TestModelObject.PROPERTY_MODEL_PROP, "testNewSetValue");

        assertThat(newModelObject.getModelProp(), is("testNewSetValue"));
    }

    public static class TestPMO implements PresentationModelObject {

        public static final String PROPERTY_PMO_PROP = "pmoProp";

        private TestModelObject modelObject;

        private String pmoProp;

        public TestPMO(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        @Override
        public Object getModelObject() {
            return modelObject;
        }

        public void setModelObject(TestModelObject modelObject) {
            this.modelObject = modelObject;
        }

        public String getPmoProp() {
            return pmoProp;
        }

        public void setPmoProp(String pmoProp) {
            this.pmoProp = pmoProp;
        }

    }

    public static class TestModelObject {

        public static final String PROPERTY_MODEL_PROP = "modelProp";

        private String modelProp;

        public String getModelProp() {
            return modelProp;
        }

        public void setModelProp(String modelProp) {
            this.modelProp = modelProp;
        }

    }

}

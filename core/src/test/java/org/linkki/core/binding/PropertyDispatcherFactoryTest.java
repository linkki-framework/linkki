/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.BindingContextTest.TestModelObject;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyDispatcherFactoryTest {

    private TestPmo pmo = new TestPmo();
    private TestModelObject modelObject = new TestModelObject();

    @Mock
    private ElementDescriptor elementDescriptor;

    private void setUpPmo() {
        modelObject = new TestModelObject();
        pmo.setModelObject(modelObject);
    }

    @Test
    public void testCreateDispatcherChain_getValueFromPmo() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = PropertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        pmo.setValue("testValue");

        Object pmoProp = defaultDispatcher.getValue();

        assertThat(pmoProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToPmo() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = PropertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.setValue("testSetValue");

        assertThat(pmo.getValue(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = PropertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        modelObject.setModelProp("testValue");

        Object modelProp = defaultDispatcher.getValue();

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = PropertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.setValue("testSetValue");

        assertThat(modelObject.getModelProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromChangedModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = PropertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);
        newModelObject.setModelProp("testNewValue");

        Object modelProp = defaultDispatcher.getValue();

        assertThat(modelProp, is("testNewValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToChangedModelObject() {
        setUpPmo();
        when(elementDescriptor.getPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        PropertyDispatcher defaultDispatcher = PropertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);

        defaultDispatcher.setValue("testNewSetValue");

        assertThat(newModelObject.getModelProp(), is("testNewSetValue"));
    }

}

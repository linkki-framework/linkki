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
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.core.ui.section.annotations.ElementDescriptor;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PropertyDispatcherFactoryTest {

    private static final String ANY_VALUE = "ukztu7kxju76r";
    private TestPmo pmo = new TestPmo();
    private TestModelObject modelObject = new TestModelObject();
    private PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();

    @Mock
    private ElementDescriptor elementDescriptor;

    private void setUpPmo() {
        modelObject = new TestModelObject();
        pmo.setModelObject(modelObject);
    }

    @Test
    public void testCreateDispatcherChain_getValueFromPmo() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn("foo");
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        pmo.setValue(ANY_VALUE);

        Object pmoProp = defaultDispatcher.getValue();

        assertThat(pmoProp, is(ANY_VALUE));
    }

    @Test
    public void testCreateDispatcherChain_setValueToPmo() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn("foo");
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.setValue(ANY_VALUE);

        assertThat(pmo.getValue(), is(ANY_VALUE));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromModelObject() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("foo");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        modelObject.setModelProp("testValue");

        Object modelProp = defaultDispatcher.getValue();

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToModelObject() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("foo");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.setValue("testSetValue");

        assertThat(modelObject.getModelProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromChangedModelObject() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("foo");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
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
        when(elementDescriptor.getModelPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("foo");

        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);

        defaultDispatcher.setValue("testNewSetValue");
        assertThat(newModelObject.getModelProp(), is("testNewSetValue"));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateDispatcherChain_nullCustomDispatchers() {
        propertyDispatcherFactory = new PropertyDispatcherFactory() {
            @Override
            protected PropertyDispatcher createCustomDispatchers(Object pmo,
                    BindingDescriptor bindingDescriptor,
                    PropertyDispatcher standardDispatcher) {
                return null;
            }
        };

        propertyDispatcherFactory.createDispatcherChain(pmo, elementDescriptor,
                                                        PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

}

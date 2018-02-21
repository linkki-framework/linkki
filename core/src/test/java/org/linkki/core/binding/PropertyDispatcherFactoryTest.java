/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.BindingContextTest.TestModelObject;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.descriptor.BindingDescriptor;
import org.linkki.core.ui.section.descriptor.ElementDescriptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("null")
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

        Object pmoProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(pmoProp, is(ANY_VALUE));
    }

    @Test
    public void testCreateDispatcherChain_pushToPmo() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn("foo");
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("value");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.push(Aspect.of("", ANY_VALUE));

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

        Object modelProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_push() {
        setUpPmo();
        when(elementDescriptor.getModelPropertyName()).thenReturn(TestModelObject.PROPERTY_MODEL_PROP);
        when(elementDescriptor.getModelObjectName()).thenReturn(ModelObject.DEFAULT_NAME);
        when(elementDescriptor.getPmoPropertyName()).thenReturn("foo");
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, elementDescriptor, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.push(Aspect.of("", "testSetValue"));

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

        Object modelProp = defaultDispatcher.pull(Aspect.of(""));

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

        defaultDispatcher.push(Aspect.of("", "testNewSetValue"));
        assertThat(newModelObject.getModelProp(), is("testNewSetValue"));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateDispatcherChain_nullCustomDispatchers() {
        propertyDispatcherFactory = new PropertyDispatcherFactory() {
            @Override
            protected PropertyDispatcher createCustomDispatchers(@SuppressWarnings("hiding") Object pmo,
                    BindingDescriptor bindingDescriptor,
                    PropertyDispatcher standardDispatcher) {
                return mock(PropertyDispatcher.class);
            }
        };

        propertyDispatcherFactory.createDispatcherChain(pmo, elementDescriptor,
                                                        PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

}

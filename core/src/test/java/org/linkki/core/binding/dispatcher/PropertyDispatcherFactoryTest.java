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
package org.linkki.core.binding.dispatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.binding.TestModelObject;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.pmo.ModelObject;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

public class PropertyDispatcherFactoryTest {

    private static final String ANY_VALUE = "ukztu7kxju76r";
    private TestModelObject modelObject = new TestModelObject();
    private TestPmo pmo = new TestPmo(modelObject);
    private PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();


    @Test
    public void testCreateDispatcherChain_getValueFromPmo() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, BoundProperty.of("value").withModelAttribute("foo"),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        pmo.setValue(ANY_VALUE);

        Object pmoProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(pmoProp, is(ANY_VALUE));
    }

    @Test
    public void testCreateDispatcherChain_pushToPmo() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, BoundProperty.of("value").withModelAttribute("foo"),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.push(Aspect.of("", ANY_VALUE));

        assertThat(pmo.getValue(), is(ANY_VALUE));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromModelObject() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        modelObject.setModelProp("testValue");

        Object modelProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_push() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.push(Aspect.of("", "testSetValue"));

        assertThat(modelObject.getModelProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_getValueFromChangedModelObject() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);
        newModelObject.setModelProp("testNewValue");

        Object modelProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(modelProp, is("testNewValue"));
    }

    @Test
    public void testCreateDispatcherChain_setValueToChangedModelObject() {

        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        TestModelObject newModelObject = new TestModelObject();
        pmo.setModelObject(newModelObject);

        defaultDispatcher.push(Aspect.of("", "testNewSetValue"));
        assertThat(newModelObject.getModelProp(), is("testNewSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_modelObjectField() {
        PmoWithModelObjectField pmoWithModelObjectField = new PmoWithModelObjectField();

        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmoWithModelObjectField,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        pmoWithModelObjectField.modelObject.setModelProp("prop");

        Object initialValue = defaultDispatcher.pull(Aspect.of(""));
        assertThat(initialValue, is("prop"));

        defaultDispatcher.push(Aspect.of("", "testNewSetValue"));
        assertThat(pmoWithModelObjectField.modelObject.getModelProp(), is("testNewSetValue"));

    }

    @Test(expected = NullPointerException.class)
    public void testCreateDispatcherChain_nullCustomDispatchers() {
        propertyDispatcherFactory = new PropertyDispatcherFactory() {
            @Override
            protected PropertyDispatcher createCustomDispatchers(@SuppressWarnings("hiding") Object pmo,
                    BoundProperty boundProperty,
                    PropertyDispatcher standardDispatcher) {
                return mock(PropertyDispatcher.class);
            }
        };

        propertyDispatcherFactory.createDispatcherChain(pmo, BoundProperty.of((String)null),
                                                        PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    private static class PmoWithModelObjectField {

        @ModelObject
        private TestModelObject modelObject = new TestModelObject();

    }
}

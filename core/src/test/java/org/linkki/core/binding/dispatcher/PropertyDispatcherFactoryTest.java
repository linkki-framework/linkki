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

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.binding.TestModelObject;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.pmo.ModelObject;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@ExtendWith(MockitoExtension.class)
public class PropertyDispatcherFactoryTest {

    private static final String ANY_VALUE = "ukztu7kxju76r";
    private TestModelObject modelObject = new TestModelObject();
    private TestPmo pmo = new TestPmo(modelObject);
    private PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();


    @Test
    public void testCreateDispatcherChain_GetValueFromPmo() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, BoundProperty.of("value").withModelAttribute("foo"),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        pmo.setValue(ANY_VALUE);

        Object pmoProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(pmoProp, is(ANY_VALUE));
    }

    @Test
    public void testCreateDispatcherChain_PushToPmo() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo, BoundProperty.of("value").withModelAttribute("foo"),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.push(Aspect.of("", ANY_VALUE));

        assertThat(pmo.getValue(), is(ANY_VALUE));
    }

    @Test
    // For the given situation: PMO has a getter only, while the model object has getter and setter, is
    // #isPushable() as well as push() shall prevent a call to the setter of the model object
    public void testCreateDispatcherChain_PushToPmoReadonly() {
        pmo.setModelObject(new ModelObjectWithPmoReadOnlyProperty());
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("readonlyEnumValue")
                                               .withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        assertThat(defaultDispatcher.isPushable(Aspect.of("", ANY_VALUE)), is(false));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            defaultDispatcher.push(Aspect.of("", ANY_VALUE));
        });
    }


    @Test
    public void testCreateDispatcherChain_GetValueFromModelObject() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
        modelObject.setModelProp("testValue");

        Object modelProp = defaultDispatcher.pull(Aspect.of(""));

        assertThat(modelProp, is("testValue"));
    }

    @Test
    public void testCreateDispatcherChain_Push() {
        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmo,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        defaultDispatcher.push(Aspect.of("", "testSetValue"));

        assertThat(modelObject.getModelProp(), is("testSetValue"));
    }

    @Test
    public void testCreateDispatcherChain_GetValueFromChangedModelObject() {
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
    public void testCreateDispatcherChain_SetValueToChangedModelObject() {

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
    public void testCreateDispatcherChain_ModelObjectField() {
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

    @Test
    public void testCreateDispatcherChain_NullModelObject() {
        PmoWithNullModelObject pmoWithNullModelObject = new PmoWithNullModelObject();

        PropertyDispatcher defaultDispatcher = propertyDispatcherFactory
                .createDispatcherChain(pmoWithNullModelObject,
                                       BoundProperty.of("foo").withModelAttribute(TestModelObject.PROPERTY_MODEL_PROP),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                                                                   () -> defaultDispatcher.pull(Aspect.of("")));
        assertThat(illegalStateException.getMessage(), containsStringIgnoringCase("object must not be null"));
    }

    @Test
    public void testCreateDispatcherChain_NullCustomDispatchers() {
        propertyDispatcherFactory = new PropertyDispatcherFactory() {
            @Override
            protected PropertyDispatcher createCustomDispatchers(@SuppressWarnings("hiding") Object pmo,
                    BoundProperty boundProperty,
                    PropertyDispatcher standardDispatcher) {
                return mock(PropertyDispatcher.class);
            }
        };

        assertThrows(NullPointerException.class, () -> propertyDispatcherFactory
                .createDispatcherChain(pmo, BoundProperty.of((String)null),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER));
    }

    private static class PmoWithModelObjectField {

        @ModelObject
        private TestModelObject modelObject = new TestModelObject();

    }

    private static class PmoWithNullModelObject {

        @CheckForNull
        @ModelObject
        private TestModelObject modelObject = null;

    }

    private static class ModelObjectWithPmoReadOnlyProperty extends TestModelObject {
        @CheckForNull
        public TestEnum getReadonlyEnumValue() {
            return TestEnum.ONE;
        }

        @SuppressWarnings("unused")
        public void setReadonlyEnumValue(TestEnum testEnum) {
            // do nothing
        }
    }
}

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

package org.linkki.ips.binding.dispatcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.AbstractPropertyDispatcherDecorator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.behavior.BehaviorDependentDispatcher;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.staticvalue.StaticValueDispatcher;
import org.linkki.core.pmo.ModelObject;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherTest.TestPmoWithIpsModelObject;
import org.linkki.ips.test.model.TestIpsObject;

public class IpsPropertyDispatcherFactoryTest {

    private IpsPropertyDispatcherFactory ipsPropertyDispatcherFactory = new IpsPropertyDispatcherFactory();


    @Test
    public void testCreateCustomDispatchers_NoModelObject() {
        PropertyDispatcher customDispatchers = ipsPropertyDispatcherFactory
                .createDispatcherChain("Foo", BoundProperty.empty(), PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        assertThat(customDispatchers, is(instanceOf(BehaviorDependentDispatcher.class)));
        assertThat(getWrappedDispatcher(customDispatchers), is(instanceOf(StaticValueDispatcher.class)));
        assertThat(customDispatchers.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI)),
                   is(""));
    }

    @Test
    public void testCreateCustomDispatchers_NonIpsModelObject() {
        SimplePmo simplePmo = new SimplePmo();
        PropertyDispatcher customDispatchers = ipsPropertyDispatcherFactory
                .createDispatcherChain(simplePmo, BoundProperty.empty(),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        assertThat(customDispatchers, is(instanceOf(BehaviorDependentDispatcher.class)));
        // As we don't know what will be returned by the modelObjectSupplier at any given time, we still
        // have to create an IpsPropertyDispatcher
        assertThat(getWrappedDispatcher(customDispatchers), is(instanceOf(IpsPropertyDispatcher.class)));
        assertThat(customDispatchers.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI)),
                   is(""));

        simplePmo.setModelObject(new TestIpsObject());
        assertThat(customDispatchers.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI)),
                   is("Ein Testobjekt"));
    }

    @Test
    public void testCreateCustomDispatchers_IpsModelObject() {
        PropertyDispatcher customDispatchers = ipsPropertyDispatcherFactory
                .createDispatcherChain(new TestPmoWithIpsModelObject(), BoundProperty.empty(),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        assertThat(customDispatchers, is(instanceOf(BehaviorDependentDispatcher.class)));
        assertThat(getWrappedDispatcher(customDispatchers), is(instanceOf(IpsPropertyDispatcher.class)));

        assertThat(customDispatchers.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI)),
                   is("Ein Testobjekt"));
    }

    @Test
    public void testCreateCustomDispatchers_IpsModelObjectAttributeSameAsPmoProperty() {
        PropertyDispatcher customDispatchers = ipsPropertyDispatcherFactory
                .createDispatcherChain(new TestPmoWithIpsModelObject(), BoundProperty.of(TestIpsObject.PROPERTY_FOO),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        assertThat(customDispatchers, is(instanceOf(BehaviorDependentDispatcher.class)));
        assertThat(getWrappedDispatcher(customDispatchers), is(instanceOf(IpsPropertyDispatcher.class)));

        assertThat(customDispatchers.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI)),
                   is("Foo auf Deutsch"));
    }

    @Test
    public void testCreateCustomDispatchers_IpsModelObjectAttribute() {
        PropertyDispatcher customDispatchers = ipsPropertyDispatcherFactory
                .createDispatcherChain(new TestPmoWithIpsModelObject(),
                                       BoundProperty.of("bar").withModelAttribute(TestIpsObject.PROPERTY_FOO),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        assertThat(customDispatchers, is(instanceOf(BehaviorDependentDispatcher.class)));
        assertThat(getWrappedDispatcher(customDispatchers), is(instanceOf(IpsPropertyDispatcher.class)));

        assertThat(customDispatchers.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI)),
                   is("Foo auf Deutsch"));
    }

    private static PropertyDispatcher getWrappedDispatcher(PropertyDispatcher wrappingDispatcher) {
        try {
            Method method = AbstractPropertyDispatcherDecorator.class.getDeclaredMethod("getWrappedDispatcher");
            method.setAccessible(true);
            return (PropertyDispatcher)method.invoke(wrappingDispatcher);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        throw new AssertionError("Could not get wrapped dispatcher");
    }


    public static class SimplePmo {

        private Object modelObject = "NonIps";

        @ModelObject
        public Object getModelObject() {
            return modelObject;
        }

        public void setModelObject(Object modelObject) {
            this.modelObject = modelObject;
        }
    }
}

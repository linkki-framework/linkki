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
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.mock.MockUi;
import org.linkki.ips.test.model.TestIpsObject;
import org.linkki.ips.test.model.TestIpsObject2;

import com.vaadin.ui.UI;

public class IpsPropertyDispatcherTest {

    @SuppressWarnings("unused")
    private UI ui;

    private TestPmoWithIpsModelObject pmo = new TestPmoWithIpsModelObject();

    private PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();


    @Before
    public void setUp() {
        ui = MockUi.mockUi();
    }

    @After
    public void cleanUpUi() {
        UI.setCurrent(null);
    }

    @Test
    public void testPull() {
        MockUi.setLocale(Locale.GERMANY);
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(
                pmo::getIpsObject,
                TestIpsObject.PROPERTY_FOO, standardDispatcherChain(TestIpsObject.PROPERTY_FOO));

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo auf Deutsch"));

        MockUi.setLocale(Locale.ENGLISH);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo in English"));
    }

    @Test
    public void testPull_ModelAttribute() {
        MockUi.setLocale(Locale.GERMANY);
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(
                pmo::getIpsObject,
                TestIpsObject.PROPERTY_FOO, propertyDispatcherFactory.createDispatcherChain(pmo,
                                                                                            BoundProperty.of("bar")
                                                                                                    .withModelAttribute(TestIpsObject.PROPERTY_FOO),
                                                                                            PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER));

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo auf Deutsch"));

        MockUi.setLocale(Locale.ENGLISH);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo in English"));
    }

    @Test
    public void testPull_DefaultLocale() {
        MockUi.setLocale(Locale.ITALY);
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(
                pmo::getIpsObject,
                TestIpsObject.PROPERTY_FOO, standardDispatcherChain(TestIpsObject.PROPERTY_FOO));

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        Locale systemDefaultLocale = Locale.getDefault();
        switch (systemDefaultLocale.getLanguage()) {
            case "de":
                // because system locale trumps IPS default locale
                assertThat(string, is("Foo auf Deutsch"));
                break;

            default:
                assertThat(string, is("Foo in English"));
        }
    }

    @Test
    public void testPull_Class() {
        MockUi.setLocale(Locale.GERMANY);
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(
                pmo::getIpsObject,
                "", standardDispatcherChain(""));

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Ein Testobjekt"));

        MockUi.setLocale(Locale.ENGLISH);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("A test object"));
    }

    @Test
    public void testPull_Class_OverwritingModelObjectClass() {
        MockUi.setLocale(Locale.ENGLISH);
        pmo.setIpsObject(new TestIpsObject2());
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(
                pmo::getIpsObject,
                "", standardDispatcherChain(""));

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("A test object overwriting the label"));

        MockUi.setLocale(Locale.GERMANY);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        // no German label in TestIpsObject2, so we fall back to the default locale
        assertThat(string, is("A test object overwriting the label"));
    }

    private PropertyDispatcher standardDispatcherChain(String propertyName) {
        return propertyDispatcherFactory.createDispatcherChain(pmo,
                                                               BoundProperty.of(propertyName),
                                                               PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    public static class TestPmoWithIpsModelObject {

        private TestIpsObject ipsObject = new TestIpsObject();

        @ModelObject
        public TestIpsObject getIpsObject() {
            return ipsObject;
        }

        public void setIpsObject(TestIpsObject ipsObject) {
            this.ipsObject = ipsObject;
        }

        @UITextField(position = 1)
        public void foo() {
            // model binding
        }

        @UITextField(position = 2, modelAttribute = TestIpsObject.PROPERTY_FOO)
        public void bar() {
            // model binding
        }

    }


}

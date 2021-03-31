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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.mock.MockUi;
import org.linkki.ips.test.model.TestIpsObject;
import org.linkki.ips.test.model.TestIpsObject2;

import com.vaadin.flow.component.UI;

public class IpsPropertyDispatcherTest {

    @SuppressWarnings("unused")
    private UI ui;

    private TestPmoWithIpsModelObject pmo = new TestPmoWithIpsModelObject();

    private PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();


    @BeforeEach
    public void setUp() {
        ui = MockUi.mockUi();
    }

    @AfterEach
    public void cleanUpUi() {
        UI.setCurrent(null);
    }

    @Test
    public void testPull() {
        MockUi.setLocale(Locale.GERMANY);
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_FOO);

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo auf Deutsch"));

        MockUi.setLocale(Locale.ENGLISH);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo in English"));
    }

    @Test
    public void testPull_DefaultLocale() {
        MockUi.setLocale(Locale.ITALY);
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_FOO);

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
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain("");

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
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain("");

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("A test object overwriting the label"));

        MockUi.setLocale(Locale.GERMANY);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        // no German label in TestIpsObject2, so we fall back to the default locale
        assertThat(string, is("A test object overwriting the label"));
    }

    @Test
    public void testPull_NotRequired_ValueSetExclNull_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, false));

        assertThat(required, is(true));
    }

    @Test
    public void testPull_NotRequired_ValueSetInclNull_ShouldNotBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETINCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, false));

        assertThat(required, is(false));
    }

    @Test
    public void testPull_Required_ValueSetExclNull_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, true));

        assertThat(required, is(true));
    }

    @Test
    public void testPull_Required_ValueSetInclNull_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETINCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, true));

        assertThat(required, is(true));
    }

    @Test
    public void testPull_DynamicNotRequired_ValueSetExclNull_ShouldNotBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME));

        assertThat(required, is(false));
    }

    @Test
    public void testPull_Visible_NotEmptyValueSet_ShouldBeVisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, true));

        assertThat(visible, is(true));
    }

    @Test
    public void testPull_Visible_EmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, true));

        assertThat(visible, is(false));
    }

    @Test
    public void testPull_Invisible_NotEmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false));

        assertThat(visible, is(false));
    }

    @Test
    public void testPull_Invisible_EmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false));

        assertThat(visible, is(false));
    }

    @Test
    public void testPull_DynamicInvisible_NotEmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));

        assertThat(visible, is(false));
    }

    @Test
    public void testPull_Enabled_NotEmptyValueSet_ShouldBeEnabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, true));

        assertThat(enabled, is(true));
    }

    @Test
    public void testPull_Enabled_EmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, true));

        assertThat(enabled, is(false));
    }

    @Test
    public void testPull_Disabled_NotEmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, false));

        assertThat(enabled, is(false));
    }

    @Test
    public void testPull_Disabled_EmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, false));

        assertThat(enabled, is(false));
    }

    @Test
    public void testPull_DynamicDisabled_NotEmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME));

        assertThat(enabled, is(false));
    }


    private IpsPropertyDispatcher ipsDispatcherChain(String modelAttribute) {
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(
                pmo::getIpsObject,
                modelAttribute,
                propertyDispatcherFactory
                        .createDispatcherChain(pmo,
                                               BoundProperty.of("bar")
                                                       .withModelAttribute(modelAttribute),
                                               PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER));
        return ipsPropertyDispatcher;
    }

    public static class TestPmoWithIpsModelObject {

        private TestIpsObject ipsObject = new TestIpsObject();

        @ModelObject
        public TestIpsObject getIpsObject() {
            return ipsObject;
        }

        @ModelObject(name = "second")
        public TestIpsObject2 getIpsObject2() {
            return new TestIpsObject2();
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

        public boolean isBarRequired() {
            return false;
        }

        public boolean isBarVisible() {
            return false;
        }

        public boolean isBarEnabled() {
            return false;
        }

    }


}

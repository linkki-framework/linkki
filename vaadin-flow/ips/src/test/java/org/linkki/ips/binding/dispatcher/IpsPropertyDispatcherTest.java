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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.test.VaadinUIExtension;
import org.linkki.ips.test.model.TestIpsObject;
import org.linkki.ips.test.model.TestIpsObject2;

import com.vaadin.flow.component.UI;

@ExtendWith(VaadinUIExtension.class)
class IpsPropertyDispatcherTest {

    private final TestPmoWithIpsModelObject pmo = new TestPmoWithIpsModelObject();

    private final PropertyDispatcherFactory propertyDispatcherFactory = new PropertyDispatcherFactory();

    @Test
    void testPull() {
        UI.getCurrent().setLocale(Locale.GERMANY);
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_FOO);

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo auf Deutsch"));

        UI.getCurrent().setLocale(Locale.ENGLISH);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Foo in English"));
    }

    @Test
    void testPull_DefaultLocale() {
        UI.getCurrent().setLocale(Locale.ITALY);
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
    void testPull_Class() {
        UI.getCurrent().setLocale(Locale.GERMANY);
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain("");

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("Ein Testobjekt"));

        UI.getCurrent().setLocale(Locale.ENGLISH);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("A test object"));
    }

    @Test
    void testPull_Class_OverwritingModelObjectClass() {
        UI.getCurrent().setLocale(Locale.ENGLISH);
        pmo.setIpsObject(new TestIpsObject2());
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain("");

        String string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        assertThat(string, is("A test object overwriting the label"));

        UI.getCurrent().setLocale(Locale.GERMANY);
        string = ipsPropertyDispatcher.pull(Aspect.of("", LinkkiAspectDefinition.DERIVED_BY_LINKKI));

        // no German label in TestIpsObject2, so we fall back to the default locale
        assertThat(string, is("A test object overwriting the label"));
    }

    @Test
    void testPull_NotRequired_ValueSetExclNull_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, false));

        assertThat(required, is(true));
    }

    @Test
    void testPull_NotRequired_ValueSetInclNull_ShouldNotBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETINCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, false));

        assertThat(required, is(false));
    }

    @Test
    void testPull_Required_ValueSetExclNull_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, true));

        assertThat(required, is(true));
    }

    @Test
    void testPull_Required_ValueSetInclNull_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETINCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, true));

        assertThat(required, is(true));
    }

    @Test
    void testPull_Required_ValueSetEmpty_ShouldBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, true));

        assertThat(required, is(true));
    }

    @Test
    void testPull_NotRequired_ValueSetEmpty_ShouldNotBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME, false));

        assertThat(required, is(false));
    }

    @Test
    void testPull_DynamicNotRequired_ValueSetExclNull_ShouldNotBeRequired() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean required = ipsPropertyDispatcher.pull(Aspect.of(RequiredAspectDefinition.NAME));

        assertThat(required, is(false));
    }

    @Test
    void testPull_Visible_NotEmptyValueSet_ShouldBeVisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, true));

        assertThat(visible, is(true));
    }

    @Test
    void testPull_Visible_EmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, true));

        assertThat(visible, is(false));
    }

    @Test
    void testPull_Invisible_NotEmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false));

        assertThat(visible, is(false));
    }

    @Test
    void testPull_Invisible_EmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME, false));

        assertThat(visible, is(false));
    }

    @Test
    void testPull_DynamicInvisible_NotEmptyValueSet_ShouldBeInvisible() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean visible = ipsPropertyDispatcher.pull(Aspect.of(VisibleAspectDefinition.NAME));

        assertThat(visible, is(false));
    }

    @Test
    void testPull_Enabled_NotEmptyValueSet_ShouldBeEnabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, true));

        assertThat(enabled, is(true));
    }

    @Test
    void testPull_Enabled_EmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, true));

        assertThat(enabled, is(false));
    }

    @Test
    void testPull_Disabled_NotEmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, false));

        assertThat(enabled, is(false));
    }

    @Test
    void testPull_Disabled_EmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_EMPTYVALUESET);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME, false));

        assertThat(enabled, is(false));
    }

    @Test
    void testPull_DynamicDisabled_NotEmptyValueSet_ShouldBeDisabled() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETEXCLNULL);

        Boolean enabled = ipsPropertyDispatcher.pull(Aspect.of(EnabledAspectDefinition.NAME));

        assertThat(enabled, is(false));
    }

    @Test
    void testPull_Collection_IntegerRangeValueSet_ShouldContainNull() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETRANGEOFINTINCLNULL);


        Collection<Integer> rangeOfInteger = ipsPropertyDispatcher
                .pull(Aspect.of(AvailableValuesAspectDefinition.NAME, Collections.emptyList()));

        assertThat(rangeOfInteger, containsInAnyOrder(0, 1, 2, 3, 4, 5, null));
        assertThat(rangeOfInteger.size(), is(7));
    }

    @Test
    void testPull_Collection_IntegerRangeValueSet_ShouldNotContainNull() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETRANGEOFINTEXCLNULL);


        Collection<Integer> rangeOfInteger = ipsPropertyDispatcher
                .pull(Aspect.of(AvailableValuesAspectDefinition.NAME, Collections.emptyList()));

        assertThat(rangeOfInteger, containsInAnyOrder(0, 1, 2, 3, 4, 5));
        assertThat(rangeOfInteger.size(), is(6));
    }

    @Test
    void testPull_Collection_NotDescreteValueSet_ShouldReturnStaticValue() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETNOTDISCRETE);
        Object testValue = new Object();

        Object returnedValue = ipsPropertyDispatcher
                .pull(Aspect.of(AvailableValuesAspectDefinition.NAME, testValue));

        assertThat(returnedValue, is(testValue));
    }

    @Test
    void testPull_Collection_IntegerUnrestrictedValueSet_ShouldBeEmpty() {
        IpsPropertyDispatcher ipsPropertyDispatcher = ipsDispatcherChain(TestIpsObject.PROPERTY_VALUESETINTUNRESTRICTED);

        Collection<?> collection = ipsPropertyDispatcher
                .pull(Aspect.of(AvailableValuesAspectDefinition.NAME, Collections.emptyList()));

        assertThat(collection, is(empty()));
    }

    /*
     * Tests, if empty value set is used instead of delegating to wrapped property dispatcher
     * (NoOpPropertyDispatcher) which would return null
     */
    @Test
    void testPull_EmptyValueSet_ShouldBeEmpty() {
        IpsPropertyDispatcher ipsPropertyDispatcher = new IpsPropertyDispatcher(pmo::getIpsObject,
                TestIpsObject.PROPERTY_EMPTYVALUESET,
                new NoOpPropertyDispatcher());


        Collection<?> collection = ipsPropertyDispatcher
                .pull(Aspect.of(AvailableValuesAspectDefinition.NAME, Collections.emptyList()));

        assertThat(collection, is(empty()));
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

    public static class NoOpPropertyDispatcher implements PropertyDispatcher {

        @Override
        public String getProperty() {
            return null;
        }

        @Override
        public Object getBoundObject() {
            return null;
        }

        @Override
        public Class<?> getValueClass() {
            return null;
        }

        @Override
        public MessageList getMessages(MessageList messageList) {
            return null;
        }

        @Override
        public <T> T pull(Aspect<T> aspect) {
            return null;
        }

        @Override
        public <T> void push(Aspect<T> aspect) {
            // nop
        }

        @Override
        public <T> boolean isPushable(Aspect<T> aspect) {
            return false;
        }

    }

}

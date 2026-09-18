/*
 * Copyright Faktor Zehn GmbH.
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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.ui.aspects.annotation.BindBadgeSeverity;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.BadgeSeverity;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.impl.ThemeListImpl;

class BadgeSeverityAspectDefinitionTest {

    @Test
    void testCreateComponentValueSetter_SetsThemeNameForSeverity() {
        var component = new Button();

        apply(component, BadgeSeverity.SUCCESS);

        assertThat(component.getThemeNames(), contains("success"));
    }

    @Test
    void testCreateComponentValueSetter_SeverityChangeRemovesPreviousSeverityTheme() {
        var component = new Button();

        apply(component, BadgeSeverity.SUCCESS);
        apply(component, BadgeSeverity.ERROR);

        assertThat(component.getThemeNames(), contains("error"));
    }

    @Test
    void testCreateComponentValueSetter_InfoRemovesSeverityThemeWithoutAddingANewOne() {
        var component = new Button();

        apply(component, BadgeSeverity.SUCCESS);
        apply(component, BadgeSeverity.INFO);

        assertThat(component.getThemeNames(), empty());
    }

    @Test
    void testCreateComponentValueSetter_KeepsUnrelatedThemeNames() {
        var component = new Button();
        component.getThemeNames().add("badge");

        apply(component, BadgeSeverity.SUCCESS);
        apply(component, BadgeSeverity.ERROR);

        assertThat(component.getThemeNames(), containsInAnyOrder("badge", "error"));
    }

    @Test
    void testCreateComponentValueSetter_NullValueRemovesSeverityTheme() {
        var component = new Button();

        apply(component, BadgeSeverity.SUCCESS);
        apply(component, null);

        assertThat(component.getThemeNames(), empty());
    }

    @Test
    void testCreateComponentValueSetter_ComponentNotImplementingHasTheme() {
        var component = new Anchor();

        apply(component, BadgeSeverity.WARNING);

        assertThat(component.getElement().getAttribute(ThemeListImpl.THEME_ATTRIBUTE_NAME), is("warning"));
    }

    @Test
    void testCreateUiUpdater_PullsValueDynamicallyFromModel() {
        var component = new Button();
        PropertyDispatcher dispatcher = new PropertyDispatcherFactory()
                .createDispatcherChain(new TestPmo(), BoundProperty.of("status"),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);

        new BindBadgeSeverity.BadgeSeverityAspectDefinition()
                .createUiUpdater(dispatcher, new NoLabelComponentWrapper(component))
                .apply();

        assertThat(component.getThemeNames(), contains("warning"));
    }

    private void apply(Component component, BadgeSeverity severity) {
        new BindBadgeSeverity.BadgeSeverityAspectDefinition()
                .createUiUpdater(dispatcherFor(severity), new NoLabelComponentWrapper(component))
                .apply();
    }

    private PropertyDispatcher dispatcherFor(BadgeSeverity severity) {
        return new PropertyDispatcherFactory()
                .createDispatcherChain(new TestObject(severity), BoundProperty.empty(),
                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    private static class TestObject {

        private final BadgeSeverity severity;

        TestObject(BadgeSeverity severity) {
            this.severity = severity;
        }

        @SuppressWarnings("unused")
        public BadgeSeverity getBadgeSeverity() {
            return severity;
        }
    }

    private static class TestPmo {

        @SuppressWarnings("unused")
        public String getStatus() {
            return "urgent";
        }

        @SuppressWarnings("unused")
        public BadgeSeverity getStatusBadgeSeverity() {
            return BadgeSeverity.WARNING;
        }
    }
}

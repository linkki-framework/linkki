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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;

public class BindStyleNamesAspectDefinitionTest {

    @Test
    public void testCreateAspect_NoStyleNames() {
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(new String[] {});

        Aspect<Object> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindStyleNamesAspectDefinition.NAME));
        // Retrieved dynamically
        assertThat(createdAspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateAspect_SingleStyleName() {
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition("foo");

        Aspect<Object> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindStyleNamesAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(Arrays.asList("foo")));
    }

    @Test
    public void testCreateAspect_MultipleStyleNames() {
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition("foo", "bar");

        Aspect<Object> createdAspect = aspectDefinition.createAspect();

        assertThat(createdAspect.getName(), is(BindStyleNamesAspectDefinition.NAME));
        assertThat(createdAspect.getValue(), is(Arrays.asList("foo", "bar")));
    }

    @Test
    public void testCreateComponentValueSetter_Button_SingleStyleName() {
        String styleName = "bar";
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(styleName);
        Button button = ComponentFactory.newButton();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(button);
        Consumer<Object> componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(styleName);

        assertThat(button.getClassName(), equalTo(styleName));
    }

    @Test
    public void testCreateComponentValueSetter_Button_MultipleStyleNames() {
        String styleNames = "foo bar";
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(styleNames);
        Button button = ComponentFactory.newButton();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(button);
        Consumer<Object> componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(styleNames);

        assertThat(button.getClassName(), equalTo(styleNames));
    }

    @Test
    public void testCreateComponentValueSetter_Button_PredefinedAndMultipleStyleNames() {
        String styleNames = "foo bar";
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(styleNames);
        Button button = ComponentFactory.newButton();
        button.setClassName("predefinedStyleName");
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(button);
        Consumer<Object> componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(styleNames);

        assertThat(button.getClassName(), equalTo("predefinedStyleName " + styleNames));
    }

    @Test
    public void testCreateComponentValueSetter_Button_ClassCastException() {
        String[] styleNames = new String[] { "foo", "bar" };
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(styleNames);
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(ComponentFactory.newButton());
        Consumer<Object> componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        // styleNames should be either String or Collection<String>
        Assertions.assertThrows(ClassCastException.class, () -> componentValueSetter.accept(styleNames));
    }

    @Test
    public void testCreateComponentValueSetter_CustomField_ShouldThrowException() {
        String styleNames = "foo bar";
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(styleNames);
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(new CustomNoStyleField());

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> aspectDefinition.createComponentValueSetter(componentWrapper));
    }

    @Test
    public void testCreateComponentValueSetter_CustomField_ShouldNotThrowException() {
        String styleNames = "foo bar";
        BindStyleNamesAspectDefinition aspectDefinition = new BindStyleNamesAspectDefinition(styleNames);
        CustomHasStyleField customHasStyleField = new CustomHasStyleField();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(customHasStyleField);
        Consumer<Object> componentValueSetter = aspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(styleNames);

        assertThat(customHasStyleField.getClassName(), equalTo(styleNames));
    }

    /*
     * Used for testing java.lang.ClassCastException when components do not implement HasStyle interface
     */
    private static class CustomNoStyleField extends CustomField<String> {
        private static final long serialVersionUID = -3797442244423692802L;

        @Override
        protected String generateModelValue() {
            return "";
        }

        @Override
        protected void setPresentationValue(String newPresentationValue) {
            // nop
        }
    }

    private static class CustomHasStyleField extends CustomNoStyleField implements HasStyle {
        private static final long serialVersionUID = 7975236266257732777L;
    }
}

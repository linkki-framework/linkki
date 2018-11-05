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
package org.linkki.core.ui.section.annotations;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.linkki.core.ui.section.annotations.adapters.CheckboxBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.ComboboxBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.DateFieldBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.DoubleFieldBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.IntegerFieldBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.LabelBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.TextAreaBindingDefinition;
import org.linkki.core.ui.section.annotations.adapters.TextFieldBindingDefinition;

import com.vaadin.ui.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class BindingDefinitionTest {

    @UICheckBox(position = 0, caption = "")
    @UIComboBox(position = 0)
    @UIDateField(position = 0)
    @UIDoubleField(position = 0)
    @UIIntegerField(position = 0)
    @UITextArea(position = 0)
    @UITextField(position = 0)
    @UILabel(position = 0)
    @UIFooBar
    @UICustom
    @FooBar
    public void annotatedMethod() {
        // Nothing to do
    }

    private <T extends Annotation> T annotation(Class<T> annotationClass) {
        try {
            T annotation = getClass().getMethod("annotatedMethod", new Class<?>[] {}).getAnnotation(annotationClass);
            return requireNonNull(annotation, () -> "Missing annotation @" + annotationClass.getSimpleName());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIsLinkkiBindingDefinition() {
        assertThat(BindingDefinition.isLinkkiBindingDefinition(null), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(FooBar.class)),
                   is(false));

        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UICheckBox.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIComboBox.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIDateField.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIDoubleField.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIIntegerField.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UITextArea.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UITextField.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UILabel.class)), is(true));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UICustom.class)), is(true));
    }

    @Test
    public void testFrom() {
        //@formatter:off
        assertThat(BindingDefinition.from(annotation(UICheckBox.class)), is(instanceOf(CheckboxBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UIComboBox.class)), is(instanceOf(ComboboxBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UIDateField.class)), is(instanceOf(DateFieldBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UIDoubleField.class)), is(instanceOf(DoubleFieldBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UIIntegerField.class)), is(instanceOf(IntegerFieldBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UITextArea.class)), is(instanceOf(TextAreaBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UITextField.class)), is(instanceOf(TextFieldBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UILabel.class)), is(instanceOf(LabelBindingDefinition.class)));
        assertThat(BindingDefinition.from(annotation(UICustom.class)), is(instanceOf(UITestBindingDefinition.class)));
        //@formatter:on
    }

    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testFrom_ThrowsExceptionForNullAnnotation() {
        BindingDefinition.from(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrom_ThrowsExceptionForNonLinkkiElementAnnotation() {
        BindingDefinition.from(annotation(FooBar.class));
    }

    @Test(expected = RuntimeException.class)
    public void testFrom_ThrowsRuntimeExceptionIfInstantiationFails() {
        BindingDefinition.from(annotation(UIFooBar.class));
    }

}

interface UIFooBarBindingDefinition extends BindingDefinition {
    // no implementation class -> instantiation will fail
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(UIFooBarBindingDefinition.class)
@interface UIFooBar {
    // dummy
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(UITestBindingDefinition.class)
@interface UICustom {
    // dummy
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface FooBar {
    // dummy
}

@SuppressWarnings("null")
@SuppressFBWarnings
class UITestBindingDefinition implements BindingDefinition {

    /**
     * @param uiCustom ignored
     */
    public UITestBindingDefinition(UICustom uiCustom) {
        // ignored
    }

    @Override
    public EnabledType enabled() {
        return null;
    }

    @Override
    public VisibleType visible() {
        return null;
    }

    @Override
    public RequiredType required() {
        return null;
    }

    @Override
    public Component newComponent() {
        return null;
    }

    @Override
    public int position() {
        return 0;
    }

    @Override
    public String label() {
        return null;
    }

    @Override
    public boolean showLabel() {
        return false;
    }

    @Override
    public String modelObject() {
        return ModelObject.DEFAULT_NAME;
    }

    @Override
    public String modelAttribute() {
        return "";
    }

}

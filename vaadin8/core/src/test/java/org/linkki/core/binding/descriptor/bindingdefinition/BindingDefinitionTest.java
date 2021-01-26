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
package org.linkki.core.binding.descriptor.bindingdefinition;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;


@Deprecated
public class BindingDefinitionTest {

    @UICheckBox(position = 0, caption = "")
    @UIComboBox(position = 0, label = "")
    @UIDateField(position = 0, label = "")
    @UIDoubleField(position = 0, label = "")
    @UIIntegerField(position = 0, label = "")
    @UITextArea(position = 0, label = "")
    @UITextField(position = 0, label = "")
    @UILabel(position = 0, label = "")
    @UIFooBar
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

    /**
     * {@link BindingDefinition} interface is not used anymore
     */
    @Test
    public void testIsLinkkiBindingDefinition() {
        assertThat(BindingDefinition.isLinkkiBindingDefinition(null), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(FooBar.class)),
                   is(false));

        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UICheckBox.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIComboBox.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIDateField.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIDoubleField.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIIntegerField.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UITextArea.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UITextField.class)), is(false));
        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UILabel.class)), is(false));

        assertThat(BindingDefinition.isLinkkiBindingDefinition(annotation(UIFooBar.class)), is(true));
    }

    @Test
    public void testFrom_ThrowsExceptionForNullAnnotation() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            BindingDefinition.from(null);
        });
    }

    @Test
    public void testFrom_ThrowsExceptionForNonLinkkiElementAnnotation() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            BindingDefinition.from(annotation(FooBar.class));
        });
    }

    @Test
    public void testFrom_ThrowsRuntimeExceptionIfInstantiationFails() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            BindingDefinition.from(annotation(UIFooBar.class));
        });

    }

}

@SuppressWarnings("deprecation")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@LinkkiBindingDefinition(UIFooBaaBindingDefinition.class)
@interface UIFooBar {
    // dummy
}

@SuppressWarnings("deprecation")
class UIFooBaaBindingDefinition
        implements org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition {

    @Override
    public Object newComponent() {
        return null;
    }

    @Override
    public String label() {
        return null;
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
    public String modelObject() {
        return null;
    }

    @Override
    public String modelAttribute() {
        return null;
    }

}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface FooBar {
    // dummy
}

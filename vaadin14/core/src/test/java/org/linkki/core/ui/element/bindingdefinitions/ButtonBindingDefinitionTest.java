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
package org.linkki.core.ui.element.bindingdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.element.annotation.UIButton;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.NonNull;

@ExtendWith(MockitoExtension.class)
public class ButtonBindingDefinitionTest {

    private UIButton getAnnotation(String name) {
        try {
            @NonNull

            UIButton annotation = getClass().getMethod(name, new Class<?>[] {})
                    .getAnnotation(UIButton.class);
            return annotation;
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @UIButton(position = 0)
    public UIButton defaultAnnotation() {
        return getAnnotation("defaultAnnotation");
    }

    @UIButton(position = 0, showIcon = true, icon = VaadinIcon.AMBULANCE)
    public UIButton customAnnotation() {
        return getAnnotation("customAnnotation");
    }

    @UIButton(position = 0)
    public UIButton shortcutButton() {
        return getAnnotation("shortcutButton");
    }

    @UIButton(position = 1, showCaption = true, caption = "test")
    public UIButton anotherAnnotation() {
        return getAnnotation("anotherAnnotation");
    }

    @Test
    public void testNewComponent_DefaultAnnotations() {
        ButtonBindingDefinition adapter = new ButtonBindingDefinition(defaultAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(Button.class)));
        Button button = (Button)component;
        assertThat(button.getIcon(), is(nullValue()));
        assertThat(button.getClassName(), is(nullValue()));
    }

    @Test
    public void testNewComponent_CustomAnnotationsAreUsed() {
        ButtonBindingDefinition adapter = new ButtonBindingDefinition(customAnnotation());
        Component component = adapter.newComponent();
        assertThat(component, is(instanceOf(Button.class)));
        Button button = (Button)component;
        assertThat(button.getIcon().getElement().getAttribute("icon"),
                   is(VaadinIcon.AMBULANCE.create().getElement().getAttribute("icon")));
    }

}

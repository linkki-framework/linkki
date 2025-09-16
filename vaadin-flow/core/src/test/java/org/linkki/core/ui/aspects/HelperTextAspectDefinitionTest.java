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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.aspects.types.HelperTextType;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.textfield.TextField;

@ExtendWith(KaribuUIExtension.class)
class HelperTextAspectDefinitionTest {
    @Test
    void testCreateAspect_Static() {
        var aspectDefinition = new HelperTextAspectDefinition("This is a static helper text", HelperTextType.STATIC);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo("helperText");
        assertThat(aspect.getValue()).isEqualTo("This is a static helper text");

    }

    @Test
    void testCreateAspect_Dynamic() {
        var aspectDefinition = new HelperTextAspectDefinition("This helper text will be ignored",
                HelperTextType.DYNAMIC);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo("helperText");
        assertThatThrownBy(aspect::getValue).withFailMessage(() -> "There is no value for the aspect helperText")
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testCreateAspect_AutoWithEmptyValue() {
        var aspectDefinition = new HelperTextAspectDefinition("", HelperTextType.AUTO);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo("helperText");
        assertThatThrownBy(aspect::getValue).withFailMessage(() -> "There is no value for the aspect helperText")
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testCreateAspect_AutoWithNonEmptyValue() {
        var aspectDefinition = new HelperTextAspectDefinition("This is a static helper text", HelperTextType.AUTO);

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo("helperText");
        assertThat(aspect.getValue()).isEqualTo("This is a static helper text");
    }

    @Test
    void testCreateComponentValueSetter() {
        var aspectDefinition = new HelperTextAspectDefinition("", HelperTextType.AUTO);
        var textField = new TextField();

        var valueSetter = aspectDefinition.createComponentValueSetter(new NoLabelComponentWrapper(textField));
        valueSetter.accept("This is a helper text");

        assertThat(textField.getHelperText()).isEqualTo("This is a helper text");
    }
}
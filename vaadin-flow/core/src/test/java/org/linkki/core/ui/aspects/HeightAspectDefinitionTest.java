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

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.textfield.TextField;

class HeightAspectDefinitionTest {
    @Test
    void testCreateAspect() {
        var aspectDefinition = new HeightAspectDefinition("300px");

        var aspect = aspectDefinition.createAspect();

        assertThat(aspect.getName()).isEqualTo(HeightAspectDefinition.NAME);
        assertThat(aspect.getValue()).isEqualTo("300px");
    }

    @Test
    void testCreateComponentValueSetter() {
        var aspectDefinition = new HeightAspectDefinition("irrelevant");
        var textField = new TextField();

        var valueSetter = aspectDefinition.createComponentValueSetter(new NoLabelComponentWrapper(textField));
        valueSetter.accept("200%");

        assertThat(textField.getHeight()).isEqualTo("200%");
    }
}

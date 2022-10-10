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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.vaadin.component.base.LinkkiText;

class IconPositionAspectDefinitionTest {

    @Test
    void testCreateComponentValueSetter_SetIconPositionRight() {
        doIconPositionTest(IconPosition.RIGHT);
    }

    @Test
    void testCreateComponentValueSetter_SetIconPositionLeft() {
        doIconPositionTest(IconPosition.LEFT);
    }

    /**
     * Tests whether the {@link IconPosition icon position} is set properly.
     *
     * @param position The expected icon position
     */
    private void doIconPositionTest(IconPosition position) {
        LinkkiText label = new LinkkiText();
        Consumer<IconPosition> iconPositionSetter = new IconPositionAspectDefinition(position)
                .createComponentValueSetter(new NoLabelComponentWrapper(label));

        iconPositionSetter.accept(position);

        assertThat(label.getIconPosition()).isEqualTo(position);
    }
}

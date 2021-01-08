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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.linkki.samples.playground.uitest.matchers.WebElementMatchers.enabled;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.ReadOnlyBehaviorPmo;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;


public class BindReadOnlyBehaviorTest extends AbstractUiTest {

    @Test
    public void testBindReadOnlyBehavior() {
        assertThat($(ButtonElement.class).id(ReadOnlyBehaviorPmo.BUTTON_DISABLED_ON_READ_ONLY),
                   is(enabled()));
        assertThat($(TextFieldElement.class).id(ReadOnlyBehaviorPmo.TEXTFIELD_ACTIVE_ON_READ_ONLY),
                   is(enabled()));
        assertDoesNotThrow(() -> $(ButtonElement.class).id(ReadOnlyBehaviorPmo.BUTTON_INVISIBLE_ON_READ_ONLY));
    }

}

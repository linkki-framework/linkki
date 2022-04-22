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

package org.linkki.core.vaadin.component.base;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.ComponentFactory;

class LinkkiCheckBoxTest {

    @Test
    void testSetReadOnly_WhenWritable_ThenEnabled() {
        LinkkiCheckBox checkBox = ComponentFactory.newCheckbox();

        checkBox.setReadOnly(false);

        assertThat(checkBox.isEnabled()).isTrue();
        assertThat(checkBox.getElement().hasAttribute("readonly")).isFalse();
    }

    @Test
    void testSetReadOnly_WhenReadOnly_ThenDisabled() {
        LinkkiCheckBox checkBox = ComponentFactory.newCheckbox();

        checkBox.setReadOnly(true);

        assertThat(checkBox.isEnabled()).isFalse();
        assertThat(checkBox.getElement().hasAttribute("readonly")).isTrue();
    }

}

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
package org.linkki.core.ui.creation.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.ui.test.TestButtonPmo;

class ButtonPmoBinderTest {

    private final BindingContext bindingContext = new BindingContext();

    @Test
    void testButtonClickIsForwaredToPmo() {
        var pmo = mock(ButtonPmo.class);
        when(pmo.isEnabled()).thenReturn(true);
        var boundButton = ButtonPmoBinder.createBoundButton(bindingContext, pmo);

        boundButton.click();

        verify(pmo).onClick();
    }

    @Test
    void testUpdateFromPmo_PmoSublass() {
        var pmo = new TestButtonPmo();
        var button = ButtonPmoBinder.createBoundButton(bindingContext, pmo);

        pmo.setEnabled(true);
        pmo.setVisible(true);

        bindingContext.modelChanged();

        assertThat(button.isVisible()).isTrue();
        assertThat(button.isEnabled()).isTrue();

        pmo.setEnabled(false);
        pmo.setVisible(false);

        bindingContext.modelChanged();

        assertThat(button.isVisible()).isFalse();
        assertThat(button.isEnabled()).isFalse();
    }

    @Test
    void testCreateBoundButton_Id() {
        var pmo = mock(ButtonPmo.class);

        var boundButton = ButtonPmoBinder.createBoundButton(bindingContext, pmo);

        assertThat(boundButton.getId()).hasValue("buttonPmo");
    }

}

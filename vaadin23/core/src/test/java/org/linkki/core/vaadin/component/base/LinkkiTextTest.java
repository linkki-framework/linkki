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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.mockito.Mockito;

import com.vaadin.flow.component.icon.VaadinIcon;

public class LinkkiTextTest {

    private LinkkiText linkkiText;

    @BeforeEach
    public void setup() {
        linkkiText = new LinkkiText();
        linkkiText.setText("Test text");
        linkkiText.setIcon(VaadinIcon.ABACUS);
    }

    @Test
    public void testSetText() {
        linkkiText.setText("New test text");

        assertThat(linkkiText.getText()).isEqualTo("New test text");
        assertThat(linkkiText.getIcon()).isEqualTo(VaadinIcon.ABACUS);
        assertThat(linkkiText.getClassName()).contains(LinkkiTheme.HAS_ICON);
    }

    @Test
    public void testSetIcon() {
        linkkiText.setIcon(VaadinIcon.ADJUST);

        assertThat(linkkiText.getIcon()).isEqualTo(VaadinIcon.ADJUST);
        assertThat(linkkiText.getClassName()).contains(LinkkiTheme.HAS_ICON);
    }

    @Test
    public void testRemoveIcon() {
        linkkiText.setIcon(null);

        assertThat(linkkiText.getIcon()).isNull();
        assertThat(linkkiText.getClassName()).doesNotContain(LinkkiTheme.HAS_ICON);
    }

    @Test
    public void testUpdateIsNotCalledWhenTextNotChanged() {
        LinkkiText spy = Mockito.spy(linkkiText);
        linkkiText.setText("Test text");

        verify(spy, never()).removeAll();
    }

    @Test
    public void testUpdateIsNotCalledWhenIconNotChanged() {
        LinkkiText spy = Mockito.spy(linkkiText);
        linkkiText.setIcon(VaadinIcon.ABACUS);

        verify(spy, never()).removeAll();
    }

}

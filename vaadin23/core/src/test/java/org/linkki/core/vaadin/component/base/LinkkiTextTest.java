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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.aspects.types.IconPosition;

import com.vaadin.flow.component.icon.VaadinIcon;

class LinkkiTextTest {

    private LinkkiText linkkiText;

    @BeforeEach
    void setup() {
        linkkiText = new LinkkiText();
        linkkiText.setText("Test text");
        linkkiText.setIcon(VaadinIcon.ABACUS);
    }

    @Test
    void testSetText() {
        linkkiText.setText("New test text");

        assertThat(linkkiText.getText()).isEqualTo("New test text");
        assertThat(linkkiText.getIcon()).isEqualTo(VaadinIcon.ABACUS);
    }

    @Test
    void testSetText_HtmlContent() {
        linkkiText.setText("<html>New test html text</html>", true);

        assertThat(linkkiText.getText()).isEqualTo("<html>New test html text</html>");
    }

    @Test
    void testSetIcon() {
        linkkiText.setIcon(VaadinIcon.ADJUST);

        assertThat(linkkiText.getIcon()).isEqualTo(VaadinIcon.ADJUST);
    }

    @Test
    void testRemoveIcon() {
        linkkiText.setIcon(null);

        assertThat(linkkiText.getIcon()).isNull();
        assertThat(linkkiText.getClassName()).isEqualTo(LinkkiText.CLASS_NAME);
    }

    @Test
    void testSetIconPosition() {
        linkkiText.setIconPosition(IconPosition.RIGHT);

        assertThat(linkkiText.getIconPosition()).isEqualTo(IconPosition.RIGHT);
    }
}

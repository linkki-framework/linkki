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
        linkkiText.setText("<b>New test html text</b>", true);

        assertThat(linkkiText.getText()).isEqualTo("<b>New test html text</b>");
    }

    @Test
    void testSetText_HtmlContent_WithStylingAttribute() {
        linkkiText.setText("<b style=\"color: red;\">New test html text</b>", true);

        assertThat(linkkiText.getText()).isEqualTo("<b style=\"color: red;\">New test html text</b>");
    }

    @Test
    void testSetText_SanitizeForbiddenHtmlTag() {
        linkkiText.setText("<b><iframe>Test</iframe></b>", true);

        assertThat(linkkiText.getText()).isEqualTo("<b>Test</b>");
    }

    @Test
    void testSetText_SanitizeHtmlAttribute() {
        linkkiText.setText("<b><i onload=\"alert('text');\"/>Test</b>", true);

        assertThat(linkkiText.getText()).isEqualTo("<b><i></i>Test</b>");
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

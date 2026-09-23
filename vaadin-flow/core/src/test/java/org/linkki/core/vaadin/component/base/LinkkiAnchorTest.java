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

package org.linkki.core.vaadin.component.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.streams.DownloadHandler;

@ExtendWith(KaribuUIExtension.class)
class LinkkiAnchorTest {

    @Test
    void testAnchor() {
        var anchor = new LinkkiAnchor();

        assertThat(anchor.getText()).isEmpty();
        assertThat(anchor.getIcon()).isNull();
    }

    @Test
    void testSetText() {
        var anchor = new LinkkiAnchor();

        anchor.setText("test");

        assertThat(anchor.getText()).isEqualTo("test");
    }

    @Test
    void testSetText_KeepsExistingIcon() {
        var anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ARCHIVE);

        anchor.setText("test");

        assertThat(anchor.getText()).isEqualTo("test");
        assertThat(anchor.getIcon()).isEqualTo(VaadinIcon.ARCHIVE);
    }

    @Test
    void testSetIcon() {
        var anchor = new LinkkiAnchor();

        anchor.setIcon(VaadinIcon.PLUS);

        assertThat(anchor.getIcon()).isEqualTo(VaadinIcon.PLUS);
        assertThat(anchor.getPrefixComponent().getElement().getProperty("icon"))
                .isEqualTo(VaadinIcon.PLUS.create().getElement().getProperty("icon"));
    }

    @Test
    void testSetIcon_RemovesPreviousIcon() {
        var anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.PLUS);

        anchor.setIcon(null);

        assertThat(anchor.getIcon()).isNull();
    }

    @Test
    void testSetIcon_KeepsExistingText() {
        var anchor = new LinkkiAnchor();
        anchor.setText("test");

        anchor.setIcon(VaadinIcon.ARCHIVE);

        assertThat(anchor.getText()).isEqualTo("test");
        assertThat(anchor.getIcon()).isEqualTo(VaadinIcon.ARCHIVE);
    }

    @Test
    void testSetHref_DownloadHandler() {
        var anchor = new LinkkiAnchor();
        assertThat(anchor.getContent().getHref()).isBlank();
        var downloadHandler = mock(DownloadHandler.class);

        anchor.setHref(downloadHandler);

        assertThat(anchor.getContent().getHref()).isNotBlank();
    }
}

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

package org.linkki.core.vaadin.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.icon.VaadinIcon;

class LinkkiAnchorTest {

    @Test
    void testAnchor() {
        LinkkiAnchor anchor = new LinkkiAnchor();

        assertThat(anchor.getText()).isEqualTo("");
        assertThat(anchor.getIcon()).isNull();
    }

    @Test
    void testSetText() {
        LinkkiAnchor anchor = new LinkkiAnchor();

        anchor.setText("test");

        assertThat(anchor.getText()).isEqualTo("test");
    }

    @Test
    void testSetText_KeepsExistingIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ARCHIVE);

        anchor.setText("test");

        assertThat(anchor.getText()).isEqualTo("test");
        assertThat(anchor.getIcon()).isEqualTo(VaadinIcon.ARCHIVE);
    }

    @Test
    void testSetIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();

        anchor.setIcon(VaadinIcon.ABACUS);

        assertThat(anchor.getIcon()).isEqualTo(VaadinIcon.ABACUS);
        assertThat(anchor.getPrefixComponent().getElement().getProperty("icon"))
                .isEqualTo(VaadinIcon.ABACUS.create().getElement().getProperty("icon"));
    }

    @Test
    void testSetIcon_RemovesPreviousIcon() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setIcon(VaadinIcon.ABACUS);

        anchor.setIcon(null);

        assertThat(anchor.getIcon()).isNull();
    }

    @Test
    void testSetIcon_KeepsExistingText() {
        LinkkiAnchor anchor = new LinkkiAnchor();
        anchor.setText("test");

        anchor.setIcon(VaadinIcon.ARCHIVE);

        assertThat(anchor.getText()).isEqualTo("test");
        assertThat(anchor.getIcon()).isEqualTo(VaadinIcon.ARCHIVE);
    }

}

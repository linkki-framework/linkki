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

package org.linkki.framework.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.ui.theme.LinkkiTheme;

import com.vaadin.flow.component.icon.VaadinIcon;

class MessageRowPmoTest {

    private MessageRowPmo pmo;

    @Test
    void testGetStyleNames_Error() {
        pmo = new MessageRowPmo(Message.newError("code", "error"));
        assertThat(pmo.getTextStyleNames()).contains("linkki-message-row", LinkkiTheme.Text.ICON_ERROR);
    }

    @Test
    void testGetStyleNames_Warning() {
        pmo = new MessageRowPmo(Message.newWarning("code", "warning"));
        assertThat(pmo.getTextStyleNames()).contains("linkki-message-row", LinkkiTheme.Text.ICON_WARNING);
    }

    @Test
    void testGetStyleNames_Info() {
        pmo = new MessageRowPmo(Message.newInfo("code", "info"));
        assertThat(pmo.getTextStyleNames()).contains("linkki-message-row", LinkkiTheme.Text.ICON_INFO);
    }

    @Test
    void testGetTextIcon_Error() {
        pmo = new MessageRowPmo(Message.newError("code", "error"));
        assertThat(pmo.getTextIcon()).isEqualTo(VaadinIcon.EXCLAMATION_CIRCLE);
    }

    @Test
    void testGetTextIcon_Warning() {
        pmo = new MessageRowPmo(Message.newWarning("code", "warning"));
        assertThat(pmo.getTextIcon()).isEqualTo(VaadinIcon.WARNING);
    }

    @Test
    void testGetTextIcon_Info() {
        pmo = new MessageRowPmo(Message.newInfo("code", "info"));
        assertThat(pmo.getTextIcon()).isEqualTo(VaadinIcon.INFO_CIRCLE);
    }

    @Test
    void testGetText() {
        pmo = new MessageRowPmo(Message.newInfo("code", "I am an information"));
        assertThat(pmo.getText()).isEqualTo("I am an information");
    }

}

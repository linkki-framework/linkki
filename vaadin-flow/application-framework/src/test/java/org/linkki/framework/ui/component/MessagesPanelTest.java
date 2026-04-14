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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.not;
import static org.linkki.core.ui.test.ComponentConditions.visible;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.test.ComponentTreeRepresentation;
import org.linkki.core.ui.test.KaribuUtils.Grids;

class MessagesPanelTest {

    @Test
    void testDefaultCaption() {
        var panel = new MessagesPanel();
        panel.showMessages(new MessageList(Message.newInfo("1", "1")));

        assertThat(panel.getCaption()).isEqualTo("Messages");
    }

    @Test
    void testCustomCaption() {
        var panel = new MessagesPanel("Validierungsfehler");
        panel.showMessages(new MessageList(Message.newInfo("1", "1")));

        assertThat(panel.getCaption()).isEqualTo("Validierungsfehler");
    }

    @Test
    void testShowMessages_Null() {
        var panel = new MessagesPanel();

        assertThatNullPointerException().isThrownBy(() -> panel.showMessages(null));
    }

    @Test
    void testShowMessages() {
        var panel = new MessagesPanel();
        assertThat(panel.getGrid()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));

        var messages = new MessageList(Message.newError("message1", "text1"),
                Message.newInfo("message2", "text2"));
        panel.showMessages(messages);

        assertThat(panel.getGrid()).withRepresentation(new ComponentTreeRepresentation())
                .is(visible());
        assertThat(Grids.getTextContentsInColumn(panel.getGrid(), "message"))
                .containsExactly("text1", "text2");

        panel.showMessages(new MessageList());

        assertThat(panel.getGrid()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));
    }
}

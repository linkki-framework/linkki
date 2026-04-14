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
import static org.assertj.core.api.Assertions.not;
import static org.linkki.core.ui.test.ComponentConditions.visible;
import static org.linkki.core.ui.test.KaribuUtils.getTextContent;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.test.ComponentTreeRepresentation;
import org.linkki.core.ui.test.KaribuUtils.Grids;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

class MessagesSplitLayoutTest {

    @Test
    void testDefaultConstructor_HasMessagesPanel() {
        var layout = new MessagesSplitLayout();

        assertThat(layout.getThemeNames()).contains(SplitLayoutVariant.LUMO_MINIMAL.getVariantName());
        assertThat(layout.getOrientation()).isEqualTo(SplitLayout.Orientation.VERTICAL);
        assertThat(layout.getSplitterPosition()).isEqualTo(100d);
        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .isInstanceOf(MessagesPanel.class)
                .is(not(visible()));
    }

    @Test
    void testSetContentComponent() {
        var layout = new MessagesSplitLayout();
        var content = new Span("text");

        layout.setContentComponent(content);

        assertThat(getTextContent(layout.getPrimaryComponent())).isEqualTo("text");
    }

    @Test
    void testSetContentComponent_ReplacesExisting() {
        var layout = new MessagesSplitLayout();
        layout.setContentComponent(new Span("old text"));

        layout.setContentComponent(new Span("new text"));

        assertThat(getTextContent(layout.getPrimaryComponent()))
                .isNotEqualTo("old text")
                .isEqualTo("new text");
        assertThat(layout.getPrimaryComponent()).is(visible());
    }

    @Test
    void testSetMessageComponent_ReplacesExisting() {
        var layout = new MessagesSplitLayout();
        var newPanel = new MessagesPanel("Fehler");

        layout.setMessageComponent(newPanel);

        assertThat(layout.getSecondaryComponent().getCaption()).isEqualTo("Fehler");
    }

    @Test
    void testSetMessageComponent_PanelInitiallyHidden() {
        var layout = new MessagesSplitLayout();
        var newPanel = new MessagesPanel("Fehler");

        layout.setMessageComponent(newPanel);

        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));
    }

    @Test
    void testDisplayMessages_NoMessages() {
        var layout = new MessagesSplitLayout();

        layout.displayMessages(new MessageList());

        assertThat(layout.getSplitterPosition()).isEqualTo(100d);
        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));
        assertThat(layout.getThemeNames())
                .contains(SplitLayoutVariant.LUMO_MINIMAL.getVariantName())
                .doesNotContain(SplitLayoutVariant.LUMO_SMALL.getVariantName());
    }

    @Test
    void testDisplayMessages_WithMessages() {
        var layout = new MessagesSplitLayout();

        var messages = new MessageList(Message.newError("code1", "text1"),
                Message.newWarning("code2", "text2"));
        layout.displayMessages(messages);

        assertThat(layout.getSplitterPosition()).isEqualTo(80d);
        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(visible());
        assertThat(layout.getThemeNames())
                .contains(SplitLayoutVariant.LUMO_SMALL.getVariantName())
                .doesNotContain(SplitLayoutVariant.LUMO_MINIMAL.getVariantName());
        assertThat(Grids.getTextContentsInColumn(layout.getSecondaryComponent().getGrid(), "message"))
                .containsExactly("text1", "text2");
    }

    @Test
    void testDisplayMessages_HidesAfterMessagesCleared() {
        var layout = new MessagesSplitLayout();
        layout.displayMessages(new MessageList(Message.newError("code1", "text1")));

        layout.displayMessages(new MessageList());

        assertThat(layout.getSplitterPosition()).isEqualTo(100d);
        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));
    }

    @Test
    void testDisplayMessages_ShowsAfterMessagesAdded() {
        var layout = new MessagesSplitLayout();
        layout.displayMessages(new MessageList());
        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));
        assertThat(layout.getSplitterPosition()).isEqualTo(100d);

        layout.displayMessages(new MessageList(Message.newError("code1", "text1")));

        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .satisfies(visible());
        assertThat(layout.getSplitterPosition()).isEqualTo(80d);
    }
}

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

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.test.ComponentTreeRepresentation;
import org.linkki.core.ui.test.KaribuUtils.Grids;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;

class MessageUiComponentsTest {

    @Test
    void testCreateMessageTable() {
        var component = MessageUiComponents.createMessageTable("Title", MessageList::new, new BindingContext());

        var children = component.getChildren().toList();
        assertThat(children).hasSize(2);
        assertThat(children.get(0)).isInstanceOf(Span.class)
                .satisfies(c -> assertThat(((Span)c).getText()).isEqualTo("Title"));
        assertThat(children.get(1)).isInstanceOf(Grid.class);
    }

    @Test
    void testCreateValidationMessagesPanel() {
        var messages = new AtomicReference<>(new MessageList());
        var bindingManager = new DefaultBindingManager(messages::get);
        var bindingContext = bindingManager.getContext("ctx");
        var otherBindingContext = bindingManager.getContext("otherCtx");

        var panel = MessageUiComponents.createValidationMessagesPanel("Errors", bindingContext);

        assertThat(panel).isInstanceOf(MessagesPanel.class);
        assertThat(panel.getCaption()).isEqualTo("Errors");
        assertThat(panel.getGrid()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));

        messages.set(new MessageList(Message.newError("code", "text")));
        otherBindingContext.modelChanged();

        assertThat(panel.getGrid()).withRepresentation(new ComponentTreeRepresentation())
                .is(visible());
        assertThat(Grids.getTextContentsInColumn(panel.getGrid(), "message"))
                .containsExactly("text");
    }

    @Test
    void testHandleMessagesAfterValidation_ShowsMessagesAfterValidation() {
        var messages = new AtomicReference<>(new MessageList());
        var bindingManager = new DefaultBindingManager(messages::get);
        var bindingContext = bindingManager.getContext("ctx");
        var otherBindingContext = bindingManager.getContext("otherCtx");
        var layout = new MessagesSplitLayout();
        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));

        MessageUiComponents.handleMessagesAfterValidation(layout, bindingContext);
        messages.set(new MessageList(Message.newError("code", "text")));
        otherBindingContext.modelChanged();

        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(visible());
        assertThat(layout.getSplitterPosition()).isEqualTo(80d);
        assertThat(Grids.getTextContentsInColumn(layout.getSecondaryComponent().getGrid(), "message"))
                .containsExactly("text");

        messages.set(new MessageList());
        otherBindingContext.modelChanged();

        assertThat(layout.getSecondaryComponent()).withRepresentation(new ComponentTreeRepresentation())
                .is(not(visible()));
        assertThat(layout.getSplitterPosition()).isEqualTo(100d);
    }
}

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
package org.linkki.samples.playground.products;

import java.io.Serial;
import java.util.Optional;

import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.dom.Style;

/**
 * Display a content component and a collapsible panel displaying messages. The message panel is
 * hidden when no messages are present and shown automatically when messages are available.
 * 
 * @since 2.10.0
 */
public class MessagesSplitLayout extends SplitLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    private final double splitterPosition;

    /**
     * Creates a new {@link MessagesSplitLayout} default splitter position, a default message panel,
     * without a content component.
     * <p>
     * Use {@link #setContentComponent(Component)} to set the content. Use
     * {@link #setMessageComponent(MessagesPanel)} to set a customized message panel.
     */
    public MessagesSplitLayout() {
        this(80d);
        setMessageComponent(new MessagesPanel());
    }

    private MessagesSplitLayout(double splitterPosition) {
        this.splitterPosition = splitterPosition;
        getStyle().setFlexGrow("1");
        setOrientation(Orientation.VERTICAL);
    }

    public void setContentComponent(Component contentComponent) {
        if (getPrimaryComponent() != null) {
            getPrimaryComponent().removeFromParent();
        }
        addToPrimary(contentComponent);
        contentComponent.getStyle().setOverflow(Style.Overflow.AUTO);
    }

    public void setMessageComponent(MessagesPanel messageComponent) {
        if (getSecondaryComponent() != null) {
            getSecondaryComponent().removeFromParent();
        }
        addToSecondary(messageComponent);
        messageComponent.getStyle().setOverflow(Style.Overflow.AUTO);
        hideMessagePanel();
    }

    @Override
    public MessagesPanel getSecondaryComponent() {
        return (MessagesPanel)super.getSecondaryComponent();
    }

    /** Hides the message panel and gives the content area the full space. */
    private void hideMessagePanel() {
        removeThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        setSplitterPosition(100d);
        Optional.ofNullable(getSecondaryComponent()).ifPresent(c -> c.setVisible(false));
    }

    /** Shows the message panel at the configured splitter position. */
    private void showMessagePanel() {
        removeThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        setSplitterPosition(splitterPosition);
        Optional.ofNullable(getSecondaryComponent()).ifPresent(c -> c.setVisible(true));
    }

    /**
     * Sets the messages in the message panel and updates the visibility of the message panel based
     * on the given messages. The panel is shown if messages are present, and hidden otherwise.
     *
     * @param messages the messages to display
     */
    public void displayMessages(MessageList messages) {
        if (messages.iterator().hasNext()) {
            showMessagePanel();
        } else {
            hideMessagePanel();
        }
        if (getSecondaryComponent() != null) {
            getSecondaryComponent().showMessages(messages);
        }
    }
}

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
package org.linkki.framework.ui.component;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.util.HtmlSanitizer;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @deprecated since 13 March 2019, {@link MessageUiComponents#createMessageTable} can be used instead.
 */
@Deprecated
public class MessageListPanel extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private VerticalLayout messages = new VerticalLayout();

    public MessageListPanel(String text) {
        addStyleName(LinkkiApplicationTheme.MESSAGE_LIST_STYLE);
        setWidth("400px");

        Label infoLabel = new Label();
        infoLabel.setWidth("100%");
        infoLabel.setValue(HtmlSanitizer.sanitize(text));
        infoLabel.setStyleName(ValoTheme.LABEL_H3);
        infoLabel.setContentMode(ContentMode.HTML);
        addComponent(infoLabel);

        Panel panel = new Panel();
        addComponent(panel);
        panel.setContent(messages);
        panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        panel.addStyleName(LinkkiApplicationTheme.MESSAGE_LIST_STYLE);
    }

    public MessageListPanel(String text, MessageList messageList) {
        this(text);
        setMessageList(messageList);
    }

    public void setMessageList(MessageList messageList) {
        messages.removeAllComponents();
        for (Message message : messageList) {
            messages.addComponent(new MessageRow(message));
        }
    }

}
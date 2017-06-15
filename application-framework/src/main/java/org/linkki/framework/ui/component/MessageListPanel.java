/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component;

import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MessageListPanel extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private Label infoLabel = new Label();

    private VerticalLayout messages = new VerticalLayout();

    public MessageListPanel(String text) {
        addStyleName(LinkkiStyles.MESSAGE_LIST_STYLE);
        setWidth("400px");
        infoLabel.setValue(text);
        infoLabel.setStyleName(ValoTheme.LABEL_H3);
        infoLabel.setContentMode(ContentMode.HTML);
        addComponent(infoLabel);
        Panel panel = new Panel();
        addComponent(panel);
        panel.setContent(messages);
        panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        panel.addStyleName(LinkkiStyles.MESSAGE_LIST_STYLE);
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

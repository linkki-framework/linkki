/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component;

import java.util.List;
import java.util.stream.Collectors;

import org.faktorips.runtime.Message;
import org.faktorips.runtime.MessageList;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.util.StreamUtil;
import org.vaadin.viritin.ListContainer;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MessageListPanel extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private Label infoLabel = new Label();

    private VerticalLayout messages = new VerticalLayout();

    private MessageListContainer container = new MessageListContainer();

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
        container.setMessageList(messageList);
    }

    private static class MessageListContainer extends ListContainer<MessageViewItem> {

        private static final long serialVersionUID = 1L;

        private List<MessageViewItem> list;

        public MessageListContainer() {
            super(MessageViewItem.class);
        }

        @Override
        protected List<MessageViewItem> getBackingList() {
            return list;
        }

        public void setMessageList(MessageList messageList) {
            this.list = StreamUtil.stream(messageList).map(m -> new MessageViewItem(m)).collect(Collectors.toList());
        }

    }

    public static class MessageViewItem {

        private final Message message;

        public MessageViewItem(Message message) {
            this.message = message;
        }

        public String getText() {
            return message.getText();
        }

        public Resource getIcon() {
            switch (message.getSeverity()) {
                case ERROR:
                    return FontAwesome.EXCLAMATION_CIRCLE;
                case WARNING:
                    return FontAwesome.EXCLAMATION;
                default:
                    return FontAwesome.INFO;
            }
        }

    }

}

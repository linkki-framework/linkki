package org.linkki.samples.messages.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;

public class MessagesPanel extends Panel {

    private static final long serialVersionUID = 1239168264524211917L;

    public void updateMessages(MessageList messages) {
        boolean hasErrors = !messages.isEmpty();
        setVisible(hasErrors);
        VerticalLayout layout = new VerticalLayout();
        for (Message message : messages) {
            Label msgLbl = new Label();
            msgLbl.setIcon(getMessageIcon(message));
            msgLbl.setCaption(message.getText());
            msgLbl.setContentMode(ContentMode.PREFORMATTED);
            layout.addComponent(msgLbl);
        }
        setContent(layout);
    }

    private FontAwesome getMessageIcon(Message message) {
        switch (message.getErrorLevel()) {
            case INFORMATION:
                return FontAwesome.INFO_CIRCLE;
            case WARNING:
                return FontAwesome.EXCLAMATION_TRIANGLE;
            default:
                return FontAwesome.EXCLAMATION_CIRCLE;
        }
    }

}

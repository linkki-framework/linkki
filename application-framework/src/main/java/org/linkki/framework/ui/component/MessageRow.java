package org.linkki.framework.ui.component;

import org.linkki.core.message.Message;
import org.linkki.core.ui.application.ApplicationStyles;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

public class MessageRow extends FormLayout {

    private static final long serialVersionUID = 1L;

    private final MessagePmo messagePmo;

    public MessageRow(Message message) {
        this.messagePmo = new MessagePmo(message);
        Label label = new Label();
        label.setIcon(getIcon());
        label.setContentMode(ContentMode.HTML);
        label.setValue(getText());
        label.addStyleName(messagePmo.getStyle());
        label.setDescription(this.messagePmo.getToolTip());
        addComponent(label);
        addStyleName(ApplicationStyles.MESSAGE_ROW);
    }

    public String getText() {
        return messagePmo.getText();
    }

    @Override
    public FontAwesome getIcon() {
        return messagePmo.getIcon();
    }

}
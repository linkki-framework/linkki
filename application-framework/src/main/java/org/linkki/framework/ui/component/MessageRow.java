package org.linkki.framework.ui.component;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.ObjectProperty;
import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

public class MessageRow extends FormLayout {

    private static final long serialVersionUID = 1L;

    private final Message message;

    public MessageRow(Message message) {
        this.message = message;
        Label label = new Label();
        label.setIcon(getIcon());
        label.setContentMode(ContentMode.HTML);
        label.setValue(getText());
        label.addStyleName(getStyle());
        label.setDescription(getToolTip());
        addComponent(label);
    }

    public String getText() {
        return message.getText();
    }

    @Override
    public FontAwesome getIcon() {
        switch (message.getSeverity()) {
            case ERROR:
                return FontAwesome.EXCLAMATION_CIRCLE;
            case WARNING:
                return FontAwesome.EXCLAMATION_TRIANGLE;
            default:
                return FontAwesome.INFO_CIRCLE;
        }
    }

    public String getStyle() {
        return LinkkiStyles.MESSAGE_PREFIX + message.getSeverity().toString().toLowerCase();
    }

    private String getToolTip() {
        String text = message.getInvalidObjectProperties().stream().map(this::getPropertyDesc)
                .collect(Collectors.joining(", "));
        return text;
    }

    private String getPropertyDesc(ObjectProperty op) {
        String simpleName = op.getObject().getClass().getSimpleName();
        if (StringUtils.isEmpty(op.getProperty())) {
            return simpleName;
        } else {
            return simpleName + ": " + op.getProperty();
        }
    }

}
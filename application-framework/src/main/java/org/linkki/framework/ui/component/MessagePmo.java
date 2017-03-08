/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.component;

import static java.util.Objects.requireNonNull;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.faktorips.runtime.Message;
import org.faktorips.runtime.ObjectProperty;
import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.server.FontAwesome;

/** PMO for {@link Message}. */
public class MessagePmo {

    private final Message message;

    public MessagePmo(Message message) {
        this.message = requireNonNull(message, "message must not be null");
    }

    public String getStyle() {
        return LinkkiStyles.MESSAGE_PREFIX + message.getSeverity().toString().toLowerCase();
    }

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

    public String getText() {
        return message.getText();
    }

    public String getToolTip() {
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

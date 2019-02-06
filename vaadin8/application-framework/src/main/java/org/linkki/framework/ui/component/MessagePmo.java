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

import static java.util.Objects.requireNonNull;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.message.Message;
import org.linkki.core.message.ObjectProperty;
import org.linkki.framework.ui.LinkkiStyles;

import com.vaadin.icons.VaadinIcons;

/** PMO for {@link Message}. */
public class MessagePmo {

    private final Message message;

    public MessagePmo(Message message) {
        this.message = requireNonNull(message, "message must not be null");
    }

    public String getStyle() {
        return LinkkiStyles.MESSAGE_PREFIX + message.getSeverity().name().toLowerCase();
    }

    public VaadinIcons getIcon() {
        switch (message.getSeverity()) {
            case ERROR:
                return VaadinIcons.EXCLAMATION_CIRCLE;
            case WARNING:
                return VaadinIcons.WARNING;
            default:
                return VaadinIcons.INFO_CIRCLE;
        }
    }

    public String getText() {
        return message.getText();
    }

    public String getTooltip() {
        String text = message.getInvalidObjectProperties().stream()
                .map(this::getPropertyDesc)
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

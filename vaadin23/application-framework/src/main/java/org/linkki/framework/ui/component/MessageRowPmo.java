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

import java.util.Arrays;
import java.util.List;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.framework.ui.LinkkiApplicationTheme;

import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * PMO for a {@link Message}, displaying an icon derived from the {@link Severity} and the
 * {@link Message#getText() text}. This should be used in a {@link MessageTablePmo} or standalone to
 * display a single message.
 */
public class MessageRowPmo {

    private final Message message;

    public MessageRowPmo(Message message) {
        this.message = requireNonNull(message, "message must not be null");
    }

    @UITableColumn(flexGrow = 1)
    @UILabel(position = 0)
    @BindIcon
    @BindStyleNames
    public String getText() {
        return message.getText();
    }

    public VaadinIcon getTextIcon() {
        return MessageUiComponents.getIcon(message.getSeverity());
    }

    public List<String> getTextStyleNames() {
        return Arrays.asList(LinkkiApplicationTheme.MESSAGE_ROW,
                             MessageUiComponents.getStyle(message.getSeverity()));
    }

}

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
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.defaults.ui.element.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.element.aspects.types.TooltipType;
import org.linkki.core.ui.element.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.table.column.annotation.UITableColumn;

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

    @UITableColumn(expandRatio = 0)
    @UILabel(position = 10, htmlContent = true)
    @BindTooltip(tooltipType = TooltipType.DYNAMIC)
    @BindStyleNames
    public String getIcon() {
        return MessageUiComponents.getIcon(message.getSeverity()).getHtml();
    }

    public List<String> getIconStyleNames() {
        return Arrays.asList(LinkkiTheme.MESSAGE_ROW, MessageUiComponents.getStyle(message.getSeverity()));
    }

    public String getIconTooltip() {
        return MessageUiComponents.getInvalidObjectPropertiesAsString(message);
    }

    @UITableColumn(expandRatio = 1)
    @UILabel(position = 20, styleNames = LinkkiTheme.MESSAGE_ROW)
    public String getText() {
        return message.getText();
    }

}

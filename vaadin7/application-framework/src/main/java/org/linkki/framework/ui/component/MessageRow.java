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
import org.linkki.core.defaults.style.LinkkiStyles;

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
        label.setDescription(this.messagePmo.getTooltip());
        addComponent(label);
        addStyleName(LinkkiStyles.MESSAGE_ROW);
    }

    public String getText() {
        return messagePmo.getText();
    }

    @Override
    public FontAwesome getIcon() {
        return messagePmo.getIcon();
    }

}
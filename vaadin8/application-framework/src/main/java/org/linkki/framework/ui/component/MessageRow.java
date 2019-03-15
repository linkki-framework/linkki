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

import org.linkki.core.message.Message;
import org.linkki.core.ui.application.ApplicationStyles;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;

/***
 * @deprecated since 13 March 2019; {@link MessageUiComponents#createMessageLabel(Message)} can be used
 *             instead.
 */
@Deprecated
public class MessageRow extends FormLayout {

    private static final long serialVersionUID = 1L;

    private final MessagePmo messagePmo;

    public MessageRow(Message message) {
        this.messagePmo = new MessagePmo(message);
        Label label = new Label();
        label.setWidth("100%");
        label.setIcon(getIcon());
        label.setContentMode(ContentMode.HTML);
        label.setValue(getCaption());
        label.addStyleName(messagePmo.getStyle());
        label.setDescription(this.messagePmo.getTooltip());
        addComponent(label);
        addStyleName(ApplicationStyles.MESSAGE_ROW);
    }

    public String getText() {
        return messagePmo.getText();
    }

    @Override
    public VaadinIcons getIcon() {
        return messagePmo.getIcon();
    }

}
/*
 * Copyright Faktor Zehn AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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

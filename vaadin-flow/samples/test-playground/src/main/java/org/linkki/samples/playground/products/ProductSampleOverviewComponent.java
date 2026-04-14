/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.products;

import java.io.Serial;
import java.util.List;
import java.util.Optional;

import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.MessagesPanel;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

/**
 * Common layout used by all products.
 */
public class ProductSampleOverviewComponent extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductSampleOverviewComponent() {
        add(new Headline("Overview"));

        var page = new ProductsSampleOverviewPage();
        page.setSizeFull();
        page.init();

        var splitLayout = new SplitLayout();
        splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setWidthFull();
        splitLayout.getStyle().setMinHeight("0");
        splitLayout.getStyle().setFlexGrow("1");
        var messagesComponent = new MessagesPanel();

        page.getBindingManager().getContext("messages-context")
                .bind(new Object(), new BindingDescriptor(BoundProperty.empty(), List.of(),
                        ((messages, componentWrapper, propertyDispatcher) -> {
                            var comp = (MessagesPanel)componentWrapper.getComponent();
                            comp.showMessages(messages);
                            updateMessagePanelVisibility(messages, splitLayout);
                            return new MessageList();
                        })),
                      new NoLabelComponentWrapper(messagesComponent));

        splitLayout.addToPrimary(page);
        splitLayout.addToSecondary(messagesComponent);
        updateMessagePanelVisibility(List.of(), splitLayout);
        add(splitLayout);

        setHeightFull();
    }

    public void updateMessagePanelVisibility(Iterable<Message> messages, SplitLayout messagePanelLayout) {
        if (messages.iterator().hasNext()) {
            messagePanelLayout.removeThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
            messagePanelLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
            messagePanelLayout.setSplitterPosition(80d);
            Optional.ofNullable(messagePanelLayout.getSecondaryComponent())
                    .ifPresent(c -> c.setVisible(true));
        } else {
            messagePanelLayout.removeThemeVariants(SplitLayoutVariant.LUMO_SMALL);
            messagePanelLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
            messagePanelLayout.setSplitterPosition(100d);
            Optional.ofNullable(messagePanelLayout.getSecondaryComponent())
                    .ifPresent(c -> c.setVisible(false));
        }
    }
}

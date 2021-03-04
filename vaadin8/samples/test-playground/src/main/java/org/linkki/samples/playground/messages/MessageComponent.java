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
package org.linkki.samples.playground.messages;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings({ "java:S2160", "java:S110" })
public class MessageComponent extends VerticalLayout implements SidebarSheetDefinition {

    public static final String ID = "Message";

    private static final long serialVersionUID = 7735020388489427827L;
    private BindingManager bindingManager;
    private RegistrationValidationService validationService;

    public MessageComponent() {

        VaadinSession.getCurrent().setAttribute(LinkkiConverterRegistry.class, new LinkkiConverterRegistry());

        MessagesPanel messagesPanel = new MessagesPanel();
        messagesPanel.setVisible(false);

        User user = new User();
        RegistrationSectionPmo registrationPmo = new RegistrationSectionPmo(user,
                this::handleRegistration);

        // tag::validation-service[]
        validationService = new RegistrationValidationService(registrationPmo);
        bindingManager = new RegistrationBindingManager(validationService, messagesPanel::updateMessages);
        // end::validation-service[]

        PmoBasedSectionFactory sectionFactory = new PmoBasedSectionFactory();
        AbstractSection section = sectionFactory.createSection(registrationPmo, bindingManager.getContext(getClass()));

        VerticalLayout layout = new VerticalLayout(messagesPanel, section);
        layout.setMargin(false);
        addComponent(layout);
    }

    private void handleRegistration(RegistrationSectionPmo userPmo) {
        // validate required fields as well
        validationService.setValidationDisplayState(ValidationDisplayState.SHOW_ALL);

        // trigger validation and display errors in the MessagesPanel
        bindingManager.afterUpdateUi();

        if (!validationService.getValidationMessages().containsErrorMsg()) {
            Notification.show("Registration successful! Thank you for joining us!");
            userPmo.reset();
            validationService.setValidationDisplayState(ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS);
        }
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.ENVELOPE;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }

}

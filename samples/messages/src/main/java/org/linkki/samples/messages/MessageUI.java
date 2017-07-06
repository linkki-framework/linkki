package org.linkki.samples.messages;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.linkki.core.binding.BindingManager;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.converters.LinkkiConverterFactory;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.samples.messages.binding.RegistrationBindingManager;
import org.linkki.samples.messages.components.MessagesPanel;
import org.linkki.samples.messages.model.User;
import org.linkki.samples.messages.pmo.RegistrationSectionPmo;
import org.linkki.samples.messages.pmo.RegistrationValidationService;
import org.linkki.samples.messages.pmo.RegistrationValidationService.ValidationMode;

@Theme(value = ValoTheme.THEME_NAME)
public class MessageUI extends UI {

    private static final long serialVersionUID = 7735020388489427827L;
    private BindingManager bindingManager;
    private RegistrationValidationService validationService;


    @Override
    protected void init(VaadinRequest request) {

        Page.getCurrent().setTitle("Linkki Samples :: Messages");

        VaadinSession.getCurrent().setConverterFactory(new LinkkiConverterFactory());

        MessagesPanel messagesPanel = new MessagesPanel();
        messagesPanel.setVisible(false);

        User user = new User();
        RegistrationSectionPmo registrationPmo = new RegistrationSectionPmo(user, u -> handleRegistration(messagesPanel, u));
        validationService = new RegistrationValidationService(registrationPmo);

        bindingManager = new RegistrationBindingManager(validationService, m -> updateMessages(messagesPanel, m));

        DefaultPmoBasedSectionFactory sectionFactory = new DefaultPmoBasedSectionFactory();
        AbstractSection section = sectionFactory.createSection(registrationPmo,
                bindingManager.getExistingContextOrStartNewOne(getClass()));

        setContent(new VerticalLayout(messagesPanel, section));
    }


    private void updateMessages(MessagesPanel messagesPanel, MessageList messageList) {
        messagesPanel.updateMessages(messageList);
    }

    private void handleRegistration(MessagesPanel messagesPanel, RegistrationSectionPmo userPmo) {
        // validate required fields as well
        validationService.setValidationMode(ValidationMode.STRICT);

        // trigger validation and display errors in the MessagesPanel
        bindingManager.afterUpdateUi();

        if (!validationService.getValidationMessages().containsErrorMsg()) {
            Notification.show("Registration successful! Thank you for joining us!");
            userPmo.reset();
            validationService.setValidationMode(ValidationMode.LENIENT);
        }
    }
}

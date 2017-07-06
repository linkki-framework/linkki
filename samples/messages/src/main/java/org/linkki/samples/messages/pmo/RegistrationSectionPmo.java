package org.linkki.samples.messages.pmo;

import java.util.function.Consumer;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.PasswordField;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UICustomField;
import org.linkki.core.ui.section.annotations.UIDateField;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextField;
import org.linkki.samples.messages.model.User;

@UISection
public class RegistrationSectionPmo implements PresentationModelObject {

    private final User user;

    private final Consumer<RegistrationSectionPmo> registerHandler;

    private String confirmPassword;

    public RegistrationSectionPmo(User user, Consumer<RegistrationSectionPmo> registerHandler) {
        this.user = user;
        this.registerHandler = registerHandler;
    }

    @ModelObject
    public User getUser() {
        return user;
    }

    @UITextField(position = 10, label = "Name", required = RequiredType.REQUIRED, modelAttribute = User.PROPERTY_NAME)
    public void name() {
        // model binding
    }
 
    // tag::ui-custom-field[]
    @UICustomField(position = 20, label = "Password", required = RequiredType.REQUIRED, modelAttribute = User.PROPERTY_PASSWORD, uiControl = PasswordField.class)
    public void password() {
        // model binding
    }
    // end::ui-custom-field[]

    @UICustomField(position = 30, label = "Confirm Password", required = RequiredType.REQUIRED, uiControl = PasswordField.class)
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @UITextField(position = 40, label = "E-Mail", required = RequiredType.REQUIRED, modelAttribute = User.PROPERTY_EMAIL)
    public void email() {
        // model binding
    }

    @UIDateField(position = 50, label = "Date of Birth", required = RequiredType.REQUIRED, modelAttribute = User.PROPERTY_BIRTHDAY)
    public void birthday() {
        // model binding
    }

    @UIButton(position = 60, caption = "Register", shortcutKeyCode = ShortcutAction.KeyCode.ENTER)
    public void register() {
        registerHandler.accept(this);
    }

    public void reset() {
        user.setName(null);
        user.setPassword(null);
        user.setEmail(null);
        user.setBirthday(null);
        confirmPassword = null;
    }

}

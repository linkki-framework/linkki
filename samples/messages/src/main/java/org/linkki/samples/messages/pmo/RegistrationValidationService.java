package org.linkki.samples.messages.pmo;

import java.time.LocalDate;
import java.util.Objects;

import com.vaadin.server.ErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.message.Message;
import org.linkki.core.message.MessageList;
import org.linkki.core.message.ObjectProperty;
import org.linkki.samples.messages.model.User;

public class RegistrationValidationService implements ValidationService {

    public enum ValidationMode {
        LENIENT, STRICT
    }

    private final RegistrationSectionPmo registrationPmo;

    private ValidationMode validationMode = ValidationMode.LENIENT;

    public RegistrationValidationService(RegistrationSectionPmo registrationPmo) {
        this.registrationPmo = registrationPmo;
    }

    @Override
    public MessageList getValidationMessages() {
        MessageList messages = new MessageList();

        validateName(registrationPmo.getUser(), messages);
        validatePassword(registrationPmo, messages);
        validateEmail(registrationPmo.getUser(), messages);
        validateBirthday(registrationPmo.getUser(), messages);

        return messages;
    }

    private void validateName(User user, MessageList messages) {
        String name = user.getName();
        if (validationMode == ValidationMode.STRICT && StringUtils.isEmpty(name)) {
            messages.add(Message.builder("Username is required", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_NAME)).markers(() -> true).create());
        } else if (name != null && name.length() < 3) {
            messages.add(Message.builder("Username must contain at least 3 characters", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObject(objectProperty(user, "name")).create());
        }
    }

    private void validatePassword(RegistrationSectionPmo pmo, MessageList messages) {
        String password = pmo.getUser().getPassword();
        if (!Objects.equals(password, pmo.getConfirmPassword())) {
            messages.add(Message.builder("Passwords do not match", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObjects(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD),
                            objectProperty(pmo, "confirmPassword"))
                    .create());
        } else if (validationMode == ValidationMode.STRICT && StringUtils.isEmpty(password)) {
            messages.add(Message.builder("Password is required", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObject(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD)).markers(() -> true).create());
        } else if (password != null) {
            if (!password.matches("[A-Za-z0-9]*")) {
                messages.add(Message
                        .builder("Password must contain at least one uppercase letter, "
                                + "one lowercase letter and one number", ErrorMessage.ErrorLevel.ERROR)
                        .invalidObject(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD)).create());
            } else if (password.length() < 8) {
                messages.add(
                        Message.builder("Password must at least contain 8 characters", ErrorMessage.ErrorLevel.ERROR)
                                .invalidObject(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD)).create());
            }
        }
    }

    private void validateEmail(User user, MessageList messages) {
        String email = user.getEmail();
        if (validationMode == ValidationMode.STRICT && StringUtils.isEmpty(email)) {
            messages.add(Message.builder("E-Mail is required", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_EMAIL)).markers(() -> true).create());
        } else if (email != null
                && !email.matches("^[a-zA-Z0-9._\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,4}$")) {
            messages.add(Message.builder("Invalid E-Mail address! (someone@example.com)", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_EMAIL)).create());
        }
    }

    private void validateBirthday(User user, MessageList messages) {
        LocalDate birthday = user.getBirthday();
        if (validationMode == ValidationMode.STRICT && birthday == null) {
            messages.add(Message.builder("Birthday is required", ErrorMessage.ErrorLevel.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_BIRTHDAY)).markers(() -> true).create());
        } else if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                messages.add(Message.builder("Birthday must not be in the future", ErrorMessage.ErrorLevel.ERROR)
                        .invalidObject(objectProperty(user, User.PROPERTY_BIRTHDAY)).create());
            } else if (birthday.isAfter(LocalDate.now().minusYears(15))) {
                messages.add(Message.builder("You must be at least 15 years to register", ErrorMessage.ErrorLevel.ERROR)
                        .invalidObject(objectProperty(user, User.PROPERTY_BIRTHDAY)).create());
            }
        }
    }

    private ObjectProperty objectProperty(Object object, String property) {
        return new ObjectProperty(object, property);
    }

    public ValidationMode getValidationMode() {
        return validationMode;
    }

    public void setValidationMode(ValidationMode validationMode) {
        this.validationMode = validationMode;
    }
}

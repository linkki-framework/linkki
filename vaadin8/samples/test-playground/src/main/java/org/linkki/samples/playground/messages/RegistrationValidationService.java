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
package org.linkki.samples.playground.messages;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.validation.ValidationDisplayState;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.ObjectProperty;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.util.validation.ValidationMarker;

import edu.umd.cs.findbugs.annotations.NonNull;

public class RegistrationValidationService implements ValidationService {

    private final RegistrationSectionPmo registrationPmo;
    private ValidationDisplayState validationDisplayState = ValidationDisplayState.HIDE_MANDATORY_FIELD_VALIDATIONS;

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
        if (StringUtils.isEmpty(name)) {
            messages.add(Message.builder("Username is required", Severity.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_NAME))
                    .markers(ValidationMarker.REQUIRED)
                    .create());
        } else if (name.length() < 3) {
            messages.add(Message.builder("Username must contain at least 3 characters", Severity.ERROR)
                    .invalidObject(objectProperty(user, "name"))
                    .create());
        }
    }

    private void validatePassword(RegistrationSectionPmo pmo, MessageList messages) {
        String password = pmo.getUser().getPassword();
        if (!Objects.equals(password, pmo.getConfirmPassword())) {
            messages.add(Message.builder("Passwords do not match", Severity.ERROR)
                    .invalidObjects(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD),
                            objectProperty(pmo, "confirmPassword"))
                    .create());
        } else if (StringUtils.isEmpty(password)) {
            // tag::message-builder[]
            Message passwordRequiredMessage = Message
                    .builder("Password is required", Severity.ERROR)
                    .invalidObject(new ObjectProperty(pmo.getUser(), User.PROPERTY_PASSWORD))
                    .markers(ValidationMarker.REQUIRED)
                    .create();
            // end::message-builder[]
            messages.add(passwordRequiredMessage);
        } else if (password != null) {
            if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$")) {
                messages.add(Message
                        .builder("Password must contain at least one uppercase letter, "
                                + "one lowercase letter and one number", Severity.ERROR)
                        .invalidObject(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD))
                        .create());
            } else if (password.length() < 8) {
                messages.add(Message
                        .builder("Password should contain at least 8 characters", Severity.WARNING)
                        .invalidObject(objectProperty(pmo.getUser(), User.PROPERTY_PASSWORD))
                        .create());
            }
        }
    }

    private void validateEmail(User user, MessageList messages) {
        String email = user.getEmail();
        if (StringUtils.isEmpty(email)) {
            messages.add(Message.builder("E-Mail is required", Severity.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_EMAIL))
                    .markers(ValidationMarker.REQUIRED)
                    .create());
        } else if (!email.matches("^[a-zA-Z0-9._\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,4}$")) {
            messages.add(Message.builder("Invalid E-Mail address! (someone@example.com)", Severity.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_EMAIL)).create());
        }
    }

    private void validateBirthday(User user, MessageList messages) {
        LocalDate birthday = user.getBirthday();
        if (birthday == null) {
            messages.add(Message.builder("Birthday is required", Severity.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_BIRTHDAY)).markers(ValidationMarker.REQUIRED)
                    .create());
        } else if (birthday.isAfter(LocalDate.now())) {
            messages.add(Message.builder("Birthday must not be in the future", Severity.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_BIRTHDAY)).create());
        } else if (birthday.isAfter(LocalDate.now().minusYears(15))) {
            messages.add(Message.builder("You must be at least 15 years to register", Severity.ERROR)
                    .invalidObject(objectProperty(user, User.PROPERTY_BIRTHDAY)).create());
        }
    }

    @NonNull
    private ObjectProperty objectProperty(Object object, String property) {
        return new ObjectProperty(object, property);
    }

    @Override
    public ValidationDisplayState getValidationDisplayState() {
        return validationDisplayState;
    }

    public void setValidationDisplayState(ValidationDisplayState validationDisplayState) {
        this.validationDisplayState = validationDisplayState;
    }

}
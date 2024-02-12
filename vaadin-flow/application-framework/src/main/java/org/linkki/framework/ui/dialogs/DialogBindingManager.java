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
package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;

/**
 * A binding manager for bindings in an {@link OkCancelDialog}. It calls the dialog's
 * {@link OkCancelDialog#validate()} when updating the UI. This makes sure that the messages
 * displayed in the dialog are updated and that the dialog can filter the messages with which the
 * binding contexts are updated according to its {@link OkCancelDialog#getValidationDisplayState()}.
 */
public class DialogBindingManager extends DefaultBindingManager {

    public DialogBindingManager(OkCancelDialog dialog, ValidationService validationService) {
        this(dialog, validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    public DialogBindingManager(OkCancelDialog dialog,
            ValidationService validationService,
            PropertyBehaviorProvider propertyBehaviorProvider) {
        this(dialog, validationService, propertyBehaviorProvider, new PropertyDispatcherFactory());
    }

    public DialogBindingManager(OkCancelDialog dialog,
            ValidationService validationService,
            PropertyBehaviorProvider defaultBehaviorProvider,
            PropertyDispatcherFactory propertyDispatcherFactory) {
        super(dialog::validate, defaultBehaviorProvider, propertyDispatcherFactory);

        requireNonNull(dialog, "dialog must not be null");
        dialog.setValidationService(validationService);
        dialog.setBeforeOkHandler(this::afterUpdateUi);
    }

}

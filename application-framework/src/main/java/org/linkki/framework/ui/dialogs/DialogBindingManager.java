/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingManager;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;

/**
 * A binding manager for bindings in an {@link OkCancelDialog}. It calls the dialog's
 * {@link OkCancelDialog#validate()} when updating the UI. This makes sure that the messages
 * displayed in the dialog are updated and that the dialog can filter the messages with which the
 * binding contexts are updated according to its {@link OkCancelDialog#getValidationDisplayState()}.
 */
public class DialogBindingManager extends BindingManager {

    private final PropertyBehaviorProvider behaviorProvider;

    public DialogBindingManager(OkCancelDialog dialog, ValidationService validationService) {
        this(dialog, validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    public DialogBindingManager(OkCancelDialog dialog, ValidationService validationService,
            PropertyBehaviorProvider behaviorProvider) {
        super(() -> dialog.validate());
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");
        this.behaviorProvider = behaviorProvider;
        requireNonNull(dialog, "dialog must not be null");
        dialog.setValidationService(validationService);
    }

    @Override
    protected BindingContext newBindingContext(String name) {
        return new BindingContext(name, behaviorProvider, this::afterUpdateUi);
    }

}

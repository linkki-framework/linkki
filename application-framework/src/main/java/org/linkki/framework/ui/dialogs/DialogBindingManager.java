/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import org.faktorips.runtime.MessageList;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingManager;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;

/**
 * A binding manager to use for bindings in an {@link OkCancelDialog}. It calls the dialog's
 * {@link OkCancelDialog#validate() validate()} when updating the UI. This makes sure that the
 * messages displayed in the dialog are updated and that the dialog can filter the messages with
 * which the binding contexts are updated according to its
 * {@link OkCancelDialog#getValidationDisplayState()}.
 */
public class DialogBindingManager extends BindingManager {

    private final OkCancelDialog dialog;
    private final PropertyBehaviorProvider behaviorProvider;

    public DialogBindingManager(@Nonnull OkCancelDialog dialog, @Nonnull ValidationService validationService) {
        this(dialog, validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    public DialogBindingManager(@Nonnull OkCancelDialog dialog, @Nonnull ValidationService validationService,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        super(validationService);
        this.behaviorProvider = requireNonNull(behaviorProvider);
        this.dialog = requireNonNull(dialog);
        dialog.setValidationService(validationService);
    }

    @Override
    public void afterUpdateUi() {
        // Obtain the messages from the dialog (and not directly from the validation service). The
        // dialog uses the validation service as well, but does the required filtering of mandatory
        // field messages
        MessageList messages = dialog.validate();
        updateMessages(messages);
    }

    @Override
    protected BindingContext newBindingContext(String name) {
        return new BindingContext(name, behaviorProvider, this::afterUpdateUi);
    }

}

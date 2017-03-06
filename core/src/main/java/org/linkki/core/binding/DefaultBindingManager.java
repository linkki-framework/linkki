/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;

/**
 * Default implementation of a {@link BindingManager} that returns standard {@link BindingContext
 * BindingContexts}.
 */
public class DefaultBindingManager extends BindingManager {

    private final PropertyBehaviorProvider behaviorProvider;

    /**
     * Creates a {@link BindingManager} that returns standard {@link BindingContext BindingContexts}
     * with {@link PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER}.
     */
    public DefaultBindingManager(ValidationService validationService) {
        this(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    /**
     * Creates a {@link BindingManager} that returns standard {@link BindingContext BindingContexts}
     * with the given {@link PropertyBehaviorProvider}.
     */
    public DefaultBindingManager(ValidationService validationService, PropertyBehaviorProvider behaviorProvider) {
        super(validationService);
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");
this.behaviorProvider = behaviorProvider;
    }

    @Override
    protected BindingContext newBindingContext(String name) {
        return new BindingContext(name, behaviorProvider, this::afterUpdateUi);
    }

}

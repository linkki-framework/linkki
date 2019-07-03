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
package org.linkki.core.binding.manager;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;

/**
 * Default implementation of a {@link BindingManager} that returns standard {@link BindingContext
 * BindingContexts}.
 */
public class DefaultBindingManager extends BindingManager {

    private final PropertyBehaviorProvider defaultBehaviorProvider;

    /**
     * Creates a {@link BindingManager} that returns standard {@link BindingContext BindingContexts}
     * with {@link PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER} and
     * {@link ValidationService#NOP_VALIDATION_SERVICE}.
     */
    public DefaultBindingManager() {
        this(ValidationService.NOP_VALIDATION_SERVICE);
    }

    /**
     * Creates a {@link BindingManager} that returns standard {@link BindingContext BindingContexts}
     * with {@link PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER}.
     */
    public DefaultBindingManager(ValidationService validationService) {
        this(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    /**
     * Creates a {@link BindingManager} that returns standard {@link BindingContext BindingContexts}
     * with the given {@link PropertyBehaviorProvider}, as long as no other
     * {@link PropertyBehaviorProvider} is specified in an explicit call to
     * {@link #createContext(Class, PropertyBehaviorProvider)}.
     */
    public DefaultBindingManager(ValidationService validationService,
            PropertyBehaviorProvider defaultBehaviorProvider) {
        super(validationService);
        this.defaultBehaviorProvider = requireNonNull(defaultBehaviorProvider,
                                                      "defaultBehaviorProvider must not be null");
    }

    @Override
    protected BindingContext newBindingContext(String name) {
        requireNonNull(name, "name must not be null");
        return new BindingContext(name, getDefaultBehaviorProvider(), this::afterUpdateUi);
    }

    @Override
    protected BindingContext newBindingContext(String name, PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(name, "name must not be null");
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");
        return new BindingContext(name, behaviorProvider, this::afterUpdateUi);
    }

    /**
     * Returns the default {@link PropertyBehaviorProvider} used for {@link #newBindingContext(String)}.
     */
    public PropertyBehaviorProvider getDefaultBehaviorProvider() {
        return defaultBehaviorProvider;
    }

}

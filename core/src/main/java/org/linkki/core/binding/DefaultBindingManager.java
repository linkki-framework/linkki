/*
 * Copyright Faktor Zehn AG.
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
        this.behaviorProvider = requireNonNull(behaviorProvider, "behaviorProvider must not be null");
    }

    @Override
    protected BindingContext newBindingContext(String name) {
        requireNonNull(name, "name must not be null");
        return new BindingContext(name, behaviorProvider, this::afterUpdateUi);
    }

}

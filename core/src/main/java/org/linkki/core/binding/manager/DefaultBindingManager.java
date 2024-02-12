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
package org.linkki.core.binding.manager;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;

/**
 * Default implementation of a {@link BindingManager} that returns standard {@link BindingContext
 * BindingContexts}.
 */
public class DefaultBindingManager extends BindingManager {

    private final PropertyBehaviorProvider defaultBehaviorProvider;
    private final PropertyDispatcherFactory propertyDispatcherFactory;

    /**
     * Creates a {@link BindingManager} that returns {@link BindingContext BindingContexts} with
     * {@link PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER},
     * {@link ValidationService#NOP_VALIDATION_SERVICE} and the default
     * {@link PropertyDispatcherFactory PropertyDispatcherFactory}.
     */
    public DefaultBindingManager() {
        this(ValidationService.NOP_VALIDATION_SERVICE);
    }

    /**
     * Creates a {@link BindingManager} that returns {@link BindingContext BindingContexts} with the
     * given {@link ValidationService ValidationService},
     * {@link PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER} and the default
     * {@link PropertyDispatcherFactory}.
     */
    public DefaultBindingManager(ValidationService validationService) {
        this(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    /**
     * Creates a {@link BindingManager} that returns {@link BindingContext BindingContexts} with the
     * given {@link ValidationService}, {@link PropertyBehaviorProvider} and the default
     * {@link PropertyDispatcherFactory PropertyDispatcherFactory}.
     *
     * @implNote The {@link PropertyBehaviorProvider} used for context creation can be overruled by
     *           specifying the {@link PropertyBehaviorProvider} explicitly in
     *           {@link #createContext(Class, PropertyBehaviorProvider)} or
     *           {@link #createContext(String, PropertyBehaviorProvider)}.
     */
    public DefaultBindingManager(ValidationService validationService,
            PropertyBehaviorProvider defaultBehaviorProvider) {
        this(validationService, defaultBehaviorProvider, new PropertyDispatcherFactory());
    }

    /**
     * Creates a {@link BindingManager} that returns standard {@link BindingContext BindingContexts}
     * with the given {@link ValidationService}, {@link PropertyBehaviorProvider} and
     * {@link PropertyDispatcherFactory}.
     * 
     * @implNote The {@link PropertyBehaviorProvider} used for context creation can be overruled by
     *           specifying the {@link PropertyBehaviorProvider} explicitly in
     *           {@link #createContext(Class, PropertyBehaviorProvider)} or
     *           {@link #createContext(String, PropertyBehaviorProvider)}.
     */
    public DefaultBindingManager(ValidationService validationService,
            PropertyBehaviorProvider defaultBehaviorProvider, PropertyDispatcherFactory propertyDispatcherFactory) {
        super(validationService);
        this.defaultBehaviorProvider = requireNonNull(defaultBehaviorProvider,
                                                      "defaultBehaviorProvider must not be null");
        this.propertyDispatcherFactory = requireNonNull(propertyDispatcherFactory,
                                                        "propertyDispatcherFactory must not be null");
    }

    @Override
    protected BindingContext newBindingContext(String name) {
        requireNonNull(name, "name must not be null");
        return new BindingContextBuilder().name(name).propertyBehaviorProvider(getDefaultBehaviorProvider())
                .propertyDispatcherFactory(propertyDispatcherFactory).afterUpdateHandler(this::afterUpdateUi).build();
    }

    @Override
    protected BindingContext newBindingContext(String name, PropertyBehaviorProvider behaviorProvider) {
        requireNonNull(name, "name must not be null");
        requireNonNull(behaviorProvider, "behaviorProvider must not be null");
        return new BindingContextBuilder().name(name).propertyBehaviorProvider(behaviorProvider)
                .propertyDispatcherFactory(propertyDispatcherFactory).afterUpdateHandler(this::afterUpdateUi).build();
    }

    /**
     * Returns the default {@link PropertyBehaviorProvider} used for
     * {@link #newBindingContext(String)}.
     */
    public PropertyBehaviorProvider getDefaultBehaviorProvider() {
        return defaultBehaviorProvider;
    }

}

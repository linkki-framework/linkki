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
package org.linkki.ips.utils;

import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;

/**
 * A utility class to facilitate the creation of {@link PmoBasedDialogFactory} instances, ensuring consistent
 * configurations with {@link IpsPropertyDispatcherFactory} across the application.
 */
public class IpsDialogCreator {

    /**
     * Private constructor to ensure that this utility class is not instantiated directly.
     */
    private IpsDialogCreator() {
        // private constructor to prevent instantiation
    }

    /**
     * Creates a new dialog factory with no validations and special property behavior. The resulting factory is
     * configured to use the {@link IpsPropertyDispatcherFactory}
     *
     * @see ValidationService#NOP_VALIDATION_SERVICE
     * @see PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER
     */
    public static PmoBasedDialogFactory create() {
        return with(ValidationService.NOP_VALIDATION_SERVICE);
    }

    /**
     * Creates a new dialog factory with {@link ValidationService} and no special property behavior. The factory will
     * also be configured to use the {@link IpsPropertyDispatcherFactory}.
     *
     * @param validationService a service validating the data in the dialog
     *
     * @return a pre-configured instance of {@link PmoBasedDialogFactory} with the specified validation service
     *
     * @see PropertyBehaviorProvider#NO_BEHAVIOR_PROVIDER
     */
    public static PmoBasedDialogFactory with(ValidationService validationService) {
        return new PmoBasedDialogFactory(validationService, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, new IpsPropertyDispatcherFactory());
    }
}

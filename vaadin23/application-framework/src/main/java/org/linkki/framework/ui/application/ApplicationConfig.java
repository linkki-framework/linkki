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
package org.linkki.framework.ui.application;

import java.util.List;
import java.util.Optional;

import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.framework.ui.application.menu.ApplicationMenu;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.Sequence;

import com.vaadin.flow.server.VaadinSession;

/**
 * Application configuration used to configure the {@link ApplicationLayout}.
 */
public interface ApplicationConfig {

    /**
     * Define the basic information for the application.
     */
    ApplicationInfo getApplicationInfo();

    /**
     * The {@link ApplicationMenuItemDefinition ApplicationMenuItemDefinitions} used to create an
     * {@link ApplicationMenu} for the {@link ApplicationHeader}.
     */
    Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions();

    /**
     * Used to create an {@link ApplicationHeader} including the {@link ApplicationMenu}.
     */
    default ApplicationHeaderDefinition getHeaderDefinition() {
        return ApplicationHeader::new;
    }

    /**
     * Optionally returns an {@link ApplicationFooterDefinition} that creates an
     * {@link ApplicationFooter}. Per default, no footer is created.
     */
    default Optional<ApplicationFooterDefinition> getFooterDefinition() {
        return Optional.empty();
    }

    /**
     * The converter registry that holds the converters that should be
     * {@link VaadinSession#setAttribute(Class, Object) registered to the VaadinSession}.
     */
    default LinkkiConverterRegistry getConverterRegistry() {
        return LinkkiConverterRegistry.DEFAULT;
    }

    /**
     * Default global theme variants that should be applied.
     */
    default List<String> getDefaultVariants() {
        return List.of(LinkkiTheme.VARIANT_COMPACT);
    }

    @FunctionalInterface
    public static interface ApplicationHeaderDefinition {
        ApplicationHeader create(ApplicationInfo applicationInfo,
                Sequence<ApplicationMenuItemDefinition> menuItemDefinitions);
    }

    @FunctionalInterface
    public static interface ApplicationFooterDefinition {
        ApplicationFooter create(ApplicationInfo applicationInfo);
    }

}

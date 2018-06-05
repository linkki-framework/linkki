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
package org.linkki.framework.state;

import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationLayout;
import org.linkki.framework.ui.application.ApplicationLayout.Builder;
import org.linkki.framework.ui.application.ApplicationNavigator;
import org.linkki.framework.ui.application.LinkkiUi;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;

/**
 * Application configuration used to {@link LinkkiUi#configure(ApplicationConfig) configure} the
 * {@link LinkkiUi}.
 */
public interface ApplicationConfig {

    /**
     * The application's name, displayed for example in the {@link ApplicationFooter}.
     */
    String getApplicationName();

    /**
     * The application's version, displayed for example in the {@link ApplicationFooter}.
     */
    String getApplicationVersion();

    /**
     * The copyright information for the application, displayed for example in the
     * {@link ApplicationFooter}.
     */
    String getCopyright();

    /**
     * The configured {@link Builder} that will be used to create the {@link ApplicationLayout} for the
     * {@link LinkkiUi}.
     */
    default ApplicationLayout.Builder<?> getApplicationLayoutBuilder() {
        return new ApplicationLayout.Builder<>();
    }

    /**
     * The {@link ApplicationNavigator} used to navigate the {@link View Views} displayed in the
     * {@link ApplicationLayout}.
     */
    default ApplicationNavigator createApplicationNavigator(UI ui, ApplicationLayout applicationLayout) {
        return new ApplicationNavigator(ui, applicationLayout);
    }

}

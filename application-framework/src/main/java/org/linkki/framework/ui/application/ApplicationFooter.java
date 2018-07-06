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
package org.linkki.framework.ui.application;

import org.linkki.framework.state.ApplicationConfig;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * The footer of the application displaying information about the application, version and
 * copyright.
 */
public class ApplicationFooter extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    private final ApplicationConfig config;

    public ApplicationFooter(ApplicationConfig config) {
        super();
        this.config = config;
    }

    public void init() {
        setMargin(false);
        addComponent(new Label(buildText(config)));
    }

    /**
     * Returns the text for the footer. May be overwritten by subclasses.
     * 
     * @param applicationConfig The current application configuration
     * @return The text that is displayed in the footer.
     */
    protected String buildText(ApplicationConfig applicationConfig) {
        return applicationConfig.getApplicationName() + ", " + applicationConfig.getApplicationVersion() + ", "
                + applicationConfig.getCopyright();
    }

}

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

import org.linkki.framework.ui.LinkkiApplicationTheme;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * The application footer displaying information about the application version and copyright.
 */
public class ApplicationFooter extends Composite<HorizontalLayout> {

    private static final long serialVersionUID = 1L;

    private final ApplicationInfo info;

    public ApplicationFooter(ApplicationInfo applicationInfo) {
        super();
        this.info = applicationInfo;
    }

    protected void init() {
        getContent().setMargin(false);
        getContent().addClassName(LinkkiApplicationTheme.APPLICATION_FOOTER);
        getContent().add(new Text(buildText(info)));
    }

    /**
     * Returns the text for the footer. May be overwritten by subclasses.
     * 
     * @param applicationInfo information about an application
     * @return The text that is displayed in the footer.
     */
    protected String buildText(ApplicationInfo applicationInfo) {
        return applicationInfo.getApplicationName() + ", " + applicationInfo.getApplicationVersion() + ", "
                + applicationInfo.getCopyright();
    }

}

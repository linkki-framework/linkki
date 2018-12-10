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
package org.linkki.samples.appsample.state;

import java.time.LocalDate;

import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.appsample.menu.StartMenuItemDefinition;
import org.linkki.samples.appsample.nls.NlsText;
import org.linkki.util.Sequence;

/**
 * An {@link ApplicationConfig} using the default {@link ApplicationHeader application header} and
 * {@link ApplicationFooter application footer}.
 */
public class SampleApplicationConfig implements ApplicationConfig {

    @Override
    public String getApplicationName() {
        return NlsText.getString("SampleApplicationConfig.Name");
    }

    @Override
    public String getApplicationVersion() {
        return NlsText.getString("SampleApplicationConfig.Version");
    }

    @Override
    public String getApplicationDescription() {
        return NlsText.getString("SampleApplicationConfig.Description");
    }

    @Override
    public String getCopyright() {
        return "Copyright © 2015 - " + LocalDate.now().getYear() + " Faktor Zehn GmbH"; //$NON-NLS-1$
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new StartMenuItemDefinition());
    }

}

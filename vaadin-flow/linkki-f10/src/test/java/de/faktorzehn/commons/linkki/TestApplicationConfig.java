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

package de.faktorzehn.commons.linkki;

import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.Sequence;
import org.linkki.util.handler.Handler;

public final class TestApplicationConfig implements ApplicationConfig {

    @Override
    public ApplicationInfo getApplicationInfo() {
        return new ApplicationInfo() {
            @Override
            public String getApplicationName() {
                return "";
            }

            @Override
            public String getApplicationVersion() {
                return "";
            }

            @Override
            public String getApplicationDescription() {
                return "";
            }

            @Override
            public String getCopyright() {
                return "";
            }
        };
    }

    @Override
    public LinkkiConverterRegistry getConverterRegistry() {
        return new LinkkiConverterRegistry(Sequence.empty());
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new ApplicationMenuItemDefinition(UiFramework.getLocale().getDisplayCountry(),
                "country", Handler.NOP_HANDLER));
    }

}
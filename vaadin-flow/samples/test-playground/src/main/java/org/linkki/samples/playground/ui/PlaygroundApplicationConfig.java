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
package org.linkki.samples.playground.ui;

import java.time.LocalDate;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.ui.converters.LinkkiConverterRegistry;
import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationFooter;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.framework.ui.dialogs.DialogErrorHandler;
import org.linkki.framework.ui.dialogs.ErrorDialogConfiguration;
import org.linkki.ips.decimalfield.FormattedDecimalFieldToStringConverter;
import org.linkki.samples.playground.application.SampleView;
import org.linkki.samples.playground.application.custom.CustomView;
import org.linkki.samples.playground.binding.BindingSampleView;
import org.linkki.samples.playground.bugs.BugCollectionView;
import org.linkki.samples.playground.dialogs.DialogsView;
import org.linkki.samples.playground.nls.PlaygroundNlsText;
import org.linkki.samples.playground.products.ProductsSampleView;
import org.linkki.samples.playground.ts.TestScenarioView;
import org.linkki.util.DateFormats;
import org.linkki.util.Sequence;

import com.vaadin.flow.server.ErrorHandler;

/**
 * An {@link ApplicationConfig} using the default {@link ApplicationHeader application header} and
 * {@link ApplicationFooter application footer}.
 */
// tag::dateFormatRegistration[]
public class PlaygroundApplicationConfig implements ApplicationConfig {

    static {
        // this only applies to fr_FR
        DateFormats.register(Locale.FRANCE, "dd/MM/yy");

        // this applies to other locales with an fr language code, such as fr_CH or fr_BE
        DateFormats.register("fr", "dd/MM/yyyy");
    }

    // end::dateFormatRegistration[]

    @Override
    public ApplicationInfo getApplicationInfo() {
        return new PlaygroundApplicationInfo();
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence
                .of(new ApplicationMenuItemDefinition("Playground", "playground", TestScenarioView.class),
                    new ApplicationMenuItemDefinition("Dialogs", "dialogs", DialogsView.class),
                    new ApplicationMenuItemDefinition("Sample Layout", "sample-layout", SampleView.class),
                    new ApplicationMenuItemDefinition("Custom Layout", "custom-layout", CustomView.class),
                    new ApplicationMenuItemDefinition("Binding", "binding", BindingSampleView.class),
                    new ApplicationMenuItemDefinition("F10 Produkte", "f10-products", ProductsSampleView.class),
                    new ApplicationMenuItemDefinition(BugCollectionView.NAME, "bugs", BugCollectionView.class));
    }

    @Override
    public ApplicationHeaderDefinition getHeaderDefinition() {
        return PlaygroundApplicationHeader::new;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        var config = ErrorDialogConfiguration.createWithHandlerNavigatingTo(StringUtils.EMPTY)
                .withCaption("Custom Error Handler")
                .withErrorMessage("Custom error message");
        return new DialogErrorHandler(config);
    }

    @Override
    public LinkkiConverterRegistry getConverterRegistry() {
        return ApplicationConfig.super.getConverterRegistry()
                .with(new FormattedDecimalFieldToStringConverter());
    }

    private static final class PlaygroundApplicationInfo implements ApplicationInfo {
        @Override
        public String getApplicationName() {
            return PlaygroundNlsText.getString("PlaygroundApplicationConfig.Name");
        }

        @Override
        public String getApplicationVersion() {
            return PlaygroundNlsText.getString("PlaygroundApplicationConfig.Version");
        }

        @Override
        public String getApplicationDescription() {
            return PlaygroundNlsText.getString("PlaygroundApplicationConfig.Description");
        }

        @Override
        public String getCopyright() {
            return "Copyright © 2020 - " + LocalDate.now().getYear() + " Faktor Zehn GmbH"; // $NON-NLS-1$
        }
    }

}

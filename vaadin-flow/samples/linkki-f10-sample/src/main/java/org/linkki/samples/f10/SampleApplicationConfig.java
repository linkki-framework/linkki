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
package org.linkki.samples.f10;

import java.time.LocalDate;

import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.Sequence;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteParameters;

import org.linkki.samples.f10.confirm.SampleBrowserConfirmationView;
import org.linkki.samples.f10.search.contextdependent.ContextDependentSearchView;
import org.linkki.samples.f10.search.contextfree.ContextFreeSearchView;

@Component
public class SampleApplicationConfig implements ApplicationConfig {

    public static final SampleApplicationConfig INSTANCE = new SampleApplicationConfig();

    @Override
    public ApplicationInfo getApplicationInfo() {
        return new SampleApplicationInfo();
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new ApplicationMenuItemDefinition("Sample", "sample", SampleUi.class),
                           new ApplicationMenuItemDefinition("Board", "org/linkki/core/vaadin/component/board", SampleBoardLayout.class),
                           new ApplicationMenuItemDefinition("Confirm", "confirm",
                                   () -> UI.getCurrent().navigate(SampleBrowserConfirmationView.class,
                                                                  new RouteParameters("id", "1"))),
                           new ApplicationMenuItemDefinition("Suche", "search", ContextFreeSearchView.class),
                           new ApplicationMenuItemDefinition("Suche im Dialog", "search-dialog",
                                   ContextDependentSearchView.class));
    }

    @Override
    public ApplicationHeaderDefinition getHeaderDefinition() {
        return SampleApplicationHeader::new;
    }

    @Component
    public static final class SampleApplicationInfo implements ApplicationInfo {
        @Override
        public String getApplicationDescription() {
            return "f10-commons-linkki-sample application description";
        }

        @Override
        public String getApplicationName() {
            return "Faktor Zehn Commons linkki Sample";
        }

        @Override
        public String getApplicationVersion() {
            return "0.1";
        }

        @Override
        public String getCopyright() {
            return "Copyright © 2020 - " + LocalDate.now().getYear() + " Faktor Zehn GmbH"; // $NON-NLS-1$
        }
    }

}

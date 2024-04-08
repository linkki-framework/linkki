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
package org.linkki.samples.appsample.ui;

import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.samples.appsample.view.BusinessPartnerView;
import org.linkki.util.Sequence;

public class BusinessPartnerConfig implements ApplicationConfig {

    @Override
    public BusinessPartnerInfo getApplicationInfo() {
        return new BusinessPartnerInfo();
    }

    // tag::searchMenu[]
    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.of(new ApplicationMenuItemDefinition("Search", "appmenu-search", BusinessPartnerView.class));
    }
    // end::searchMenu[]

}
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
package org.linkki.samples.playground.search;

import org.linkki.core.vaadin.component.menu.MenuItemDefinition;
import org.linkki.framework.ui.notifications.NotificationUtil;
import org.linkki.samples.playground.search.service.SampleModelObject;

import com.vaadin.flow.component.UI;

public class ResultActions {

    private ResultActions() {
        // utility class
    }

    public static MenuItemDefinition showPartner(SampleModelObject result) {
        return new MenuItemDefinition("Show Partner", null,
                () -> navigateToPartner(result),
                "appmenu-search-view-partner");
    }

    public static MenuItemDefinition showPolicies(SampleModelObject result) {
        return new MenuItemDefinition("Show Policies", null,
                () -> showNotificationAndNavigate("Navigate to policy management system", result),
                "appmenu-search-view-policy");
    }

    public static MenuItemDefinition showClaims(SampleModelObject result) {
        return new MenuItemDefinition("Show Claims", null,
                () -> showNotificationAndNavigate("Navigate to claim management system", result),
                "appmenu-search-view-claim");
    }

    public static void navigateToPartner(SampleModelObject result) {
        showNotificationAndNavigate("Navigate to partner", result);
    }

    private static void showNotificationAndNavigate(String text, SampleModelObject result) {
        NotificationUtil.showInfo(text, result.getPartnerNumber());
        UI.getCurrent().navigate("");
    }
}

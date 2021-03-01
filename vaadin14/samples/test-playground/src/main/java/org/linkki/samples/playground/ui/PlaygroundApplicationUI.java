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
package org.linkki.samples.playground.ui;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.alignment.AlignmentPage;
import org.linkki.samples.playground.allelements.AllUiElementsPage;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.locale.LocaleInfoPage;
import org.linkki.samples.playground.messages.MessagesComponent;
import org.linkki.samples.playground.nestedcomponent.NestedComponentPage;
import org.linkki.samples.playground.tablayout.TabLayoutPage;
import org.linkki.samples.playground.table.TablePage;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = PlaygroundAppLayout.class)
public class PlaygroundApplicationUI extends Div implements HasUrlParameter<String> {

    private static final long serialVersionUID = 1L;

    public static final String ALL_COMPONENTS_TAB_ID = "all";
    public static final String DYNAMIC_ASPECT_TAB_ID = "dynamic";
    public static final String BUGS_TAB_ID = "bugs";
    public static final String TABLES_TAB_ID = "tables";
    public static final String LOCALE_TAB_ID = "locale";
    public static final String NESTED_COMPONENT_PAGE_TAB_ID = "nestedComponentPage";
    public static final String TAB_LAYOUT_TAB_ID = "tab-layout";
    public static final String ALIGNMENT_TAB_ID = "alignment";
    public static final String MESSAGES_TAB_ID = "messages";

    public static final String PARAM_READONLY = "read-only";

    public PlaygroundApplicationUI() {
        setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        removeAll();
        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.VERTICAL);
        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder(ALL_COMPONENTS_TAB_ID)
                                       .caption("All")
                                       .description("All UI Components")
                                       .content(new AllUiElementsPage(
                                               () -> StringUtils.equals(parameter, PARAM_READONLY)))
                                       .build(),
                               LinkkiTabSheet.builder(DYNAMIC_ASPECT_TAB_ID)
                                       .caption("Dynamic")
                                       .description("Dynamic Aspects")
                                       .content(new DynamicAnnotationsLayout()).build(),
                               LinkkiTabSheet.builder(BUGS_TAB_ID)
                                       .caption("Bugs")
                                       .content(new BugCollectionLayout()).build(),
                               LinkkiTabSheet.builder(TABLES_TAB_ID)
                                       .caption("Tables")
                                       .content(new TablePage()).build(),
                               LinkkiTabSheet.builder(NESTED_COMPONENT_PAGE_TAB_ID)
                                       .caption("Nested")
                                       .description("Nested Components")
                                       .content(new NestedComponentPage()).build(),
                               LinkkiTabSheet.builder(LOCALE_TAB_ID)
                                       .caption("Locale")
                                       .content(new LocaleInfoPage()).build(),
                               LinkkiTabSheet.builder(TAB_LAYOUT_TAB_ID)
                                       .caption("Tab Layout")
                                       .content(new TabLayoutPage()).build(),
                               LinkkiTabSheet.builder(ALIGNMENT_TAB_ID)
                                       .caption("Alignment")
                                       .content(new AlignmentPage()).build(),
                               LinkkiTabSheet.builder(MESSAGES_TAB_ID)
                                       .caption("Messages")
                                       .content(new MessagesComponent()).build());

        tabLayout.getTabsComponent().setWidth("120px");
        add(tabLayout);
    }

}

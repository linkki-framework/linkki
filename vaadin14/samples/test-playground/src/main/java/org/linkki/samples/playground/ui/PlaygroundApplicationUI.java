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
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.formsection.FormSectionPage;
import org.linkki.samples.playground.ips.IpsComponent;
import org.linkki.samples.playground.layouts.LayoutsPage;
import org.linkki.samples.playground.locale.LocaleInfoPage;
import org.linkki.samples.playground.messages.MessagesComponent;
import org.linkki.samples.playground.nestedcomponent.NestedComponentPage;
import org.linkki.samples.playground.tablayout.TabLayoutPage;
import org.linkki.samples.playground.table.TablePage;
import org.linkki.samples.playground.treetable.SampleTreeTableComponent;
import org.linkki.samples.playground.ts001.BasicElementsLayoutBehaviourPage;
import org.linkki.samples.playground.ts002.SectionHeaderBehaviorPage;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = PlaygroundAppLayout.class)
public class PlaygroundApplicationUI extends Div implements HasUrlParameter<String> {

    private static final long serialVersionUID = 1L;

    public static final String ALL_COMPONENTS_TAB_ID = "all";
    public static final String FORMSECTION_TAB_ID = "formsection";
    public static final String LAYOUTS_TAB_ID = "layouts";
    public static final String DYNAMIC_ASPECT_TAB_ID = "dynamic";
    public static final String BUGS_TAB_ID = "bugs";
    public static final String TABLES_TAB_ID = "tables";
    public static final String LOCALE_TAB_ID = "locale";
    public static final String NESTED_COMPONENT_PAGE_TAB_ID = "nestedComponentPage";
    public static final String TAB_LAYOUT_TAB_ID = "tab-layout";
    public static final String ALIGNMENT_TAB_ID = "alignment";
    public static final String MESSAGES_TAB_ID = "messages";
    public static final String TREETABLE_TAB_ID = "tree-table";
    public static final String IPS_TAB_ID = "ips";

    public static final String PARAM_READONLY = "read-only";

    public PlaygroundApplicationUI() {
        setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        removeAll();
        LinkkiTabLayout tabLayout = LinkkiTabLayout.newSidebarLayout();
        tabLayout.setId("test-scenario-selector");
        tabLayout.addTabSheets(

                               // old tab sheets
                               LinkkiTabSheet.builder(ALL_COMPONENTS_TAB_ID)
                                       .caption(VaadinIcon.FORM.create())
                                       .description("All UI Components")
                                       .content(new AllUiElementsPage(
                                               () -> StringUtils.equals(parameter, PARAM_READONLY)))
                                       .build(),
                               LinkkiTabSheet.builder(FORMSECTION_TAB_ID)
                                       .caption(VaadinIcon.FORM.create())
                                       .description("@UIFormSection")
                                       .content(new FormSectionPage(
                                               () -> StringUtils.equals(parameter, PARAM_READONLY)))
                                       .build(),
                               LinkkiTabSheet.builder(LAYOUTS_TAB_ID)
                                       .caption(VaadinIcon.LAYOUT.create())
                                       .description("Layouts")
                                       .content(new LayoutsPage())
                                       .build(),
                               LinkkiTabSheet.builder(DYNAMIC_ASPECT_TAB_ID)
                                       .caption(VaadinIcon.FLIGHT_TAKEOFF.create())
                                       .description("Dynamic Aspects")
                                       .content(new DynamicAnnotationsLayout()).build(),
                               LinkkiTabSheet.builder(TABLES_TAB_ID)
                                       .caption(VaadinIcon.TABLE.create())
                                       .description("Tables")
                                       .content(new TablePage()).build(),
                               LinkkiTabSheet.builder(NESTED_COMPONENT_PAGE_TAB_ID)
                                       .caption(VaadinIcon.ROAD_BRANCHES.create())
                                       .description("Nested Components")
                                       .content(new NestedComponentPage()).build(),
                               LinkkiTabSheet.builder(LOCALE_TAB_ID)
                                       .caption(VaadinIcon.MAP_MARKER.create())
                                       .description("Locale")
                                       .content(new LocaleInfoPage()).build(),
                               LinkkiTabSheet.builder(TAB_LAYOUT_TAB_ID)
                                       .caption(VaadinIcon.TABS.create())
                                       .description("Tab Layout")
                                       .content(new TabLayoutPage()).build(),
                               LinkkiTabSheet.builder(ALIGNMENT_TAB_ID)
                                       .caption(VaadinIcon.ALIGN_CENTER.create())
                                       .description("Alignment")
                                       .content(new AlignmentPage()).build(),
                               LinkkiTabSheet.builder(MESSAGES_TAB_ID)
                                       .caption(VaadinIcon.COMMENT_ELLIPSIS_O.create())
                                       .description("Messages Component")
                                       .content(new MessagesComponent()).build(),
                               LinkkiTabSheet.builder(TREETABLE_TAB_ID)
                                       .caption(VaadinIcon.FILE_TREE.create())
                                       .description("Tree Table")
                                       .content(new SampleTreeTableComponent()).build(),
                               LinkkiTabSheet.builder(IPS_TAB_ID)
                                       .caption(VaadinIcon.TWITTER.create())
                                       .description("IPS")
                                       .content(new IpsComponent()).build(),

                               // new test scenarios
                               LinkkiTabSheet.builder("TS001")
                                       .caption("TS001")
                                       .description("Test Scenario 001: Basic Elements Behavior")
                                       .content(new BasicElementsLayoutBehaviourPage())
                                       .build(),
                               LinkkiTabSheet.builder("TS002")
                                       .caption("TS002")
                                       .description("Test Scenario 002: Section Header Behavior")
                                       .content(new SectionHeaderBehaviorPage())
                                       .build());

        add(tabLayout);
    }

}

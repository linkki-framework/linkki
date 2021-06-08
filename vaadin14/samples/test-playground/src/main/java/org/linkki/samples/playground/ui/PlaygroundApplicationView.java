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
import org.faktorips.runtime.ValidationContext;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.ips.messages.MessageConverter;
import org.linkki.samples.playground.TestScenario;
import org.linkki.samples.playground.alignment.AlignmentPage;
import org.linkki.samples.playground.allelements.AllUiElementsPage;
import org.linkki.samples.playground.dynamicannotations.DynamicAnnotationsLayout;
import org.linkki.samples.playground.formsection.FormSectionPage;
import org.linkki.samples.playground.ips.model.IpsModelObject;
import org.linkki.samples.playground.layouts.LayoutsPage;
import org.linkki.samples.playground.messages.MessagesComponent;
import org.linkki.samples.playground.nestedcomponent.NestedComponentPage;
import org.linkki.samples.playground.tablayout.TabLayoutPage;
import org.linkki.samples.playground.table.TablePage;
import org.linkki.samples.playground.treetable.SampleTreeTableComponent;
import org.linkki.samples.playground.ts.basicelements.BasicElementsLayoutBehaviorFormSectionPmo;
import org.linkki.samples.playground.ts.basicelements.BasicElementsLayoutBehaviorHorizontalLayoutPmo;
import org.linkki.samples.playground.ts.basicelements.BasicElementsLayoutBehaviorUiSectionPmo;
import org.linkki.samples.playground.ts.basicelements.BasicElementsLayoutBehaviorVerticalLayoutPmo;
import org.linkki.samples.playground.ts.ips.DecimalFieldPmo;
import org.linkki.samples.playground.ts.ips.EnabledSectionPmo;
import org.linkki.samples.playground.ts.ips.IpsPmo;
import org.linkki.samples.playground.ts.ips.RequiredSectionPmo;
import org.linkki.samples.playground.ts.ips.VisibleSectionPmo;
import org.linkki.samples.playground.ts.localization.I18NElementsLocalizationPmo;
import org.linkki.samples.playground.ts.sectionheader.SectionHeaderBehaviorPmo;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = PlaygroundAppLayout.class)
@PageTitle("linkki Sample :: Playground")
public class PlaygroundApplicationView extends Div implements HasUrlParameter<String> {

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

    public PlaygroundApplicationView() {
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

                               // new test scenarios
                               TestScenario.id("TS001").description("Basic Elements Behavior")
                                       .testCase("TC001", new BasicElementsLayoutBehaviorUiSectionPmo())
                                       .testCase("TC002", new BasicElementsLayoutBehaviorFormSectionPmo())
                                       .testCase("TC003", new BasicElementsLayoutBehaviorHorizontalLayoutPmo())
                                       .testCase("TC004", new BasicElementsLayoutBehaviorVerticalLayoutPmo())
                                       .createTabSheet(),
                               TestScenario.id("TS002").description("Section Header Behavior")
                                       .testCase("TC001", new SectionHeaderBehaviorPmo())
                                       .createTabSheet(),
                               TestScenario.id("TS003").description("I18N Localization")
                                       .testCase("TC001", new I18NElementsLocalizationPmo())
                                       .createTabSheet(),
                               addIpsTabSheet()

        );
        add(tabLayout);
    }

    private LinkkiTabSheet addIpsTabSheet() {
        IpsModelObject ipsModelObject = new IpsModelObject();

        ValidationService validationService = () -> MessageConverter
                .convert(ipsModelObject.validate(new ValidationContext(UiFramework.getLocale())));

        BindingManager bindingManager = new DefaultBindingManager(validationService,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, new IpsPropertyDispatcherFactory());

        BindingContext bc = bindingManager.getContext("IpsBindingContext");

        return TestScenario.id("TS004").description("IPS")
                .testCase("TC001", VaadinUiCreator.createComponent(new IpsPmo(ipsModelObject), bc))
                .testCase("TC002", VaadinUiCreator.createComponent(new DecimalFieldPmo(), bc))
                .testCase("TC003", VaadinUiCreator.createComponent(new RequiredSectionPmo(), bc))
                .testCase("TC004", VaadinUiCreator.createComponent(new VisibleSectionPmo(), bc))
                .testCase("TC005", VaadinUiCreator.createComponent(new EnabledSectionPmo(), bc))
                .createTabSheet();
    }

}

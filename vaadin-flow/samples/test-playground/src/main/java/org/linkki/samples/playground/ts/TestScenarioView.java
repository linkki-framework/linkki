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
package org.linkki.samples.playground.ts;
import java.io.Serial;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.faktorips.runtime.ValidationContext;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.ComponentStyles;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.core.vaadin.component.section.GridSection;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.ips.messages.MessageConverter;
import org.linkki.samples.playground.ips.model.IpsModelObject;
import org.linkki.samples.playground.table.NumberFooterTablePmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithEmptyPlaceholderPmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithInheritedPlaceholderPmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithPlaceholderPmo;
import org.linkki.samples.playground.table.SimplePlaygroundTablePmo.TableWithoutPlaceholderPmo;
import org.linkki.samples.playground.table.SortableTablePmo;
import org.linkki.samples.playground.table.TableWithEmptyLabelColumnPmo;
import org.linkki.samples.playground.table.TableWithValidationSection;
import org.linkki.samples.playground.table.VaryingAlignmentTablePmo;
import org.linkki.samples.playground.table.collapsible.CollapsibleColumnTablePmo;
import org.linkki.samples.playground.table.columnwidth.ColumnWidthTablePmo;
import org.linkki.samples.playground.table.dynamicfields.DynamicFieldsSection;
import org.linkki.samples.playground.table.selection.SelectableTableSection;
import org.linkki.samples.playground.treetable.TreeTableSection;
import org.linkki.samples.playground.ts.aspects.BindAutoFocusPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithCloseButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithEditButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithSectionHeaderButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindCaptionWithoutButtonPmo;
import org.linkki.samples.playground.ts.aspects.BindComboBoxItemStylePmo;
import org.linkki.samples.playground.ts.aspects.BindIconPmo;
import org.linkki.samples.playground.ts.aspects.BindLabelPmo;
import org.linkki.samples.playground.ts.aspects.BindPlaceholderPmo;
import org.linkki.samples.playground.ts.aspects.BindReadOnlyBehaviorPmo;
import org.linkki.samples.playground.ts.aspects.BindSlotPmo;
import org.linkki.samples.playground.ts.aspects.BindStyleNamesPmo;
import org.linkki.samples.playground.ts.aspects.BindSuffixPmo;
import org.linkki.samples.playground.ts.aspects.BindTooltipPmo;
import org.linkki.samples.playground.ts.aspects.BindVariantNamesTables.BindVariantNamesTablePmoNoBorder;
import org.linkki.samples.playground.ts.aspects.BindVariantNamesTables.BindVariantNamesTablePmoWithoutVariant;
import org.linkki.samples.playground.ts.aspects.BindVisiblePmo;
import org.linkki.samples.playground.ts.components.ButtonPmo;
import org.linkki.samples.playground.ts.components.CheckboxesPmo;
import org.linkki.samples.playground.ts.components.ComboBoxCaptionRefreshPmo;
import org.linkki.samples.playground.ts.components.ComboBoxPmo;
import org.linkki.samples.playground.ts.components.CustomFieldPmo;
import org.linkki.samples.playground.ts.components.DateFieldPmo;
import org.linkki.samples.playground.ts.components.DateTimeFieldPmo;
import org.linkki.samples.playground.ts.components.DoubleFieldPmo;
import org.linkki.samples.playground.ts.components.DynamicComponentPage;
import org.linkki.samples.playground.ts.components.IntegerFieldPmo;
import org.linkki.samples.playground.ts.components.LabelPmo;
import org.linkki.samples.playground.ts.components.LinkPmo;
import org.linkki.samples.playground.ts.components.LongFieldPmo;
import org.linkki.samples.playground.ts.components.MultiSelectPmo;
import org.linkki.samples.playground.ts.components.RadioButtonsPmo;
import org.linkki.samples.playground.ts.components.TextAreaPmo;
import org.linkki.samples.playground.ts.components.TextFieldPmo;
import org.linkki.samples.playground.ts.components.TimeFieldPmo;
import org.linkki.samples.playground.ts.components.YesNoComboBoxPmo;
import org.linkki.samples.playground.ts.converters.ConvertersPmo;
import org.linkki.samples.playground.ts.dialogs.DialogErrorHandlerPmo;
import org.linkki.samples.playground.ts.dialogs.DialogWithCustomSizePmo;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogHandlerPmo;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogMessagePmo;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogOverflowPmo;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogSectionSpacingPmo;
import org.linkki.samples.playground.ts.dialogs.QuestionAndConfirmationDialogPmo;
import org.linkki.samples.playground.ts.dialogs.SetFormItemLabelWidthDialogPmo;
import org.linkki.samples.playground.ts.error.ErrorDialogPmo;
import org.linkki.samples.playground.ts.error.ErrorPagePmo;
import org.linkki.samples.playground.ts.ips.AvailableValuesSectionPmo;
import org.linkki.samples.playground.ts.ips.DecimalFieldPmo;
import org.linkki.samples.playground.ts.ips.DecimalLabelPmo;
import org.linkki.samples.playground.ts.ips.EnabledSectionPmo;
import org.linkki.samples.playground.ts.ips.IpsConvertersPmo;
import org.linkki.samples.playground.ts.ips.IpsLabelPage;
import org.linkki.samples.playground.ts.ips.RequiredSectionPmo;
import org.linkki.samples.playground.ts.ips.VisibleSectionPmo;
import org.linkki.samples.playground.ts.layout.HorizontalAlignmentTestComponent;
import org.linkki.samples.playground.ts.layout.HorizontalPaddingSpacingTestComponent;
import org.linkki.samples.playground.ts.layout.VerticalAlignmentTestComponent;
import org.linkki.samples.playground.ts.layout.VerticalPaddingSpacingTestComponent;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorCssLayoutPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorFormLayoutPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorFormSectionPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorHorizontalLayoutPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionComponent;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorVerticalLayoutPmo;
import org.linkki.samples.playground.ts.linkkipage.AbstractPageComponent;
import org.linkki.samples.playground.ts.linkkitext.LinkkiAnchorComponent;
import org.linkki.samples.playground.ts.linkkitext.LinkkiTextComponent;
import org.linkki.samples.playground.ts.localization.I18NElementsLocalizationPmo;
import org.linkki.samples.playground.ts.messages.ButtonValidationPmo;
import org.linkki.samples.playground.ts.messages.ConverterErrorPmo;
import org.linkki.samples.playground.ts.messages.FieldValidationPmo;
import org.linkki.samples.playground.ts.messages.MessageTableSection;
import org.linkki.samples.playground.ts.messages.RequiredValidationPmo;
import org.linkki.samples.playground.ts.nestedcomponent.NestedComponentPmo;
import org.linkki.samples.playground.ts.nestedcomponent.NestedComponentWidthPmo;
import org.linkki.samples.playground.ts.nestedcomponent.NullableModelObjectInInvisibleNestedPmo;
import org.linkki.samples.playground.ts.notifications.MessageListNotificationPmo;
import org.linkki.samples.playground.ts.notifications.TextNotificationPmo;
import org.linkki.samples.playground.ts.section.CardSectionComponent;
import org.linkki.samples.playground.ts.section.GridSectionLayoutPmo;
import org.linkki.samples.playground.ts.section.SectionHeaderAnnotationPmo;
import org.linkki.samples.playground.ts.section.SectionHeaderBehaviorComponent;
import org.linkki.samples.playground.ts.section.SectionLayoutComponent;
import org.linkki.samples.playground.ts.section.SectionThemeVariantPmo;
import org.linkki.samples.playground.ts.section.SectionsWithPlaceholder;
import org.linkki.samples.playground.ts.section.UiFormSectionMultiColumnComponentsPmo;
import org.linkki.samples.playground.ts.tablayout.AfterTabSelectedComponent;
import org.linkki.samples.playground.ts.tablayout.HorizontalTabLayoutComponent;
import org.linkki.samples.playground.ts.tablayout.TabLayoutVisibilityComponent;
import org.linkki.samples.playground.ts.tablayout.VerticalTabLayoutComponent;
import org.linkki.samples.playground.ts.table.UITableComponentPmo;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.WildcardParameter;

@RouteAlias(value = "", layout = PlaygroundAppLayout.class)
@Route(value = TestScenarioView.ROUTE, layout = PlaygroundAppLayout.class)
@PageTitle("linkki Sample :: Playground")
public class TestScenarioView extends Div implements HasUrlParameter<String> {

    public static final String TS001 = "TS001";
    public static final String TS002 = "TS002";
    public static final String TS003 = "TS003";
    public static final String TS004 = "TS004";
    public static final String TS005 = "TS005";
    public static final String TS006 = "TS006";
    public static final String TS007 = "TS007";
    public static final String TS008 = "TS008";
    public static final String TS009 = "TS009";
    public static final String TS010 = "TS010";
    public static final String TS011 = "TS011";
    public static final String TS012 = "TS012";
    public static final String TS013 = "TS013";
    public static final String TS014 = "TS014";
    public static final String TS015 = "TS015";
    public static final String TS016 = "TS016";
    public static final String TS017 = "TS017";
    public static final String TS018 = "TS018";

    public static final String TC001 = "TC001";
    public static final String TC002 = "TC002";
    public static final String TC003 = "TC003";
    public static final String TC004 = "TC004";
    public static final String TC005 = "TC005";
    public static final String TC006 = "TC006";
    public static final String TC007 = "TC007";
    public static final String TC008 = "TC008";
    public static final String TC009 = "TC009";
    public static final String TC010 = "TC010";
    public static final String TC011 = "TC011";
    public static final String TC012 = "TC012";
    public static final String TC013 = "TC013";
    public static final String TC014 = "TC014";
    public static final String TC015 = "TC015";
    public static final String TC016 = "TC016";
    public static final String TC017 = "TC017";
    public static final String TC018 = "TC018";

    static final String ROUTE = "playground";

    @Serial
    private static final long serialVersionUID = 1L;

    private final LinkkiTabLayout tabLayout;

    @SuppressWarnings("MethodLength")
    public TestScenarioView() {
        setSizeFull();
        tabLayout = LinkkiTabLayout.newSidebarLayout();
        // prevent automatically loading first tab
        tabLayout.getTabsComponent().setAutoselect(false);
        tabLayout.setId("test-scenario-selector");
        tabLayout.addTabSheets(
                               // new test scenarios
                               TestScenario.id(TS001)
                                       .testCase(TC001, BasicElementsLayoutBehaviorUiSectionComponent::new)
                                       .testCase(TC002, () -> {
                                           var component = VaadinUiCreator
                                                   .createComponent(new BasicElementsLayoutBehaviorFormSectionPmo(),
                                                                    new BindingContext(TC002));
                                           ComponentStyles.setFormItemLabelWidth(component, "200px");
                                           return component;
                                       })
                                       .testCase(TC003, new BasicElementsLayoutBehaviorHorizontalLayoutPmo())
                                       .testCase(TC004, new BasicElementsLayoutBehaviorVerticalLayoutPmo())
                                       .testCase(TC005, new BasicElementsLayoutBehaviorFormLayoutPmo())
                                       .testCase(TC006, new BasicElementsLayoutBehaviorCssLayoutPmo())
                                       .createTabSheet(),
                               TestScenario.id(TS002)
                                       .testCase(TC001, new SectionHeaderAnnotationPmo())
                                       .testCase(TC002, () -> {
                                           var component = VaadinUiCreator
                                                   .createComponent(new GridSectionLayoutPmo(), new BindingContext(
                                                           GridSectionLayoutPmo.class.getSimpleName()));
                                           component.getElement().getStyle().setHeight("200px");
                                           return component;
                                       })
                                       .testCase(TC003, SectionLayoutComponent::new)
                                       .testCase(TC004,
                                                 () -> new VerticalLayout(
                                                         SectionHeaderBehaviorComponent.createClosableSection(),
                                                         SectionHeaderBehaviorComponent.createNotClosableSection(),
                                                         SectionHeaderBehaviorComponent
                                                                 .createSectionWithRightComponent()))
                                       .testCase(TC005, new UiFormSectionMultiColumnComponentsPmo())
                                       .testCase(TC006, new SectionsWithPlaceholder())
                                       .testCase(TC007, new SectionThemeVariantPmo())
                                       .testCase(TC008, CardSectionComponent::create)
                                       .createTabSheet(),
                               TestScenario.id(TS003)
                                       .testCase(TC001, new I18NElementsLocalizationPmo())
                                       .createTabSheet(),
                               addIpsTabSheet(),
                               TestScenario.id(TS005)
                                       .testCase(TC001, new LabelPmo())
                                       .testCase(TC002, new IntegerFieldPmo())
                                       .testCase(TC003, new DoubleFieldPmo())
                                       .testCase(TC004, new TextAreaPmo())
                                       .testCase(TC005, new TextFieldPmo())
                                       .testCase(TC006, new DateFieldPmo())
                                       .testCase(TC007, new ComboBoxPmo(), new ComboBoxCaptionRefreshPmo())
                                       .testCase(TC008, new RadioButtonsPmo())
                                       .testCase(TC009, new LinkPmo())
                                       .testCase(TC010, new ButtonPmo())
                                       .testCase(TC011, new CustomFieldPmo())
                                       .testCase(TC012, DynamicComponentPage::new)
                                       .testCase(TC013, new DateTimeFieldPmo())
                                       .testCase(TC014, new YesNoComboBoxPmo())
                                       .testCase(TC015, new MultiSelectPmo())
                                       .testCase(TC016, new TimeFieldPmo())
                                       .testCase(TC017, new CheckboxesPmo())
                                       .testCase(TC018, new LongFieldPmo())
                                       .createTabSheet(),
                               TestScenario.id(TS006)
                                       .testCase(TC001, LinkkiTextComponent::new)
                                       .testCase(TC002, LinkkiAnchorComponent::new)
                                       .createTabSheet(),
                               TestScenario.id(TS007)
                                       .testCase(TC001, VerticalAlignmentTestComponent::new)
                                       .testCase(TC002, HorizontalAlignmentTestComponent::new)
                                       .testCase(TC003, new HorizontalPaddingSpacingTestComponent())
                                       .testCase(TC004, new VerticalPaddingSpacingTestComponent())
                                       .createTabSheet(),
                               TestScenario.id(TS008)
                                       .testCase(TC001, new BindTooltipPmo())
                                       .testCase(TC002, new BindCaptionWithoutButtonPmo())
                                       .testCase(TC003, new BindCaptionWithEditButtonPmo())
                                       .testCase(TC004, new BindCaptionWithSectionHeaderButtonPmo())
                                       .testCase(TC005, new BindCaptionWithCloseButtonPmo())
                                       .testCase(TC006, new BindVisiblePmo())
                                       .testCase(TC007, new BindIconPmo())
                                       .testCase(TC008, new BindStyleNamesPmo())
                                       .testCase(TC009, new BindSuffixPmo())
                                       .testCase(TC010, new BindPlaceholderPmo())
                                       .testCase(TC011, new BindComboBoxItemStylePmo())
                                       .testCase(TC012, () -> new VerticalLayout(
                                               VaadinUiCreator
                                                       .createComponent(new BindReadOnlyBehaviorPmo("writable section"),
                                                                        new BindingContext()),
                                               VaadinUiCreator
                                                       .createComponent(new BindReadOnlyBehaviorPmo(
                                                               "read-only section"),
                                                                        new BindingContextBuilder()
                                                                                .name("readOnlyContext")
                                                                                .propertyBehaviorProvider(PropertyBehaviorProvider
                                                                                        .with(PropertyBehavior
                                                                                                .readOnly()))
                                                                                .build())))
                                       .testCase(TC013, new BindAutoFocusPmo())
                                       .testCase(TC014, new BindVariantNamesTablePmoNoBorder(),
                                                 new BindVariantNamesTablePmoWithoutVariant())
                                       .testCase(TC015, new BindLabelPmo())
                                       .testCase(TC016, new BindSlotPmo(new BindSlotPmo.RightSlotPmo()))
                                       .createTabSheet(),
                               TestScenario.id(TS009)
                                       .testCase(TC001, new TextNotificationPmo())
                                       .testCase(TC002, new MessageListNotificationPmo())
                                       .createTabSheet(),
                               TestScenario.id(TS010)
                                       .testCase(TC001, HorizontalTabLayoutComponent::new)
                                       .testCase(TC002, VerticalTabLayoutComponent::new)
                                       .testCase(TC003, TabLayoutVisibilityComponent::new)
                                       .testCase(TC004, AfterTabSelectedComponent::new)
                                       .createTabSheet(),
                               TestScenario.id(TS011)
                                       .testCase(TC001, OkCancelDialogHandlerPmo::create)
                                       .testCase(TC002, new QuestionAndConfirmationDialogPmo())
                                       .testCase(TC003, new OkCancelDialogOverflowPmo())
                                       .testCase(TC004, new OkCancelDialogSectionSpacingPmo())
                                       .testCase(TC005, new SetFormItemLabelWidthDialogPmo())
                                       .testCase(TC006, new DialogWithCustomSizePmo())
                                       .testCase(TC007, new DialogErrorHandlerPmo())
                                       .testCase(TC008, new OkCancelDialogMessagePmo())
                                       .createTabSheet(),
                               TestScenario.id(TS012)
                                       .testCase(TC001, TableWithValidationSection::create)
                                       .testCase(TC002, SelectableTableSection::create)
                                       .testCase(TC003, new ColumnWidthTablePmo())
                                       .testCase(TC004, DynamicFieldsSection::create)
                                       .testCase(TC005, new NumberFooterTablePmo())
                                       .testCase(TC006,
                                                 new TableWithPlaceholderPmo(),
                                                 new TableWithInheritedPlaceholderPmo(),
                                                 new TableWithEmptyPlaceholderPmo(),
                                                 new TableWithoutPlaceholderPmo())
                                       .testCase(TC007, new VaryingAlignmentTablePmo())
                                       .testCase(TC008, () -> {
                                           GridSection section = (GridSection)VaadinUiCreator
                                                   .createComponent(new CollapsibleColumnTablePmo(),
                                                                    new BindingContext());
                                           section.setColumnVisible("programaticallyCollapsed", false);
                                           return section;
                                       })
                                       .testCase(TC009, new TableWithEmptyLabelColumnPmo())
                                       .testCase(TC010, new SortableTablePmo())
                                       .testCase(TC011, new UITableComponentPmo())
                                       .createTabSheet(),
                               TestScenario.id(TS013)
                                       .testCase(TC001, MessageTableSection::create)
                                       .testCase(TC002, FieldValidationPmo::createComponent)
                                       .testCase(TC003, ButtonValidationPmo::createComponent)
                                       .testCase(TC004, ConverterErrorPmo::createComponent)
                                       .testCase(TC005, RequiredValidationPmo::createComponent)
                                       .createTabSheet(),
                               TestScenario.id(TS014)
                                       .testCase(TC001, TreeTableSection::createPersonTreeTableSection)
                                       .testCase(TC002, TreeTableSection::createLeagueTreeTableSection)
                                       .testCase(TC003, TreeTableSection::createUpdateNodeTreeTableSection)
                                       .testCase(TC004, TreeTableSection::createTreeTableWithPlaceholderSection)
                                       .createTabSheet(),
                               TestScenario.id(TS015)
                                       .testCase(TC001, AbstractPageComponent::create)
                                       .createTabSheet(),
                               TestScenario.id(TS016)
                                       .testCase(TC001, new NestedComponentPmo())
                                       .testCase(TC002, new NullableModelObjectInInvisibleNestedPmo())
                                       .testCase(TC003, new NestedComponentWidthPmo())
                                       .createTabSheet(),
                               TestScenario.id(TS017)
                                       .testCase(TC001, () -> {
                                           var component = VaadinUiCreator
                                                   .createComponent(new ErrorDialogPmo(),
                                                                    new BindingContext());
                                           ComponentStyles.setFormItemLabelWidth(component, "300px");
                                           return component;
                                       })
                                       .testCase(TC002, () -> {
                                           var component = VaadinUiCreator
                                                   .createComponent(new ErrorPagePmo(),
                                                                    new BindingContext());
                                           ComponentStyles.setFormItemLabelWidth(component, "300px");
                                           return component;
                                       })
                                       .createTabSheet(),
                               TestScenario.id(TS018)
                                       .testCase(TC001, new ConvertersPmo())
                                       .createTabSheet());
        add(tabLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        if (!parameter.isEmpty()) {
            var location = new Location(parameter);
            tabLayout.setSelectedTabSheet(location.getFirstSegment());
            location.getSubLocation()
                    .ifPresentOrElse(l -> getTestCaseTabLayout().setSelectedTabSheet(l.getFirstSegment()),
                                     () -> getTestCaseTabLayout().setSelectedIndex(0));
        } else {
            tabLayout.setSelectedIndex(0);
            getTestCaseTabLayout().setSelectedIndex(0);
        }
    }

    private LinkkiTabLayout getTestCaseTabLayout() {
        return ((LinkkiTabLayout)tabLayout.getSelectedTabSheet().getContent());
    }

    private LinkkiTabSheet addIpsTabSheet() {
        var ipsModelObject = new IpsModelObject();
        // tag::createValidationService[]
        ValidationService validationService = () -> MessageConverter
                .convert(ipsModelObject.validate(new ValidationContext(UiFramework.getLocale())));
        // end::createValidationService[]

        // tag::createBindingManager[]
        BindingManager bindingManager = new DefaultBindingManager(validationService,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, new IpsPropertyDispatcherFactory());
        // end::createBindingManager[]

        BindingContext bc = bindingManager.getContext("IpsBindingContext");

        return TestScenario.id(TS004)
                .testCase(TC001, () -> new IpsLabelPage(bc))
                .testCase(TC002, () -> VaadinUiCreator.createComponent(new DecimalFieldPmo(), bc))
                .testCase(TC003, () -> VaadinUiCreator.createComponent(new RequiredSectionPmo(), bc))
                .testCase(TC004, () -> VaadinUiCreator.createComponent(new VisibleSectionPmo(), bc))
                .testCase(TC005, () -> VaadinUiCreator.createComponent(new EnabledSectionPmo(), bc))
                .testCase(TC006, () -> VaadinUiCreator.createComponent(new DecimalLabelPmo(), bc))
                .testCase(TC007, () -> VaadinUiCreator.createComponent(new AvailableValuesSectionPmo(), bc))
                .testCase(TC008, () -> VaadinUiCreator.createComponent(new IpsConvertersPmo(), bc))
                .createTabSheet();
    }

    public static String getLocation(String tsId, String tcId) {
        return ROUTE + "/" + tsId + Optional.ofNullable(tcId)
                .filter(StringUtils::isNotBlank)
                .map(s -> "/" + s)
                .orElse("");
    }

}

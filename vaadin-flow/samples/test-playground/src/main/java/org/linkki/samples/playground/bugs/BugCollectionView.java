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
package org.linkki.samples.playground.bugs;

import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.bugs.lin1486.ComboBoxVanishingValuePmo;
import org.linkki.samples.playground.bugs.lin1608.PmoReadonlyModelNotReadonlyPmo;
import org.linkki.samples.playground.bugs.lin1738.DoubleClickPmo;
import org.linkki.samples.playground.bugs.lin1795.ComboBoxPmo;
import org.linkki.samples.playground.bugs.lin1797.OnlyTablePmo;
import org.linkki.samples.playground.bugs.lin1797.SectionTablePmo;
import org.linkki.samples.playground.bugs.lin1890.Lin1890HierarchicalTablePmo;
import org.linkki.samples.playground.bugs.lin1917.TriangleTablePmo;
import org.linkki.samples.playground.bugs.lin2200.ComboBoxNewInstancePmo;
import org.linkki.samples.playground.bugs.lin2555.TextfieldWithEnterButtonPmo;
import org.linkki.samples.playground.bugs.lin2567.TabSheetContentWithText;
import org.linkki.samples.playground.bugs.lin2622.MassValuesComboBoxPmo;
import org.linkki.samples.playground.bugs.lin2867.FocusringBug;
import org.linkki.samples.playground.bugs.lin2915.OverflowIssues;
import org.linkki.samples.playground.bugs.lin3497.FrontendDependencyInjectionTestInterface;
import org.linkki.samples.playground.bugs.lin3531.BindMessagesOnGridColumnsBug;
import org.linkki.samples.playground.bugs.lin3846.ReactRouterTestView;
import org.linkki.samples.playground.bugs.lin3884.EmptyLabelComponentsPmo;
import org.linkki.samples.playground.bugs.lin4780.OverlappingDialogHeadersBug;
import org.linkki.samples.playground.bugs.lin4798.LongLabelPmo;
import org.linkki.samples.playground.bugs.lin4803.DialogMinWidthBug;
import org.linkki.samples.playground.bugs.lin4808.VariantCardSectionsDialogBug;
import org.linkki.samples.playground.ts.formelements.ComboBoxCaptionRefreshPmo;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = BugCollectionView.ROUTE, layout = PlaygroundAppLayout.class)
@PageTitle("linkki Sample :: " + BugCollectionView.NAME)
public class BugCollectionView extends LinkkiTabLayout implements HasUrlParameter<String> {

    public static final String ROUTE = "bugs";

    public static final String NAME = "Bugs";

    private static final long serialVersionUID = 5094485819774125238L;

    public BugCollectionView(FrontendDependencyInjectionTestInterface frontendDependencyInjectionTest) {
        super(Orientation.VERTICAL);

        addTabSheets(createPmoTabSheet(ComboBoxCaptionRefreshPmo.ID,
                                       ComboBoxCaptionRefreshPmo.CAPTION,
                                       ComboBoxCaptionRefreshPmo::new),
                     createPmoTabSheet(ComboBoxVanishingValuePmo.LIN_1486,
                                       ComboBoxVanishingValuePmo.CAPTION,
                                       bc -> new ComboBoxVanishingValuePmo(bc::modelChanged)),
                     createPmoTabSheet(ComboBoxNewInstancePmo.LIN_2200,
                                       ComboBoxNewInstancePmo.CAPTION,
                                       ComboBoxNewInstancePmo::new),
                     createPmoTabSheet(PmoReadonlyModelNotReadonlyPmo.LIN_1608,
                                       PmoReadonlyModelNotReadonlyPmo.CAPTION,
                                       PmoReadonlyModelNotReadonlyPmo::new),
                     createPmoTabSheet(DoubleClickPmo.LIN_1738,
                                       DoubleClickPmo.CAPTION,
                                       DoubleClickPmo::new),
                     createPmoTabSheet(ComboBoxPmo.LIN_1795,
                                       ComboBoxPmo.CAPTION,
                                       ComboBoxPmo::new),
                     createTabSheet(OnlyTablePmo.LIN_1797, OnlyTablePmo.CAPTION,
                                    () -> new PmoBasedSectionFactory().createSection(new OnlyTablePmo(),
                                                                                     new BindingContext())),
                     createPmoTabSheet(SectionTablePmo.LIN_1797_SECTION,
                                       SectionTablePmo.CAPTION,
                                       SectionTablePmo::new),
                     createPmoTabSheet(Lin1890HierarchicalTablePmo.LIN_1890,
                                       Lin1890HierarchicalTablePmo.CAPTION,
                                       Lin1890HierarchicalTablePmo::new),
                     createPmoTabSheet(TriangleTablePmo.LIN_1917,
                                       TriangleTablePmo.CAPTION,
                                       TriangleTablePmo::new),
                     createPmoTabSheet(TextfieldWithEnterButtonPmo.LIN_2555,
                                       TextfieldWithEnterButtonPmo.CAPTION,
                                       TextfieldWithEnterButtonPmo::new),
                     createTabSheet(TabSheetContentWithText.LIN_2567,
                                    TabSheetContentWithText.CAPTION, TabSheetContentWithText::new),
                     createPmoTabSheet(MassValuesComboBoxPmo.LIN_2622,
                                       MassValuesComboBoxPmo.CAPTION,
                                       MassValuesComboBoxPmo::new),
                     createTabSheet(OverflowIssues.LIN_2915,
                                    OverflowIssues.CAPTION, OverflowIssues::new),
                     createTabSheet(FocusringBug.LIN_2867,
                                    FocusringBug.CAPTION, FocusringBug::new),
                     createTabSheet(FrontendDependencyInjectionTestInterface.ID,
                                    FrontendDependencyInjectionTestInterface.CAPTION,
                                    () -> new VerticalLayout(
                                            frontendDependencyInjectionTest.createComponent(),
                                            VaadinUiCreator.createComponent(
                                                                            frontendDependencyInjectionTest.createPmo(),
                                                                            new BindingContext()))),
                     createTabSheet(BindMessagesOnGridColumnsBug.LIN_3531,
                                    BindMessagesOnGridColumnsBug.CAPTION,
                                    BindMessagesOnGridColumnsBug::new),
                     createPmoTabSheet(EmptyLabelComponentsPmo.LIN_3884,
                                       EmptyLabelComponentsPmo.CAPTION,
                                       EmptyLabelComponentsPmo::new),
                     createTabSheet(ReactRouterTestView.LIN_3846,
                                    ReactRouterTestView.CAPTION, ReactRouterTestView::new),
                     createTabSheet(OverlappingDialogHeadersBug.LIN_4780,
                                    OverlappingDialogHeadersBug.CAPTION,
                                    OverlappingDialogHeadersBug::new),
                     createTabSheet(DialogMinWidthBug.LIN_4803,
                                    DialogMinWidthBug.CAPTION,
                                    DialogMinWidthBug::new),
                     createPmoTabSheet(LongLabelPmo.LIN_4798,
                                       LongLabelPmo.CAPTION,
                                       LongLabelPmo::new),
                     createTabSheet(VariantCardSectionsDialogBug.LIN_4808,
                                    VariantCardSectionsDialogBug.CAPTION,
                                    VariantCardSectionsDialogBug::new));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (StringUtils.isNoneEmpty(parameter)) {
            setSelectedTabSheet(parameter);
        }
    }

    private LinkkiTabSheet createPmoTabSheet(String id, String caption, Supplier<Object> pmoCreation) {
        var bindingContext = new BindingContext();
        var pmo = pmoCreation.get();
        var component = VaadinUiCreator.createComponent(pmo, bindingContext);
        return LinkkiTabSheet.builder(id)
                .caption(createCaptionLink(id, caption))
                .content(() -> new VerticalLayout(component))
                .build();
    }

    private LinkkiTabSheet createPmoTabSheet(String id,
            String caption,
            Function<BindingContext, Object> pmoCreation) {
        var bindingContext = new BindingContext();
        var pmo = pmoCreation.apply(bindingContext);
        var component = VaadinUiCreator.createComponent(pmo, bindingContext);
        return LinkkiTabSheet.builder(id)
                .caption(createCaptionLink(id, caption))
                .content(() -> new VerticalLayout(component))
                .build();
    }

    private LinkkiTabSheet createTabSheet(String id, String caption, Supplier<Component> content) {
        return LinkkiTabSheet.builder(id)
                .caption(createCaptionLink(id, caption))
                .content(content)
                .build();
    }

    private Component createCaptionLink(String id, String caption) {
        return new Anchor(ROUTE + "/" + id, caption);
    }
}
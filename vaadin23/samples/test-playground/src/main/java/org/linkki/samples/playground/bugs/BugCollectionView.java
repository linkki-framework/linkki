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
package org.linkki.samples.playground.bugs;

import java.util.function.Function;
import java.util.function.Supplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.bugs.lin1442.ComboBoxCaptionRefreshPmo;
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
import org.linkki.samples.playground.ui.PlaygroundAppLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "bugs", layout = PlaygroundAppLayout.class)
@PageTitle("linkki Sample :: " + BugCollectionView.NAME)
public class BugCollectionView extends LinkkiTabLayout {

    public static final String NAME = "Bugs";

    private static final long serialVersionUID = 5094485819774125238L;

    public BugCollectionView() {
        super(Orientation.VERTICAL);

        addTabSheets(createTabSheet(ComboBoxCaptionRefreshPmo::new),
                     createTabSheet(bc -> new ComboBoxVanishingValuePmo(bc::modelChanged)),
                     createTabSheet(ComboBoxNewInstancePmo::new),
                     createTabSheet(PmoReadonlyModelNotReadonlyPmo::new),
                     createTabSheet(DoubleClickPmo::new),
                     createTabSheet(ComboBoxPmo::new),
                     LinkkiTabSheet.builder(OnlyTablePmo.CAPTION)
                             .content(() -> new PmoBasedSectionFactory().createSection(new OnlyTablePmo(),
                                                                                       new BindingContext()))
                             .build(),
                     createTabSheet(SectionTablePmo::new),
                     createTabSheet(Lin1890HierarchicalTablePmo::new),
                     createTabSheet(TriangleTablePmo::new),
                     createTabSheet(TextfieldWithEnterButtonPmo::new),
                     LinkkiTabSheet.builder(TabSheetContentWithText.CAPTION).content(TabSheetContentWithText::new)
                             .build(),
                     createTabSheet(MassValuesComboBoxPmo::new));
    }

    private LinkkiTabSheet createTabSheet(Supplier<Object> pmoCreation) {
        return createTabSheet(bc -> pmoCreation.get());
    }

    private LinkkiTabSheet createTabSheet(Function<BindingContext, Object> pmoCreation) {
        BindingContext bindingContext = new BindingContext();
        Object pmo = pmoCreation.apply(bindingContext);
        Component component = VaadinUiCreator.createComponent(pmo, bindingContext);

        if (component instanceof AbstractSection) {
            AbstractSection section = (AbstractSection)component;
            return LinkkiTabSheet.builder(section.getCaption())
                    .caption(section.getCaption())
                    .content(() -> new VerticalLayout(section))
                    .build();
        } else {
            return LinkkiTabSheet.builder(pmo.getClass().getSimpleName())
                    .content(() -> component)
                    .build();
        }
    }
}
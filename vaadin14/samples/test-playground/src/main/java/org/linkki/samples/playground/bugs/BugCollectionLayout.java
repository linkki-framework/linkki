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

import org.linkki.core.binding.BindingContext;
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
import org.linkki.samples.playground.bugs.lin1917.TriangleTablePmo;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "bugs", layout = PlaygroundAppLayout.class)
@PageTitle(BugCollectionLayout.PAGE_TITLE)
public class BugCollectionLayout extends LinkkiTabLayout {

    public static final String PAGE_TITLE = "Bugs";

    private static final long serialVersionUID = 5094485819774125238L;

    public BugCollectionLayout() {
        super(Orientation.VERTICAL);
        getContent().getElement().getThemeList().add(THEME_VARIANT_SOLID);

        addTabSheets(createTabSheet(PmoBasedSectionFactory
                .createAndBindSection(new ComboBoxCaptionRefreshPmo(), new BindingContext())),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new ComboBoxVanishingValuePmo(
                             bc::modelChanged))),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new PmoReadonlyModelNotReadonlyPmo())),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new DoubleClickPmo())),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new ComboBoxPmo())),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new OnlyTablePmo())),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new SectionTablePmo())),
                     // TODO LIN-2088
                     // createTabSheet(createSectionWithSeparateBindingContext(bc -> new
                     // Lin1890HierarchicalTablePmo())),
                     createTabSheet(createSectionWithSeparateBindingContext(bc -> new TriangleTablePmo())));
    }

    private AbstractSection createSectionWithSeparateBindingContext(Function<BindingContext, Object> pmoCreation) {
        BindingContext bindingContext = new BindingContext();
        AbstractSection section = PmoBasedSectionFactory.createAndBindSection(pmoCreation.apply(bindingContext),
                                                                              bindingContext);
        return section;
    }

    private LinkkiTabSheet createTabSheet(AbstractSection section) {
        String caption = section.getCaption();
        return LinkkiTabSheet.builder(caption)
                .caption(caption)
                .content(new VerticalLayout(section))
                .build();
    }
}
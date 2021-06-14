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
import org.linkki.samples.playground.bugs.lin1442.ComboBoxCaptionRefreshPmo;
import org.linkki.samples.playground.bugs.lin1486.ComboBoxVanishingValuePmo;
import org.linkki.samples.playground.bugs.lin1608.PmoReadonlyModelNotReadonlyPmo;
import org.linkki.samples.playground.bugs.lin1738.DoubleClickPmo;
import org.linkki.samples.playground.bugs.lin1795.ComboBoxPmo;
import org.linkki.samples.playground.bugs.lin1797.OnlyTablePmo;
import org.linkki.samples.playground.bugs.lin1797.SectionTablePmo;
import org.linkki.samples.playground.bugs.lin1917.TriangleTablePmo;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class BugCollectionLayout extends VerticalLayout {

    public static final String ID = "BugCollection";

    private static final long serialVersionUID = 1L;

    public BugCollectionLayout() {
        BindingContext bindingContext = new BindingContext();
        add(PmoBasedSectionFactory.createAndBindSection(new ComboBoxCaptionRefreshPmo(), bindingContext));
        addComponentWithSeparateBindingContext(bc -> new ComboBoxVanishingValuePmo(bc::modelChanged));
        addComponentWithSeparateBindingContext(bc -> new PmoReadonlyModelNotReadonlyPmo());
        addComponentWithSeparateBindingContext(bc -> new DoubleClickPmo());
        addComponentWithSeparateBindingContext(bc -> new ComboBoxPmo());
        // TODO Tooltips do not work yet LIN-2054
        // addComponentWithSeparateBindingContext(bc -> new Lin1890HierarchicalTablePmo());
        addComponentWithSeparateBindingContext(bc -> new OnlyTablePmo());
        addComponentWithSeparateBindingContext(bc -> new SectionTablePmo());
        addComponentWithSeparateBindingContext(bc -> new TriangleTablePmo());
    }

    private void addComponentWithSeparateBindingContext(Function<BindingContext, Object> pmoCreation) {
        BindingContext bindingContext = new BindingContext();
        add(PmoBasedSectionFactory.createAndBindSection(pmoCreation.apply(bindingContext), bindingContext));
    }

}
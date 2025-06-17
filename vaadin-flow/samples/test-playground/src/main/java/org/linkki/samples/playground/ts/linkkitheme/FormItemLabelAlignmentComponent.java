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
package org.linkki.samples.playground.ts.linkkitheme;

import java.io.Serial;

import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.theme.LinkkiTheme;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.ts.layouts.AbstractBasicElementsLayoutBehaviorPmo;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionPmo;

import com.vaadin.flow.component.Component;

public class FormItemLabelAlignmentComponent extends LinkkiTabLayout {

    public static final String ALIGNMENT_START = "START";
    public static final String ALIGNMENT_END = "END";

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String DESCRIPTION = "@UISection with label alignment ";

    public FormItemLabelAlignmentComponent() {
        setWidthFull();

        addTabSheet(LinkkiTabSheet.builder(ALIGNMENT_START)
                .description(DESCRIPTION + ALIGNMENT_START)
                .content(() -> createSheetContent(new BasicElementsLayoutLabelStartPmo()))
                .build());
        addTabSheet(LinkkiTabSheet.builder(ALIGNMENT_END)
                .description(DESCRIPTION + ALIGNMENT_END)
                .content(() -> createSheetContent(new BasicElementsLayoutBehaviorUiSectionPmo()))
                .build());
    }

    private Component createSheetContent(AbstractBasicElementsLayoutBehaviorPmo pmo) {
        var bindingContext = new DefaultBindingManager(pmo::validate).getContext(pmo.getClass());
        var component = VaadinUiCreator.createComponent(pmo, bindingContext);
        component.getElement().getStyle().setPadding("var(--lumo-space-m)");
        return component;
    }

    @UISection(caption = "@UISection")
    @BindVariantNames(LinkkiTheme.VARIANT_FORM_ITEM_LABEL_START)
    private static class BasicElementsLayoutLabelStartPmo extends AbstractBasicElementsLayoutBehaviorPmo {
    }
}

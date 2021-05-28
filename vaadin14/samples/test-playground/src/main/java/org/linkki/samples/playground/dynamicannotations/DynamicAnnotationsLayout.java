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

package org.linkki.samples.playground.dynamicannotations;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DynamicAnnotationsLayout extends VerticalLayout {

    public static final String ID = "Dynamic Annotations";

    private static final long serialVersionUID = 1L;

    public DynamicAnnotationsLayout() {
        BindingContext bindingContext = new BindingContext();
        add(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithoutButtonPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithEditButtonPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithSectionHeaderButtonPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new DynamicCaptionWithCloseButtonPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new DynamicTooltipPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new BindVisibleSectionPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new BindIconComponentsPmo(), bindingContext));
        add(PmoBasedSectionFactory
                .createAndBindSection(new BindStyleNamesComponentsPmo(), bindingContext));
    }

}

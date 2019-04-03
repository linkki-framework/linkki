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
package org.linkki.core.ui.creation.section;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.component.section.BaseSection;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory.SectionBuilder;

/**
 * Object holding the state about PMO and binding context, as well as the property dispatcher in use,
 * while creating a section for them. Intended to be used only once.
 * <p>
 * Only used for {@link BaseSection}.
 * 
 * @deprecated since January 2019. Use {@link PmoBasedSectionFactory} instead.
 */
@Deprecated
public class SectionCreationContext {

    private SectionBuilder<?> sectionBuilder;

    /**
     * @deprecated since January 2019. Use {@link PmoBasedSectionFactory} instead.
     */
    @Deprecated
    public SectionCreationContext(Object pmo, BindingContext bindingContext) {
        sectionBuilder = new PmoBasedSectionFactory.SectionBuilder<>(requireNonNull(pmo, "pmo must not be null"),
                requireNonNull(bindingContext, "bindingContext must not be null"));
    }

    /**
     * @deprecated since January 2019. Use
     *             {@link PmoBasedSectionFactory#createSection(Object, BindingContext)} instead.
     */
    @Deprecated
    public BaseSection createSection() {
        return (BaseSection)sectionBuilder.createSection();
    }

}
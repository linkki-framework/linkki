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
package org.linkki.core.ui.table;

import static java.util.Objects.requireNonNull;

import org.eclipse.jdt.annotation.NonNull;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.section.PmoBasedSectionFactory;

/**
 * A factory to create sections showing a single table based on a PMO.
 * 
 * @deprecated since January 2019. Use {@link PmoBasedSectionFactory} for {@link TableSection} as
 *             well.
 */
@Deprecated
public class PmoBasedTableSectionFactory<@NonNull ROW> {

    private final ContainerPmo<ROW> containerPmo;
    private final BindingContext bindingContext;

    /**
     * Creates a new factory.
     * 
     * @deprecated since January 2019. Use {@link PmoBasedSectionFactory} for {@link TableSection} as
     *             well.
     */
    @Deprecated
    public PmoBasedTableSectionFactory(ContainerPmo<ROW> containerPmo, BindingContext bindingContext) {
        this.containerPmo = requireNonNull(containerPmo, "containerPmo must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
    }

    /**
     * Creates a new section showing a table, table structure and content is defined by the factory's
     * {@link ContainerPmo}.
     * 
     * @deprecated since January 2019. Use
     *             {@link PmoBasedSectionFactory#createSection(Object, BindingContext)} instead.
     */
    @Deprecated
    public TableSection createSection() {
        return (TableSection)new PmoBasedSectionFactory().createSection(containerPmo, bindingContext);
    }
}

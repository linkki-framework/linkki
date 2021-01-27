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
package org.linkki.core.vaadin.component.tablayout;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import org.linkki.util.LazyReference;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;

/**
 * Represents a tab that is displayed in a {@link LinkkiTabLayout}. This consists of a @{@link Tab}
 * containing the metadata for this sheet and the corresponding content {@link Component}.
 */
public class LinkkiTabSheet {

    private final Tab tab;
    private final LazyReference<Component> contentReference;

    /**
     * Creates a {@link LinkkiTabSheet} with the given ID, caption component, description and content.
     * The description will be displayed as tooltip information of the tab. The content is provided by a
     * {@link Supplier} that is called when the sheet is selected for the first time.
     * 
     * @param id The identifier to identify this tab sheet
     * @param caption Caption that is shown on the tab
     * @param description The description of the sheet that is displayed as tooltip
     * @param contentSupplier The content of the tab sheet that is displayed if this sheet is selected
     */
    public LinkkiTabSheet(String id, String caption, String description, Supplier<Component> contentSupplier) {
        this.tab = new Tab(requireNonNull(caption, "caption must not be null"));
        tab.setId(id);
        // TODO LIN-2054 description must be set as tooltip
        this.contentReference = new LazyReference<>(
                requireNonNull(contentSupplier, "contentSupplier must not be null"));
    }

    /**
     * Creates a {@link LinkkiTabSheet} with the given ID, caption component, description and content.
     * The description will be displayed as tooltip information of the tab.
     * 
     * @param id The identifier to identify this tab sheet
     * @param caption Caption that is shown on the tab
     * @param description The description of the sheet that is displayed as tooltip
     * @param content The content of the tab sheet that is displayed if this sheet is selected
     */
    public LinkkiTabSheet(String id, String caption, String description, Component content) {
        this(id, caption, description, () -> requireNonNull(content, "content must not be null"));
    }

    /**
     * Creates a {@link LinkkiTabSheet} with the given ID, caption component, description and content.
     * The description will be displayed as tooltip information of the tab. The content is provided by a
     * {@link Supplier} that is called when the sheet is selected for the first time.
     * 
     * @param id The identifier to identify this tab sheet within the {@link LinkkiTabLayout}
     * @param captionComponent Component that is shown as caption on the tab
     * @param description The description that is displayed as tooltip
     * @param contentSupplier The supplier for the content of the sheet that is displayed if this sheet
     *            is selected
     */
    public LinkkiTabSheet(String id, Component captionComponent, String description,
            Supplier<Component> contentSupplier) {
        this(id, "", description, contentSupplier);
        tab.add(captionComponent);
    }

    /**
     * Creates a {@link LinkkiTabSheet} with the given ID, caption component, description and content.
     * The description will be displayed as tooltip information of the tab.
     * 
     * @param id The identifier to identify this tab sheet within the {@link LinkkiTabLayout}
     * @param captionComponent Component that is shown as caption on the tab
     * @param description The description that is displayed as tooltip
     * @param content The supplier for the content of the sheet that is displayed if this sheet is
     *            selected
     */
    public LinkkiTabSheet(String id, Component captionComponent, String description, Component content) {
        this(id, captionComponent, description, () -> requireNonNull(content, "content must not be null"));
    }

    public Tab getTab() {
        return tab;
    }

    /**
     * Returns the content of this tab sheet.
     * <p>
     * Note that this method should not be called before the first selection to ensure lazy loading.
     */
    Component getContent() {
        return contentReference.getReference();
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s])", getClass().getSimpleName(), tab.getId().get());
    }
}
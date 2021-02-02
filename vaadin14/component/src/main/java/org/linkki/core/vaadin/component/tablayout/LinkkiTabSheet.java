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

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.util.LazyReference;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.tabs.Tab;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Represents a tab that is displayed in a {@link LinkkiTabLayout}. This consists of a @{@link Tab}
 * containing the metadata for this sheet and the corresponding content {@link Component}.
 */
public class LinkkiTabSheet {

    private final Tab tab;
    private final LazyReference<Component> contentReference;
    private final Handler onSelectionHandler;

    /* private */ LinkkiTabSheet(String id, String caption, @CheckForNull Component captionComponent,
            String description,
            Supplier<Component> contentSupplier,
            Handler onSelectionHandler) {
        this.tab = new Tab(requireNonNull(caption, "caption must not be null"));
        tab.setId(requireNonNull(id, "id must not be null"));
        // TODO LIN-2054 description must be set as tooltip
        tab.getElement().setAttribute("title", description);
        this.contentReference = new LazyReference<>(
                requireNonNull(contentSupplier, "contentSupplier must not be null"));
        this.onSelectionHandler = requireNonNull(onSelectionHandler, "onSelectionHandler must not be null");

        if (captionComponent != null) {
            tab.add(captionComponent);
        }
    }

    public Tab getTab() {
        return tab;
    }

    /**
     * Returns the content of this tab sheet.
     * <p>
     * Note that this method should not be called before the first selection to ensure lazy loading.
     */
    public Component getContent() {
        return contentReference.getReference();
    }

    protected Handler getOnSelectionHandler() {
        return onSelectionHandler;
    }

    /**
     * Returns the ID of this tab sheet
     * 
     * @return ID of the tab sheet
     * 
     * @see #builder(String)
     */
    public String getId() {
        return getTab().getId().get();
    }

    /**
     * Returns the description of the tab sheet that is shown as tooltip on the tab.
     * 
     * @return description of the tab sheet
     */
    public String getDescription() {
        return getTab().getElement().getProperty("title");
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s])", getClass().getSimpleName(), tab.getId().get());
    }

    /**
     * Creates a new builder. The given ID is used as the HTML ID of the {@link Tab}.
     */
    public static LinkkiTabSheetBuilder builder(String id) {
        return new LinkkiTabSheetBuilder(id);
    }

    public static class LinkkiTabSheetBuilder {

        private final String id;

        @CheckForNull
        private String caption;

        @CheckForNull
        private Component captionComponent;

        @CheckForNull
        private String description;

        @CheckForNull
        private Supplier<Component> contentSupplier;

        private Handler onSelectionHandler = Handler.NOP_HANDLER;

        private LinkkiTabSheetBuilder(String id) {
            this.id = requireNonNull(id, "id must not be null");
        }

        /**
         * Specifies the given {@link Component} as caption of the tab. Any existing caption that is set
         * by {@link #caption(Component) or #withCaption(String)} is overwritten. The caption of
         * sheet can be either a text of a component. To use a text caption, call
         * {@link #caption(String)} instead.
         * 
         * @param newCaption caption component of the tab
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder caption(Component newCaption) {
            this.captionComponent = requireNonNull(newCaption, "newCaption must not be null");
            this.caption = null;
            return this;
        }

        /**
         * Specifies the caption of the tab. Any existing caption that is set by
         * {@link #caption(Component) or #withCaption(String)} is overwritten. To use a
         * {@link Component} as the caption, call {@link #caption(Component)} instead.
         * 
         * @param newCaption caption text of the tab
         * @return {@code this} for method chaining
         * 
         * @see Tab#getLabel()
         */
        public LinkkiTabSheetBuilder caption(String newCaption) {
            this.caption = requireNonNull(newCaption, "newCaption must not be null");
            this.captionComponent = null;
            return this;
        }

        /**
         * Specifies the description of the tab sheet that is shown as tooltip of the tab.
         * 
         * @param newDescription description of the tab sheet
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder description(String newDescription) {
            this.description = requireNonNull(newDescription, "newDescription must not be null");
            return this;
        }

        /**
         * Specifies the content of the tab sheet that is shown upon selection.
         * 
         * @param newContent content of the tab sheet
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder content(Component newContent) {
            this.contentSupplier = () -> requireNonNull(newContent, "newContent must not be null");
            return this;
        }

        /**
         * Specifies the content of the tab sheet as a supplier. The supplier is called only once when
         * the tab is selected for the first time.
         * 
         * @param newContentSupplier content of the tab sheet
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder content(Supplier<Component> newContentSupplier) {
            this.contentSupplier = requireNonNull(newContentSupplier, "newContentSupplier must not be null");
            return this;
        }

        /**
         * Specifies a handler that is called upon selection.
         * 
         * @param newOnSelectionHandler handler that should be called on selection
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder onSelectionHandler(Handler newOnSelectionHandler) {
            this.onSelectionHandler = requireNonNull(newOnSelectionHandler, "newOnSelectionHandler must not be null");
            return this;
        }

        /**
         * Builds a {@link LinkkiTabSheet} instance using the values in this builder.
         * <p>
         * Note that a content must be provided. The caption and description default to the ID if none
         * is provided.
         * 
         * @return a new {@link LinkkiTabSheet}
         */
        public LinkkiTabSheet build() {
            String descriptionWithFallback = StringUtils.defaultIfBlank(description, "");
            Supplier<Component> nonNullContentSupplier = requireNonNull(contentSupplier,
                                                                        "Content must be specified for a "
                                                                                + LinkkiTabSheet.class.getSimpleName());

            String captionWithFallBack = (captionComponent == null && caption == null) ? id
                    : Optional.ofNullable(caption).orElse("");
            return new LinkkiTabSheet(id, captionWithFallBack, captionComponent, descriptionWithFallback,
                    nonNullContentSupplier,
                    onSelectionHandler);
        }
    }
}
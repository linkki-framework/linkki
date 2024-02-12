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
package org.linkki.core.vaadin.component.tablayout;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.util.LazyReference;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.EventUtil;
import com.vaadin.flow.shared.Registration;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Represents a tab that is displayed in a {@link LinkkiTabLayout}. This consists of a @{@link Tab}
 * containing the metadata for this sheet and the corresponding content {@link Component}.
 */
public class LinkkiTabSheet {

    private final Tab tab;
    private final LazyReference<Component> contentReference;
    private final BooleanSupplier visibilitySupplier;

    @CheckForNull
    private ComponentEventBus eventBus;

    /* private */ LinkkiTabSheet(String id, String caption, @CheckForNull Component captionComponent,
            String description,
            Supplier<Component> contentSupplier,
            BooleanSupplier visibilitySupplier,
            List<ComponentEventListener<TabSheetSelectionChangeEvent>> onSelectionListener) {
        this.tab = new Tab(requireNonNull(caption, "caption must not be null"));
        tab.setId(requireNonNull(id, "id must not be null"));
        // TODO LIN-2054 description must be set as tooltip
        tab.getElement().setAttribute("title", description);
        this.contentReference = new LazyReference<>(
                requireNonNull(contentSupplier, "contentSupplier must not be null"));
        onSelectionListener.forEach(this::addTabSelectionChangeListener);

        this.visibilitySupplier = requireNonNull(visibilitySupplier, "visibilitySupplier must not be null");
        tab.setVisible(visibilitySupplier.getAsBoolean());

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
        return contentReference.get();
    }

    boolean isContentLoaded() {
        return contentReference.isValuePresent();
    }

    protected boolean isVisible() {
        return visibilitySupplier.getAsBoolean();
    }

    /**
     * Returns the ID of this tab sheet
     * 
     * @return ID of the tab sheet
     * 
     * @see #builder(String)
     */
    public String getId() {
        return getTab().getId().orElseThrow();
    }

    /**
     * Returns the description of the tab sheet that is shown as tooltip on the tab.
     * 
     * @return description of the tab sheet
     */
    public String getDescription() {
        return getTab().getElement().getAttribute("title");
    }

    protected void select(HasComponents contentWrapper, SelectedChangeEvent e) {
        Component content = getContent();
        if (!content.getParent().isPresent()) {
            contentWrapper.add(content);
        }
        content.setVisible(true);
        TabSheetSelectionChangeEvent tabSelectedEvent = new TabSheetSelectionChangeEvent(e, this);
        fireTabSheetSelectionChangeEvent(tabSelectedEvent);
        callAfterSelectionObserver(tabSelectedEvent);
    }

    protected ComponentEventBus getEventBus() {
        if (eventBus == null) {
            eventBus = new ComponentEventBus(tab);
        }
        return eventBus;
    }

    public Registration addTabSelectionChangeListener(ComponentEventListener<TabSheetSelectionChangeEvent> listener) {
        return getEventBus().addListener(TabSheetSelectionChangeEvent.class, listener);
    }

    private void fireTabSheetSelectionChangeEvent(TabSheetSelectionChangeEvent e) {
        getEventBus().fireEvent(e);
    }

    private void callAfterSelectionObserver(TabSheetSelectionChangeEvent event) {
        Component content = event.getTabSheet().getContent();

        Collection<Element> descendants = new ArrayList<>();
        EventUtil.inspectHierarchy(content.getElement(), descendants,
                                   e -> e.getComponent()
                                           .filter(c -> !(c instanceof Text))
                                           .map(Component::isVisible)
                                           .orElse(false));
        EventUtil.getImplementingComponents(descendants.stream(), AfterTabSelectedObserver.class)
                .forEach(o -> o.afterTabSelected(event));
    }

    protected void unselect() {
        getContent().setVisible(false);
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %s])", getClass().getSimpleName(), getId());
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

        private BooleanSupplier visibilitySupplier = () -> true;

        private final List<ComponentEventListener<TabSheetSelectionChangeEvent>> selectionChangeListeners =
                new ArrayList<>();

        private LinkkiTabSheetBuilder(String id) {
            this.id = requireNonNull(id, "id must not be null");
        }

        /**
         * Specifies the given {@link Component} as caption of the tab. Any existing caption that is
         * set by {@link #caption(Component) or #withCaption(String)} is overwritten. The caption of
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
         * Specifies the description of the tab sheet that is shown as the tooltip of the tab.
         * 
         * @param newDescription description of the tab sheet
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder description(String newDescription) {
            this.description = requireNonNull(newDescription, "newDescription must not be null");
            return this;
        }

        /**
         * Specifies the content of the tab sheet using a supplier.
         * <p>
         * For performance reasons the supplier should only create components when called: <br>
         * 
         * <pre>
         * {@code
         * .content(() -> new Component())
         * }
         * </pre>
         * 
         * Do <b>not</b> create the component outside of the supplier:
         * 
         * <pre>
         * {@code
         * Component c = new Component();
         * ...
         * .content(() -> c)
         * }
         * </pre>
         * 
         * The created component must support {@link Component#setVisible(boolean)}. This is not
         * supported by {@link Text}.
         * 
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder content(Supplier<Component> newContentSupplier) {
            this.contentSupplier = requireNonNull(newContentSupplier, "newContentSupplier must not be null");
            return this;
        }

        /**
         * Specifies the visibility of the tab sheet using a supplier. Visibility is updated when
         * {@link LinkkiTabLayout#afterNavigation(com.vaadin.flow.router.AfterNavigationEvent)
         * navigation is complete} or {@link LinkkiTabLayout#updateSheetVisibility()} is called.
         * 
         * @return {@code this} for method chaining
         */
        public LinkkiTabSheetBuilder visibleWhen(BooleanSupplier newVisibilitySupplier) {
            this.visibilitySupplier = requireNonNull(newVisibilitySupplier, "newVisibilitySupplier must not be null");
            return this;
        }

        /**
         * Builds a {@link LinkkiTabSheet} instance using the values in this builder.
         * <p>
         * Note that a content must be provided. The caption and description default to the ID if
         * none is provided.
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
                    nonNullContentSupplier, visibilitySupplier, selectionChangeListeners);
        }

    }

    public static class TabSheetSelectionChangeEvent extends SelectedChangeEvent {

        private static final long serialVersionUID = 1L;

        private final LinkkiTabSheet tabSheet;

        public TabSheetSelectionChangeEvent(SelectedChangeEvent wrappedEvent, LinkkiTabSheet tabSheet) {
            super(wrappedEvent.getSource(), wrappedEvent.getPreviousTab(), wrappedEvent.isFromClient());
            this.tabSheet = tabSheet;
        }

        /**
         * Returns the {@link LinkkiTabSheet} that is linked to the tab which is now selected. The
         * corresponding {@link Tab} is {@link #getSelectedTab()}.
         * 
         * @return The {@link LinkkiTabSheet} of {@link #getSelectedTab()}
         */
        public LinkkiTabSheet getTabSheet() {
            return tabSheet;
        }

    }

}
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
package org.linkki.framework.ui.component.sidebar;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.manager.UiUpdateObserver;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.util.LazyReference;
import org.linkki.util.handler.Handler;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Wrapper class for a component that is displayed in a {@link SidebarLayout}.
 * <p>
 * This consists of a {@link Component} that is shown in the content area and metadata including a
 * {@link Resource button} for the side bar and a tooltip. Additionally, a {@link Handler} must be given
 * that is called when the sheet is added.
 * <p>
 * It is possible to provide an {@link UiUpdateObserver} to the constructor to get an event when this
 * sheet is selected.
 */
public class SidebarSheet {

    private final Button button;

    private final String name;

    private final LazyReference<Component> contentSupplier;

    private final Optional<UiUpdateObserver> uiUpdateObserver;

    /**
     * @deprecated Since June 14th, 2018. Use {@link #SidebarSheet(Resource, String, Component)}
     *             instead. This constructor will be removed in the next release.
     */
    @Deprecated
    public SidebarSheet(Resource icon, Component content, String tooltip) {
        this(icon, tooltip, content);
    }

    /**
     * Simply creates a {@link SidebarSheet} with the given icon, name and content. The name will be
     * displayed as tooltip information.
     * 
     * @param icon The icon for the sidebar button
     * @param name The name of the sidebar sheet that is displayed as tooltip
     * @param content The content of the sidebar sheet that is displayed if this sheed is selected
     */
    public SidebarSheet(Resource icon, String name, Component content) {
        this(icon, name, () -> content);
    }

    /**
     * Simply creates a {@link SidebarSheet} with the given icon, name and content. The name will be
     * displayed as tooltip information.
     * 
     * @param icon The icon for the sidebar button
     * @param name The name of the sidebar sheet that is displayed as tooltip
     * @param content The content of the sidebar sheet that is displayed if this sheed is selected
     * @param uiUpdateObserver An {@link UiUpdateObserver} that is triggered when this sidebar sheet is
     *            selected
     */
    public SidebarSheet(Resource icon, String name, Component content, UiUpdateObserver uiUpdateObserver) {
        this(icon, name, () -> content, uiUpdateObserver);
    }

    /**
     * Simply creates a {@link SidebarSheet} with the given icon, name and content. The name will be
     * displayed as tooltip information.
     * 
     * @implNote The content is provided by a {@link Supplier} that is called when the sheet is selected
     *           for the first time.
     * 
     * @param icon The icon for the sidebar button
     * @param name The name of the sidebar sheet that is displayed as tooltip
     * @param contentSupplier The supplier for the content of the sidebar sheet that is displayed if
     *            this sheed is selected
     */
    public SidebarSheet(Resource icon, String name, Supplier<Component> contentSupplier) {
        this(icon, name, contentSupplier, null);
    }

    /**
     * Simply creates a {@link SidebarSheet} with the given icon, name and content. The name will be
     * displayed as tooltip information.
     * 
     * @implNote The content is provided by a {@link Supplier} that is called when the sheet is selected
     *           for the first time.
     * 
     * @param icon the icon for the sidebar button
     * @param name the name of the sidebar sheet that is displayed as tooltip
     * @param contentSupplier the supplier for the content of the sidebar sheet that is displayed if
     *            this sheet is selected
     * @param uiUpdateObserver A {@link UiUpdateObserver} that is triggered when this sidebar sheet is
     *            selected. May be {@code null}.
     */
    public SidebarSheet(Resource icon, String name, Supplier<Component> contentSupplier,
            @CheckForNull UiUpdateObserver uiUpdateObserver) {
        this.button = new Button("", requireNonNull(icon, "icon must not be null")); // $NON-NLS-1
        this.contentSupplier = new LazyReference<>(requireNonNull(contentSupplier, "content must not be null"));
        this.name = requireNonNull(name, "tooltip must not be null");
        if (StringUtils.isNotEmpty(name)) {
            this.button.setDescription(name);
        }
        this.uiUpdateObserver = Optional.ofNullable(uiUpdateObserver);
    }

    public Button getButton() {
        return button;
    }

    public Component getContent() {
        return contentSupplier.getReference();
    }

    public String getName() {
        return name;
    }

    /**
     * Should only be called by {@link SidebarLayout}.
     */
    protected void select() {
        getContent().setVisible(true);
        getButton().addStyleName(LinkkiApplicationTheme.SIDEBAR_SELECTED);
        uiUpdateObserver.ifPresent(UiUpdateObserver::uiUpdated);
    }

    /**
     * Should only be called by {@link SidebarLayout}.
     */
    protected void unselect() {
        getButton().removeStyleName(LinkkiApplicationTheme.SIDEBAR_SELECTED);
        getContent().setVisible(false);
    }

    @Override
    public String toString() {
        return name;
    }
}
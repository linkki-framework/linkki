/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.framework.ui.component.sidebar;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.util.LazyReference;
import org.linkki.util.handler.Handler;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * Wrapper class for a component that is displayed in a {@link SidebarLayout}.
 * <p>
 * This consists of a {@link Component} that is shown in the content area and metadata including an
 * {@link Resource button} for the side bar and a tooltip. Additionally, a {@link Handler} must be
 * given that is called when the sheet is added.
 */
public class SidebarSheet {

    private final Button button;
    private final String tooltip;
    private final LazyReference<Component> contentSupplier;

    public SidebarSheet(Resource icon, Component content, String tooltip) {
        this(icon, () -> content, tooltip);
    }

    public SidebarSheet(Resource icon, Supplier<Component> contentSupplier, String tooltip) {
        this.button = new Button("", requireNonNull(icon, "icon must not be null")); // $NON-NLS-1
        this.contentSupplier = new LazyReference<Component>(
                requireNonNull(contentSupplier, "content must not be null"));
        this.tooltip = requireNonNull(tooltip, "tooltip must not be null");
        if (StringUtils.isNotEmpty(tooltip)) {
            this.button.setDescription(tooltip);
        }
    }

    public Button getButton() {
        return button;
    }

    public Component getContent() {
        return contentSupplier.getReference();
    }

    public String getToolTip() {
        return tooltip;
    }

    /**
     * Should only be called by {@link SidebarLayout}
     */
    void select() {
        getContent().setVisible(true);
        getButton().addStyleName(LinkkiStyles.SIDEBAR_SELECTED);
    }

    /**
     * Should only be called by {@link SidebarLayout}
     */
    void unselect() {
        getButton().removeStyleName(LinkkiStyles.SIDEBAR_SELECTED);
        getContent().setVisible(false);
    }
}
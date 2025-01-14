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

package de.faktorzehn.commons.linkki.infotool;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.UiUpdateObserver;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;

/**
 * A tool that allows to view and/or edit an aspect of an object in {@link InfoToolsComponent}.
 */
public class InfoTool {

    private final String id;
    private final String caption;
    private final Component component;
    private final Handler updateUiHandler;

    /**
     * Creates a new InfoTool with the given id, caption and component.
     * <p>
     * If updates are necessary on every content change consider registering a
     * {@link UiUpdateObserver} at the {@link BindingManager}.
     *
     * @param id the to identify the component
     * @param caption the caption that is shown above the component
     * @param component The component that should be displayed as content of the tool
     */
    public InfoTool(String id, String caption, Component component) {
        this(id, caption, component, Handler.NOP_HANDLER);
    }

    public InfoTool(String id, String caption, Component component, Handler updateUiHandler) {
        this.id = requireNonNull(id, "id must not be null"); // $NON-NLS-1$
        this.caption = requireNonNull(caption, "caption must not be null"); // $NON-NLS-1$
        this.component = requireNonNull(component, "component must not be null"); // $NON-NLS-1$
        this.updateUiHandler = requireNonNull(updateUiHandler, "updateUiHandler must not be null"); // $NON-NLS-1$
    }

    /**
     * Returns the ID of the accordion panel.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the caption used in the tools area for this tool.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Returns the "main" component of the tool. This component is added into the tools area.
     */
    public Component getComponent() {
        return component;
    }

    public Handler getUpdateUiHandler() {
        return updateUiHandler;
    }

    /**
     * Returns a list of style names (CSS classes) that should be set to this tool's content.
     * <p>
     * The style names are updated on every content change! Make sure that there are no performance
     * expensive operations.
     * <p>
     * This method is called after the {@link #getUpdateUiHandler()}
     *
     * @return a list of CSS classes that are set to the accordion tab
     */
    public List<String> getStyleNames() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Tool[" + caption + "]"; // $NON-NLS-1$ // $NON-NLS-2$
    }

}

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

package org.linkki.framework.ui.component.infotool;

import java.io.Serial;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.linkki.core.binding.manager.UiUpdateObserver;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.util.Sequence;
import org.linkki.util.StreamUtil;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.Details.OpenedChangeEvent;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Location;

/**
 * A component that manages tools for viewing and editing information.
 *
 * @since 2.8.0
 */
public class InfoToolsComponent<T extends InfoTool> extends VerticalLayout
        implements UiUpdateObserver, AfterNavigationObserver {

    public static final String QUERY_PARAM = "tools";
    protected static final String NO_TOOL_OPEN = "0";

    @Serial
    private static final long serialVersionUID = 1L;

    private final Sequence<T> tools;
    private final Sequence<T> defaultTools;

    public InfoToolsComponent(Sequence<T> tools, Sequence<T> defaultTools) {
        this.tools = tools;
        this.defaultTools = defaultTools;

        setWidthFull();
        setPadding(true);

        StreamUtil.stream(tools)
                .distinct()
                .map(this::createTool)
                .forEach(this::add);
        uiUpdated();
    }

    protected Sequence<T> getTools() {
        return tools;
    }

    protected Sequence<T> getDefaultTools() {
        return defaultTools;
    }

    private Details createTool(InfoTool tool) {
        LinkkiText caption = new LinkkiText(tool.getCaption(), null);
        Details details = new Details(caption, tool.getComponent());
        details.setWidthFull();
        details.setId(tool.getId());
        details.addThemeVariants(DetailsVariant.LUMO_FILLED);
        details.addOpenedChangeListener(this::onOpenedChange);
        return details;
    }

    /**
     * Called whenever the opened state is changed.
     */
    protected void onOpenedChange(OpenedChangeEvent event) {
        if (event.isFromClient()) {
            updateUrlParameters();
            // need to update all tools because there might be URLs in other tools (for example in
            // the
            // history tool) which need to be updated to include the changed URL parameters.
            uiUpdated();
        }
    }

    private void updateUrlParameters() {
        List<String> openToolIds = getOpenedTools()
                .map(Details::getId)
                .flatMap(Optional::stream)
                .toList();

        if (openToolIds.isEmpty()) {
            openToolIds = List.of(NO_TOOL_OPEN);
        }
        updateUrlParameters(openToolIds);
    }

    /**
     * Updates the URL parameters with the list of opened tools.
     *
     * @param openedTools IDs of the tools that are open
     */
    protected void updateUrlParameters(List<String> openedTools) {
        // do nothing
    }

    @Override
    public void uiUpdated() {
        tools.forEach(this::updateToolUi);
    }

    private void updateToolUi(InfoTool tool) {
        tool.getUpdateUiHandler().apply();
        findToolDetailsById(tool.getId()).ifPresent(t -> {
            t.getClassNames().clear();
            tool.getStyleNames().forEach(t::addClassName);
        });
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        openToolsFromUrl(getOpenToolsIdsFromUrl(event.getLocation()));
    }

    /**
     * Returns a list tools that should be open based on the current URL.
     *
     * @param location location containing the current URL
     * @return list of IDs for the tools that should be open
     */
    protected List<String> getOpenToolsIdsFromUrl(Location location) {
        var parameters = location.getQueryParameters().getParameters();
        return parameters.getOrDefault(QUERY_PARAM, List.of());
    }

    /* private */ void openToolsFromUrl(List<String> openToolsIdsFromUrl) {
        var openToolsIds = openToolsIdsFromUrl.isEmpty()
                ? defaultTools.stream().map(InfoTool::getId).toList()
                : openToolsIdsFromUrl;
        openToolsIds.stream()
                .map(this::findToolDetailsById)
                .flatMap(Optional::stream)
                .forEach(d -> d.setOpened(true));
    }

    private Optional<Details> findToolDetailsById(String id) {
        return getAllToolDetails()
                .filter(c -> c.getId().orElse("noId").equals(id)).findFirst();
    }

    protected Stream<Details> getOpenedTools() {
        return getAllToolDetails()
                .filter(Details::isOpened);
    }

    protected Stream<Details> getAllToolDetails() {
        return this.getChildren()
                .filter(Details.class::isInstance)
                .map(Details.class::cast);
    }

}
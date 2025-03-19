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
package org.linkki.samples.f10.search.contextfree;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

import de.faktorzehn.commons.linkki.ui.menu.MenuItemDefinition;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.samples.f10.SampleApplicationLayout;
import org.linkki.samples.f10.search.SampleSearchParametersPmo;
import org.linkki.samples.f10.search.SampleSearchResultRowPmo;
import org.linkki.samples.f10.search.service.SampleModelObject;
import org.linkki.samples.f10.search.service.SampleSearchParameters;
import org.linkki.samples.f10.search.service.SampleSearchParametersMapper;
import org.linkki.samples.f10.search.service.SampleSearchResult;
import org.linkki.samples.f10.search.service.SampleSearchService;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

import de.faktorzehn.commons.linkki.search.SearchLayoutBuilder;
import de.faktorzehn.commons.linkki.search.model.RoutingSearchController;
import de.faktorzehn.commons.linkki.search.pmo.SearchLayoutPmo;

@SuppressWarnings("deprecation")
@Route(value = ContextFreeSearchView.NAME, layout = SampleApplicationLayout.class)
public class ContextFreeSearchView extends VerticalLayout implements AfterNavigationObserver {

    public static final String NAME = "context-free";

    @Serial
    private static final long serialVersionUID = 1L;
    private final RoutingSearchController<SampleSearchParameters, SampleSearchResult> searchController = createSearchController();

    private final BindingContext bindingContext = new BindingContext();

    public ContextFreeSearchView() {
        add(VaadinUiCreator.createComponent(createSearchLayoutPmo(), bindingContext));
        setSizeFull();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        searchController.initialize(event.getLocation());
        bindingContext.modelChanged();
    }

    private RoutingSearchController<SampleSearchParameters, SampleSearchResult> createSearchController() {
        var searchService = new SampleSearchService();
        return new RoutingSearchController<>(ContextFreeSearchView.NAME,
                searchService::search,
                new SampleSearchParametersMapper(),
                SampleSearchResult::getMessages);
    }

    private SearchLayoutPmo<SampleSearchResultRowPmo> createSearchLayoutPmo() {
        return SearchLayoutBuilder
                .<SampleSearchParameters, SampleSearchResult, SampleModelObject, SampleSearchResultRowPmo> with()
                .searchParametersPmo(SampleSearchParametersPmo::new)
                .searchResultRowPmo(m -> new SampleSearchResultRowPmo(m, getAdditionalActions(m)),
                                    SampleSearchResultRowPmo::getModelObject,
                                    SampleSearchResultRowPmo.class)
                .searchController(searchController, SampleSearchResult::getResult)
                .primaryAction(ContextFreeSearchView::showObject)
                .maxResult(SampleSearchResult.DEFAULT_MAX_RESULT_SIZE)
                .caption("Partnersuche")
                .build();
    }

    private static List<MenuItemDefinition> getAdditionalActions(SampleModelObject modelObject) {
        return Arrays.asList(
                             new MenuItemDefinition("Partner anzeigen", null,
                                     () -> Notification
                                             .show("Navigiere zum Partner " + modelObject.getPartnerNummber()),
                                     "appmenu-search-view-partner"),
                             new MenuItemDefinition("Policen anzeigen", null,
                                     () -> Notification.show("Navigiere zur Bestandsystem"),
                                     "appmenu-search-view-policy"),
                             new MenuItemDefinition("Schadensfälle anzeigen", null,
                                     () -> Notification.show("Navigiere zu Schadensystem"),
                                     "appmenu-search-view-claim"));
    }

    private static void showObject(SampleModelObject m) {
        Notification.show("Navigiere zum Partner " + m.getPartnerNummber());
    }

}
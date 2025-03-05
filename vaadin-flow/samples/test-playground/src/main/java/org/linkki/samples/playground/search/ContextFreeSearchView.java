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
package org.linkki.samples.playground.search;

import static org.linkki.samples.playground.search.ResultActions.showClaims;
import static org.linkki.samples.playground.search.ResultActions.showPartner;
import static org.linkki.samples.playground.search.ResultActions.showPolicies;

import java.io.Serial;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;
import org.linkki.samples.playground.search.service.SampleModelObject;
import org.linkki.samples.playground.search.service.SampleSearchParameters;
import org.linkki.samples.playground.search.service.SampleSearchParametersMapper;
import org.linkki.samples.playground.search.service.SampleSearchResult;
import org.linkki.samples.playground.search.service.SampleSearchService;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;
import org.linkki.search.SearchLayoutBuilder;
import org.linkki.search.model.RoutingSearchController;
import org.linkki.search.pmo.SearchLayoutPmo;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

// tag::ContextFreeSearchView[]
@Route(value = ContextFreeSearchView.NAME, layout = PlaygroundAppLayout.class)
public class ContextFreeSearchView extends VerticalLayout implements AfterNavigationObserver {
    // end::ContextFreeSearchView[]

    public static final String NAME = "context-free";

    @Serial
    private static final long serialVersionUID = 1L;

    private final RoutingSearchController<SampleSearchParameters, SampleSearchResult> searchController;
    // tag::content[]
    private final BindingContext bindingContext = new BindingContext();

    public ContextFreeSearchView() {
        searchController = createSearchController();
        add(VaadinUiCreator.createComponent(createSearchLayoutPmo(), bindingContext));
        setSizeFull();
    }
    // end::content[]

    // tag::initialize[]
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        searchController.initialize(event.getLocation());
        bindingContext.modelChanged();
    }
    // end::initialize[]

    private static RoutingSearchController<SampleSearchParameters, SampleSearchResult> createSearchController() {
        var searchService = new SampleSearchService();
        // tag::searchController[]
        return new RoutingSearchController<>(ContextFreeSearchView.NAME,
                searchService::search,
                new SampleSearchParametersMapper(),
                SampleSearchResult::getMessages);
        // end::searchController[]
    }

    private SearchLayoutPmo<SampleSearchResultRowPmo> createSearchLayoutPmo() {
        // tag::searchLayoutBuilder[]
        return SearchLayoutBuilder
                .<SampleSearchParameters, SampleSearchResult, SampleModelObject, SampleSearchResultRowPmo> with()
                .searchParametersPmo(SampleSearchParametersPmo::new)
                .searchResultRowPmo(m -> new SampleSearchResultRowPmo(m, getAdditionalActions(m)),
                                    SampleSearchResultRowPmo::getModelObject,
                                    SampleSearchResultRowPmo.class)
                .searchController(searchController, SampleSearchResult::getResult)
                .primaryAction(ResultActions::navigateToPartner)
                .maxResult(SampleSearchResult.DEFAULT_MAX_RESULT_SIZE)
                .caption("Search for Partners")
                .build();
        // end::searchLayoutBuilder[]
    }

    private static List<MenuItemDefinition> getAdditionalActions(SampleModelObject modelObject) {
        return List.of(showPartner(modelObject), showPolicies(modelObject), showClaims(modelObject));
    }
}
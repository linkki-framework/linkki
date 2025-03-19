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
import java.util.function.Consumer;

import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.dialogs.DialogBindingManager;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.samples.playground.search.service.SampleModelObject;
import org.linkki.samples.playground.search.service.SampleSearchParameters;
import org.linkki.samples.playground.search.service.SampleSearchResult;
import org.linkki.samples.playground.search.service.SampleSearchService;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;
import org.linkki.search.SearchLayoutBuilder;
import org.linkki.search.model.SearchController;
import org.linkki.search.model.SimpleSearchController;
import org.linkki.search.pmo.SearchLayoutPmo;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = ContextDependentSearchView.NAME, layout = PlaygroundAppLayout.class)
public class ContextDependentSearchView extends VerticalLayout {

    public static final String NAME = "context-dependent";

    @Serial
    private static final long serialVersionUID = 1L;

    public ContextDependentSearchView() {
        var openDialogWithoutTabs = new Button("Open Dialog Without Tabs",
                e -> createSearchDialog(createSearchController(), this::selectObject).open());
        openDialogWithoutTabs.setId("openDialogWithoutTabs");
        var openDialogWithTabs = new Button("Open Dialog With Tabs",
                e -> createSearchDialogWithTabs(createSearchController(), this::selectObject).open());
        openDialogWithTabs.setId("openDialogWithTabs");

        add(openDialogWithoutTabs, openDialogWithTabs);
        setSizeFull();
    }

    private SearchController<SampleSearchParameters, SampleSearchResult> createSearchController() {
        var searchService = new SampleSearchService();
        // tag::SimpleSearchController[]
        return new SimpleSearchController<>(SampleSearchParameters::new,
                searchService::search,
                SampleSearchResult::getMessages);
        // end::SimpleSearchController[]
    }

    private OkCancelDialog createSearchDialog(
            SearchController<SampleSearchParameters, SampleSearchResult> searchController,
            Consumer<SampleModelObject> primaryAction) {
        // tag::showSearchDialog[]
        var searchLayoutPmo = createSearchLayoutPmo(searchController, selection -> {
            if (!validateSelection(selection).containsErrorMsg()) {
                primaryAction.accept(selection);
            }
        });
        ValidationService validationService = () -> searchLayoutPmo.getSelectedResult()
                .map(SampleSearchResultRowPmo::getModelObject)
                .map(this::validateSelection)
                .orElse(createEmptySelectionMessage());
        var searchDialog = new PmoBasedDialogFactory(validationService)
                .newOkCancelDialog("Search for a business partner",
                                   () -> searchLayoutPmo.getSelectedResult()
                                           .map(SampleSearchResultRowPmo::getModelObject)
                                           .ifPresent(primaryAction),
                                   searchLayoutPmo);
        // end::showSearchDialog[]

        searchDialog.setSize("85%", "85%");
        return searchDialog;
    }

    private OkCancelDialog createSearchDialogWithTabs(
            SearchController<SampleSearchParameters, SampleSearchResult> searchController,
            Consumer<SampleModelObject> primaryAction) {
        var searchLayoutPmo = createSearchLayoutPmo(searchController, selection -> {
            if (!validateSelection(selection).containsErrorMsg()) {
                primaryAction.accept(selection);
            }
        });
        ValidationService validationService = () -> searchLayoutPmo.getSelectedResult()
                .map(SampleSearchResultRowPmo::getModelObject)
                .map(this::validateSelection)
                .orElse(createEmptySelectionMessage());

        var dialog = OkCancelDialog.builder("Search for a business partner")
                .okHandler(() -> searchLayoutPmo.getSelectedResult()
                        .map(SampleSearchResultRowPmo::getModelObject)
                        .ifPresent(primaryAction))
                .size("85%", "85%")
                .build();

        var bindingManager = new DialogBindingManager(dialog, validationService);

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("search")
                                       .caption("Search")
                                       .content(() -> VaadinUiCreator
                                               .createComponent(searchLayoutPmo, bindingManager.getContext("search")))
                                       .build(),
                               LinkkiTabSheet.builder("dummy")
                                       .caption("Dummy")
                                       .content(() -> new Span("Dummy sheet to test tab layouts in dialog"))
                                       .build());
        dialog.addContent(tabLayout);

        return dialog;
    }

    private SearchLayoutPmo<SampleSearchResultRowPmo> createSearchLayoutPmo(
            SearchController<SampleSearchParameters, SampleSearchResult> searchController,
            Consumer<SampleModelObject> selectResult) {
        return SearchLayoutBuilder
                .<SampleSearchParameters, SampleSearchResult, SampleModelObject, SampleSearchResultRowPmo> with()
                .searchParametersPmo(SampleSearchParametersPmo::new)
                .searchResultRowPmo(m -> new SampleSearchResultRowPmo(m, getAdditionalActions(m)),
                                    SampleSearchResultRowPmo::getModelObject,
                                    SampleSearchResultRowPmo.class)
                .searchController(searchController, SampleSearchResult::getResult)
                .primaryAction(selectResult)
                .maxResult(SampleSearchResult.DEFAULT_MAX_RESULT_SIZE)
                .build();
    }

    private List<MenuItemDefinition> getAdditionalActions(SampleModelObject modelObject) {
        return List.of(showPartner(modelObject), showPolicies(modelObject), showClaims(modelObject));
    }

    private void selectObject(SampleModelObject m) {
        ResultActions.navigateToPartner(m);
        UI.getCurrent().navigate(ContextDependentSearchView.class);
    }

    private MessageList validateSelection(SampleModelObject selection) {
        if (!selection.getName().contains("2")) {
            return new MessageList(
                    Message.builder("Only partner with a 2 in name can be selected.", Severity.ERROR)
                            .code("error.no2")
                            .create());
        } else {
            return new MessageList();
        }
    }

    private MessageList createEmptySelectionMessage() {
        return new MessageList(Message.builder("A partner must be selected.", Severity.ERROR)
                .code("error.emptySelection")
                .markers(ValidationMarker.REQUIRED)
                .create());
    }
}

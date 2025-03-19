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
package org.linkki.samples.f10.search.contextdependent;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import de.faktorzehn.commons.linkki.ui.menu.MenuItemDefinition;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.framework.ui.dialogs.DialogBindingManager;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.samples.f10.SampleApplicationLayout;
import org.linkki.samples.f10.search.SampleSearchParametersPmo;
import org.linkki.samples.f10.search.SampleSearchResultRowPmo;
import org.linkki.samples.f10.search.service.SampleModelObject;
import org.linkki.samples.f10.search.service.SampleSearchParameters;
import org.linkki.samples.f10.search.service.SampleSearchResult;
import org.linkki.samples.f10.search.service.SampleSearchService;
import org.linkki.util.validation.ValidationMarker;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import de.faktorzehn.commons.linkki.search.SearchLayoutBuilder;
import de.faktorzehn.commons.linkki.search.model.SearchController;
import de.faktorzehn.commons.linkki.search.model.SimpleSearchController;
import de.faktorzehn.commons.linkki.search.pmo.SearchLayoutPmo;

@SuppressWarnings("deprecation")
@Route(value = ContextDependentSearchView.NAME, layout = SampleApplicationLayout.class)
public class ContextDependentSearchView extends VerticalLayout {

    public static final String NAME = "context-dependent";

    @Serial
    private static final long serialVersionUID = 1L;

    public ContextDependentSearchView() {
        var openDialogWithoutTabs = new Button("Open Dialog without Tabs",
                e -> createSearchDialog(createSearchController(), this::showObject).open());
        openDialogWithoutTabs.setId("openDialogWithoutTabs");
        var openDialogWithTabs = new Button("Open Dialog with Tabs",
                e -> createSearchDialogWithTabs(createSearchController(), this::showObject).open());
        openDialogWithTabs.setId("openDialogWithTabs");

        add(openDialogWithoutTabs, openDialogWithTabs);
        setSizeFull();
    }

    private SearchController<SampleSearchParameters, SampleSearchResult> createSearchController() {
        var searchService = new SampleSearchService();
        return new SimpleSearchController<>(SampleSearchParameters::new,
                searchService::search,
                SampleSearchResult::getMessages);
    }

    private static OkCancelDialog createSearchDialog(
            SearchController<SampleSearchParameters, SampleSearchResult> searchController,
            Consumer<SampleModelObject> showObject) {
        var searchLayoutPmo = createSearchLayoutPmo(searchController, showObject);
        var validationService = new SearchValidationService(searchLayoutPmo);

        var searchDialog = new PmoBasedDialogFactory(validationService)
                .newOkCancelDialog("Nach Partner suchen",
                                   () -> searchLayoutPmo.getSelectedResult()
                                           .map(SampleSearchResultRowPmo::getModelObject)
                                           .ifPresent(showObject),
                                   searchLayoutPmo);

        searchDialog.setSize("85%", "85%");

        return searchDialog;
    }

    private static OkCancelDialog createSearchDialogWithTabs(
            SearchController<SampleSearchParameters, SampleSearchResult> searchController,
            Consumer<SampleModelObject> showObject) {
        var searchLayoutPmo = createSearchLayoutPmo(searchController, showObject);
        var validationService = new SearchValidationService(searchLayoutPmo);

        var dialog = OkCancelDialog.builder("Nach Partner suchen")
                .okHandler(() -> searchLayoutPmo.getSelectedResult()
                        .map(SampleSearchResultRowPmo::getModelObject)
                        .ifPresent(showObject))
                .size("85%", "85%")
                .build();

        var bindingManager = new DialogBindingManager(dialog, validationService);

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(
                               LinkkiTabSheet.builder("search")
                                       .caption("Suche")
                                       .content(() -> VaadinUiCreator
                                               .createComponent(searchLayoutPmo, bindingManager.getContext("search")))
                                       .build(),
                               LinkkiTabSheet.builder("dummy")
                                       .caption("Dummy")
                                       .content(() -> new Span("Dummy Sheet um Layout zu testen"))
                                       .build());
        dialog.addContent(tabLayout);

        return dialog;
    }

    private static SearchLayoutPmo<SampleSearchResultRowPmo> createSearchLayoutPmo(
            SearchController<SampleSearchParameters, SampleSearchResult> searchController,
            Consumer<SampleModelObject> showObject) {
        return SearchLayoutBuilder
                .<SampleSearchParameters, SampleSearchResult, SampleModelObject, SampleSearchResultRowPmo> with()
                .searchParametersPmo(SampleSearchParametersPmo::new)
                .searchResultRowPmo(m -> new SampleSearchResultRowPmo(m, getAdditionalActions(m)),
                                    SampleSearchResultRowPmo::getModelObject,
                                    SampleSearchResultRowPmo.class)
                .searchController(searchController, SampleSearchResult::getResult)
                .primaryAction(showObject)
                .maxResult(SampleSearchResult.DEFAULT_MAX_RESULT_SIZE)
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

    private void showObject(SampleModelObject m) {
        Notification.show("Navigiere zum Partner " + m.getPartnerNummber());

        UI.getCurrent().navigate(ContextDependentSearchView.class);
    }

    private static class SearchValidationService implements ValidationService {

        private final SearchLayoutPmo<?> searchLayout;

        private SearchValidationService(SearchLayoutPmo<?> searchLayout) {
            this.searchLayout = searchLayout;
        }

        @Override
        public MessageList getValidationMessages() {
            if (searchLayout.getSelectedResult().isEmpty()) {
                return new MessageList(Message.builder("Ein Partner muss ausgewählt werden", Severity.ERROR)
                        .code("error.emptySelection")
                        .markers(ValidationMarker.REQUIRED)
                        .create());
            } else {
                return new MessageList();
            }

        }
    }
}

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

package de.faktorzehn.commons.linkki.search;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;

import de.faktorzehn.commons.linkki.search.model.SearchController;
import de.faktorzehn.commons.linkki.search.model.SimpleSearchController;
import de.faktorzehn.commons.linkki.search.pmo.SearchInputPmo;
import de.faktorzehn.commons.linkki.search.pmo.SearchLayoutPmo;
import de.faktorzehn.commons.linkki.search.pmo.SearchResultPmo;
import de.faktorzehn.commons.linkki.search.pmo.SearchResultTablePmo;

/**
 * This builder is used to create a {@link SearchLayoutPmo} by providing all necessary information
 * to create the customizable parts of the search. Use
 * {@link VaadinUiCreator#createComponent(Object, BindingContext)} to create the search layout.
 * <p>
 * The most customizable parts of the search layout are:
 * <ul>
 * <li>The PMO to enter the search parameters, see {@link #searchParametersPmo(Function)}</li>
 * <li>The columns of the result table, see
 * {@link #searchResultRowPmo(Function, Function, Class)}</li>
 * <li>The search logic that is called to get the result, see
 * {@link #searchController(SearchController, Function)}</li>
 * <li>The primary action that is triggered by double click, see
 * {@link #primaryAction(Consumer)}</li>
 * </ul>
 * 
 * @param <PARAM> Type of search parameters
 * @param <RESULT> Type of search result model object, typically a list result object that wraps the
 *            result list and some optional messages
 * @param <MODELOBJECT> Type of the model object of a single result entity
 * @param <ROW> Type of the row PMO that displays a single model object of type M
 *
 * @deprecated moved to linkki-search-vaadin-flow. Use org.linkki.search.SearchLayoutBuilder instead
 */
@SuppressWarnings("hiding")
@Deprecated(since = "2.8.0")
public class SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> {

    private String caption;
    private Function<Supplier<PARAM>, Object> parametersPmo;
    private Function<MODELOBJECT, ROW> resultRowPmo;
    private Function<ROW, MODELOBJECT> rowToResult;
    private Class<? extends ROW> rowType;
    private SearchController<PARAM, RESULT> searchController;
    private Consumer<MODELOBJECT> primaryAction;
    private Function<RESULT, List<? extends MODELOBJECT>> toResultList;
    private Optional<Integer> maxResult = Optional.empty();

    public static <PARAM, RESULT, MODELOBJECT, ROW> SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> with() {
        return new SearchLayoutBuilder<>();
    }

    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> caption(String caption) {
        this.caption = caption;
        return this;
    }

    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> searchParametersPmo(
            Function<Supplier<PARAM>, Object> criteriaPmo) {
        this.parametersPmo = criteriaPmo;
        return this;
    }

    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> searchResultRowPmo(
            Function<MODELOBJECT, ROW> resultRowPmo,
            Function<ROW, MODELOBJECT> rowToResult,
            Class<? extends ROW> rowType) {
        this.resultRowPmo = resultRowPmo;
        this.rowToResult = rowToResult;
        this.rowType = rowType;
        return this;
    }

    /**
     * @deprecated Use {@link #searchController(SearchController, Function)} with a
     *             {@link SimpleSearchController} instead.
     */
    @Deprecated(since = "22.3.0")
    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> searchProxy(
            de.faktorzehn.commons.linkki.search.model.SearchProxy<PARAM, RESULT> searchProxy,
            Function<RESULT, List<? extends MODELOBJECT>> toResultList) {
        return searchController(searchProxy, toResultList);
    }

    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> searchController(
            SearchController<PARAM, RESULT> searchController,
            Function<RESULT, List<? extends MODELOBJECT>> toResultList) {
        this.searchController = searchController;
        this.toResultList = toResultList;
        return this;
    }

    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> primaryAction(Consumer<MODELOBJECT> primaryAction) {
        this.primaryAction = primaryAction;
        return this;
    }

    /**
     * Sets the maximum number of results returned by the
     * {@link #searchController(SearchController, Function) search controller}. If the number of
     * results is equal to or exceeds {@code maxResult}, a text indicates that there may be more
     * matches that are not displayed.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> maxResult(int maxResult) {
        this.maxResult = Optional.of(maxResult);
        return this;
    }

    public SearchLayoutPmo<ROW> build() {
        requireNonNull(parametersPmo, "parametersPmo must not be null");
        requireNonNull(resultRowPmo, "resultRowPmo must not be null");
        requireNonNull(rowToResult, "rowToResult must not be null");
        requireNonNull(rowType, "rowType must not be null");
        requireNonNull(searchController, "searchController must not be null");
        requireNonNull(primaryAction, "primaryAction must not be null");
        requireNonNull(toResultList, "toResultList must not be null");
        return new SearchLayoutPmo<>(Optional.ofNullable(caption), createSearchInputPmo(),
                createSearchResultPmo());
    }

    private SearchInputPmo createSearchInputPmo() {
        return new SearchInputPmo(parametersPmo.apply(searchController::getParameters),
                searchController::search,
                searchController::reset);
    }

    private SearchResultPmo<MODELOBJECT, ROW> createSearchResultPmo() {
        SearchResultTablePmo<MODELOBJECT, ROW> searchResultTablePmo = new SearchResultTablePmo<>(
                this::getResult,
                resultRowPmo,
                rowType,
                r -> primaryAction.accept(rowToResult.apply(r)));
        return new SearchResultPmo<>(
                searchResultTablePmo,
                searchController::getMessages,
                maxResult);
    }

    private List<? extends MODELOBJECT> getResult() {
        return searchController.get().map(toResultList).orElse(Collections.emptyList());
    }

}
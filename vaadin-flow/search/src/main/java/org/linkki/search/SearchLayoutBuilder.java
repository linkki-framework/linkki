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

package org.linkki.search;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.search.model.SearchController;
import org.linkki.search.pmo.SearchInputPmo;
import org.linkki.search.pmo.SearchLayoutPmo;
import org.linkki.search.pmo.SearchResultPmo;
import org.linkki.search.pmo.SearchResultTablePmo;

import edu.umd.cs.findbugs.annotations.CheckForNull;

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
 * <li>The primary action that is triggered by double-clicking on a row, see
 * {@link #primaryAction(Consumer)}</li>
 * </ul>
 *
 * @param <PARAM> Type of search parameters
 * @param <RESULT> Type of search result model object, typically a list result object that wraps the
 *            result list and some optional messages
 * @param <MODEL_OBJECT> Type of the model object of a single result entity
 * @param <ROW> Type of the row PMO that displays a single model object of type M
 * @since 2.8.0
 */
public class SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> {

    @CheckForNull
    private String caption;
    @CheckForNull
    private Function<Supplier<PARAM>, Object> parametersPmo;
    @CheckForNull
    private Function<MODEL_OBJECT, ROW> resultRowPmo;
    @CheckForNull
    private Function<ROW, MODEL_OBJECT> rowToResult;
    @CheckForNull
    private Class<? extends ROW> rowType;
    @CheckForNull
    private SearchController<PARAM, RESULT> searchController;
    @CheckForNull
    private Consumer<MODEL_OBJECT> primaryAction;
    @CheckForNull
    private Function<RESULT, List<? extends MODEL_OBJECT>> toResultList;
    private Optional<Integer> maxResult = Optional.empty();
    private Optional<Integer> pageSize = Optional.empty();

    public static <PARAM, RESULT, MODELOBJECT, ROW> SearchLayoutBuilder<PARAM, RESULT, MODELOBJECT, ROW> with() {
        return new SearchLayoutBuilder<>();
    }

    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> caption(String caption) {
        this.caption = caption;
        return this;
    }

    /**
     * Defines the PMO that should be used to display the search input area.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> searchParametersPmo(
            Function<Supplier<PARAM>, Object> criteriaPmo) {
        this.parametersPmo = criteriaPmo;
        return this;
    }

    /**
     * Defines the PMO that should be used to display a result as table row.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> searchResultRowPmo(
            Function<MODEL_OBJECT, ROW> resultRowPmo,
            Function<ROW, MODEL_OBJECT> rowToResult,
            Class<? extends ROW> rowType) {
        this.resultRowPmo = resultRowPmo;
        this.rowToResult = rowToResult;
        this.rowType = rowType;
        return this;
    }

    /**
     * Defines the {@link SearchController} that should be used.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> searchController(
            SearchController<PARAM, RESULT> searchController,
            Function<RESULT, List<? extends MODEL_OBJECT>> toResultList) {
        this.searchController = searchController;
        this.toResultList = toResultList;
        return this;
    }

    /**
     * Defines the action that should be executed when double-clicking on a row.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> primaryAction(Consumer<MODEL_OBJECT> primaryAction) {
        this.primaryAction = primaryAction;
        return this;
    }

    /**
     * Sets the maximum number of results returned by the
     * {@link #searchController(SearchController, Function) search controller}. If the number of
     * results is equal to or exceeds {@code maxResult}, a text indicates that there may be more
     * matches that are not displayed.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> maxResult(int maxResult) {
        this.maxResult = Optional.of(maxResult);
        return this;
    }

    /**
     * Sets the maximum number of results shown on one page. If the number of results exceeds the
     * page size, the results are shown on multiple page.
     */
    public SearchLayoutBuilder<PARAM, RESULT, MODEL_OBJECT, ROW> pageSize(int pageSize) {
        this.pageSize = Optional.of(pageSize);
        return this;
    }

    public SearchLayoutPmo<ROW> build() {
        var parametersPmoChecked = requireNonNull(parametersPmo, "parametersPmo must not be null");
        var resultRowPmoChecked = requireNonNull(resultRowPmo, "resultRowPmo must not be null");
        var rowToResultChecked = requireNonNull(rowToResult, "rowToResult must not be null");
        var rowTypeChecked = requireNonNull(rowType, "rowType must not be null");
        var searchControllerChecked = requireNonNull(searchController, "searchController must not be null");
        var primaryActionChecked = requireNonNull(primaryAction, "primaryAction must not be null");
        var toResultListChecked = requireNonNull(toResultList, "toResultList must not be null");
        return new SearchLayoutPmo<>(Optional.ofNullable(caption),
                createSearchInputPmo(parametersPmoChecked, searchControllerChecked),
                createSearchResultPmo(resultRowPmoChecked, rowTypeChecked, rowToResultChecked, primaryActionChecked,
                                      searchControllerChecked, toResultListChecked));
    }

    private SearchInputPmo createSearchInputPmo(Function<Supplier<PARAM>, Object> parametersPmo,
            SearchController<PARAM, RESULT> searchController) {
        return new SearchInputPmo(parametersPmo.apply(searchController::getParameters),
                searchController::search,
                searchController::reset);
    }

    private SearchResultPmo<MODEL_OBJECT, ROW> createSearchResultPmo(Function<MODEL_OBJECT, ROW> resultRowPmo,
            Class<? extends ROW> rowType,
            Function<ROW, MODEL_OBJECT> rowToResult,
            Consumer<MODEL_OBJECT> primaryAction,
            SearchController<PARAM, RESULT> searchController,
            Function<RESULT, List<? extends MODEL_OBJECT>> toResultList) {
        var searchResultTablePmo = new SearchResultTablePmo<>(
                () -> getLimitedResult(searchController, toResultList),
                resultRowPmo,
                rowType,
                r -> primaryAction.accept(rowToResult.apply(r)),
                pageSize);
        return new SearchResultPmo<>(
                searchResultTablePmo,
                () -> getResult(searchController, toResultList),
                searchController::getMessages,
                maxResult);
    }

    /**
     * Retrieves the search results from the search controller
     */
    private List<? extends MODEL_OBJECT> getResult(SearchController<PARAM, RESULT> searchController,
            Function<RESULT, List<? extends MODEL_OBJECT>> toResultList) {
        return searchController.get().map(toResultList).orElse(Collections.emptyList());
    }

    /**
     * Returns a limited number of search results retrieved from the search controller
     */
    private List<? extends MODEL_OBJECT> getLimitedResult(SearchController<PARAM, RESULT> searchController,
            Function<RESULT, List<? extends MODEL_OBJECT>> toResultList) {
        var results = getResult(searchController, toResultList);
        if (maxResult.isPresent() && results.size() > maxResult.get()) {
            return results.subList(0, maxResult.get());
        }
        return results;
    }
}
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
package de.faktorzehn.commons.linkki.search.pmo;

import java.util.Optional;
import java.util.function.Supplier;

import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

import de.faktorzehn.commons.linkki.search.util.NlsSearch;

/**
 * PMO to display search result.
 *
 * @see SearchResultTablePmo
 */
public class SearchResultPmo<MODELOBJECT, ROW> {

    private static final String RESULT_COUNT_TEMPLATE_PLURAL = "SearchResultPmo.resultCount";
    private static final String RESULT_COUNT_LIMITED_TEMPLATE_PLURAL = "SearchResultPmo.resultCountLimited";
    private static final String RESULT_COUNT_TEMPLATE_SINGULAR = "SearchResultPmo.oneResult";
    private static final String INITIAL_SEARCH_HINT_TEMPLATE = "SearchResultPmo.initialSearchHint";
    private static final String EMPTY_SEARCH_RESULT_HINT_TEMPLATE = "SearchResultPmo.emptySearchResultHint";

    private final SearchResultTablePmo<MODELOBJECT, ROW> searchResultTablePmo;
    private final Optional<Integer> maxResult;

    private final Supplier<MessageList> messages;

    /**
     * Creates a result pmo containing a {@link SearchResultTablePmo result table} and optional some
     * messages.
     * 
     * @param searchResultTablePmo The result table that should be visible if there is any result to
     *            be selected.
     * @param messages The optional messages to give some information to the user. If the search was
     *            triggered but there is no result for the search criteria, an appropriate message
     *            should exists.
     * @param maxResult The maximum count of possibly returned results, if exists it is given as an
     *            extra hint to the user.
     */
    public SearchResultPmo(SearchResultTablePmo<MODELOBJECT, ROW> searchResultTablePmo,
            Supplier<MessageList> messages,
            Optional<Integer> maxResult) {
        this.searchResultTablePmo = searchResultTablePmo;
        this.messages = messages;
        this.maxResult = maxResult;
    }

    @BindVisible
    @UILabel(position = 10)
    public String getInitialSearchHint() {
        return NlsSearch.getString(INITIAL_SEARCH_HINT_TEMPLATE);
    }

    public boolean isInitialSearchHintVisible() {
        return messages.get().isEmpty() && getResultCount() <= 0;
    }

    @UILabel(position = 20, visible = VisibleType.DYNAMIC)
    public String getEmptySearchResultHint() {
        return NlsSearch.getString(EMPTY_SEARCH_RESULT_HINT_TEMPLATE);
    }

    public boolean isEmptySearchResultHintVisible() {
        return getResultCount() <= 0 && isMostSevereMessageVisible();
    }

    @BindVisible
    @UILabel(position = 40)
    public String getMostSevereMessage() {
        return messages.get().getMessageWithHighestSeverity().map(Message::getText).orElse("");
    }

    public boolean isMostSevereMessageVisible() {
        return messages.get().getMessageWithHighestSeverity().isPresent();
    }

    @UILabel(position = 60, visible = VisibleType.DYNAMIC)
    public String getSearchResultCount() {
        int count = getResultCount();
        if (count <= 0) {
            return "";
        } else if (count == 1) {
            return NlsSearch.getString(RESULT_COUNT_TEMPLATE_SINGULAR);
        } else if (maxResult.isPresent() && count >= maxResult.get()) {
            return NlsSearch.format(RESULT_COUNT_LIMITED_TEMPLATE_PLURAL,
                                    Integer.toString(count));
        } else {
            return NlsSearch.format(RESULT_COUNT_TEMPLATE_PLURAL,
                                    Integer.toString(count));
        }
    }

    public boolean isSearchResultCountVisible() {
        return isResultPresent();
    }

    @BindVisible
    @UINestedComponent(position = 50)
    public SearchResultTablePmo<MODELOBJECT, ROW> getSearchResultTable() {
        return searchResultTablePmo;
    }

    public boolean isSearchResultTableVisible() {
        return isResultPresent();
    }

    private int getResultCount() {
        return searchResultTablePmo.getItems().size();
    }

    private boolean isResultPresent() {
        return getResultCount() > 0;
    }

    public Optional<ROW> getSelectedRow() {
        if (isResultPresent()) {
            return Optional.ofNullable(getSearchResultTable().getSelection());
        } else {
            return Optional.empty();
        }

    }

}
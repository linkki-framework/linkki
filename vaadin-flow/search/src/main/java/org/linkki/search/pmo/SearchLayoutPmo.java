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
package org.linkki.search.pmo;

import java.util.Optional;

import org.linkki.core.ui.aspects.annotation.BindSlot;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.framework.ui.component.UIHeadline;
import org.linkki.search.SearchLayoutBuilder;
import org.linkki.search.annotation.UISearchInputLayout;
import org.linkki.search.annotation.UISearchLayout;
import org.linkki.search.annotation.UISearchResultLayout;
import org.linkki.search.component.SearchLayout;

/**
 * Search layout that displays search parameter inputs on the left and search results on the right.
 * A caption can be optionally shown.
 * 
 * @see SearchLayoutBuilder
 *
 * @since 2.8.0
 */
@UISearchLayout
public class SearchLayoutPmo<ROW> {

    private final Optional<String> caption;
    private final SearchInputPmo searchInputPmo;
    private final SearchResultPmo<?, ROW> searchResultPmo;

    public SearchLayoutPmo(Optional<String> caption, SearchInputPmo searchInputPmo,
            SearchResultPmo<?, ROW> searchResultPmo) {
        this.caption = caption;
        this.searchInputPmo = searchInputPmo;
        this.searchResultPmo = searchResultPmo;
    }

    @BindSlot(SearchLayout.SLOT_HEADLINE)
    @BindVisible
    @UIHeadline
    public String getCaption() {
        return caption.orElse("");
    }

    public boolean isCaptionVisible() {
        return caption.isPresent();
    }

    @BindSlot(SearchLayout.SLOT_INPUT)
    @UISearchInputLayout(position = 10)
    public SearchInputPmo getSearchInputPmo() {
        return searchInputPmo;
    }

    @BindSlot(SearchLayout.SLOT_RESULT)
    @UISearchResultLayout(position = 20)
    public SearchResultPmo<?, ?> getSearchResultPmo() {
        return searchResultPmo;
    }

    public Optional<ROW> getSelectedResult() {
        return searchResultPmo.getSelectedRow();
    }
}

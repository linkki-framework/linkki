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

import org.linkki.core.ui.aspects.annotation.BindSlot;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.util.handler.Handler;

import de.faktorzehn.commons.linkki.search.annotation.UISearchParameters;
import de.faktorzehn.commons.linkki.search.component.SearchInputLayout;

public class SearchInputPmo {

    private final Object searchParametersPmo;
    private final SearchButtonsPmo searchButtonsPmo;

    /**
     * Creates a new PMO for search criteria with a given parameters PMO. The PMO should contain all
     * input controls. Note that the annotation on the PMO itself is ignored. Only the elements are
     * used.
     * 
     * @param searchParametersPmo PMO for the search parameters
     * @param search action for search
     * @param reset action for reset
     */
    public SearchInputPmo(Object searchParametersPmo, Handler search, Handler reset) {
        this.searchParametersPmo = searchParametersPmo;
        searchButtonsPmo = new SearchButtonsPmo(search, reset);
    }

    @BindSlot(SearchInputLayout.SLOT_INPUTS)
    @UISearchParameters(position = 10)
    public Object getSearchParametersPmo() {
        return searchParametersPmo;
    }

    @BindSlot(SearchInputLayout.SLOT_BUTTONS)
    @UINestedComponent(position = 20, width = "unset")
    public SearchButtonsPmo getSearchButtonsPmo() {
        return searchButtonsPmo;
    }

}

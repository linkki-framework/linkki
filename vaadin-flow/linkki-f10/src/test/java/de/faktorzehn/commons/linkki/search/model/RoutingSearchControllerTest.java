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

package de.faktorzehn.commons.linkki.search.model;

import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

class RoutingSearchControllerTest {

    @BeforeAll
    static void mockUI() {
        UI.setCurrent(mock(UI.class));
    }

    RoutingSearchController<SearchParams, SearchResult> controller = new RoutingSearchController<>(
            "search", $ -> new SearchResult(), new SearchParamsMapper(), $ -> new MessageList());

    @Test
    void testAfterNavigation_RemovesEmptyParameters() {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("emptyList", Collections.emptyList());
        queryParams.put("emptyValues", Arrays.asList("", null));

        controller.initialize(new Location("search", new QueryParameters(queryParams)));
    }

    private static class SearchParams {
        // empty
    }

    private static class SearchResult {
        // empty
    }

    private static class SearchParamsMapper implements SearchParameterMapper<SearchParams> {

        @Override
        public SearchParams toSearchParameters(Map<String, List<String>> queryParams) {
            if (!queryParams.isEmpty()) {
                throw new IllegalArgumentException("queryParams expected to be empty");
            }

            return new SearchParams();
        }

        @Override
        public Map<String, List<String>> toQueryParameters(SearchParams searchParams) {
            return Collections.emptyMap();
        }

    }

}

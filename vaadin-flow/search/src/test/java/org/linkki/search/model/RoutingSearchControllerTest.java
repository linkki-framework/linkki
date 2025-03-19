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

package org.linkki.search.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

class RoutingSearchControllerTest {

    @BeforeEach
    void mockUI() {
        UI.setCurrent(mock(UI.class));
    }

    RoutingSearchController<SearchParams, SearchResult> controller = new RoutingSearchController<>(
            "search", p -> new SearchResult(), new SearchParamsMapper(), p -> new MessageList());

    @Test
    void testInitialize_RemovesEmptyParameters() {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("emptyList", Collections.emptyList());
        queryParams.put("emptyValues", Arrays.asList("", null));

        controller.initialize(new Location("search", new QueryParameters(queryParams)));
        SearchParams searchParameters = controller.getParameters();

        assertThat(searchParameters.queryParams).isEmpty();
    }

    @Test
    void testInitialize_EmptyResultForEmptyParameters_NoSubmit() {
        Map<String, List<String>> queryParams = new HashMap<>();

        controller.initialize(new Location("search", new QueryParameters(queryParams)));

        var result = controller.get();
        assertThat(result).isNotPresent();
    }

    @Test
    void testInitialize_NoResultForSomeParameters_NoSubmit() {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("someList", Arrays.asList("1", "2", "3"));

        controller.initialize(new Location("search", new QueryParameters(queryParams)));

        var result = controller.get();
        assertThat(result).isNotPresent();
    }

    @Test
    void testInitialize_ResultForSomeParameters_WithSubmit() {
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("someList", Arrays.asList("1", "2", "3"));
        queryParams.put("submit", Arrays.asList("true"));

        controller.initialize(new Location("search", new QueryParameters(queryParams)));

        var result = controller.get();
        assertThat(result).isPresent();
    }

    private static class SearchParams {

        // the queryParams are only testing purposes
        final Map<String, List<String>> queryParams;

        SearchParams(Map<String, List<String>> queryParams) {
            this.queryParams = queryParams;
        }
    }

    private static class SearchResult {
        // empty
    }

    private static class SearchParamsMapper implements SearchParameterMapper<SearchParams> {

        @Override
        public SearchParams toSearchParameters(Map<String, List<String>> queryParams) {
            return new SearchParams(queryParams);
        }

        @Override
        public Map<String, List<String>> toQueryParameters(SearchParams searchParams) {
            return Collections.emptyMap();
        }

    }

}

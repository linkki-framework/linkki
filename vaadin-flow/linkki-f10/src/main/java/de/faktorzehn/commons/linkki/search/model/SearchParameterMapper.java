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

import java.util.List;
import java.util.Map;

import de.faktorzehn.commons.linkki.search.util.ParamsUtil;

/**
 * A mapper that can transform a search parameter object to query parameters and vice-versa.
 * 
 * @apiNote This interface is designed to support parameters that have multiple values associated
 *          with a single key. If all your parameters only support a single value, you can use a
 *          {@code Map<String, String>} by utilizing {@link ParamsUtil#flatten(Map)} and
 *          {@link ParamsUtil#expand(Map)}.
 *          <p>
 *          Additionally, a few {@code format...} and {@code parse...} methods are provided by
 *          {@link ParamsUtil}.
 * 
 * @see RoutingSearchController
 */
public interface SearchParameterMapper<PARAM> {

    /**
     * Creates search parameters for the given query parameters.
     * <p>
     * {@code queryParams} does {@link ParamsUtil#removeEmptyValues(Map) not contain empty values}.
     * 
     * @see ParamsUtil#flatten(Map)
     */
    public PARAM toSearchParameters(Map<String, List<String>> queryParams);

    /**
     * Creates query parameters for the given search parameters. All
     * {@link ParamsUtil#removeEmptyValues(Map) empty values will be removed} from the returned map
     * before being used in a URL.
     * 
     * @see ParamsUtil#expand(Map)
     */
    public Map<String, List<String>> toQueryParameters(PARAM searchParams);

}

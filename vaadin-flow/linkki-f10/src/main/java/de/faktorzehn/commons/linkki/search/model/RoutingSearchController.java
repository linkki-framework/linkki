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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.validation.message.MessageList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import de.faktorzehn.commons.linkki.search.util.ParamsUtil;

/**
 * A {@link SearchController} that uses URL query parameters as search parameters.
 * <p>
 * Triggering the search navigates to a URL containing the search parameters (encoded as query
 * parameters). The view must then call {@link #initialize(Location)}, which will call the search
 * function in either {@link BeforeEnterObserver before enter} or {@link AfterNavigationObserver
 * after navigation} step of the navigation lifecycle. In before enter it is possible to redirect to
 * another location, for example if there is only one search result, while after navigation is the
 * more common event to update the UI state. See.
 * https://vaadin.com/docs/latest/flow/routing/lifecycle for more details.
 */
public class RoutingSearchController<PARAM, RESULT> implements SearchController<PARAM, RESULT> {

    private static final String SUBMIT_PARAMETER = "submit";

    private final String viewPath;
    private final SearchParameterMapper<PARAM> parameterMapper;
    private final Function<PARAM, RESULT> searchFunction;
    private PARAM parameters;
    private final Function<RESULT, MessageList> messages;
    private RESULT result;

    /**
     * Creates a RoutingSearchController.
     * 
     * @param viewPath the {@link UI#navigate(String, QueryParameters) location} of the search view,
     *            most likely the value defined in the {@link Route} annotation
     * @param searchFunction a function that returns a search result for the given search parameters
     * @param parameterMapper a {@link SearchParameterMapper}
     * @param messages a function to retrieve a {@link MessageList} from a result
     */
    public RoutingSearchController(String viewPath,
            Function<PARAM, RESULT> searchFunction,
            SearchParameterMapper<PARAM> parameterMapper,
            Function<RESULT, MessageList> messages) {
        this.viewPath = viewPath;
        this.searchFunction = searchFunction;
        this.parameterMapper = parameterMapper;
        this.parameters = parameterMapper.toSearchParameters(Collections.emptyMap());
        this.messages = messages;
    }

    /**
     * Navigates to the given viewPath, which then <b>runs the search</b>. The search parameters
     * must be specified directly as query parameters. To transform a search parameter object to
     * {@code Map<String, List<String>>}, use the corresponding implementation of
     * {@link SearchParameterMapper#toQueryParameters(Object)}.
     * 
     * @param path the {@link UI#navigate(String, QueryParameters) viewPath} of the search view,
     *            most likely the value defined in the {@link Route} annotation
     * @param queryParams the parameters to use for the search
     */
    public static void navigateTo(String path, Map<String, List<String>> queryParams) {
        Map<String, List<String>> params = ParamsUtil.removeEmptyValues(queryParams);
        params.put(SUBMIT_PARAMETER, Arrays.asList("true"));

        UI.getCurrent().navigate(path, new QueryParameters(params));
    }

    /**
     * Reads the URL query parameters and may perform a search for given search parameters. It has
     * to be called when an {@link AfterNavigationEvent} occurs.
     * <p>
     * The search view must implement {@link AfterNavigationObserver} and call this method with the
     * given event in {@link AfterNavigationObserver#afterNavigation(AfterNavigationEvent)
     * afterNavigation}. Additionally, a binding context update has to be triggered using
     * {@link BindingContext#modelChanged()} after calling this method. This is due to the fact that
     * the query parameters have been mapped to a new search parameters object.
     * <p>
     * A minimal implementation of <code>afterNavigation</code> in the search view could look like
     * this:
     * 
     * <pre>
     * public void afterNavigation(AfterNavigationEvent event) {
     *     searchController.afterNavigation(event);
     *     bindingContext.modelChanged();
     * }
     * </pre>
     * 
     * @param newLocation the {@link Location} you can get from either
     *            {@link BeforeEnterEvent#getLocation()} or from
     *            {@link AfterNavigationEvent#getLocation()} to pass the query parameters to the
     *            {@link SearchParameterMapper}.
     */
    public void initialize(Location newLocation) {
        Map<String, List<String>> queryParams = newLocation.getQueryParameters().getParameters();
        queryParams = ParamsUtil.removeEmptyValues(queryParams);

        if (queryParams.containsKey(SUBMIT_PARAMETER)) {
            queryParams.remove(SUBMIT_PARAMETER);
            parameters = parameterMapper.toSearchParameters(queryParams);
            result = searchFunction.apply(parameters);
        } else {
            parameters = parameterMapper.toSearchParameters(queryParams);
            result = null;
        }
    }

    @Override
    public Optional<RESULT> get() {
        return Optional.ofNullable(result);
    }

    @Override
    public void search() {
        navigateTo(viewPath, parameterMapper.toQueryParameters(parameters));
    }

    @Override
    public MessageList getMessages() {
        return get().map(messages).orElse(new MessageList());
    }

    @Override
    public void reset() {
        UI.getCurrent().navigate(viewPath, QueryParameters.empty());
    }

    @Override
    public PARAM getParameters() {
        return parameters;
    }
}

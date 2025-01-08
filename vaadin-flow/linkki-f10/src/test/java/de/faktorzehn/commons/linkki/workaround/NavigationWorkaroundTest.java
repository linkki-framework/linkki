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

package de.faktorzehn.commons.linkki.workaround;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

class NavigationWorkaroundTest {

    private static final String ROUTE_PARAM = "path";
    private static final String ROUTE_PARAM_PATH_1 = "path1";
    private static final String ROUTE_PARAM_PATH_2 = "path2";
    private static final String ROUTE_PARAM_OTHER = "other";

    @BeforeEach
    void setupVaadin() {
        MockVaadin.setup(new Routes(
                Stream.of(NavigationWorkaround.RedirectionPage.class,
                          StartView.class,
                          ViewWithRouteParam.class,
                          ViewWithoutRouteParam.class)
                        .collect(Collectors.toSet()),
                Collections.emptySet(), true));

        // Navigate to start view
        UI.getCurrent().navigate("view/path1");
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testNavigation_EmptyParameters() {
        NavigationWorkaround.navigateToPage(ViewWithoutRouteParam.class);

        assertNavigation(ViewWithoutRouteParam.class, "view3/noparams");
    }

    @Test
    void testNavigation_ToSameView() {
        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_PATH_1));

        NavigationWorkaround.navigateToPage(StartView.class, routeParameters);

        assertNavigation(StartView.class, "view/path1");
    }

    @Test
    void testNavigation_ToSameView_WithQuery() {
        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_PATH_1));
        var query = new QueryParameters(Map.of("test", List.of("query")));

        NavigationWorkaround.navigateToPage(StartView.class, routeParameters, query);

        assertNavigation(StartView.class, "view/path1?test=query");
    }

    @Test
    void testNavigation_ToSameView_WithDifferentRouteParameters() {
        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_PATH_2));

        NavigationWorkaround.navigateToPage(StartView.class, routeParameters);

        assertNavigation(StartView.class, "view/path2");
    }

    @Test
    void testNavigation_ToDifferentView() {
        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_OTHER));

        NavigationWorkaround.navigateToPage(ViewWithRouteParam.class, routeParameters);

        assertNavigation(ViewWithRouteParam.class, "view2/other");
    }

    @Test
    void testNavigation_ToDifferentView_WithQuery() {
        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_OTHER));
        var query = new QueryParameters(Map.of("test", List.of("query")));

        NavigationWorkaround.navigateToPage(ViewWithRouteParam.class, routeParameters, query);

        assertNavigation(ViewWithRouteParam.class, "view2/other?test=query");
    }

    /**
     * Checks that the currently opened view equals the expected navigation result.
     *
     * @param targetClass the expected concrete class of the component
     * @param targetPath the expected target path
     */
    private void assertNavigation(Class<? extends Component> targetClass, String targetPath) {
        // Make sure the rerouting is finished
        MockVaadin.clientRoundtrip();
        var routerTargets = UI.getCurrent().getInternals().getActiveRouterTargetsChain();
        var pathWithQuery = UI.getCurrent().getInternals().getActiveViewLocation().getPathWithQueryParameters();

        assertThat(routerTargets).hasSize(1)
                .first()
                .isInstanceOf(targetClass);
        assertThat(pathWithQuery).isEqualTo(targetPath);
    }

    @Route(value = "view/:path(path1|path2)")
    public static class StartView extends Div {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Route(value = "view2/:path(other)")
    public static class ViewWithRouteParam extends Div {
        @Serial
        private static final long serialVersionUID = 1L;
    }

    @Route(value = "view3/noparams")
    public static class ViewWithoutRouteParam extends Div {
        @Serial
        private static final long serialVersionUID = 1L;
    }
}

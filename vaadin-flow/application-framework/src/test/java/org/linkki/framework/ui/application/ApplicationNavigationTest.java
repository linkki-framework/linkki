/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.framework.ui.application;

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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

/**
 * Tests that the navigation to a different page works.
 */
class ApplicationNavigationTest {

    private static final String EMPTY_ROUTE = "";
    private static final String TEST_VIEW_ROUTE = "route";
    private static final String EMPTY_VIEW_INNER_ELEMENT_ID = "empty";
    private static final String TEST_VIEW_INNER_ELEMENT_ID = "test";

    /**
     * Creates a {@link Span} with the given id.
     */
    private static Span createSampleSpan(String id) {
        var span = new Span();
        span.setId(id);
        return span;
    }

    @BeforeEach
    void setupVaadin() {
        MockVaadin.setup(new Routes(Stream.of(TestView.class, EmptyView.class)
                .collect(Collectors.toSet()),
                Collections.emptySet(), true));
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testNavigation() {
        UI.getCurrent().navigate(TEST_VIEW_ROUTE);

        var location = getCurrentLocation();
        assertRouteAfterNavigation(location, TEST_VIEW_ROUTE);
        assertRouteClassAfterNavigation(TestView.class, TEST_VIEW_INNER_ELEMENT_ID);
    }

    @Test
    void testNavigation_WithQuery() {
        UI.getCurrent().navigate(TEST_VIEW_ROUTE, createSampleQuery());

        var location = getCurrentLocation();
        assertRouteAfterNavigation(location, TEST_VIEW_ROUTE);
        assertQueryParameterAfterNavigation(location, "key", List.of("value"));
        assertRouteClassAfterNavigation(TestView.class, TEST_VIEW_INNER_ELEMENT_ID);
    }

    @Test
    void testNavigation_EmptyRoute() {
        UI.getCurrent().navigate(TEST_VIEW_ROUTE);
        assertRouteClassAfterNavigation(TestView.class, TEST_VIEW_INNER_ELEMENT_ID);

        UI.getCurrent().navigate(EMPTY_ROUTE);

        var location = getCurrentLocation();
        assertRouteAfterNavigation(location, EMPTY_ROUTE);
        assertRouteClassAfterNavigation(EmptyView.class, EMPTY_VIEW_INNER_ELEMENT_ID);
    }

    @Test
    void testNavigation_EmptyRouteWithQuery() {
        UI.getCurrent().navigate(TEST_VIEW_ROUTE);
        assertRouteClassAfterNavigation(TestView.class, TEST_VIEW_INNER_ELEMENT_ID);

        UI.getCurrent().navigate(EMPTY_ROUTE, QueryParameters.fromString("key"));

        var location = getCurrentLocation();
        assertRouteAfterNavigation(location, EMPTY_ROUTE);
        assertQueryParameterAfterNavigation(location, "key", List.of(""));
        assertRouteClassAfterNavigation(EmptyView.class, EMPTY_VIEW_INNER_ELEMENT_ID);
    }

    private Location getCurrentLocation() {
        return UI.getCurrent().getInternals().getActiveViewLocation();
    }

    /**
     * Checks the currently shown {@link Component}.
     * 
     * @param clazz the expected concrete class of the component
     * @param innerElementId the ID of the inner {@link Span} element that is contained by each view
     */
    private void assertRouteClassAfterNavigation(Class<? extends Component> clazz, String innerElementId) {
        var routerTargets = UI.getCurrent().getInternals().getActiveRouterTargetsChain();
        assertThat(routerTargets).hasSize(1);
        var currentClass = routerTargets.get(0);
        assertThat(currentClass).isInstanceOf(clazz);
        var expectedInnerElement = new Element("span");
        expectedInnerElement.setAttribute("id", innerElementId);
        assertThat(currentClass.getElement().getChildren()).hasSize(1);
        var child = currentClass.getElement().getChild(0);
        assertThat(child.getTag()).isEqualTo("span");
        assertThat(child.getAttribute("id")).isEqualTo(innerElementId);
    }

    private void assertRouteAfterNavigation(Location currentLocation, String expectedLocation) {
        assertThat(currentLocation.getSegments()).last()
                .as("Check current route")
                .isEqualTo(expectedLocation);
    }

    private void assertQueryParameterAfterNavigation(Location currentLocation, String key, List<String> values) {
        assertThat(currentLocation.getQueryParameters().getParameters())
                .as("Check query parameters")
                .containsExactly(Map.entry(key, values));
    }

    /**
     * Creates {@link QueryParameters} that contain a single parameter with the key 'key' and the value
     * 'value'.
     */
    private QueryParameters createSampleQuery() {
        var queryKey = "key";
        var queryValues = List.of("value");
        return new QueryParameters(Map.of(queryKey, queryValues));
    }

    @Route(value = TEST_VIEW_ROUTE)
    public static class TestView extends Div {
        @Serial
        private static final long serialVersionUID = 1L;

        public TestView() {
            super(createSampleSpan(TEST_VIEW_INNER_ELEMENT_ID));
        }
    }

    @Route(value = EMPTY_ROUTE)
    public static class EmptyView extends Div {
        @Serial
        private static final long serialVersionUID = 1L;

        public EmptyView() {
            super(createSampleSpan(EMPTY_VIEW_INNER_ELEMENT_ID));
        }
    }
}

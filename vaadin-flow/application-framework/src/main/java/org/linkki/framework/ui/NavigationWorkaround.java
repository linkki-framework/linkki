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

package org.linkki.framework.ui;

import java.io.Serial;
import java.util.HashMap;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteParameters;

/**
 * Workaround for resolving an error that occurs when navigating from a page to itself. Only use
 * this workaround if the described error occurs, specifically when navigating from a page to itself
 * does not work properly.
 * <p>
 * The workaround works as follows:
 * <ol>
 * <li>If the currently shown page is the same as the navigation target page, the workaround is
 * triggered.</li>
 * <li>The page is first navigated to a special intermediate page called
 * {@link RedirectionPage}.</li>
 * <li>From {@link RedirectionPage}, it is then navigated to the original target page.</li>
 * </ol>
 * This two-step navigation process avoids the error described in the linked issue. If the target
 * page differs from the current page, the navigation is executed as expected without using the
 * workaround.
 * <p>
 * Note that the {@link Route} used for this workaround has to be included in the Vaadin package
 * scan, see {@link #WORKAROUND_ROUTE_PACKAGE}.
 *
 * @see <a href="https://github.com/vaadin/flow/issues/16984">related GitHub issue</a>
 * 
 * @since 2.8.0
 */
// TODO: remove this class and its usages if the described issue is fixed.
// Also remove the jakarta.servlet-api dependency from this module'S POM since it is only required
// for this class.
public class NavigationWorkaround {

    /**
     * Use this to include the {@link Route} used for this workaround in the Vaadin package scan,
     * e.g. {@code @EnableVaadin(NavigationWorkaround.WORKAROUND_ROUTE_PACKAGE)}.
     */
    public static final String WORKAROUND_ROUTE_PACKAGE = "org.linkki.framework.ui.workaround";

    private static final String REDIRECTION_PAGE_ROUTE = "redirect";

    private NavigationWorkaround() {
        // utility class
    }

    /**
     * Uses {@link NavigationWorkaround#navigateToPage(Class, RouteParameters)}.
     */
    public static <T extends Component> void navigateToPage(Class<T> target) {
        navigateToPage(target, new RouteParameters());
    }

    /**
     * Uses {@link NavigationWorkaround#navigateToPage(Class, RouteParameters, QueryParameters)}.
     */
    public static <T extends Component> void navigateToPage(Class<T> target, RouteParameters routeParameters) {
        navigateToPage(target, routeParameters, new QueryParameters(new HashMap<>()));
    }

    /**
     * Only use this workaround if the described error occurs, specifically when navigating from a
     * page to itself does not work properly.
     *
     * @apiNote The workaround is only applied if the target does not differ from the current page.
     *          Otherwise, the navigation is executed as expected.
     */
    public static <T extends Component> void navigateToPage(Class<T> target,
            RouteParameters routeParameters,
            QueryParameters queryParameters) {
        var ui = UI.getCurrent();
        if (ui.getCurrentView() != null && ui.getCurrentView().getClass() == target) {
            var targetPath = RouteConfiguration.forSessionScope().getUrl(target, routeParameters);
            var redirectionPath = REDIRECTION_PAGE_ROUTE + "/" + targetPath;
            ui.navigate(redirectionPath, queryParameters);
        } else {
            ui.navigate(target, routeParameters, queryParameters);
        }
    }

    /**
     * The redirection page expects a path starting with {@code redirect}, followed by the path
     * segments the page should redirect to. {@link QueryParameters} are also handled.
     *
     * @implNote Forwarding by using the {@link BeforeEnterObserver} does not work since it would
     *           cause that the {@link BeforeLeaveObserver} of the current page is called twice.
     *           This behavior is by design in Vaadin. For example, if a {@link BeforeLeaveObserver}
     *           displays a dialog, it would be displayed twice. Consequently,
     *           {@link AfterNavigationObserver} has to be used.
     */
    @Route(value = REDIRECTION_PAGE_ROUTE + "/:path*")
    public static class RedirectionPage extends Div implements AfterNavigationObserver {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void afterNavigation(AfterNavigationEvent event) {
            var ui = UI.getCurrent();
            var location = event.getLocation();
            var path = location.getPath().replace(REDIRECTION_PAGE_ROUTE, "");
            var query = event.getLocation().getQueryParameters();
            // access() ensures the complete execution of the navigation.
            // Otherwise, the user might get stuck in this page.
            ui.access(() -> ui.navigate(path, query));
        }
    }
}

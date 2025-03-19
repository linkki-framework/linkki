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

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertNone;
import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.framework.ui.NavigationWorkaround.navigateToPage;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

/**
 * Tests a {@link NavigationWorkaround} for a known Vaadin bug <a href="https://github.com/vaadin/flow/issues/16984">.
 */
class NavigationWorkaroundTest {

    private static final String ROUTE_PARAM = "path";
    private static final String ROUTE_PARAM_PATH_1 = "path1";
    private static final String ROUTE_PARAM_PATH_2 = "path2";
    private static final String ROUTE_PARAM_OTHER = "other";

    @BeforeAll
    static void disableReact() {
        System.setProperty("vaadin.react.enable", "false");
    }

    @AfterAll
    static void clearReactEnable() {
        System.clearProperty("vaadin.react.enable");
    }

    @BeforeEach
    void setupVaadin() {
        MockVaadin.setup(new Routes(
                Stream.of(NavigationWorkaround.RedirectionPage.class,
                                StartView.class,
                                ViewWithRouteParam.class,
                          ViewWithoutRouteParam.class,
                          ViewForBug.class,
                          ViewForWorkaround.class)
                        .collect(Collectors.toSet()),
                Collections.emptySet(), true));
        assertThat(UI.getCurrent().getSession().getConfiguration().isReactEnabled()).isFalse();
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testBugReproduction() {

        UI.getCurrent().navigate("view4/1");

        _get(Button.class).click();
        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();

        assertThatUIIsNavigatedTo(ViewForBug.class, "view4/1");

        _get(Button.class).click();

        // at this point the dialog doesn't open because the navigation was not triggered and
        // therefore the beforeLeave is not called
        _assertNone(Dialog.class);
    }

    @Test
    void testWorkaround() {

        UI.getCurrent().navigate("view4/2");

        _get(Button.class).click();
        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();

        assertThatUIIsNavigatedTo(ViewForWorkaround.class, "view4/2");

        _get(Button.class).click();
        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();

        assertThatUIIsNavigatedTo(ViewForWorkaround.class, "view4/2");
    }

    @Test
    void testNavigation_EmptyParameters() {
        UI.getCurrent().navigate("view/path1");

        navigateToPage(ViewWithoutRouteParam.class);

        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();
        assertThatUIIsNavigatedTo(ViewWithoutRouteParam.class, "view3/noparams");
    }

    @Test
    void testNavigation_ToSameView() {
        UI.getCurrent().navigate("view/path1");

        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_PATH_1));

        navigateToPage(StartView.class, routeParameters);

        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();
        assertThatUIIsNavigatedTo(StartView.class, "view/path1");
    }

    @Test
    void testNavigation_ToSameView_WithQuery() {
        UI.getCurrent().navigate("view/path1");

        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_PATH_1));
        var query = new QueryParameters(Map.of("test", List.of("query")));

        navigateToPage(StartView.class, routeParameters, query);

        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();
        assertThatUIIsNavigatedTo(StartView.class, "view/path1?test=query");
    }

    @Test
    void testNavigation_ToSameView_WithDifferentRouteParameters() {
        UI.getCurrent().navigate("view/path1");

        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_PATH_2));

        navigateToPage(StartView.class, routeParameters);

        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();
        assertThatUIIsNavigatedTo(StartView.class, "view/path2");
    }

    @Test
    void testNavigation_ToDifferentView() {
        UI.getCurrent().navigate("view/path1");

        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_OTHER));

        navigateToPage(ViewWithRouteParam.class, routeParameters);

        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();
        assertThatUIIsNavigatedTo(ViewWithRouteParam.class, "view2/other");
    }

    @Test
    void testNavigation_ToDifferentView_WithQuery() {
        UI.getCurrent().navigate("view/path1");

        var routeParameters = new RouteParameters(Map.of(ROUTE_PARAM, ROUTE_PARAM_OTHER));
        var query = new QueryParameters(Map.of("test", List.of("query")));

        navigateToPage(ViewWithRouteParam.class, routeParameters, query);

        _assertOne(Dialog.class);
        KaribuUtils.Dialogs.clickOkButton();
        assertThatUIIsNavigatedTo(ViewWithRouteParam.class, "view2/other?test=query");
    }

    /**
     * Checks that the currently opened view equals the expected navigation result.
     *
     * @param targetClass the expected concrete class of the component
     * @param targetPath  the expected target path
     */
    private void assertThatUIIsNavigatedTo(Class<? extends Component> targetClass, String targetPath) {
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
    public static class StartView extends Div implements BeforeLeaveObserver {
        @Serial
        private static final long serialVersionUID = 1L;

        private boolean showLeaveConfirmation = true;

        @Override
        public void beforeLeave(BeforeLeaveEvent event) {
            if (showLeaveConfirmation) {
                var action = event.postpone();
                ConfirmationDialog.open("", new Div(), () -> {
                    showLeaveConfirmation = false;
                    action.proceed();
                });
            }
        }
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

    @Route(value = "view4/1")
    public static class ViewForBug extends TestView {
        @Serial
        private static final long serialVersionUID = 1L;

        public ViewForBug() {
            super(e -> UI.getCurrent().navigate(ViewForBug.class));
        }
    }

    @Route(value = "view4/2")
    public static class ViewForWorkaround extends TestView {
        @Serial
        private static final long serialVersionUID = 1L;

        public ViewForWorkaround() {
            super(e -> NavigationWorkaround.navigateToPage(ViewForWorkaround.class));
        }
    }

    abstract static class TestView extends Div implements BeforeLeaveObserver {
        @Serial
        private static final long serialVersionUID = 1L;

        public TestView(Consumer<ClickEvent<?>> clickHandler) {
            add(new Button("Navigate", clickHandler::accept));
        }

        @Override
        public void beforeLeave(BeforeLeaveEvent event) {
            var action = event.postpone();
            var dialog = new Dialog();
            var okButton = new Button("Proceed", e -> {
                action.proceed();
                dialog.close();
            });
            okButton.setId("okButton");
            dialog.add(okButton);
            dialog.open();
        }

    }

}

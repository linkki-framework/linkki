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
package org.linkki.framework.ui.dialogs;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;

class DialogErrorHandlerTest {

    private static final String TEST_VIEW_ROUTE = "route";

    @BeforeAll
    static void setupLanguage() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void vaadinTearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testErrorHandling() {
        MockVaadin.setup(new Routes(Stream.of(TestView.class)
                .collect(Collectors.toSet()),
                Collections.emptySet(), true));

        var errorDialog = openDefaultErrorDialog();

        assertThat(errorDialog.getCaption()).isEqualTo("Error in the Application");
        errorDialog.ok();
        var location = UI.getCurrent().getInternals().getActiveViewLocation();
        assertThat(location.getSegments()).last().isEqualTo(TEST_VIEW_ROUTE);
        var queryParameters = location.getQueryParameters().getParameters();
        assertThat(queryParameters).containsExactly(new AbstractMap.SimpleEntry<>(
                DialogErrorHandler.ERROR_PARAM, List.of(StringUtils.EMPTY)));
    }

    /**
     * Opens a new error dialog which uses the default configuration from
     * {@link ErrorDialogConfiguration}. The used OK handler navigates to {@link TestView}.
     */
    private OkCancelDialog openDefaultErrorDialog() {
        var dialogConfig = ErrorDialogConfiguration.createWithHandlerNavigatingTo(TEST_VIEW_ROUTE);
        var handler = new DialogErrorHandler(dialogConfig);
        handler.error(new ErrorEvent(new RuntimeException()));
        return _get(OkCancelDialog.class);
    }

    @Route(value = TEST_VIEW_ROUTE)
    public static class TestView extends Div {
        private static final long serialVersionUID = 1L;
    }
}

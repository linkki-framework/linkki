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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.linkki.util.handler.Handler;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorEvent;

class DialogErrorHandlerTest {

    @Test
    void testErrorOccurredParameter() {
        MockVaadin.setup(new Routes(Stream.of(TestView.class)
                .collect(Collectors.toSet()),
                Collections.emptySet(), true));

        DialogErrorHandler handler = new DialogErrorHandler(TestConfirmationDialog::new);

        handler.error(new ErrorEvent(new RuntimeException("some test exception")));

        assertThat(UI.getCurrent().getInternals().getActiveViewLocation().getQueryParameters().getQueryString(),
                   containsString(DialogErrorHandler.ERROR_PARAM));

        MockVaadin.tearDown();
    }

    @Route(value = "")
    public static class TestView extends Div {
        private static final long serialVersionUID = 1L;
    }

    static class TestConfirmationDialog extends DefaultErrorDialog {

        private static final long serialVersionUID = 1L;

        public TestConfirmationDialog(ErrorEvent errorEvent, Handler navigateToStartView) {
            super(errorEvent, navigateToStartView);
        }

        @Override
        public OkCancelDialog open() {
            OkCancelDialog d = super.open();
            // close dialog to trigger navigation to TestView
            d.close();
            return d;
        }
    }
}

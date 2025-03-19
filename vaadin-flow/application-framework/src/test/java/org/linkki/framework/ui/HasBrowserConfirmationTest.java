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

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.KaribuUIExtension.withConfiguration;
import static org.linkki.core.ui.test.KaribuUIExtension.KaribuConfiguration.withDefaults;
import static org.mockito.Mockito.spy;

import java.io.Serial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.framework.ui.dialogs.OkCancelDialog;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

class HasBrowserConfirmationTest {

    @RegisterExtension
    static KaribuUIExtension karibu = withConfiguration(withDefaults()
            .addRoute(TestView.class, () -> spy(new TestView()))
            .addRoutes(OtherView.class));

    @Test
    void testBeforeLeave_CallDisableBrowserConfirmation() {
        assertThat(UI.getCurrent().getCurrentView()).isInstanceOf(TestView.class);

        UI.getCurrent().refreshCurrentRoute(true);

        _assertOne(OkCancelDialog.class);
    }

    @Route("")
    public static class TestView extends Div implements HasBrowserConfirmation {

        @Serial
        private static final long serialVersionUID = 5648012617343370024L;

        @Override
        public void disableBrowserConfirmation() {
            ConfirmationDialog.open("does something additionally that can be tested", "");
            HasBrowserConfirmation.super.disableBrowserConfirmation();
        }
    }

    @Route("other")
    public static class OtherView extends Div {

        @Serial
        private static final long serialVersionUID = 5648012617343370024L;
    }
}

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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public class DialogViewChangeTest extends AbstractUiTest {

    @Test
    public void testDialogClosedOnViewChange() {
        navigateToView("dialog");

        assertThat($(WindowElement.class).all().size(), is(1));
        $(ButtonElement.class).caption("OK").first().click();

        clickButton("openEmptyOkCancelDialog");
        assertThat($(WindowElement.class).all().size(), is(1));

        navigateToView("");
        assertThat($(WindowElement.class).all().size(), is(0));
    }
}

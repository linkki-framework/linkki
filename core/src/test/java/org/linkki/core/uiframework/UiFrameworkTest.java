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

package org.linkki.core.uiframework;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.nls.TestUiComponent;

public class UiFrameworkTest {

    @Test
    public void testGet() {
        assertThat(UiFramework.get(), is(instanceOf(TestUiFramework.class)));
    }


    @Test
    public void testIsVisible() {
        var uiComponent = new TestUiComponent();
        assertThat(UiFramework.isVisible(uiComponent), is(true));

        uiComponent.setVisible(false);
        assertThat(UiFramework.isVisible(uiComponent), is(false));
    }
}

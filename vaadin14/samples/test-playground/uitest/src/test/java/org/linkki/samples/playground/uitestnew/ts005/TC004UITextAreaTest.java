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

package org.linkki.samples.playground.uitestnew.ts005;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.uitestnew.BaseUITest;

import com.vaadin.flow.component.textfield.testbench.TextAreaElement;

public class TC004UITextAreaTest extends BaseUITest {

    @BeforeEach
    public void setUp() {
        goToTestCase("TS005", "TC004");
    }

    @Test
    public void testTextArea_Height() {
        TextAreaElement textArea = $(TextAreaElement.class).id("content");

        // custom height defined in annotation
        assertThat(textArea.getPropertyString("style", "height"), is("5em"));
    }

}

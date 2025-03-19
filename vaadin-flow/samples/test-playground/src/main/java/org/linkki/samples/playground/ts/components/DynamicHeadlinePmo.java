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

package org.linkki.samples.playground.ts.components;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.framework.ui.component.UIHeadline;

@UIVerticalLayout
public class DynamicHeadlinePmo {

    private int counter = 0;

    @UIHeadline
    public String getHeadline() {
        return "Headline - " + counter;
    }

    @UIButton(position = 10, caption = "Click on this button. The headline should be updated.")
    public void increment() {
        counter++;
    }

}

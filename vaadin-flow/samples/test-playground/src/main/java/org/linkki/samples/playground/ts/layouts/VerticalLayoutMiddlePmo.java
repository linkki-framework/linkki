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

package org.linkki.samples.playground.ts.layouts;

import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.HorizontalAlignment;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

@UIVerticalLayout(alignment = HorizontalAlignment.MIDDLE)
public class VerticalLayoutMiddlePmo {

    @UITextField(position = 1, width = "15em")
    public String getText() {
        return "I should be in the middle";
    }

}

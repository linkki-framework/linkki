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

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;

@UIHorizontalLayout(spacing = false)
public class HorizontalLayoutSpacingPmo {

    @UILabel(position = -1)
    public String getLabel() {
        return "Second Layout !";
    }

    @UITextField(position = 1)
    public String getText() {
        return "This Layout should have no spacing and no padding";
    }

    @UITextField(position = 2)
    public String getText2() {
        return "This Layout should have no spacing and no padding";
    }

    @UITextField(position = 3)
    public String getText3() {
        return "This Layout should have no spacing and no padding";
    }
}

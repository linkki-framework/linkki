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

package org.linkki.samples.playground.ts.aspects;

import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

import com.vaadin.flow.component.dependency.CssImport;

@CssImport("./styles/shared-styles.css")
@UIVerticalLayout
@BindStyleNames({ "style1", "style2", "style3" })
public class BindStyleNamesPmo {

    @UILabel(position = 1)
    public String getString() {
        return "I am a white text within a blue box with a thick border";
    }

}

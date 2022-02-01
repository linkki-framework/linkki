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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "SectionLayout.HORIZONTAL", layout = SectionLayout.HORIZONTAL)
public class UiSectionLayoutHorizontalComponentsPmo extends AbstractUiSectionLayoutComponentsPmo {

    @BindTooltip(value = "A smaller TextField with width set to \"\"")
    @UITextField(position = 90, label = "A smaller TextField ", width = "")
    public String getTextFieldWithEmptyWidth() {
        return "A smaller TextField with width set to \"\"";
    }

    @BindTooltip(value = "A smaller TextArea with width set to \"\"")
    @UITextArea(position = 100, height = "5em", label = "A smaller TextArea", width = "")
    public String getTextAreaWithEmptyWidth() {
        return "A smaller TextArea with width set to \"\"";
    }

}

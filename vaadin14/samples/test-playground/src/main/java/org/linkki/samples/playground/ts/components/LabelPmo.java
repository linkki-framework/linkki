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

package org.linkki.samples.playground.ts.components;

import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class LabelPmo {

    @UILabel(position = 10, label = "", htmlContent = true)
    public String getHtmlContentLabel() {
        return "<i style=\"color: red;\">HTML</i> <b>Content</b>";
    }

    @UILabel(position = 20, label = "")
    public String getNotHtmlContentLabel() {
        return "<b>NOT</b> HTML Content";
    }

    @BindIcon(value = VaadinIcon.ABACUS)
    @UILabel(position = 30, label = "", htmlContent = true)
    public String getNotHtmlContentLabelMitIcon() {
        return "<i style=\\\"color: red;\\\">HTML</i> <b>Content</b> mit Icon";
    }

    @BindIcon(value = VaadinIcon.ABACUS)
    @UILabel(position = 40, label = "")
    public String getHtmlContentLabelMitIcon() {
        return "<div><b>NOT</b> HTML Content mit Icon</div>";
    }

}

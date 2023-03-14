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

import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class LinkPmo {

    private static final String LINKKI_URL = "https://linkki-framework.org/";

    private String caption = "linkki-framework.org";
    private String href = LINKKI_URL;
    private VaadinIcon icon = VaadinIcon.EXTERNAL_LINK;

    @UILink(position = 0, target = "_blank", label = "Link")
    public String getLink() {
        return href;
    }

    public String getLinkCaption() {
        return caption;
    }

    public VaadinIcon getLinkIcon() {
        return icon;
    }

    @UITextField(position = 10, label = "Link Caption")
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @UITextField(position = 11, label = "Link Address")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @UIComboBox(position = 12, label = "Link Icon")
    public VaadinIcon getIcon() {
        return icon;
    }

    public void setIcon(VaadinIcon icon) {
        this.icon = icon;
    }

    @UILabel(position = 15)
    public String getLabel() {
        return "Links with icons:";
    }

    @UILink(position = 20, target = "_blank", label = "Link with an icon on the left",
            iconPosition = IconPosition.LEFT, caption = "Link with an icon on the left")
    @BindIcon(value = VaadinIcon.EXTERNAL_LINK, iconType = IconType.STATIC)
    public String getLinkIconLeft() {
        return LINKKI_URL;
    }

    @UILink(position = 21, target = "_blank", label = "Link with an icon on the right",
            iconPosition = IconPosition.RIGHT, caption = "Link with an icon on the right")
    @BindIcon(value = VaadinIcon.EXTERNAL_LINK, iconType = IconType.STATIC)
    public String getLinkIconRight() {
        return LINKKI_URL;
    }
}

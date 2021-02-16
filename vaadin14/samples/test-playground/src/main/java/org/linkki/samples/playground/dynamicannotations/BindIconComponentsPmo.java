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

package org.linkki.samples.playground.dynamicannotations;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UILink.LinkTarget;
import org.linkki.core.ui.layout.annotation.UIFormSection;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

@UIFormSection(caption = "@BindIconSectionPmo")
public class BindIconComponentsPmo {

    @BindIcon(value = VaadinIcon.ANCHOR)
    @UIButton(position = 10, captionType = CaptionType.STATIC, caption = "Start shipping")
    public void buttonWithStaticIcon() {
        Notification.show("Shipping started");
    }

    @BindIcon(iconType = IconType.DYNAMIC)
    @UILink(position = 20, caption = "Click to continue", target = LinkTarget.BLANK)
    public String getLinkField() {
        return "/linkki-sample-test-playground-vaadin14/";
    }

    public VaadinIcon getLinkFieldIcon() {
        return VaadinIcon.ARROW_CIRCLE_RIGHT;
    }

    @BindIcon(iconType = IconType.STATIC, value = VaadinIcon.DOWNLOAD)
    @UILink(position = 30, label = "Download", caption = "", captionType = CaptionType.STATIC, target = LinkTarget.BLANK)
    public String getDownloadLinkField() {
        return "/linkki-sample-test-playground-vaadin14/";
    }

}

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

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UILink.LinkTarget;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIFormSection;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

@UIFormSection(caption = "Components with @BindIcon and @BindCaption")
public class BindIconComponentsPmo {

    private VaadinIcon icon = VaadinIcon.ANCHOR;
    private String text = "Text";

    @UIComboBox(position = 0, label = "Icon", content = AvailableValuesType.ENUM_VALUES_INCL_NULL)
    public VaadinIcon getComponentIcon() {
        return icon;
    }

    public void setComponentIcon(VaadinIcon icon) {
        this.icon = icon;
    }

    @UITextField(position = 1, label = "Text")
    public String getComponentText() {
        return text;
    }

    public void setComponentText(String text) {
        this.text = text;
    }

    @UILabel(position = 10, htmlContent = true)
    public String getButtonHeadline() {
        return "<h5>@UIButton</h5>";
    }

    @BindIcon
    @BindCaption
    @UIButton(position = 11, label = "dynamic")
    public void dynamicButton() {
        Notification.show("Button clicked");
    }

    public VaadinIcon getDynamicButtonIcon() {
        return icon;
    }

    public String getDynamicButtonCaption() {
        return text;
    }

    @BindIcon(VaadinIcon.COMPILE)
    @BindCaption
    @UIButton(position = 12, label = "static")
    public void staticButton() {
        Notification.show("Button clicked");
    }

    public String getStaticButtonCaption() {
        return text;
    }

    @UILabel(position = 20, htmlContent = true)
    public String getLinkHeadline() {
        return "<h5>@UILink</h5>";
    }

    @BindIcon
    @BindCaption
    @UILink(position = 21, label = "dynamic", target = LinkTarget.BLANK)
    public String getDynamicLink() {
        return "/linkki-sample-test-playground-vaadin14/";
    }

    public VaadinIcon getDynamicLinkIcon() {
        return icon;
    }

    public String getDynamicLinkCaption() {
        return text;
    }

    @BindIcon(VaadinIcon.COMPILE)
    @BindCaption
    @UILink(position = 22, label = "static", target = LinkTarget.BLANK)
    public String getStaticLink() {
        return "/linkki-sample-test-playground-vaadin14/";
    }

    public String getStaticLinkCaption() {
        return text;
    }

    @UILabel(position = 30, htmlContent = true)
    public String getLabelHeadline() {
        return "<h5>@UILabel</h5>";
    }

    @BindIcon
    @UILabel(position = 31, label = "dynamic")
    public String getDynamicLabel() {
        return text;
    }

    public VaadinIcon getDynamicLabelIcon() {
        return icon;
    }

    @BindIcon(VaadinIcon.COMPILE)
    @UILabel(position = 32, label = "static")
    public String getStaticLabel() {
        return text;
    }


}

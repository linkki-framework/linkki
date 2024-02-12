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

package org.linkki.samples.playground.ts.aspects;

import org.linkki.core.defaults.ui.aspects.types.LabelType;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.aspects.annotation.BindLabel;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.util.HtmlContent;

/**
 * Test PMO in order to test {@link BindLabel @BindLabel}. Belongs to test scenario TS008/TC015.
 */
@UISection
public class BindLabelPmo implements PresentationModelObject {

    private String dynamicLabel = "Dynamic label";

    @UITextField(position = 5, label = "Set dynamic label:")
    public String getDynamicLabel() {
        return dynamicLabel;
    }

    @UILabel(position = 10)
    public HtmlContent getDynamicLabelsHeadline() {
        return h4("Dynamic labels");
    }

    public void setDynamicLabel(String label) {
        this.dynamicLabel = label;
    }

    // tag::bindLabel-dynamic[]
    @BindLabel(labelType = LabelType.DYNAMIC)
    @UIButton(position = 20)
    public void dynamicButton() {
        // button
    }

    public String getDynamicButtonLabel() {
        return dynamicLabel;
    }
    // end::bindLabel-dynamic[]

    @BindLabel(labelType = LabelType.DYNAMIC)
    @UICheckBox(position = 30)
    public boolean isDynamicCheckbox() {
        return true;
    }

    public String getDynamicCheckboxLabel() {
        return dynamicLabel;
    }

    @BindReadOnly
    @BindLabel(labelType = LabelType.DYNAMIC)
    @UITextField(position = 40, label = "Do not display")
    public void dynamicTextField() {
        // text field
    }

    public String getDynamicTextField() {
        return "DynamicTextField";
    }

    public String getDynamicTextFieldLabel() {
        return dynamicLabel;
    }

    @UILabel(position = 45)
    public HtmlContent getAutoLabelsHeadline() {
        return h4("Auto type labels");
    }

    @BindLabel(labelType = LabelType.AUTO)
    @UIButton(position = 50)
    public void autoDynamicButton() {
        // button
    }

    public String getAutoDynamicButtonLabel() {
        return dynamicLabel;
    }

    @BindLabel(labelType = LabelType.AUTO, value = "Auto type label with static content")
    @UIButton(position = 55)
    public void autoStaticButton() {
        // button
    }

    public String getAutoStaticButtonLabel() {
        return dynamicLabel;
    }

    @UILabel(position = 60)
    public HtmlContent getStaticLabelsHeadline() {
        return h4("Static labels");
    }

    // tag::bindLabel-static[]
    @BindLabel(labelType = LabelType.STATIC, value = "Static label")
    @UIButton(position = 65)
    public void staticButton() {
        // button
    }
    // end::bindLabel-static[]

    @UILabel(position = 70)
    public HtmlContent getNoneLabelsHeadline() {
        return h4("No labels");
    }

    @BindLabel(labelType = LabelType.NONE)
    @UIButton(position = 75, label = "Do not display")
    public void noneButton() {
        // button
    }

    private HtmlContent h4(String string) {
        return HtmlContent.builder().tag("h4", string).build();
    }
}

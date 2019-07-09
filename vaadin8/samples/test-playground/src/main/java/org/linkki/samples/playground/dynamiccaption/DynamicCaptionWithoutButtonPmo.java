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

package org.linkki.samples.playground.dynamiccaption;

import java.util.Optional;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@BindCaption(captionType = CaptionType.DYNAMIC)
@UISection
public class DynamicCaptionWithoutButtonPmo implements PresentationModelObject {

    public static final String PROPERTY_SECTION_CAPTION = "sectionCaption";
    public static final String PROPERTY_FIELD_CAPTION = "fieldCaption";
    public static final String PROPERTY_BUTTON = "button";
    public static final String PROPERTY_CHECKBOX = "checkbox";

    private String sectionCaption = "Dynamic caption";
    private String fieldCaption = "even more captions";
    private boolean checkbox = true;

    // aspect "caption" for the section
    public String getCaption() {
        return sectionCaption;
    }

    @UITextField(position = 10, label = PROPERTY_SECTION_CAPTION)
    public String getSectionCaption() {
        return sectionCaption;
    }

    public void setSectionCaption(String caption) {
        this.sectionCaption = caption;
    }

    @BindCaption(captionType = CaptionType.DYNAMIC)
    @UITextField(position = 20, label = PROPERTY_FIELD_CAPTION)
    public String getFieldCaption() {
        return fieldCaption;
    }

    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
    }

    public String getFieldCaptionCaption() {
        return fieldCaption;
    }

    @BindCaption(captionType = CaptionType.DYNAMIC)
    @UIButton(position = 30, label = PROPERTY_BUTTON)
    public void button() {
        // button
    }

    public String getButtonCaption() {
        return fieldCaption;
    }

    @BindCaption(captionType = CaptionType.DYNAMIC)
    @UICheckBox(position = 40, label = PROPERTY_CHECKBOX, caption = "")
    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getCheckboxCaption() {
        return fieldCaption;
    }

    @Override
    public Optional<ButtonPmo> getEditButtonPmo() {
        return Optional.empty();
    }

}

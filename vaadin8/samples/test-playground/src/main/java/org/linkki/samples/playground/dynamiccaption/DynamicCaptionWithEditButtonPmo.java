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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.icons.VaadinIcons;

@BindCaption(captionType = CaptionType.DYNAMIC)
@UISection
public class DynamicCaptionWithEditButtonPmo implements PresentationModelObject {

    public static final String PROPERTY_SECTION_CAPTION = "editCaption";

    private String sectionCaption = "Dynamic caption with edit button";

    // aspect "caption" for the section
    public String getCaption() {
        return sectionCaption;
    }

    @UILabel(position = 10, htmlContent = true)
    public String getDescription() {
        return "<ul><li>The caption should update dynamically</li><li>Header button should be visible even if the caption is empty</li></ul>";
    }

    @UITextField(position = 20, label = "Caption for section with button")
    public String getEditCaption() {
        return sectionCaption;
    }

    public void setEditCaption(String caption) {
        this.sectionCaption = caption;
    }

    @Override
    public Optional<ButtonPmo> getEditButtonPmo() {
        ButtonPmo button = new ButtonPmo() {

            @Override
            public void onClick() {
                // nothing to do
            }

            @Override
            public List<String> getStyleNames() {
                return Collections.emptyList();
            }

            @Override
            public Object getButtonIcon() {
                return VaadinIcons.AMBULANCE;
            }
        };

        return Optional.of(button);
    }

}

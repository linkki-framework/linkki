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

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@BindCaption(captionType = CaptionType.DYNAMIC)
@UISection(closeable = true)
public class BindCaptionWithCloseButtonPmo implements PresentationModelObject {

    public static final String PROPERTY_SECTION_CAPTION = "closeCaption";

    private String sectionCaption = "Dynamic caption with close button";

    // aspect "caption" for the section
    public String getCaption() {
        return sectionCaption;
    }

    @UITextField(position = 20, label = "Caption for closable section")
    public String getCloseCaption() {
        return sectionCaption;
    }

    public void setCloseCaption(String caption) {
        this.sectionCaption = caption;
    }
}

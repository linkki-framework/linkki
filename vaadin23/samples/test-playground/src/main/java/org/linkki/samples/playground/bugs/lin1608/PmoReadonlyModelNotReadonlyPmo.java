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

package org.linkki.samples.playground.bugs.lin1608;

import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "LIN-1608")
public class PmoReadonlyModelNotReadonlyPmo {

    public static final String ID_TEXT = "text";

    public static final String ID_TEXT_WITH_MODEL_ATTRIBUTE = "textWithModelAttribute";

    public static final String ID_TEXT_WITH_MODEL_BINDING = "textWithModelBinding";

    @ModelObject
    private StringPropertyModelObject modelObject = new StringPropertyModelObject();

    @UILabel(position = 0)
    public String getDescription() {
        return "First two text fields have a matching getter in PMO but only a setter in model object, they should be readonly."
                + " Third fields uses direct model binding and should be writable.";
    }

    @UITextField(position = 5, label = "Implicit matching model attribute")
    public String getText() {
        return modelObject.getText();
    }

    @UITextField(position = 10, label = "Explicitly defined modelAttribute", modelAttribute = StringPropertyModelObject.PROPERTY_TEXT)
    public String getTextWithModelAttribute() {
        return modelObject.getText();
    }

    @UITextField(position = 15, label = "Direct Model Binding", modelAttribute = StringPropertyModelObject.PROPERTY_TEXT)
    public String textWithModelBinding() {
        return modelObject.getText();
    }

    public static class StringPropertyModelObject {

        private static final String PROPERTY_TEXT = "text";

        private String text = "text";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}

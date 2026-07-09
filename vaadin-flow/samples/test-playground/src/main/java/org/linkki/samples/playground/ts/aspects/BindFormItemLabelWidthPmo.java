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

import org.linkki.core.ui.aspects.annotation.BindFormItemLabelWidth;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

/**
 * Test PMOs in order to test {@link BindFormItemLabelWidth @BindFormItemLabelWidth}.
 */
@UISection(layout = SectionLayout.VERTICAL)
public class BindFormItemLabelWidthPmo {

    @UINestedComponent(position = 10)
    public ExtendedLabelWidthPmo getExtendedLabelWidthPmo() {
        return new ExtendedLabelWidthPmo();
    }

    @UINestedComponent(position = 20)
    public DefaultPmo getDefaultPmo() {
        return new DefaultPmo();
    }

    @BindFormItemLabelWidth("40em")
    @UISection(caption = "Pmo with extended label width")
    public static class ExtendedLabelWidthPmo {

        private String text = "Sample text";

        @UITextField(position = 10, label = "This is a very long label that exceeds the standard label width")
        public String getWidth() {
            return text;
        }

        public void setWidth(String text) {
            this.text = text;
        }
    }

    @UISection(caption = "Pmo with standard label width")
    public static class DefaultPmo {

        private String text = "Sample text";

        @UITextField(position = 10, label = "This is a very long label that exceeds the standard label width")
        public String getWidth() {
            return text;
        }

        public void setWidth(String text) {
            this.text = text;
        }
    }

}

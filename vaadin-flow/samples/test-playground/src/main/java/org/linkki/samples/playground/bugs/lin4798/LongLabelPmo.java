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

package org.linkki.samples.playground.bugs.lin4798;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.util.HtmlContent;

@UISection(caption = LongLabelPmo.CAPTION)
public class LongLabelPmo {

    public static final String LIN_4798 = "LIN-4798";
    public static final String CAPTION = LIN_4798 + " :: Long label overflow";
    public static final String LONG_LABEL = "TextFieldWithALongExtendedLabel toTestLabelOverflowBehavior";

    @UILabel(position = 10, label = "Bug description")
    public HtmlContent getDescription() {
        return HtmlContent
                .text("""
                        Long labels in vaadin-form-items did not abbreviate with ".." but wrapped after \
                        migration to Vaadin 25.
                        """);
    }

    @UITextField(position = 20, label = LONG_LABEL)
    public String getValue() {
        return "";
    }
}

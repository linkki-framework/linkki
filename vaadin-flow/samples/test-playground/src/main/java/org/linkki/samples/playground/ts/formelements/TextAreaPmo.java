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

package org.linkki.samples.playground.ts.formelements;

import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class TextAreaPmo {

    private String content;

    @UITextArea(position = 0, label = "@UITextArea", height = "5em")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

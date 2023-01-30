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

package org.linkki.samples.playground.bugs.lin3319;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "LIN-3319 :: Sanitize HTML content")
public class HtmlLabelPmo {

    @UILabel(position = 10, label = "HTML label with sanitized script which would open an alert", htmlContent = true)
    public String getHtmlWithSanitizedContentLabel() {
        return "<b>This should be bold text (alert should not be shown on reloading the page)<iframe " +
                "onload=\"alert('LIN-3319 - This should not be shown!');\"/></b>";
    }

    @UILabel(position = 15, label = "Label with not whitelisted HTML tag <iframe> displayed as plain text")
    public String getTextWithHtmlContentLabel() {
        return "<iframe>Text</iframe>";
    }
}
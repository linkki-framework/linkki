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

package org.linkki.samples.playground.ts.components;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ips.model.Marker;

@UISection
public class LabelPmo {

    @UILabel(position = 10, label = "Label with HTML Content", htmlContent = true)
    public String getHtmlContentLabel() {
        return "<i style=\"color: red;\">HTML</i> <b>Content</b>";
    }

    @UILabel(position = 20, label = "Label without HTML Content")
    public String getNotHtmlContentLabel() {
        return "<b>NOT</b> HTML Content";
    }

    @UILabel(position = 50, label = "Label with a BigDecimal as Value")
    public BigDecimal getBigDecimalLabel() {
        return BigDecimal.valueOf(12345.6789);
    }

    /* demonstration only, unit tested */
    @UILabel(position = 60, label = "Label with a LocalDate as Value")
    public LocalDate getLocalDateLabel() {
        return LocalDate.of(4321, 5, 6);
    }

    /* demonstration only, unit tested */
    @UILabel(position = 70, label = "Label with a LocalDateTime as Value")
    public LocalDateTime getLocalDateTimeLabel() {
        return LocalDateTime.of(3456, 1, 2, 7, 8, 9);
    }

    /* demonstration only, unit tested */
    @UILabel(position = 80, label = "Label with a enum as Value")
    public RoundingMode getEnumLabel() {
        return RoundingMode.DOWN;
    }

    /* demonstration only, unit tested */
    @UILabel(position = 90, label = "Label with a enum with getName() as Value")
    public Marker getNamedEnumLabel() {
        return Marker.REQUIRED_INFORMATION_MISSING;
    }
}

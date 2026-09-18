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

import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.aspects.annotation.BindBadgeSeverity;
import org.linkki.core.ui.element.annotation.UIBadge;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.BadgeSeverity;

@UISection(caption = "BindBadgeSeverity", layout = SectionLayout.VERTICAL)
public class BindBadgeSeverityPmo {

    private BadgeSeverity badgeSeverity = BadgeSeverity.INFO;

    // tag::bindBadgeSeverity[]
    @BindBadgeSeverity
    @UIBadge(position = 10)
    public String getDynamicSeverityBadge() {
        return "Dynamic Severity Badge";
    }

    public BadgeSeverity getDynamicSeverityBadgeBadgeSeverity() {
        return badgeSeverity;
    }
    // end::bindBadgeSeverity[]

    @UIComboBox(position = 15, label = "Severity", content = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
    public BadgeSeverity getBadgeSeverity() {
        return badgeSeverity;
    }

    public void setBadgeSeverity(BadgeSeverity badgeSeverity) {
        this.badgeSeverity = badgeSeverity;
    }
}

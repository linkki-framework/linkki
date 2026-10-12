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

import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UIBadge;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.BadgeSeverity;
import org.linkki.core.vaadin.component.BadgeStyleVariant;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class BadgePmo {

    @UIBadge(position = 0, label = "Default Badge")
    public String getDefaultBadge() {
        return "Badge";
    }

    @UIBadge(position = 10, label = "Info Badge", severity = BadgeSeverity.INFO)
    public String getInfoBadge() {
        return "Badge";
    }

    @UIBadge(position = 20, label = "Success Badge", severity = BadgeSeverity.SUCCESS)
    public String getSuccessBadge() {
        return "Badge";
    }

    @UIBadge(position = 30, label = "Warning Badge", severity = BadgeSeverity.WARNING)
    public String getWarningBadge() {
        return "Badge";
    }

    @UIBadge(position = 40, label = "Error Badge", severity = BadgeSeverity.ERROR)
    public String getErrorBadge() {
        return "Badge";
    }

    @BindIcon(VaadinIcon.LOCK)
    @UIBadge(position = 50, label = "Badge with icon and custom styles",
            styles = {BadgeStyleVariant.SMALL, BadgeStyleVariant.FILLED})
    public String getBadgeWithIcon() {
        return "Badge";
    }

    @UIBadge(position = 60, label = "Square Badge", severity = BadgeSeverity.SUCCESS,
            styles = BadgeStyleVariant.SQUARE)
    public String getSquareBadge() {
        return "Badge";
    }

    @UIBadge(position = 70, label = "Dot Badge", severity = BadgeSeverity.ERROR, styles = BadgeStyleVariant.DOT)
    public String getDotBadge() {
        return "Badge";
    }

}

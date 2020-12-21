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

package org.linkki.samples.playground.allelements;

import com.vaadin.flow.component.icon.VaadinIcon;

public enum Direction {
    UP(VaadinIcon.ARROW_CIRCLE_UP),
    DOWN(VaadinIcon.ARROW_CIRCLE_DOWN),
    LEFT(VaadinIcon.ARROW_CIRCLE_LEFT),
    RIGHT(VaadinIcon.ARROW_CIRCLE_RIGHT);

    private final VaadinIcon icon;

    private Direction(VaadinIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name();
    }

    public VaadinIcon getIcon() {
        return icon;
    }
}
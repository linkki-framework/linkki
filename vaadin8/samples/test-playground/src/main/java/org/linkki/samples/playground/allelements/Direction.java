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

import com.vaadin.icons.VaadinIcons;

public enum Direction {
    UP(VaadinIcons.ARROW_CIRCLE_UP),
    DOWN(VaadinIcons.ARROW_CIRCLE_DOWN),
    LEFT(VaadinIcons.ARROW_CIRCLE_LEFT),
    RIGHT(VaadinIcons.ARROW_CIRCLE_RIGHT);

    private final VaadinIcons icon;

    private Direction(VaadinIcons icon) {
        this.icon = icon;
    }

    public String getName() {
        return name();
    }

    public VaadinIcons getIcon() {
        return icon;
    }
}
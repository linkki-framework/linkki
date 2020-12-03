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

package org.linkki.core.ui.layout;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

/**
 * Defines how the child components should be horizontally aligned in the parent component.
 */
// TODO LIN-2058
public enum HorizontalAlignment {
    LEFT(Alignment.START),
    MIDDLE(Alignment.CENTER),
    RIGHT(Alignment.END);

    private final Alignment alignment;

    private HorizontalAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Alignment getAlignment() {
        return alignment;
    }
}

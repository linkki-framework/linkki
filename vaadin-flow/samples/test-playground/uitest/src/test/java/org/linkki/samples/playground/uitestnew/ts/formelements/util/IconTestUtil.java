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

package org.linkki.samples.playground.uitestnew.ts.formelements.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.testbench.pageobjects.LinkkiTextElement;

/**
 * Helper for verifying icon specific aspects.
 */
public class IconTestUtil {

    /**
     * Verifies the {@link IconPosition icon position}.
     *
     * @param element The {@link LinkkiTextElement text element} which contains the icon
     * @param iconPosition The position of the icon - left and right from the element is possible
     */
    public static void verifyIconPosition(LinkkiTextElement element, IconPosition iconPosition) {
        String iconSlot = IconPosition.RIGHT == iconPosition ? "suffix" : "prefix";

        assertThat(element).isNotNull();
        assertThat(element.hasIcon()).isTrue();
        assertThat(element.getIcon().getDomAttribute("slot")).isEqualTo(iconSlot);
    }
}

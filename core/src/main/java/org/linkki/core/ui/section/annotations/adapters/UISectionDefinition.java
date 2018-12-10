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

package org.linkki.core.ui.section.annotations.adapters;


import org.linkki.core.ui.section.annotations.UISection;

public enum UISectionDefinition {
    /* singleton */;

    @SuppressWarnings("null")
    public static UISection from(Object pmo) {

        UISection sectionDefinition = pmo.getClass().getAnnotation(UISection.class);

        if (sectionDefinition == null) {
            sectionDefinition = DefaultUISection.class.getAnnotation(UISection.class);
        }

        return sectionDefinition;
    }

    @UISection
    private static final class DefaultUISection {
        // Default UISection class if there is no @UISection annotation.
        // This way, the default values from the @UISection annotation get used.
    }
}

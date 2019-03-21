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

package org.linkki.samples.binding.pmo;

import java.util.Locale;

import org.linkki.core.defaults.uielement.ItemCaptionProvider;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.samples.binding.model.Contact.Gender;

public class GenderCaptionProvider implements ItemCaptionProvider<Gender> {

    @Override
    public String getCaption(Gender gender) {
        Locale locale = UiFramework.getLocale();
        if (Locale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
            switch (gender) {
                case FEMALE:
                    return "female";
                case MALE:
                    return "male";
                case NON_BINARY:
                    return "non-binary";
            }
        } else if (Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
            switch (gender) {
                case FEMALE:
                    return "weiblich";
                case MALE:
                    return "männlich";
                case NON_BINARY:
                    return "divers";
            }
        }
        return gender.name();
    }
}

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

package org.linkki.samples.playground.ts.aspects;

import org.linkki.core.ui.aspects.annotation.BindSuffix;
import org.linkki.core.ui.aspects.types.SuffixType;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindSuffixPmo {

    private String suffix = "I can be changed";

    @UITextField(position = 10, label = "Suffix")
    public String getSuffixText() {
        return suffix;
    }

    public void setSuffixText(String suffix) {
        this.suffix = suffix;
    }

    @UITextField(position = 20, label = "Dynamic Suffix")
    @BindSuffix(suffixType = SuffixType.DYNAMIC)
    public String getSuffixDynamicText() {
        return "This field has a dynamic suffix that changes dynamically";
    }

    public String getSuffixDynamicTextSuffix() {
        return suffix;
    }

    @UITextField(position = 22, label = "Static Suffix")
    @BindSuffix(value = "A nice static suffix", suffixType = SuffixType.STATIC)
    public String getSuffixStaticText() {
        return "This field has a static suffix that cannot be changed";
    }

}
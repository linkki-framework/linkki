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

package org.linkki.samples.playground.bugs.lin1797;

import java.util.Collections;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.bugs.lin1797.SectionTablePmo.SectionRowPmo;

@BindCaption(SectionTablePmo.CAPTION)
@UISection
public class SectionTablePmo implements ContainerPmo<SectionRowPmo> {

    public static final String CAPTION = OnlyTablePmo.LIN_1797 + " :: @BindCaption on Table with @UISection";
    private static final List<SectionRowPmo> ROW = Collections.singletonList(new SectionRowPmo());

    @Override
    public List<SectionRowPmo> getItems() {
        return ROW;
    }

    @Override
    public int getPageLength() {
        return 1;
    }

    public static class SectionRowPmo {

        @UILabel(position = 0)
        public String getValue() {
            return "foo";
        }
    }
}

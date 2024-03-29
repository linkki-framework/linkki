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

package org.linkki.samples.playground.bugs.lin1797;

import java.util.Collections;
import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.samples.playground.bugs.lin1797.OnlyTablePmo.OnlyTableRowPmo;

@BindCaption(OnlyTablePmo.CAPTION)
public class OnlyTablePmo implements ContainerPmo<OnlyTableRowPmo> {

    public static final String LIN_1797 = "LIN-1797";
    public static final String CAPTION = LIN_1797 + " :: @BindCaption on Table without @UISection";
    private static final List<OnlyTableRowPmo> ROW = Collections.singletonList(new OnlyTableRowPmo());

    @Override
    public List<OnlyTableRowPmo> getItems() {
        return ROW;
    }

    @Override
    public int getPageLength() {
        return 1;
    }

    public static class OnlyTableRowPmo {

        @UILabel(position = 0)
        public String getValue() {
            return "bar";
        }
    }
}

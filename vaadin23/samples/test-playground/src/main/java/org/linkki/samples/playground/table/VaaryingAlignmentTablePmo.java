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

package org.linkki.samples.playground.table;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TableFooterPmo;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection(caption = "Columns with vaarying alignment")
public class VaaryingAlignmentTablePmo implements ContainerPmo<VaaryingAlignmentRowPmo> {

    private final List<VaaryingAlignmentRowPmo> items = Arrays.asList(new VaaryingAlignmentRowPmo());

    @Override
    public List<VaaryingAlignmentRowPmo> getItems() {
        return items;
    }

    @Override
    public int getPageLength() {
        return 0;
    }

    @Override
    public Optional<TableFooterPmo> getFooterPmo() {
        return Optional.of($ -> "footer");
    }

}

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

package org.linkki.samples.playground.ts.section;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.samples.playground.ts.section.GridSectionLayoutPmo.GridSectionLayoutRowPmo;

@UISection(caption = "GridSection")
public class GridSectionLayoutPmo implements ContainerPmo<GridSectionLayoutRowPmo> {

    @Override
    public List<GridSectionLayoutRowPmo> getItems() {
        return IntStream.range(0, 50).boxed().map(i -> new GridSectionLayoutRowPmo()).collect(Collectors.toList());
    }

    public static class GridSectionLayoutRowPmo {

        private String label2 = "label2";

        @UILabel(position = 0)
        public String getLabel1() {
            return "label1";
        }

        @UITextField(position = 1)
        public String getLabel2() {
            return label2;
        }

        public void setLabel2(String label2) {
            this.label2 = label2;
        }
    }
}

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

package org.linkki.samples.playground.ts.aspects;

import java.util.List;

import org.linkki.core.ui.aspects.annotation.BindHeight;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class BindHeightPmo {

    @BindHeight
    @UITextField(position = 0, label = "First Component")
    public String getFirstComponent() {
        return "Ich habe die Default-Höhe";
    }

    @BindHeight("100px")
    @UITextField(position = 10, label = "Second Component")
    public String getSecondComponent() {
        return "Ich habe eine Höhe von 100px";
    }

    @BindHeight("200px")
    @UITextField(position = 20, label = "Third Component")
    public String getThirdComponent() {
        return "Ich habe eine Höhe von 200px";
    }

    @BindHeight()
    @UITextField(position = 30, label = "Fourth Component")
    public String getFourthComponent() {
        return "Ich habe die Default-Höhe";
    }

    @BindHeight("200px")
    @UITableComponent(position = 40, rowPmoClass = TestRowPmo.class)
    public List<TestRowPmo> getRows() {
        return List.of(new TestRowPmo(), new TestRowPmo(), new TestRowPmo());
    }

    public static class TestRowPmo {

        @UILabel(position = 0, label = "First Column")
        public String getFirstLabel() {
            return "First Label";
        }

        @UILabel(position = 10, label = "Second Column")
        public String getSecondLabel() {
            return "Second Label";
        }

        @UILabel(position = 20, label = "Third Column")
        public String getThirdLabel() {
            return "Third Label";
        }
    }
}

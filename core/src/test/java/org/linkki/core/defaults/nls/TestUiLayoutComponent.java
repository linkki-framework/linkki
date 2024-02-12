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

package org.linkki.core.defaults.nls;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestUiLayoutComponent extends TestUiComponent {

    private List<TestUiComponent> children = new LinkedList<>();

    public TestUiLayoutComponent(TestUiComponent... children) {
        this.children.addAll(Arrays.asList(children));
    }

    public List<TestUiComponent> getChildren() {
        return children;
    }

    public void addChild(TestUiComponent child) {
        this.children.add(child);
    }

}
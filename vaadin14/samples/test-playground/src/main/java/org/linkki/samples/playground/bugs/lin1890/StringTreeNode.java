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

package org.linkki.samples.playground.bugs.lin1890;

import java.util.ArrayList;
import java.util.List;

public class StringTreeNode {

    private final List<StringTreeNode> children = new ArrayList<StringTreeNode>();
    private final String value;

    public StringTreeNode(String value, List<StringTreeNode> children) {
        this.value = value;
        this.children.addAll(children);
    }

    public List<StringTreeNode> getChildren() {
        return children;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}

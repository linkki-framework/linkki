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

package org.linkki.samples.playground.ts.components;

import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class IntegerFieldPmo {

    private int primitiveInteger;
    private Integer boxedInteger;

    @UIIntegerField(position = 0, label = "int")
    public int getPrimitiveInteger() {
        return primitiveInteger;
    }

    public void setPrimitiveInteger(int primitiveInteger) {
        this.primitiveInteger = primitiveInteger;
    }

    @UIIntegerField(position = 1, label = "Integer")
    public Integer getBoxedInteger() {
        return boxedInteger;
    }

    public void setBoxedInteger(Integer boxedInteger) {
        this.boxedInteger = boxedInteger;
    }

}

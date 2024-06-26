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
package org.linkki.samples.playground.ts.components;

import org.linkki.core.ui.element.annotation.UILongField;
import org.linkki.core.ui.layout.annotation.UISection;

@UISection
public class LongFieldPmo {

    private long primitiveLong;
    private Long boxedLong;
    private Long defaultFormattedLong = 12345678910L;
    private Long userFormattedLong = 12345678910L;

    @UILongField(position = 0, label = "long")
    public long getPrimitiveLong() {
        return primitiveLong;
    }

    public void setPrimitiveLong(long primitiveLong) {
        this.primitiveLong = primitiveLong;
    }

    @UILongField(position = 1, label = "Long")
    public Long getBoxedLong() {
        return boxedLong;
    }

    public void setBoxedLong(Long boxedLong) {
        this.boxedLong = boxedLong;
    }

    @UILongField(position = 2, label = "Default format")
    public Long getDefaultFormattedLong() {
        return defaultFormattedLong;
    }

    public void setDefaultFormattedLong(Long defaultFormattedLong) {
        this.defaultFormattedLong = defaultFormattedLong;
    }

    @UILongField(position = 3, label = "User-defined format", format = "###")
    public Long getUserFormattedLong() {
        return userFormattedLong;
    }

    public void setUserFormattedLong(Long userFormattedLong) {
        this.userFormattedLong = userFormattedLong;
    }
}

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

package org.linkki.core.ui.table.hierarchy;

import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UITextField;

public abstract class AbstractCodeRow {

    public static final String PROPERTY_UPPER_CASE_LETTER = "upperCaseLetter";
    public static final String PROPERTY_LOWER_CASE_LETTER = "lowerCaseLetter";
    public static final String PROPERTY_NUMBER = "number";

    @UITextField(position = 10, label = "Upper")
    public abstract String getUpperCaseLetter();

    @UITextField(position = 20, label = "Lower")
    public abstract String getLowerCaseLetter();

    @UIIntegerField(position = 30, label = "Number")
    public abstract int getNumber();

    @Override
    public String toString() {
        return getClass().getSimpleName();// + "@" + Integer.toHexString(hashCode());
    }

}

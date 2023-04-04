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

package org.linkki.core.ui.table.hierarchy;

public class Code {

    private String upperCaseLetter;
    private String lowerCaseLetter;
    private int number;

    public Code(String upperCaseLetter, String lowerCaseLetter, int number) {
        this.upperCaseLetter = upperCaseLetter;
        this.lowerCaseLetter = lowerCaseLetter;
        this.number = number;
    }

    public String getUpperCaseLetter() {
        return upperCaseLetter;
    }

    public void setUpperCaseLetter(String upperCaseLetter) {
        this.upperCaseLetter = upperCaseLetter;
    }

    public String getLowerCaseLetter() {
        return lowerCaseLetter;
    }

    public void setLowerCaseLetter(String lowerCaseLetter) {
        this.lowerCaseLetter = lowerCaseLetter;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}

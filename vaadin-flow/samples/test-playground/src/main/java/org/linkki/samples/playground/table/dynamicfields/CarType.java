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
package org.linkki.samples.playground.table.dynamicfields;

public enum CarType {

    STANDARD,
    PREMIUM;

    /**
     * Method is called by linkki for presentation.
     *
     * @return captialized name of the enum value
     */
    public String getName() {
        String name = name();
        return name.substring(0, 1) + name.substring(1).toLowerCase();
    }
}

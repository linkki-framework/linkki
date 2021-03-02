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
package org.linkki.samples.playground.binding.model;

public final class Country {

    private final String isoA3;
    private final String name;

    public Country(String isoA3, String name) {
        this.isoA3 = isoA3;
        this.name = name;
    }

    public String getIsoA3() {
        return isoA3;
    }

    public String getName() {
        return name;
    }
}

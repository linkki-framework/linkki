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

package org.linkki.samples.treetable.fixed.pmo;

import java.util.List;

public class CityRowPmo extends SummarizingPersonRowPmo {

    private String city;

    public CityRowPmo(String city, List<PersonRowPmo> personRows) {
        super(personRows);
        this.city = city;
    }

    @Override
    public String getAddress() {
        return city + " (" + getChildRows().size() + ")";
    }

}
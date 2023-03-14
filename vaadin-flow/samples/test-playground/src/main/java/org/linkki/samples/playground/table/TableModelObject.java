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

package org.linkki.samples.playground.table;

import java.time.LocalDate;

public class TableModelObject {

    public static final String PROPERTY_INDEX = "index";

    public static final String PROPERTY_ACTIVE = "active";

    public static final String PROPERTY_NAME = "name";

    public static final String PROPERTY_OPTION = "option";

    public static final String PROPERTY_DATE = "date";

    public static final String PROPERTY_DATE_TIME = "dateTime";

    private int index;

    private boolean active = true;

    private String name;

    private Option option;

    private LocalDate date;

    public TableModelObject(int i) {
        this.index = i;
        this.name = "Name " + i;
        this.option = Option.values()[i % Option.values().length];
        this.date = LocalDate.now().plusDays(i);
    }

    public int getIndex() {
        return index;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean selected) {
        this.active = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static enum Option {

        OPTION1,
        OPTION2,
        OPTION3;

    }

}

package org.linkki.tooling.apt.test;
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

import java.util.concurrent.atomic.AtomicInteger;

public class Report {
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_FLAG = "flag";

    private static AtomicInteger idGenerator = new AtomicInteger(0);

    private Integer id;

    private String description;
    private ReportType type;

    private Boolean flag;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Boolean getFlag() {
        return flag;
    }


    public void save() {
        if (id == null) {
            id = idGenerator.incrementAndGet();
        }
    }
}

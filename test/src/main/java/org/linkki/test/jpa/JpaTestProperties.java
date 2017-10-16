/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.test.jpa;

import java.util.HashMap;
import java.util.Map;

public class JpaTestProperties {

    /**
     * Utility class that should not be instantiated.
     */
    private JpaTestProperties() {
        super();
    }

    /**
     * Returns a map with properties suitable for JPA tests using a H2 in-memory database.
     * 
     * @param databaseName the name of the database to use in the properties
     * @return properties suitable for JPA tests
     */
    public static Map<String, String> properties(String databaseName) {
        Map<String, String> properties = new HashMap<>();
        properties.put(PersistenceProperties.JPA_NON_JTA_DATA_SOURCE, null);
        properties.put(PersistenceProperties.JPA_JTA_DATA_SOURCE, null);
        properties.put(PersistenceProperties.JPA_TRANSACTION_TYPE, "RESOURCE_LOCAL");
        properties.put(PersistenceProperties.JPA_JDBC_URL, "jdbc:h2:mem:" + databaseName + ";DB_CLOSE_DELAY=-1");
        properties.put(PersistenceProperties.JPA_JDBC_DRIVER, "org.h2.Driver");
        properties.put(PersistenceProperties.JPA_JDBC_USER, "sa");
        properties.put(PersistenceProperties.JPA_SCHEMA_GENERATION_DATABASE_ACTION, "drop-and-create");
        properties.put(PersistenceProperties.ECLIPSELINK_LOGGING_LEVEL, "OFF");
        return properties;
    }

}

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

public final class PersistenceProperties {

    public static final String ECLIPSELINK_PERSISTENCE_XML = "eclipselink.persistencexml";
    public static final String ECLIPSELINK_LOGGING_LEVEL = "eclipselink.logging.level";

    public static final String JPA_JDBC_USER = "javax.persistence.jdbc.user";
    public static final String JPA_JDBC_DRIVER = "javax.persistence.jdbc.driver";
    public static final String JPA_JDBC_URL = "javax.persistence.jdbc.url";
    public static final String JPA_TRANSACTION_TYPE = "javax.persistence.transactionType";
    public static final String JPA_JTA_DATA_SOURCE = "javax.persistence.jtaDataSource";
    public static final String JPA_NON_JTA_DATA_SOURCE = "javax.persistence.nonJtaDataSource";
    public static final String JPA_SCHEMA_GENERATION_DATABASE_ACTION = "javax.persistence.schema-generation.database.action";

    // Should not be instantiated
    private PersistenceProperties() {
        super();
    }

}

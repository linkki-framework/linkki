/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package de.faktorzehn.ipm.test.jpa;

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

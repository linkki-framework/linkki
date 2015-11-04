package org.linkki.test.jpa;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder for database properties for H2 test databases.
 */
public class H2TestDatabasePropertyBuilder {

    private final Map<String, String> dbProperties = new HashMap<String, String>();

    private H2TestDatabasePropertyBuilder() {
        super();
        dbProperties.put(PersistenceProperties.ECLIPSELINK_PERSISTENCE_XML, "META-INF/test-persistence.xml");
        dbProperties.put(PersistenceProperties.ECLIPSELINK_LOGGING_LEVEL, "OFF");
    }

    public static H2TestDatabasePropertyBuilder dbProperties() {
        return new H2TestDatabasePropertyBuilder();
    }

    public H2TestDatabasePropertyBuilder forDatabase(String name) {
        dbProperties.put(PersistenceProperties.JPA_JDBC_URL, "jdbc:h2:mem:" + name
                + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        return this;
    }

    public H2TestDatabasePropertyBuilder createTables(boolean createTables) {
        if (createTables) {
            dbProperties.put(PersistenceProperties.JPA_SCHEMA_GENERATION_DATABASE_ACTION, "drop-and-create");
        }
        return this;
    }

    public Map<String, String> build() {
        return Collections.unmodifiableMap(dbProperties);
    }

}
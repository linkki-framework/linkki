package org.linkki.samples.gettingstarted.model;

import java.util.concurrent.atomic.AtomicInteger;

// tag::report[]
public class Report {

    // end::report[]
    private static AtomicInteger idGenerator = new AtomicInteger(0);

    // tag::report[]
    private Integer id;

    private String description;
    private ReportType type;
    // end::report[]


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


    public void save() {
        if (id == null) {
            id = idGenerator.incrementAndGet();
        }
    }
}

package org.linkki.samples.gettingstarted.model;

// tag::report-type[]
public enum ReportType {

    BUG,
    IMPROVEMENT,
    QUESTION;

    // end::report-type[]


    /**
     * This method is called from linkki to display a caption for the enum value.
     * 
     * @return the representation for the enum value
     */
    // tag::report-type[]
    public String getName() {
        String name = name();
        return name.substring(0, 1) + name.substring(1).toLowerCase();
    }
}
// end::report-type[]
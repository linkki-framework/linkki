package org.linkki.core.vaadin.component.shared;

import com.vaadin.shared.ui.datefield.LocalDateFieldState;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Shared state for MultiformatDateField
 */
public class MultiformatDateFieldState extends LocalDateFieldState {

    private static final long serialVersionUID = 1L;

    private String[] alternativeFormats = {};

    public MultiformatDateFieldState() {

    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "this is just used to pass data between client and server")
    public String[] getAlternativeFormats() {
        return alternativeFormats;
    }

    public void setAlternativeFormats(String[] alternativeFormats) {
        this.alternativeFormats = alternativeFormats;
    }

}
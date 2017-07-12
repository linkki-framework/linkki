package org.linkki.doc;

//tag::class[]
public class BeispielPmo {

    private String nachname;
    private String anrede;

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getNamensanzeige() {
        return String.join(" ", anrede, nachname);
    }
}
//end::class[]

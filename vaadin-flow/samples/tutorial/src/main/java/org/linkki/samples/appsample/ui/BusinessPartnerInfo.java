package org.linkki.samples.appsample.ui;

import java.time.Year;

import org.linkki.framework.ui.application.ApplicationInfo;

public class BusinessPartnerInfo implements ApplicationInfo {

    public static final String APPLICATION_NAME = "BusinessPartner";

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public String getApplicationVersion() {
        return "1.0";
    }

    @Override
    public String getApplicationDescription() {
        return "BusinessPartner";
    }

    @Override
    public String getCopyright() {
        return "© Faktor Zehn " + Year.now();
    }
}
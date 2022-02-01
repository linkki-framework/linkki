#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.ui;

import java.time.Year;

import org.linkki.framework.ui.application.ApplicationInfo;

public class ${ApplicationName}Info implements ApplicationInfo {

    public static final String APPLICATION_NAME = "Training Sample Application";

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
        return "Faktor Zehn linkki Training Sample Application";
    }

    @Override
    public String getCopyright() {
        return "© Faktor Zehn " + Year.now();
    }
}
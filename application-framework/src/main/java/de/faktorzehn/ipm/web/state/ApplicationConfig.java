package de.faktorzehn.ipm.web.state;

/**
 * Application configuration parameters.
 */
public interface ApplicationConfig {

    public static final boolean DEBUG = false;

    public String getApplicationName();

    public String getApplicationVersion();

    public String getCopyright();

}

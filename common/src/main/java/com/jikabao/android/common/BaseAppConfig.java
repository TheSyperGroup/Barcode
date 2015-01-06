package com.jikabao.android.common;

/**
 * Implement this interface to provide build time configuration details.
 */
public interface BaseAppConfig {

    boolean isDebug();

    boolean isUsingSdcard();

    /*
     * set true to prevent GA's hits from being sent to reports
     */
    boolean isDryRun();

    /**
     * Revision string for displaying in the 'About' view.
     */
    String getAppRevision();

    /**
     * Host name of the server.
     */
    String getHostname();
}

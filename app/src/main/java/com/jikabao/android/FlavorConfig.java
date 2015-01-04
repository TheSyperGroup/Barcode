package com.jikabao.android;

/**
 * Implement this interface to provide flavor configuration details.
 */
public interface FlavorConfig {

    public boolean isFlavor(String flavor);

    public String flavorName();

    public String registerUrl();
}

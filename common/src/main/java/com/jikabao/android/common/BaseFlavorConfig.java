package com.jikabao.android.common;

/**
 * Implement this interface to provide flavor configuration details.
 */
public interface BaseFlavorConfig {

    public boolean isFlavor(String flavor);

    public String flavorName();

    public String registerUrl();
}

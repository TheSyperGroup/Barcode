package com.jikabao.android;

import android.content.Context;

/**
 * Configuration related to the product flavor jikabao
 */
public enum ProductConfig implements FlavorConfig {

    INSTANCE;

    public static final String PRODUCT_FLAVOR_NAME = "jikabao";

    public void initialize(Context appContext) {
        // Do nothing for this product
    }

    @Override
    public boolean isFlavor(String flavor) {
        return PRODUCT_FLAVOR_NAME.equalsIgnoreCase(flavor);
    }

    @Override
    public String flavorName() {
        return PRODUCT_FLAVOR_NAME;
    }

    @Override
    public String registerUrl() {
        return "http://www.jikabao.com";
    }
}

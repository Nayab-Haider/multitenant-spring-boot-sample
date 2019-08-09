package com.sample.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContextHolder {

    private static Logger logger = LoggerFactory.getLogger(TenantContextHolder.class);


    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        logger.debug("Setting tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.set(null);
    }
}

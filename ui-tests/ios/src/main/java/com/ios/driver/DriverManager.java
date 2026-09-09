package com.ios.driver;

import io.appium.java_client.ios.IOSDriver;

/**
 * Holds one driver per thread so TestNG can run suites in parallel without
 * tests stealing each other's session.
 */
public final class DriverManager {

    private static final ThreadLocal<IOSDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static IOSDriver get() {
        IOSDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "No driver for thread '" + Thread.currentThread().getName()
                            + "'. Did the test extend BaseTest?");
        }
        return driver;
    }

    /** Null-safe variant for listeners that run after teardown. */
    public static IOSDriver getOrNull() {
        return DRIVER.get();
    }

    public static void set(IOSDriver driver) {
        DRIVER.set(driver);
    }

    public static void quit() {
        IOSDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}

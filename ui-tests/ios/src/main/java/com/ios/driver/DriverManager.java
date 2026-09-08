package com.ios.driver;

import io.appium.java_client.ios.IOSDriver;

public final class DriverManager {
    private static final ThreadLocal<IOSDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static IOSDriver get() {
        IOSDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("Driver not initialised for this thread");
        return driver;
    }

    public static void set(IOSDriver driver) { DRIVER.set(driver); }

    public static void quit() {
        IOSDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
    
}

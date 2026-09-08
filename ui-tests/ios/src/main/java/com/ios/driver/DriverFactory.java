package com.ios.driver;

import java.net.URI;
import java.time.Duration;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

public class DriverFactory {

    public static IOSDriver create(TestConfig cfg) throws Exception {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformVersion(cfg.platformVersion())   // "18.2"
                .setDeviceName(cfg.deviceName())             // "iPhone 16 Pro"
                .setAutomationName("XCUITest")
                .setApp(cfg.appPath())
                .setWdaLaunchTimeout(Duration.ofMinutes(4))
                .setNewCommandTimeout(Duration.ofSeconds(120))
                .setNoReset(false);

        if (cfg.isRealDevice()) {
            options.setUdid(cfg.udid())
                   .setXcodeOrgId(cfg.teamId())
                   .setXcodeSigningId("iPhone Developer")
                   .setWdaLocalPort(cfg.wdaPort());  // unique per parallel thread
        }

        IOSDriver driver = new IOSDriver(new URI(cfg.serverUrl()).toURL(), options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO); // use explicit waits only
        return driver;
    }
    
}

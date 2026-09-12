package com.ios.tests;

import java.net.URI;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

public class RawTest {

    private IOSDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        XCUITestOptions options = new XCUITestOptions()
            .setUdid("1B31CB41-715F-4768-A561-FD9C6D0B91D3")
            .setApp("/Users/Rise5599/Desktop/LINEPlanetCall.app")
            .autoAcceptAlerts();

        driver = new IOSDriver(new URI("http://127.0.0.1:4723").toURL(), options);
    }

    //@Test
    public void tapsElement() {
        WebElement el = driver.findElement(AppiumBy.xpath("//XCUIElementTypeImage[@name=\"gearshape\"]"));
        el.click();
        System.out.println(driver.getPageSource());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

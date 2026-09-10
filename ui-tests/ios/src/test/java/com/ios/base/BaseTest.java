package com.ios.base;

import com.ios.config.ConfigReader;
import com.ios.driver.DriverFactory;
import com.ios.driver.DriverManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {

	@BeforeClass(alwaysRun = true)
	public void startDriver() {
		DriverManager.set(DriverFactory.create(ConfigReader.load()));
	}

	@AfterClass(alwaysRun = true)
	public void stopDriver() {
		DriverManager.quit();
	}
}

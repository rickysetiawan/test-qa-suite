package com.ios.pages;

import org.openqa.selenium.WebElement;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class HomePage extends BasePage {

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"LINE Planet Call\"]")
    private WebElement HomePageTitle;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"1:1 Call\"]")
    private WebElement oneToOneCallButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage[@name=\"gearshape\"]")
    private WebElement configButton;

    @Override
    public boolean isLoaded() {
        return isVisible(HomePageTitle) && isVisible(oneToOneCallButton) && isVisible(configButton);
    }
    
    public void tapConfigButton() {
        configButton.click();
    }


    //XCUIElementTypeButton[@name="Basic Call"]
    //XCUIElementTypeButton[@name="chevron.left"]
    //XCUIElementTypeStaticText[@name="1:1 Call"]
    //XCUIElementTypeButton[@name="house"]

    //XCUIElementTypeTextField[@value="Input Peer id (User id)"]
    //XCUIElementTypeButton[@name="Audio Call"]
    //XCUIElementTypeButton[@name="Video Call"]
    //XCUIElementTypeStaticText[@name="Start Fail"]
    //XCUIElementTypeButton[@name="OK"]*/
}


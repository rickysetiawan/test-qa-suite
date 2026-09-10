package com.ios.pages;

import org.openqa.selenium.WebElement;

import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class HomePage extends BasePage {

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"LINE Planet Call\"]")
    private WebElement HomePageTitle;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"1:1 Call\"]")
    private WebElement oneToOneCallButton;

    @Override
    public boolean isLoaded() {
        return isVisible(HomePageTitle);
    }

    /*@iOSXCUITFindBy(accessibility = "login_submit_button")
    private WebElement submitButton;

    @iOSXCUITFindBy(iOSNsPredicate = "type == 'XCUIElementTypeStaticText' AND name == 'login_error_message'")
    private WebElement errorBanner;

    public String errorMessage() {
        return isVisible(errorBanner, 5) ? errorBanner.getText() : "";
    }
    
    //XCUIElementTypeImage[@name="gearshape"]
    //XCUIElementTypeButton[@name="1:1 Call"]
    //XCUIElementTypeButton[@name="Group Call"]
    //XCUIElementTypeStaticText[@name="Setting"]

    //XCUIElementTypeTextField[@value="Set your name"]
    //XCUIElementTypeTextField[@value="Set your user id"]
    //XCUIElementTypeButton[@name="Save"]
    //XCUIElementTypeButton[@name="RESET NOW"]
    //XCUIElementTypeButton[@name="chevron.left"]


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


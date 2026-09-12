package com.ios.pages;

import org.openqa.selenium.WebElement;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Setting\"]")
    private WebElement LoginPageTitle;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@value=\"Set your name\"]")
    private WebElement NameField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@value=\"Set your user id\"]")
    private WebElement UserIdField;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"Save\"]")
    private WebElement SaveButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"chevron.left\"]")
    private WebElement BackButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name=\"RESET NOW\"]")
    private WebElement ResetButton;

    @Override
    public boolean isLoaded() {
        return isVisible(LoginPageTitle);
    }

    public boolean isSuccess() {
        return isVisible(ResetButton);
    }
    
    @Step("Log in as {name}")
    public HomePage loginAs(String name, String userId) {
        type(NameField, name);
        type(UserIdField, userId);
        tap(SaveButton);
        return new HomePage();
    }
}
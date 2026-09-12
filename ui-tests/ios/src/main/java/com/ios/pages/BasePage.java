package com.ios.pages;

import com.ios.config.ConfigReader;
import com.ios.driver.DriverManager;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    private static final int TIMEOUT_SECONDS = ConfigReader.load().explicitWaitSeconds();

    protected final IOSDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.get();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        // Duration.ZERO here: lookups are driven by the explicit waits below,
        // not by the proxy's own implicit wait.
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ZERO), this);

        try {
        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException ignored) {
            // no prompt this run — already granted
        }
    }

    protected void tap(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void type(WebElement element, String text) {
        WebElement field = wait.until(ExpectedConditions.visibilityOf(element));
        field.clear();
        field.sendKeys(text);
    }

    protected String textOf(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    protected boolean isVisible(WebElement element) {
        return isVisible(element, TIMEOUT_SECONDS);
    }

    protected boolean isVisible(WebElement element, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    /** Every page declares how to know it finished loading. */
    public abstract boolean isLoaded();
}

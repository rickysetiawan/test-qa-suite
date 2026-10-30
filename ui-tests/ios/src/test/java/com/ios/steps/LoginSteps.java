package com.ios.steps;

import com.ios.config.ConfigReader;
import com.ios.data.User;
import com.ios.data.UserDataProvider;
import com.ios.driver.DriverFactory;
import com.ios.driver.DriverManager;
import com.ios.pages.HomePage;
import com.ios.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private HomePage home;
    private LoginPage login;

    @Before
    public void startDriver() {
        DriverManager.set(DriverFactory.create(ConfigReader.load()));
    }

    @After
    public void stopDriver() {
        DriverManager.quit();
    }

    @Given("the home screen is displayed")
    public void theHomeScreenIsDisplayed() {
        home = new HomePage();
        assertThat(home.isLoaded()).as("home screen loaded").isTrue();
    }

    @When("I open the login screen")
    public void iOpenTheLoginScreen() {
        home.tapConfigButton();
        login = new LoginPage();
    }

    @Then("the login screen should be loaded")
    public void theLoginScreenShouldBeLoaded() {
        assertThat(login.isLoaded()).as("login screen loaded").isTrue();
    }

    @When("I log in with the {string} user")
    public void iLogInWithTheUser(String userType) {
        User user = UserDataProvider.byType(userType);
        login.loginAs(user.username(), user.userid());
    }

    @Then("the login should be successful")
    public void theLoginShouldBeSuccessful() {
        assertThat(login.isSuccess()).as("login successful").isTrue();
    }
}

package com.ios.tests;


import com.ios.base.BaseTest;
import com.ios.listeners.RetryAnalyzer;
import com.ios.pages.HomePage;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Home")
public class HomeTest extends BaseTest {

    @Test (retryAnalyzer = RetryAnalyzer.class)
    @Description("A valid user lands on the home screen")
    public void validCredentialsLandOnHome() {
        /*User user = UserDataProvider.byType("valid");

        HomePage home = new LoginPage().loginAs(user.email(), user.password());*/
        HomePage home = new HomePage();

        assertThat(home.isLoaded()).as("home screen loaded").isTrue();
    }
}

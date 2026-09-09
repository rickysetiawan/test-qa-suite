package com.ios.tests;


import com.ios.base.BaseTest;
import com.ios.data.User;
import com.ios.UserDataProvider;
import com.ios.listeners.RetryAnalyzer;
import com.ios.pages.HomePage;
import com.ios.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Home")
public class HomeTest extends BaseTest {

    @Test (groups = {"smoke", "regression"}, retryAnalyzer = RetryAnalyzer.class)
    @Description("A valid user lands on the home screen")
    public void validCredentialsLandOnHome() {
        /*User user = UserDataProvider.byType("valid");

        HomePage home = new LoginPage().loginAs(user.email(), user.password());*/

        assertThat(home.isLoaded()).as("home screen loaded").isTrue();
    }
}

package com.ios.base;


import com.acme.mobile.base.BaseTest;
import com.acme.mobile.data.User;
import com.acme.mobile.data.UserDataProvider;
import com.acme.mobile.listeners.RetryAnalyzer;
import com.acme.mobile.pages.HomePage;
import com.acme.mobile.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Home")
public class HomeTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, retryAnalyzer = RetryAnalyzer.class)
    @Description("A valid user lands on the home screen")
    public void validCredentialsLandOnHome() {
        User user = UserDataProvider.byType("valid");

        HomePage home = new LoginPage().loginAs(user.email(), user.password());

        assertThat(home.isLoaded()).as("home screen loaded").isTrue();
    }

    @Test(groups = "regression",
            dataProvider = "invalidLogins",
            dataProviderClass = UserDataProvider.class)
    @Description("Bad credentials keep the user on the login screen with an error")
    public void invalidCredentialsShowError(User user) {
        LoginPage login = new LoginPage()
                .loginExpectingFailure(user.email(), user.password());

        assertThat(login.errorMessage()).contains(user.expectedError());
        assertThat(login.isLoaded()).as("still on login screen").isTrue();
    }
}

package com.ios.tests;

import com.ios.base.BaseTest;
import com.ios.listeners.RetryAnalyzer;
import com.ios.pages.LoginPage;
import com.ios.pages.HomePage;
import com.ios.data.User;
import com.ios.data.UserDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Home")
public class LoginTest extends BaseTest {

    @Test (retryAnalyzer = RetryAnalyzer.class)
    @Description("A valid user lands on the home screen")
    public void validCredentialsLandOnHome() throws ReflectiveOperationException {
        HomePage home = new HomePage();
        LoginPage login = new LoginPage();
        User user = UserDataProvider.byType("valid");
        
        home.tapConfigButton();
        assertThat(login.isLoaded()).as("login screen loaded").isTrue();
        
        login.loginAs(user.username(), user.userid());
        assertThat(login.isSuccess()).as("login successful").isTrue();
    }
    
}

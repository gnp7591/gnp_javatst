package UiTests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Feature;

import Base.DriverFactory;
import Base.LoginPage;
import Base.UiBaseClass;

@Feature("Login Functionality")
@Story("User Login Scenarios")
public class LoginTest extends UiBaseClass {

    @Test(description = "Verify that user can login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test verifies that a user can successfully log in with valid credentials.")
    public void testValidLogin() {
        DriverFactory.getDriver().get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("standard_user", "secret_sauce");
        //Assertion here.
        Assert.assertEquals(DriverFactory.getDriver().getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }
    @Test(description = "Verify that user cannot login with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test verifies that a user cannot log in with invalid credentials.")
    public void testInvalidLogin() {
        DriverFactory.getDriver().get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("invalid_user", "invalid_password");
        
        // Wait for error message to appear (up to 10 seconds)
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
        By errorLocator = By.cssSelector("[data-test='error']");
        wait.until(ExpectedConditions.presenceOfElementLocated(errorLocator));
        
        // Verify error message is displayed and contains expected text
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(
            errorMessage.contains("Username and password do not match"),
            "Error message should contain validation failure text. Actual: " + errorMessage
        );
    }

    @Test(description = "Verify that user cannot login with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that a user cannot log in with empty credentials.")
    public void testLoginWithEmptyCredentials() {
        DriverFactory.getDriver().get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("", "");
        
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
        By errorLocator = By.cssSelector("[data-test='error']");
        wait.until(ExpectedConditions.presenceOfElementLocated(errorLocator));
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(
            errorMessage.toLowerCase().contains("username") || errorMessage.toLowerCase().contains("password"),
            "Error message should mention username or password. Actual: " + errorMessage
        );
    }

    @Test(description = "Verify that user cannot login with invalid username only")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test verifies that a user cannot log in with an invalid username and valid password.")
    public void testInvalidUsernameValidPassword() {
        DriverFactory.getDriver().get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("invalid_user", "secret_sauce");
        
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
        By errorLocator = By.cssSelector("[data-test='error']");
        wait.until(ExpectedConditions.presenceOfElementLocated(errorLocator));
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(
            errorMessage.contains("Username and password do not match"),
            "Error message should contain validation failure text. Actual: " + errorMessage
        );
    }
        


}

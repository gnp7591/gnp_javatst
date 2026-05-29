package UiTests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import Base.DriverFactory;
import Base.LoginPage;
import Base.UiBaseClass;

public class LoginTest extends UiBaseClass {

    @Test(description = "Verify that user can login with valid credentials")
    public void testValidLogin() {
        DriverFactory.getDriver().get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("standard_user", "secret_sauce");
        //Assertion here.
        Assert.assertEquals(DriverFactory.getDriver().getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }
    @Test(description = "Verify that user cannot login with invalid credentials")
    public void testInvalidLogin() {
        DriverFactory.getDriver().get("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login("invalid_user", "invalid_password");
        
        // Wait for error message to appear (up to 10 seconds)
        WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10));
        By errorLocator = By.cssSelector("[data-test='error']");
        
        Assert.assertTrue(
            wait.until(ExpectedConditions.presenceOfElementLocated(errorLocator)).isDisplayed(),
            "Error message should be displayed for invalid credentials"
        );
    }
        


}

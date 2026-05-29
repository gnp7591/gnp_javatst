package UiTests;

import org.testng.Assert;
import org.testng.annotations.Test;

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
        


}

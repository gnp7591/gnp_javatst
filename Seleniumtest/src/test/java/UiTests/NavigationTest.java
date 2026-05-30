package UiTests;

import Base.DriverFactory;
import Base.LoginPage;
import Base.NavigationMenu;
import Base.UiBaseClass;
import Base.ConfigReader;
import io.qameta.allure.Feature;   
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import Base.AllureReportListener;

@Listeners(AllureReportListener.class)
@Feature("Navigation Functionality")
@Story("User Navigation Scenarios")
public class NavigationTest extends UiBaseClass {

    @Test(description = "Verify user can navigate to All Items")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigate to All Items page and verify current URL")
    public void testNavigateToAllItems() {
        // Login first
        DriverFactory.getDriver().get(ConfigReader.getApplicationUrl());
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        
        // Navigate to All Items
        NavigationMenu menu = new NavigationMenu(DriverFactory.getDriver());
        menu.navigateToAllItems();
        
        // Verify we're on inventory page
        Assert.assertEquals(DriverFactory.getDriver().getCurrentUrl(), ConfigReader.getInventoryUrl());
    }

    @Test(description = "Verify user can navigate to About page")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigate to About page and verify it opens the SauceLabs URL")
    public void testNavigateToAbout() {
        // Login first
        DriverFactory.getDriver().get(ConfigReader.getApplicationUrl());
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        
        // Navigate to About
        NavigationMenu menu = new NavigationMenu(DriverFactory.getDriver());
        menu.navigateToAbout();
        
        // Switch to new window/tab if About opens in new tab
        String currentHandle = DriverFactory.getDriver().getWindowHandle();
        if (DriverFactory.getDriver().getWindowHandles().size() > 1) {
            for (String handle : DriverFactory.getDriver().getWindowHandles()) {
                if (!handle.equals(currentHandle)) {
                    DriverFactory.getDriver().switchTo().window(handle);
                    break;
                }
            }
        }
        
        // Verify About page URL
        Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("saucelabs.com"));
    }

    @Test(description = "Verify user can logout")
    @Severity(SeverityLevel.BLOCKER)
    @Description("User logout navigates back to login page")
    public void testLogout() {
        // Login first
        DriverFactory.getDriver().get(ConfigReader.getApplicationUrl());
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        
        // Logout
        NavigationMenu menu = new NavigationMenu(DriverFactory.getDriver());
        menu.logout();
        
        // Verify we're back at login page
        Assert.assertEquals(DriverFactory.getDriver().getCurrentUrl(), ConfigReader.getApplicationUrl());
    }

    @Test(description = "Verify reset app state clears cart and settings")
    @Severity(SeverityLevel.NORMAL)
    @Description("Reset app state should clear saved carts and settings")
    public void testResetAppState() {
        // Login first
        DriverFactory.getDriver().get(ConfigReader.getApplicationUrl());
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.login(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());
        
        // Reset App State
        NavigationMenu menu = new NavigationMenu(DriverFactory.getDriver());
        menu.resetAppState();
        
        // Verify we're still on inventory page (reset should stay on same page)
        Assert.assertEquals(DriverFactory.getDriver().getCurrentUrl(), ConfigReader.getInventoryUrl());
        // Additional verification: Verify cart is empty or settings are reset
        // (Add specific assertions based on your app's behavior)
    }
}
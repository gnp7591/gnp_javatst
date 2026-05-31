package Base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class NavigationMenu {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Menu button and item locators
    private By menuButton = By.id("react-burger-menu-btn");
    private By allItemsLink = By.id("inventory_sidebar_link");
    private By aboutLink = By.id("about_sidebar_link");
    private By logoutLink = By.id("logout_sidebar_link");
    private By resetLink = By.id("reset_sidebar_link");
    
    public NavigationMenu(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    // Helper method to open menu
    public void openMenu() {
        driver.findElement(menuButton).click();
    }
    
    // Navigate to All Items
    public void navigateToAllItems() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(allItemsLink)).click();
    }
    
    // Navigate to About
    public void navigateToAbout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(aboutLink)).click();
    }
    
    // Logout
    public void logout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }
    
    // Reset App State
    public void resetAppState() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(resetLink)).click();
    }
}
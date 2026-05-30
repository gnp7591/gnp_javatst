package Base;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;

    By usernameField = By.id("user-name");
    By passwordField = By.id("password");
    By loginButton = By.id("login-button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        return driver.findElement(By.cssSelector("[data-test='error']")).getText();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(By.cssSelector("[data-test='error']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


}

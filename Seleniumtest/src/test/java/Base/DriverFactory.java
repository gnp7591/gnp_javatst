package Base;
import org.openqa.selenium.WebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

// This class is responsible for managing the WebDriver instances in a thread-safe manner using ThreadLocal.
public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(){
        WebDriverManager.chromedriver().setup();
        driver.set(new org.openqa.selenium.chrome.ChromeDriver());
        getDriver().manage().window().maximize();
    }

    public static WebDriver getDriver(){
        return driver.get();
    }

    public static void quitDriver(){
        if(getDriver() != null){
            getDriver().quit();
            driver.remove();
        }
    }

}

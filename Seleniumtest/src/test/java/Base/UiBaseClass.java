package Base;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;


public class UiBaseClass {
    @BeforeMethod()
    public void setUp(){
        DriverFactory.initDriver();
    }

    @AfterMethod()
    public void tearDown(){
        DriverFactory.quitDriver();
    }

}

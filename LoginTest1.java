package logintest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

//import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest1 {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		//WebDriverManager.chromedriver().setup();
		
		WebDriver driver3 = new ChromeDriver();
		// launch saucedemo site
		
		
		driver3.get("https://www.saucedemo.com/v1/");
		
		driver3.manage().window().maximize();   //maximize the window
		
		Thread.sleep(5000);
		
		//username capturing
	WebElement txtusername	= driver3.findElement(By.name("user-name"));
		 txtusername.sendKeys("standard_user");
		
		//password capturing
		 driver3.findElement(By.name("password")).sendKeys("secret_sauce");
		 
		 //submit button  
		 driver3.findElement(By.id("login-button")).click();
		 
	String Title= driver3.getTitle();
	String acttitle="SWAGLABS";	 
	
	 if(acttitle.equals(Title))
	 {
		 System.out.println("test passed");   // Test case for validating page title
	 }
	 else
	 {
		 System.out.println("failed");
	 }
	 
	 driver3.quit(); // closing the Test case
	}

}

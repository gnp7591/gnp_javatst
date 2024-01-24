package testdemo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CssSelectorDemo {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
		WebDriver driver4 = new ChromeDriver();
		// launch saucedemo site
		
		
		driver4.get("https://www.saucedemo.com/v1/");
		
		driver4.manage().window().maximize();   //maximize the window
		
		Thread.sleep(5000);
		
		//username capturing
	WebElement txtusername	= driver4.findElement(By.name("user-name"));
		 txtusername.sendKeys("standard_user");
		
		//password capturing
		 driver4.findElement(By.name("password")).sendKeys("secret_sauce");
		 
		 //submit button  
		 driver4 .findElement(By.id("login-button")).click();
		 
		
		
		List<WebElement> bpoptions = driver4.findElements(By.cssSelector("a[id='item_4_title_link'] div[class='inventory_item_name']"));
		
		System.out.println("Backpack counts:-"+bpoptions.size());
		Thread.sleep(3000);
		
		WebElement xe =  driver4.findElement(By.cssSelector("#inventory_container > div > div:nth-child(2) > div.pricebar > button"));
		xe.click();
		
		System.out.println("clicked on backpack 2");
		
		
	}

}

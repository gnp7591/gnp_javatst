package testdemo;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class seleniumdemo1 {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		
		
		//import io.github.bonigarcia.wdm.WebDriverManager;

		
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
				 
//			String Title= driver3.getTitle();
//			String acttitle="SWAGLABS";	 
//			
//			 if(acttitle.equals(Title))
//			 {
//				 System.out.println("test passed");   // Test case for validating page title
//			 }
//			 else
//			 {
//				 System.out.println("failed");
//			 }
				 
			List<WebElement> inventory = driver3.findElements(By.className("inventory_list"));
			
			System.out.println("Inventory List size:-"+inventory.size()); // counts of Inventory lists
			
			List<WebElement> images= driver3.findElements(By.className("inventory_item_img"));
			System.out.println("Images count:-"+images.size()); // counts of Images
			
			List<WebElement> links =driver3.findElements(By.tagName("a"));	 
			System.out.println("Total links:-"+links.size());  // count of total links
			
			// Examples of System Locators above
			
			
			
			 driver3.quit(); // closing the Test case
			}


	}



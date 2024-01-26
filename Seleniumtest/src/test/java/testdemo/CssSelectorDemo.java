package testdemo;

import java.time.Duration;
import java.util.List;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CssSelectorDemo {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		WebDriverManager.chromedriver().setup();
		WebDriver driver4 = new ChromeDriver();
		// launch saucedemo site
		
		
		driver4.get("https://www.saucedemo.com/v1/");
		
		driver4.manage().window().maximize();   //maximize the window
		WebDriverWait mywait = new WebDriverWait(driver4,Duration.ofSeconds(5));
		
		/* Thread.sleep(2000); */
		
		//username capturing
	WebElement txtusername	= driver4.findElement(By.name("user-name"));
		 txtusername.sendKeys("standard_user");
		
		//password capturing
		 driver4.findElement(By.name("password")).sendKeys("secret_sauce");
		 
		 //submit button  
		 driver4 .findElement(By.id("login-button")).click();
		 
		 WebElement xe5 = mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[normalize-space()='Open Menu']")));
		 xe5.click();
		 System.out.println("Menu Opened");
		 
		 Thread.sleep(200);
		  WebElement invopt = driver4.findElement(By.xpath("//a[@id='inventory_sidebar_link']"));
		  System.out.println("Element Displayed"+invopt.isDisplayed());
		  
		  
			/* System.out.println("Navigation options"+invopt.size()); */
			/*
			 * for (int i=0; i<invopt.size();i++){ System.out.println("naviagtion menu:" +
			 * invopt.get(i).getAttribute("value")); }
			 */
			/*
			 * JavascriptExecutor js = (JavascriptExecutor)driver4;
			 * js.executeScript("invopt[0].click();", invopt);
			 */
		  
		 Thread.sleep(4000);
		
		List<WebElement> bpoptions = driver4.findElements(By.cssSelector("a[id='item_4_title_link'] div[class='inventory_item_name']"));
		
		System.out.println("Backpack counts:-"+bpoptions.size());
		Thread.sleep(2000);
		
		WebElement xe =  driver4.findElement(By.cssSelector("#inventory_container > div > div:nth-child(2) > div.pricebar > button"));
		xe.click();
		
		System.out.println("clicked on backpack 2");
		
		WebElement xe2 = driver4.findElement(By.cssSelector("#inventory_container > div > div:nth-child(6) > div.pricebar > button"));
		 xe2.click();
		 
		 
		WebElement xe3 = driver4.findElement(By.cssSelector("#shopping_cart_container > a > svg")); 
		xe3.click();
		
		System.out.println("clicked on cart of swglabs");
		
		Assert.assertTrue(true, "clicked on cart");
		
		Assert.assertTrue(true);
		
		
		
		//	driver4.quit();
		
		
		
	}

}

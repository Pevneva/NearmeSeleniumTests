package ru.st.selenium;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.testng.*;
import org.testng.annotations.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class CreatingOfferForSmbBusiness extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testCreatingOfferForSmbBusiness() throws Exception {    
	String TradName="";

  	System.out.println("=================================================");  
	System.out.println("");		
	System.out.println("===== ADMIN CREATING OFFER FOR SMB BUSINESS =====");	
	System.out.println("");		
	driver.manage().window().maximize();
	loginAsAdmin();
	
	TradName="Business & venue & offer - test";
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");	
	addBusiness(TradName,"SMB", "2");
	
//	clicking on the 'add a venue' link
	System.out.println("Clicking on the 'add a venue' link...");
    driver.findElement(By.linkText("add a venue")).click();
	System.out.println("OK!");	
	
	addVenue();
	
	updateOfferPlanForBusiness(TradName);
	
//	opening business
	System.out.println("opening business...");
    driver.findElement(By.linkText(TradName)).click();
	System.out.println("OK!");	
	
//	checking that venue page will be opened
	System.out.println("Checking that venue page will be opened...");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class=\"photo_video\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	

//	clicking on the Offers tab	
	System.out.println("Checking that venue page will be opened...");
	driver.findElement(By.xpath("//a[contains(text(),'Offers')]")).click();
	System.out.println("OK!");
	
//	clicking on the 'Yes, lets get started' link
	System.out.println("clicking on the 'Yes, lets get started' link...");
    driver.findElement(By.linkText("Yes, lets get started")).click();
	System.out.println("OK!");
	
	addOffer("01/01/13","31/12/14");
	
//	clicking on the 'Create another Offer' link
	System.out.println("clicking on the 'Create another Offer' link...");
    driver.findElement(By.linkText("Create another Offer")).click();
 	System.out.println("OK!");
	
	addOffer("01/01/12","31/12/12");
	
//	clicking on the 'Finish' button
	System.out.println("clicking on the 'Finish' button...");
    driver.findElement(By.linkText("Finish")).click();
 	System.out.println("OK!");	
	
//	clicking on the 'Create Offer' button
	System.out.println("clicking on the 'create offer' button...");
    driver.findElement(By.cssSelector("a.add_button > span")).click();
 	System.out.println("OK!");

	addOffer("01/01/15","31/12/15");
	
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");

	logout();
	
	System.out.println(""); 
	System.out.println("===== ADMIN CREATING OFFER FOR SMB BUSINESS WAS COMPLETED! ====="); 
	System.out.println(""); 
	System.out.println("================================================================");
	
	}
 
 
 private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
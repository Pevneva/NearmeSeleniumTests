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

public class CreatingVenue extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testCreatingBusiness() throws Exception {    
	String TradName="";
	
	System.out.println("========== ADMIN CREATING VENUE ==========");	
	driver.manage().window().maximize();
	loginAsAdmin();
	
	TradName="Business & venue - test";
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");	
	addBusiness(TradName,"SMB", "2");
//	clicking on the 'add a venue' link
	System.out.println("Clicking on the 'add a venue' link...");
    driver.findElement(By.linkText("add a venue")).click();
	System.out.println("OK!");	
	addVenue();

	removeBusiness(TradName);
	removeUser("email_01@mail.ru");
	addBusiness(TradName,"Chain", "100");
//	clicking on the "Add a Venue" button
	System.out.println("Clicking on the \"Add a Venue\" button...");
	driver.findElement(By.cssSelector("a.add_button > span")).click();
	System.out.println("OK!");	
	addVenue();

	removeBusiness(TradName);
	removeUser("email_01@mail.ru");
	addBusiness(TradName,"Mall", "100");
//	clicking on the "Add a Venue" button
	System.out.println("Clicking on the \"Add a Venue\" button...");
	driver.findElement(By.cssSelector("a.add_button > span")).click();
	System.out.println("OK!");	
	addVenue();
	
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");	
	
	logout();
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
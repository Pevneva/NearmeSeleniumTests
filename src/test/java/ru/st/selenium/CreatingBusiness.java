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

public class CreatingBusiness extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testOnlineRegistration() throws Exception {    

	String TradName="Auto Admin Trading";
	
	driver.manage().window().maximize();
	loginAsAdmin();
	
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");
	
	addBusiness(TradName,"SMB", "2");
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");

	addBusiness(TradName,"Chain", "100");
	removeBusiness(TradName);
	removeUser("email_01@mail.ru");
	
	addBusiness(TradName,"Mall", "100");
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
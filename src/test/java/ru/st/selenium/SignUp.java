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

public class SignUp extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testSignUp() throws Exception {

	System.out.println("========================================");  
	System.out.println("");  
	System.out.println("========== SIGN UP STANDARD ==========");  
	driver.manage().window().maximize();
	
	loginAsAdmin();
	removeUser("lyudmila_test_accountant@mail.ru");
	logout();
	System.out.println("");
	//opening start portal page
	System.out.println("Opening start portal page...");	
	driver.get(baseUrl);
	//checking that start page is opened
	checkStartPage();
	System.out.println("OK!");	
	System.out.println("Opening Sig Up page...");	
	//clicking on the "Log In" link
	driver.findElement(By.linkText("Log In")).click();
	//clicking on the "Sign Up" link
	driver.findElement(By.linkText("Sign Up")).click();
	System.out.println("OK!");	
	System.out.println("Filling all fields and continue...");	
 	//filling all fields
    driver.findElement(By.id("firstName")).clear();
    driver.findElement(By.id("firstName")).sendKeys("Ivan");
    driver.findElement(By.id("lastName")).clear();
    driver.findElement(By.id("lastName")).sendKeys("Ivanov");
    new Select(driver.findElement(By.id("gender"))).selectByVisibleText("Male");
    driver.findElement(By.id("birthday")).clear();
    driver.findElement(By.id("birthday")).sendKeys("01/05/83");
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys("lyudmila_test_accountant@mail.ru");
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("TestUserName");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("12345678");
    new Select(driver.findElement(By.id("countryCode"))).selectByVisibleText("Belarus");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//*[@id=\"homeCity\"]/*[@value=\"Minsk\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    new Select(driver.findElement(By.id("homeCity"))).selectByVisibleText("Minsk (Horad Minsk)");
	//clicking on the "Join" button
    driver.findElement(By.name("_action_join")).click();
	//checking that page is opened
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Confirm Your Email[\\s\\S]*$"));	
	System.out.println("OK!");	
	System.out.println("Checking that email was sent...");	
	//opening mail.ru site
    driver.get( "www.mail.ru");
	//waiting some time
    Thread.sleep(2000);
	//going to test email
    driver.findElement(By.id("mailbox__login")).clear();
    driver.findElement(By.id("mailbox__login")).sendKeys("lyudmila_test_accountant");	
    driver.findElement(By.id("mailbox__password")).clear();
    driver.findElement(By.id("mailbox__password")).sendKeys("test123");
    driver.findElement(By.id("mailbox__auth__button")).click();
	//opening last messages
	driver.findElement(By.cssSelector("span.messageline__body__name")).click();
	System.out.println("OK!");	
	System.out.println("Clicking on the activation link...");	
	//waiting some time
    Thread.sleep(4000);
	//clicking on link for authorization
    driver.findElement(By.xpath("//a[contains(text(),'http')]")).click();
    Thread.sleep(3000);
	System.out.println("OK!");
	System.out.println("Checking that Edit User page is opened...");
    //finding window with 'Edit User' title and go to it
        for (String handle : driver.getWindowHandles())
                {
                        driver.switchTo().window(handle);
                        if (driver.getTitle().equals("Edit User")){break;};
                }
	//check that Profile page is opened
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class=\"user_profile_menu\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.id("birthday"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");	
	//logout
	logout();
	//login as created user
	login("TestUserName","12345678");
	//check that Account Details page is opened
	System.out.println("Checking that Account Details page is opened...");	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class=\"user_profile_menu\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }		
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//form[@id=\"userForm\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");	
	//logout
	logout();
	
	/* Removing created data */
	
	System.out.println("");	
	System.out.println("--- Removing created data... ---");	
	System.out.println("");		
	//login as admin
	loginAsAdmin();
    //removing of created user
	removeUser("lyudmila_test_accountant@mail.ru");
    //logout
	logout();
	System.out.println("OK!");	
	
	/* Log out from email */
	
	System.out.println("Logging out from email...");	
	//opening mail.ru site
    driver.get( "www.mail.ru");	
	//log out from mail.ru
    driver.findElement(By.xpath("//a[@id=\"PH_logoutLink\"]")).click();
	System.out.println("OK!");

	System.out.println("===== SIGN UP STANDARD TEST WAS COMPLETED! ====="); 
	System.out.println(""); 
	System.out.println("========================================"); 
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

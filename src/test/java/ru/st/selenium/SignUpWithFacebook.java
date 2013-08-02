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

public class SignUpWithFacebook extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testSignUpWithFacebook() throws Exception {

	System.out.println("===========================================");  
	System.out.println("");    
	System.out.println("========== SIGN UP WITH FACEBOOK =========="); 
	System.out.println(""); 	
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
	//clicking on the "Sign up with Facebook" button
	System.out.println("Clicking on the \"Sign up with Facebook\" button");
	driver.findElement(By.id("joinWithFacebook")).click();
	System.out.println("OK!");
	//login to facebook
	System.out.println("Logging in to facebook...");
	driver.findElement(By.id("email")).sendKeys("lyudmila_test_accountant@mail.ru");
	driver.findElement(By.id("pass")).sendKeys("test12345");
	Thread.sleep(1000);
	driver.findElement(By.id("u_0_1")).click();
	Thread.sleep(3000);
	//checking that Facebook Sign Up page was opened
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class='facebook-signup-greeting__container']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");	
 	//filling all fields
	System.out.println("Filling all fields and continue...");	
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("TestFacebookUserName");
    new Select(driver.findElement(By.id("countryCode"))).selectByVisibleText("Belarus");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//*[@id=\"homeCity\"]/*[@value=\"Minsk\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    new Select(driver.findElement(By.id("homeCity"))).selectByVisibleText("Minsk (Horad Minsk)");
	//clicking on the "Join" button
    driver.findElement(By.name("_action_join")).click();
	System.out.println("OK!");	
	//check that Profile page is opened
	System.out.println("Checking that Profile page is opened...");		
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
	//login with facebook
	System.out.println("Logging in with facebook...");	
	//clicking on the "Log In" link
	driver.findElement(By.linkText("Log In")).click();
	//clicking on the "Log In With Facebook" button
	driver.findElement(By.id("joinWithFacebook")).click();
	System.out.println("OK!");	
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

	System.out.println(""); 
	System.out.println("====== SIGN UP WITH FACEBOOK WAS COMPLETED! ======"); 
	System.out.println(""); 
	System.out.println("=================================================="); 	
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

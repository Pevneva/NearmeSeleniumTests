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

public class OnlineRegistrationExistingUser extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testOnlineRegistration() throws Exception {    

  	System.out.println("==========================================================");  
	System.out.println("");    
	System.out.println("========== ONLINE REGISTRATION BY EXISTING USER ==========");
	System.out.println(""); 	
	driver.manage().window().maximize();

	loginAsAdmin();
	removeBusiness("Auto Online Trading");
	removeUser("lyudmila_test_mm@mail.ru");
	logout();

	login("Consumer","12345678");
	OnlineRegistrWithUser("Registration only","",true);
	login("Consumer","12345678");
	OnlineRegistrWithUser("Registration and 3 daily offers","PROMO-SALES-PLA",false);
	
	System.out.println(""); 
	System.out.println("===== ONLINE REGISTRATION BY EXISTING USER WAS COMPLETED! ====="); 
	System.out.println(""); 
	System.out.println("==============================================================="); 	
	
	}
	
public void OnlineRegistrWithUser(String ContractType, String PromoCode, boolean isNotedCheckbox)throws Exception {
	System.out.println("");
	System.out.println("--- Checking online registration with next data: ---");
	System.out.println("");
	System.out.println("CONTRACT TYPE:     "+ContractType);
	System.out.println("PROMO CODE:     "+PromoCode);
	System.out.println("IS 'Use my account...' CHECKBOX NOTED?:     "+isNotedCheckbox);
	System.out.println("");
	firstStepOfOnlineRegistrationWithUser(ContractType, PromoCode, isNotedCheckbox );
	if (!isNotedCheckbox) { 
	checkFirstEmailOfOnlineRegistration("lyudmila_test_mm@mail.ru");
	}
	 else {
	 checkFirstEmailOfOnlineRegistration("lyudmila_test_operator@mail.ru");
	 }
	checkPendingBusiness();
	if (!isNotedCheckbox) {
		checkDisabledUser("lyudmila_test_mm@mail.ru");
	}
	approveBusiness("Auto Online Trading");
	logout();
	/* Checking that emails notifications were sent */
	if (!isNotedCheckbox) {
		checkSecondEmailOfOnlineRegistration("lyudmila_test_mm@mail.ru");
		clickClickHereLinkAndFindCompleteBRwindow();		
	}
	else {
		checkSecondEmailOfOnlineRegistration("lyudmila_test_operator@mail.ru");
		login("Consumer","12345678");		
	}
	finalRegistrationStepOfOnlineRegistration();
	logoutFromMailru();
	/* Removing created data */
	System.out.println("");	
	System.out.println("--- Removing created data... ---");	
	System.out.println("");		
	loginAsAdmin();
	removeBusiness("Auto Online Trading");
	if (!isNotedCheckbox) {	removeUser("lyudmila_test_mm@mail.ru");}
	logout();
	System.out.println("OK!");	
}

public void firstStepOfOnlineRegistrationWithUser(String ContractType, String PromoCode, boolean isNotedCheckbox)throws Exception {
	//opening external registration page
	driver.get(baseUrl+"merchant/signup");
	//filling company data fields
	System.out.println("Filling all fields on external page and continue...");	
    driver.findElement(By.id("companyName")).clear();
    driver.findElement(By.id("companyName")).sendKeys("Auto Test Company");
    driver.findElement(By.id("tradingName")).clear();
    driver.findElement(By.id("tradingName")).sendKeys("Auto Online Trading");
    driver.findElement(By.id("registeredVatNo")).clear();
    driver.findElement(By.id("registeredVatNo")).sendKeys("ComVatNo");
    //filling category for business
	new Select(driver.findElement(By.id("categoryId"))).selectByVisibleText("Card & Stationery");
    //filling adress data fields
	new Select(driver.findElement(By.id("countryCode"))).selectByVisibleText("United Kingdom");
    driver.findElement(By.id("address1")).clear();
    driver.findElement(By.id("address1")).sendKeys("Pikadili");
    driver.findElement(By.id("address2")).clear();
    driver.findElement(By.id("address2")).sendKeys("Skver");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//option[@value='London']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    new Select(driver.findElement(By.id("city"))).selectByVisibleText("London");
    driver.findElement(By.id("postalCode")).clear();
    driver.findElement(By.id("postalCode")).sendKeys("44400");	

	if (!isNotedCheckbox) {
		//removing a tick from 'Use my account as Merchant Manager' check box
		driver.findElement(By.id("useCurrentUserAsManager")).click();
		//filling Merchant Manager fields
		driver.findElement(By.id("firstName")).clear();
		driver.findElement(By.id("firstName")).sendKeys("Merchant");
		driver.findElement(By.id("lastName")).clear();
		driver.findElement(By.id("lastName")).sendKeys("Manager");
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys("lyudmila_test_mm@mail.ru");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("Manager");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("12345678");		
	}

    //filling contract data fields
	new Select(driver.findElement(By.id("contractTypeId"))).selectByVisibleText(ContractType);
    driver.findElement(By.id("numberOfVenues")).clear();
    driver.findElement(By.id("numberOfVenues")).sendKeys("100");
    driver.findElement(By.id("promotionalCode")).clear();
    driver.findElement(By.id("promotionalCode")).sendKeys(PromoCode);	
    //filling payment card fields
	driver.findElement(By.id("cardNumber")).clear();
    driver.findElement(By.id("cardNumber")).sendKeys("340000432128428");
    driver.findElement(By.id("cardHolderName")).clear();
    driver.findElement(By.id("cardHolderName")).sendKeys("test");
    driver.findElement(By.id("cardExpireMonth")).clear();
    driver.findElement(By.id("cardExpireMonth")).sendKeys("12");
    driver.findElement(By.id("cardExpireYear")).clear();
    driver.findElement(By.id("cardExpireYear")).sendKeys("2015");
    driver.findElement(By.id("cardSecurityCode")).clear();
    driver.findElement(By.id("cardSecurityCode")).sendKeys("3469");
    //noting 'I agree...' check box 
	driver.findElement(By.id("termsAgreement")).click();
	//clicking on the "Continue" button
    driver.findElement(By.name("_action_signupConfirm")).click();
	//checking that div with 'order_summary' class exists
    for (int second = 0;; second++) {
   	if (second >= 60) fail("timeout");
   	try { if (isElementPresent(By.xpath("//div[@class='order_summary']"))) break; } catch (Exception e) {}
   	Thread.sleep(1000);
    }
	//waiting until "Submit" button will be shown
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.name("_action_signupProcessing"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	//clicking on the "Submit" button
    driver.findElement(By.name("_action_signupProcessing")).click();
	//checking that div with 'print_area' id exists
    for (int second = 0;; second++) {
		if (second >= 60) fail("timeout");
		try { if (isElementPresent(By.xpath("//div[@id='print_area']"))) break; } catch (Exception e) {}
		Thread.sleep(1000);
    }
	//waiting until "Finish" button will be shown
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.name("_action_null"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	//clicking on the "Finish" button
    driver.findElement(By.name("_action_null")).click();
	
	//checking that 'Account Details' page was opened 
	for (int second = 0;; second++) {
		if (second >= 60) fail("timeout");
		try { if (isElementPresent(By.id("firstName"))) break; } catch (Exception e) {}
		Thread.sleep(1000);
	}			
	System.out.println("OK!");
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

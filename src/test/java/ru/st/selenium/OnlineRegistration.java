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

public class OnlineRegistration extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testOnlineRegistration() throws Exception {    

	System.out.println("========== ONLINE REGISTRATION ==========");
	driver.manage().window().maximize();

	loginAsAdmin();
	removeBusiness("Auto Online Trading");
	removeUser("lyudmila_test_mm@mail.ru");
	logout();

	OnlineRegistr("Registration only","");
	OnlineRegistr("Registration and 3 daily offers","PROMO-SALES-PLA");
	
	}

  public void OnlineRegistr(String ContractType, String PromoCode)throws Exception {
	System.out.println("");
	System.out.println("--- Checking online registration with next data: ---");
	System.out.println("");
	System.out.println("CONTRACT TYPE:     "+ContractType);
	System.out.println("PROMO CODE:     "+PromoCode);
	System.out.println("");	
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
	//checking that start page of nearme was opened
	checkStartPage();
	System.out.println("OK!");
	//opening mail.ru site
	System.out.println("Going to mail.ru and logging in as lyudmila_test_mm@mail.ru user...");	
    driver.get( "www.mail.ru");
	//going to test email
    driver.findElement(By.id("mailbox__login")).clear();
    driver.findElement(By.id("mailbox__login")).sendKeys("lyudmila_test_mm@mail.ru");	
    driver.findElement(By.id("mailbox__password")).clear();
    driver.findElement(By.id("mailbox__password")).sendKeys("test12345");
    driver.findElement(By.id("mailbox__auth__button")).click();
	System.out.println("OK!");	
	System.out.println("Checking that 'Regisrtation in NearMe' email was sent...");	
	//checking that subject of first messages contains 'Registration in NearMe' text
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@id=\"ML0\"]/div[1]//span[contains(text(),'Registration in NearMe')]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");		
	//opening first messages
	System.out.println("Opening first messages...");		
	driver.findElement(By.cssSelector("span.messageline__body__name")).click();
	System.out.println("OK!");	
	//checking that subject contains 'Registration in NearMe' text
	System.out.println("Checking that subject contains 'Registration in NearMe' text...");	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class=\"mr_read__top_in\"]//span[contains(text(),'Registration in NearMe')]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");	
	//log out from 'lyudmila_test_mm@mail.ru' email
	System.out.println("Logging out from 'lyudmila_test_mm@mail.ru' email");	
    driver.findElement(By.xpath("//a[@id=\"PH_logoutLink\"]")).click();	
	System.out.println("OK!");
	
	
	/* Checking that Business with 'Auto Online Trading' trading name was created and has 'Pending' status */

	System.out.println("Checking that Business with 'Auto Online Trading' trading name was created and has 'Pending' status...");	
	loginAsAdmin();
	//going to Businesses tab
    driver.findElement(By.cssSelector("#registrationsTab > span.nav_btn_text")).click();
    //entering 'Auto Online Trading' text to 'Keywords' field
	driver.findElement(By.id("keywords")).clear();
    driver.findElement(By.id("keywords")).sendKeys("Auto Online Trading");
	//clicking on Search icon
    driver.findElement(By.id("action_button")).click();
	//checking that "searchResultList" table contains contains 'Auto Online Trading' text
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//a[contains(text(),'Auto Online Trading')]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
   //checking that "searchResultList" table contains 'status_pending' class
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//div[@class='status_pending']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
	System.out.println("OK!");

	
	/* Checking that User with 'lyudmila_test_mm@mail.ru' email was created and has 'Disabled' status */

	System.out.println("Checking that User with 'lyudmila_test_mm@mail.ru' email was created and has 'Disabled' status...");	
	//Going to Users tab
	driver.findElement(By.cssSelector("#usersTab > span.nav_btn_text")).click();
	//entering 'lyudmila_test_mm@mail.ru' to "Keywords' field
	driver.findElement(By.id("keywords")).clear();
	driver.findElement(By.id("keywords")).sendKeys("lyudmila_test_mm@mail.ru");
	driver.findElement(By.id("action_button")).click();	
	//checking that "searchResultList" table contains 'lyudmila_test_mm@mail.ru' text
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//td[contains(text(),'lyudmila_test_mm@mail.ru')]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
	//checking that "searchResultList" table contains 'status_rejected' class	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//div[@class='status_rejected']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");
	

	/* Approving new business */

	System.out.println("Approving new business...");	
	//going to Businesses tab
    driver.findElement(By.cssSelector("#registrationsTab > span.nav_btn_text")).click();
    //entering 'Auto Online Trading' text to 'Keywords' field
	driver.findElement(By.id("keywords")).clear();
    driver.findElement(By.id("keywords")).sendKeys("Auto Online Trading");
	//clicking on Search icon
    driver.findElement(By.id("action_button")).click();
	//clicking on 'select_all' checkbox
    driver.findElement(By.name("selectAll")).click();	
	//selecting 'Publish' value in 'Status' select box
    new Select(driver.findElement(By.id("newStatus"))).selectByVisibleText("Publish / Approve");
	//clicking on the "Update" button
    driver.findElement(By.name("_action_updateContract")).click();
	//checking that "searchResultList" table contains 'status_live' class	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//div[@class='status_live']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
	System.out.println("OK!");
	

	/* Checking that emails notifications were sent */

	System.out.println("Checking that emails notifications were sent...");
	//opening mail.ru site
    driver.get( "www.mail.ru");
	//going to test email
    driver.findElement(By.id("mailbox__login")).clear();
    driver.findElement(By.id("mailbox__login")).sendKeys("lyudmila_test_mm@mail.ru");	
    driver.findElement(By.id("mailbox__password")).clear();
    driver.findElement(By.id("mailbox__password")).sendKeys("test12345");
    driver.findElement(By.id("mailbox__auth__button")).click();
	//waiting until subject of 3rd notification will not be 'Registration in NearMe' text (2 emails will not be sent)
	System.out.println("Taking subject of 3rd email and waiting until it will not be 'Registration in NearMe'... ");
	String  S1="";
	for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { 
			S1  = driver.findElement(By.xpath("//div[@id=\"ML0\"]/div[3]//span[contains(text(),'Registration in')]")).getText();
			System.out.println("S1 = "+S1);
			if (S1.equals("Registration in NearMe")) 
			break; 
			} catch (Exception e) {}
    	Thread.sleep(1000);
    }		
	//opening first messages
	System.out.println("Opening first message...");
	driver.findElement(By.cssSelector("span.messageline__body__name")).click();
	System.out.println("OK!");	
	//taking subject of email
	System.out.println("Taking subject of first email...");	
	S1= driver.findElement(By.xpath("//div[@id ='msgFieldSubject']//span[@class='val']")).getText();
	System.out.println("OK!	   Subject = "+S1);	
	if (S1.contains("Payment Confirmation")){
		//clicking on the 'Письма' link
		driver.findElement(By.xpath("//a[@id=\"HeaderBtnCheckNewMsg\"]")).click();
		//opening second message		
		System.out.println("Opening second message...");
		driver.findElement(By.xpath("//div[@id=\"ML0\"]/div[2]//span[contains(text(),'Welcome to NearMe')]")).click();
		System.out.println("OK!");		
		}
	//clicking on the 'click here' link
	System.out.println("Clicking on the 'click here' link...");	
    driver.findElement(By.linkText("click here")).click();
    //finding window with 'Complete Business Registration' title and go to it
    for (String handle : driver.getWindowHandles())
       {
       driver.switchTo().window(handle);
       if (driver.getTitle().equals("Complete Business Registration")){break;};
       }	
	System.out.println("OK!");	
	
	/*  Completing final registration step */

	System.out.println("--- Completing final registration step... ---");	
	//checking that 'Complete Business Registration' page was opened
	System.out.println("Checking that 'Complete Business Registration' page was opened...");	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    //	try { if (isElementPresent(By.xpath("//h1[contains(text(),'Complete Business Registration')]"))) break; } catch (Exception e) {}
    	try { if (isElementPresent(By.xpath("//input[@id='managers[0].firstName']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
//https://stage.getnearme.com/merchant/completeRegistration	
	System.out.println("OK!");		
	//filling of empty fields
	System.out.println("Filling of empty fields...");		
	driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.jobTitle")).clear();
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.jobTitle")).sendKeys("Job Title");     
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.nationalInsuranceNo")).clear();
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.nationalInsuranceNo")).sendKeys("NatInsNo");
    driver.findElement(By.id("managers[0].contact.telephone")).clear();
    driver.findElement(By.id("managers[0].contact.telephone")).sendKeys("2220010");
    driver.findElement(By.id("managers[0].contact.mobile")).clear();
    driver.findElement(By.id("managers[0].contact.mobile")).sendKeys("334455");
    driver.findElement(By.id("registeredCompanyNo")).clear();
    driver.findElement(By.id("registeredCompanyNo")).sendKeys("companyNo");
    new Select(driver.findElement(By.id("registeredAddress.region"))).selectByVisibleText("London");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.id("registeredAddress.county"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
    new Select(driver.findElement(By.id("registeredAddress.county"))).selectByVisibleText("Dorset");
    driver.findElement(By.id("companyContactDetails.telephone")).clear();
    driver.findElement(By.id("companyContactDetails.telephone")).sendKeys("100001");
    driver.findElement(By.id("companyContactDetails.fax")).clear();
    driver.findElement(By.id("companyContactDetails.fax")).sendKeys("100002");
    driver.findElement(By.id("companyContactDetails.email")).clear();
    driver.findElement(By.id("companyContactDetails.email")).sendKeys("email001@com.com");
    driver.findElement(By.id("companyContactDetails.websiteUrl")).clear();
    driver.findElement(By.id("companyContactDetails.websiteUrl")).sendKeys("http://www.website001.com");
    driver.findElement(By.id("companyContactDetails.facebook")).clear();
    driver.findElement(By.id("companyContactDetails.facebook")).sendKeys("http://www.facebook001.com");
    driver.findElement(By.id("companyContactDetails.twitter")).clear();
    driver.findElement(By.id("companyContactDetails.twitter")).sendKeys("http://www.twitter001.com");
    driver.findElement(By.id("companyContactDetails.foursquare")).clear();
    driver.findElement(By.id("companyContactDetails.foursquare")).sendKeys("http://www.foursquare001.com");
    driver.findElement(By.id("companyContactDetails.instagram")).clear();
    driver.findElement(By.id("companyContactDetails.instagram")).sendKeys("http://www.instagram.com");
    driver.findElement(By.id("companyContactDetails.pinterest")).clear();
    driver.findElement(By.id("companyContactDetails.pinterest")).sendKeys("http://www.pinterest.com");
    driver.findElement(By.xpath("//fieldset[6]//ul/li[contains(text(),\"Candy Shop\")]")).click();
    driver.findElement(By.cssSelector("button.plus_btn")).click();
    driver.findElement(By.id("keywords.ti1")).clear();
    driver.findElement(By.id("keywords.ti1")).sendKeys("keyword001");
    driver.findElement(By.id("keywords.ti1")).click();
	System.out.println("OK!");	
	//clicking on the "Continue" button
	System.out.println("Clicking on the Continue button...");	
    driver.findElement(By.name("_action_save")).click();
	//waiting until new page was not opened
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class=\"period_item\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");		
	//clicking on the "Use Company Details" button
	System.out.println("Clicking on the 'Use Company Details' button...");		
    driver.findElement(By.id("copyCompanyDetailsButton")).click();
	System.out.println("OK!");	
	//clicking on the "Use Contact Details" button	
	System.out.println("Clicking on the 'Use Contact Details' button...");	
    driver.findElement(By.id("copyContactDetailsButton")).click();
	System.out.println("OK!");		
	//filling "Url Name" filed
	System.out.println("Filling \"Url Name\" filed...");		
    driver.findElement(By.id("displayName")).sendKeys("venue001");
	System.out.println("OK!");
	
	/* Filling Opening Hours forms */	
	System.out.println("Filling Opening Hours forms...");
	
	//noting by tick 'I'd like to enter two sets of hours for a single day.' select box	
    driver.findElement(By.id("splitHours")).click();
	//filling 'From' input field for Timetable1
    driver.findElement(By.id("0.periodFrom")).sendKeys("02/06/13");
	//filling 'To' input field for Timetable2
    driver.findElement(By.id("0.periodTo")).sendKeys("08/06/13");
	//selecting '8:00' in 'Open' select box for Monday in first case
    new Select(driver.findElement(By.id("openingHours.main.0.Monday.from_hour"))).selectByVisibleText("08:00");
	//selecting '17:30' in 'Close' select box for Monday for first case
    new Select(driver.findElement(By.id("openingHours.main.0.Monday.to_hour"))).selectByVisibleText("17:30");
	//checking 'Closed' check box for Sunday
    driver.findElement(By.id("closedSunday0Period")).click();
	//checking '24 Hours' check box for Saterday
    driver.findElement(By.id("fullDaySaturday0Period")).click();
	//noting by tick 'Note: Uncheck to edit hours individually for each day' checkbox
    driver.findElement(By.id("selectHoursSync")).click();
	//adding Timetable2
    driver.findElement(By.id("add_period_item")).click();
	//filling 'From' input field for Timetable1
    driver.findElement(By.id("0.periodFrom")).sendKeys("09/06/13");
	//filling 'To' input field for Timetable2
    driver.findElement(By.id("0.periodTo")).sendKeys("15/06/13");
	//selecting '10:00' in 'Open' select box for Monday in first case
    new Select(driver.findElement(By.id("openingHours.main.1.Monday.from_hour"))).selectByVisibleText("10:00");
	//selecting '14:00' in 'Close' select box for Monday for first case
    new Select(driver.findElement(By.id("openingHours.main.1.Monday.to_hour"))).selectByVisibleText("17:30");
	//selecting '15:00' in 'Open' select box for Monday in second case
    new Select(driver.findElement(By.id("openingHours.additional.1.Monday.from_hour"))).selectByVisibleText("15:00");
	//selecting '19:00' in 'Close' select box for Monday for second case
    new Select(driver.findElement(By.id("openingHours.additional.1.Monday.to_hour"))).selectByVisibleText("19:30");	
	System.out.println("OK!");	

	/* Filling Holidays forms */
	System.out.println("Filling Holidays forms...");	

	//filling 'From' input field for Holiday1
    driver.findElement(By.id("holiday.0.periodFrom")).sendKeys("12/06/13");
	//filling 'To' input field for Holiday1
    driver.findElement(By.id("holiday.0.periodTo")).sendKeys("13/06/13");	
	//entering Holiday name
    driver.findElement(By.id("holiday.0.name")).sendKeys("Holiday1");		
	//clicking on the 'Add another holiday period' link
    driver.findElement(By.id("add_holiday_item")).click();
	//filling 'From' input field for Holiday1
    driver.findElement(By.id("holiday.1.periodFrom")).sendKeys("20/06/13");
	//filling 'To' input field for Holiday1
    driver.findElement(By.id("holiday.1.periodTo")).sendKeys("30/06/13");	
	//entering Holiday name
    driver.findElement(By.id("holiday.1.name")).sendKeys("Holiday2");
	System.out.println("OK!");	

	/* Adding Additional Info */
	
	//selecting 'Sky Sports' value
    new Select(driver.findElement(By.id("infos.type.0"))).selectByVisibleText("Sky Sports");	
	//clicking on the 'Add additional info' link
    driver.findElement(By.id("add_info")).click();
	//clicking on the "Finish" button
    driver.findElement(By.name("_action_save")).click();	
	//checking that merchant page will be opened
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//h3[@class=\"info_details__title\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	//clicking on the 'Venue' tab
    driver.findElement(By.linkText("Venue")).click();	
	//checking that venue page will be opened
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//div[@class=\"photo_video\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	logout();
	System.out.println("OK!");

	/* Log out from email */
	System.out.println("Logging out from email...");	
	//opening mail.ru site
    driver.get( "www.mail.ru");	
	//log out from mail.ru
    driver.findElement(By.xpath("//a[@id=\"PH_logoutLink\"]")).click();		
	System.out.println("OK!");
	
	/* Removing created data */
	System.out.println("");	
	System.out.println("--- Removing created data... ---");	
	System.out.println("");		
	loginAsAdmin();
	removeBusiness("Auto Online Trading");
	removeUser("lyudmila_test_mm@mail.ru");
	logout();
	
	System.out.println("OK!");		
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

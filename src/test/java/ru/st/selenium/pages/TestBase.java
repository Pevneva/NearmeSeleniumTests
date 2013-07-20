package ru.st.selenium.pages;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.ScreenshotException;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import ru.st.selenium.util.PropertyLoader;
import ru.st.selenium.util.Browser;
import ru.st.selenium.webdriver.WebDriverFactory;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/*
 * Base class for all the test classes
 * 
 * @author Sebastiano Armeli-Battana
 */

public class TestBase {
	private static final String SCREENSHOT_FOLDER = "target/screenshots/";
	private static final String SCREENSHOT_FORMAT = ".png";
	private boolean acceptNextAlert = true;
//	private StringBuffer verificationErrors = new StringBuffer();
	
	protected WebDriver driver;

	protected String gridHubUrl;

	protected String baseUrl;
	
	protected String baseURL;
	
	protected Browser browser;

	@BeforeClass
	public void init() {
		baseUrl = PropertyLoader.loadProperty("site.url");
		baseURL = "http://95.110.204.46/nearme-portal/";
		gridHubUrl = PropertyLoader.loadProperty("grid2.hub");

		browser = new Browser();
		browser.setName(PropertyLoader.loadProperty("browser.name"));
		browser.setVersion(PropertyLoader.loadProperty("browser.version"));
		browser.setPlatform(PropertyLoader.loadProperty("browser.platform"));

		String username = PropertyLoader.loadProperty("user.username");
		String password = PropertyLoader.loadProperty("user.password");
		
		driver = WebDriverFactory.getInstance(gridHubUrl, browser, username,
				password);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	public void login(String UserName, String Password) throws Exception {
		System.out.println("Logging in as" + UserName + " user...");
		driver.get(baseUrl + "login");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(UserName);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(Password);
		driver.findElement(By.name("_action_save")).click();
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.xpath("//li[@class=\"profile_menu_top\"]"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
		System.out.println("OK!");
	}
	
	public void loginAsAdmin() throws Exception {
		String user="";
		if (baseUrl.equals("http://95.110.204.46/nearme-portal/")) {user="4680092575";}
		if (baseUrl.equals("https://stage.getnearme.com/")) {user="3372014222";}
		System.out.println("Logging in as "+user+" user...");	
		driver.get(baseUrl + "login");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(user);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(user);
		driver.findElement(By.name("_action_save")).click();
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.xpath("//li[@class=\"profile_menu_top\"]"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
		System.out.println("OK!");		
	}		

	public void logout() throws Exception{
		System.out.println("Logging out...");	
		driver.findElement(By.cssSelector("a.profile_menu_dropdown_link")).click();
		driver.findElement(By.linkText("Log Out")).click();	
		checkStartPage();
		System.out.println("OK!");		
	}
	
	public void checkStartPage() throws Exception {
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.xpath("//div[@class='home-login-container']"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
			}
	}	
	
	public void removeUser(String email) throws Exception {
		System.out.println("Removing user with " + email + " email...");	
		driver.findElement(By.cssSelector("#usersTab > span.nav_btn_text")).click();
		driver.findElement(By.id("keywords")).clear();
		driver.findElement(By.id("keywords")).sendKeys(email);
		driver.findElement(By.id("action_button")).click();
		if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//td[contains(text(),Email)]"))) {
			driver.findElement(By.id("selectAll")).click();
			driver.findElement(By.name("_action_bulkDelete")).click();
			assertTrue(closeAlertAndGetItsText().matches("^Are you sure[\\s\\S]$"));
			for (int second = 0;; second++) {
				if (second >= 60) fail("timeout");
				try { if (isElementPresent(By.xpath("//div[@class=\"bottom_row\"]"))) break; } catch (Exception e) {}
				Thread.sleep(1000);
			}
		}
		System.out.println("OK!");		
	}


	public void removeBusiness(String TradingName) throws Exception {
		System.out.println("Removing Business with " + TradingName + " trading name...");	
		//going to Businesses tab
		driver.findElement(By.cssSelector("#registrationsTab > span.nav_btn_text")).click();
		//entering <TradingName> text to 'Keywords' field
		driver.findElement(By.id("keywords")).clear();
		driver.findElement(By.id("keywords")).sendKeys(TradingName);
		//clicking on Search icon
		driver.findElement(By.id("action_button")).click();
		//checking whether such business was found
		if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//td[2]/a[contains(text(),TradingName)]"))) {
			//clicking on 'Select all' checkbox
			driver.findElement(By.id("selectAll")).click();
			//clicking on the "Delete" button
			driver.findElement(By.name("_action_buldDeleteMerchants")).click();
			assertTrue(closeAlertAndGetItsText().matches("^Are you sure[\\s\\S]$"));	
			for (int second = 0;; second++) {
				if (second >= 60) fail("timeout");
				try { if (isElementPresent(By.xpath("//div[@class=\"bottom_row\"]"))) break; } catch (Exception e) {}
				Thread.sleep(1000);
			}
		}
		System.out.println("OK!");
	}	

	public void addBusiness(String TradingName, String BusinessType, String NumberOfVenues) throws Exception {
    System.out.println("Adding business...");
    System.out.println("TRADING NAME:     "+TradingName);
    System.out.println("BUSINESS TYPE:     "+BusinessType);
    System.out.println("NUMBER OF VENUES:     "+NumberOfVenues);
	//going to Add Business page
	driver.findElement(By.cssSelector("span.nav_btn_arrow")).click();
    driver.findElement(By.linkText("New Business")).click();
	//checking that 'Sales Representative' select box exists on opened page
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.id("contracts[0].salesRep.id"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }		
	//selecting Agent
    new Select(driver.findElement(By.id("contracts[0].salesRep.id"))).selectByVisibleText("PLA - Lyudmila Pevneva");
	//entering Contract Number
    driver.findElement(By.id("contracts[0].contractId")).clear();
    driver.findElement(By.id("contracts[0].contractId")).sendKeys("CoNo-001");
	//selecting Contract Type
    new Select(driver.findElement(By.id("contracts[0].type.id"))).selectByVisibleText("IT_0002 : Registration NearMe + 1 Photo of the activity (24 months)");
	//selecting Payment Method
    new Select(driver.findElement(By.id("contracts[0].paymentMethod.id"))).selectByVisibleText("Paypal");
	//selecting Business Type
    new Select(driver.findElement(By.id("type.id"))).selectByVisibleText(BusinessType);
	//entering number of venues
    driver.findElement(By.id("contracts[0].numberOfVenues")).clear();
    driver.findElement(By.id("contracts[0].numberOfVenues")).sendKeys(NumberOfVenues);
	//entering Start of Data
    driver.findElement(By.xpath("//input[@id='contracts[0].startDate']")).sendKeys("01/05/13");
	//entering First Name for Merchant Manager
    driver.findElement(By.id("managers[0].firstName")).clear();
    driver.findElement(By.id("managers[0].firstName")).sendKeys("First01");
	//entering Last Name for Merchant Manager
    driver.findElement(By.id("managers[0].lastName")).clear();
    driver.findElement(By.id("managers[0].lastName")).sendKeys("Last01");
	//entering email for Merchant Manager
    driver.findElement(By.id("managers[0].email")).clear();
    driver.findElement(By.id("managers[0].email")).sendKeys("email_01@mail.ru");
	//entering Job Title for Merchant Manager	
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.jobTitle")).clear();
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.jobTitle")).sendKeys("Job Title");
	//entering National Insurance No for Merchant Manager	
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.nationalInsuranceNo")).clear();
    driver.findElement(By.id("managers[0].merchantManagerRoleDefinition.nationalInsuranceNo")).sendKeys("National Ins");
	//entering Telephone for Merchant Manager	
    driver.findElement(By.id("managers[0].contact.telephone")).clear();
    driver.findElement(By.id("managers[0].contact.telephone")).sendKeys("2220010");
	//entering Mobile for Merchant Manager	
    driver.findElement(By.id("managers[0].contact.mobile")).clear();
    driver.findElement(By.id("managers[0].contact.mobile")).sendKeys("5550020");
	//pressing on the "Continue" button
    driver.findElement(By.name("_action_saveCommercialDetails")).click();
	//checking that 'Trading Name' field exists on opened page
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.id("tradingName"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
	//entering URL
    driver.findElement(By.id("displayName")).clear();
    driver.findElement(By.id("displayName")).sendKeys("smb01");
	//entering Company No
    driver.findElement(By.id("registeredCompanyNo")).clear();
    driver.findElement(By.id("registeredCompanyNo")).sendKeys("companyNo");
	//entering Company Vat No	
    driver.findElement(By.id("registeredVatNo")).clear();
    driver.findElement(By.id("registeredVatNo")).sendKeys("ComVatNo");
	//entering Company Name
    driver.findElement(By.id("companyName")).clear();
    driver.findElement(By.id("companyName")).sendKeys("Company Name");
	//entering Trading Name
    driver.findElement(By.id("tradingName")).clear();
    driver.findElement(By.id("tradingName")).sendKeys(TradingName);
	//entering Address1
    driver.findElement(By.id("registeredAddress.address1")).clear();
    driver.findElement(By.id("registeredAddress.address1")).sendKeys("Pikadili");
	//entering City
    driver.findElement(By.id("registeredAddress.city")).clear();
    driver.findElement(By.id("registeredAddress.city")).sendKeys("London");
	//entering Postal Code
    driver.findElement(By.id("registeredAddress.postalCode")).clear();
    driver.findElement(By.id("registeredAddress.postalCode")).sendKeys("44004");
	//selecting country
    new Select(driver.findElement(By.id("registeredAddress.countryCode"))).selectByVisibleText("United Kingdom");
	//filling fields on the Contact Details section
    driver.findElement(By.id("companyContactDetails.telephone")).clear();
    driver.findElement(By.id("companyContactDetails.telephone")).sendKeys("100001");
    driver.findElement(By.id("companyContactDetails.fax")).clear();
    driver.findElement(By.id("companyContactDetails.fax")).sendKeys("100002");
    driver.findElement(By.id("companyContactDetails.email")).clear();
    driver.findElement(By.id("companyContactDetails.email")).sendKeys("Email@optional.net");
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
	//pressing on the "Continue" button
    driver.findElement(By.name("_action_saveMerchantDetails")).click();
	//checking that 'Keywords' field exists on opened page
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.id("keywords.ti1"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
	//selecting Category
    driver.findElement(By.xpath("//div[@class='pickList']/div/ul/li[contains(text(),'Attraction')]")).click();
    driver.findElement(By.cssSelector("button.plus_btn")).click();
	//entering Keyword
    driver.findElement(By.id("keywords.ti1")).clear();
    driver.findElement(By.id("keywords.ti1")).sendKeys("Key Word");
	//selecting 'Yes' value for 'Show the Additional Info Page in the App' select box
    new Select(driver.findElement(By.id("isAdditionalInfoForClientEnabled"))).selectByVisibleText("Yes");
	//press on the "Continue" button
    driver.findElement(By.name("_action_saveAndReview")).click();
	//checking that 'Business Added' text is shown
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//h1[contains(text(),'Business Added')]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!" );	
}	

public void approveBusiness(String TradingName) throws Exception{
	/* Approving business */

	System.out.println("Approving new business...");	
	//going to Businesses tab
    driver.findElement(By.cssSelector("#registrationsTab > span.nav_btn_text")).click();
    //entering 'Auto Online Trading' text to 'Keywords' field
	driver.findElement(By.id("keywords")).clear();
    driver.findElement(By.id("keywords")).sendKeys(TradingName);
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
}

public void checkFirstEmailOfOnlineRegistration(String mailruEmail) throws Exception{
	//opening mail.ru site
	System.out.println("Going to mail.ru and logging in as " + mailruEmail + " user...");	
    driver.get( "www.mail.ru");
	//going to test email
    driver.findElement(By.id("mailbox__login")).clear();
    driver.findElement(By.id("mailbox__login")).sendKeys(mailruEmail);	
    driver.findElement(By.id("mailbox__password")).clear();
    driver.findElement(By.id("mailbox__password")).sendKeys("test12345");
    driver.findElement(By.id("mailbox__auth__button")).click();
	System.out.println("OK!");
	Thread.sleep(1000);	
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
	driver.findElement(By.xpath("//div[@id=\"ML0\"]/div[1]//span[contains(text(),'Registration in NearMe')]")).click();
	System.out.println("OK!");	
	//log out from 'lyudmila_test_mm@mail.ru' email
	System.out.println("Logging out from " + mailruEmail + " email");	
    driver.findElement(By.xpath("//a[@id=\"PH_logoutLink\"]")).click();	
	System.out.println("OK!");
}

public void checkSecondEmailOfOnlineRegistration(String mailruEmail)throws Exception{
	/* Checking that emails notifications were sent */

	System.out.println("Checking that emails notifications were sent to " + mailruEmail + "...");
	//opening mail.ru site
    driver.get( "www.mail.ru");
	//going to test email
    driver.findElement(By.id("mailbox__login")).clear();
    driver.findElement(By.id("mailbox__login")).sendKeys(mailruEmail);	
    driver.findElement(By.id("mailbox__password")).clear();
    driver.findElement(By.id("mailbox__password")).sendKeys("test12345");
    driver.findElement(By.id("mailbox__auth__button")).click();
	//waiting until subject of 3rd notification will not be 'Registration in NearMe' text (2 emails will not be sent)
	System.out.println("Taking subject of 3rd message of lyudmila_test_mm@mail.ru email and waiting until it will not be 'Registration in NearMe'... ");
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
	//waiting until subject of 2rd notification will NOT be 'Registration in NearMe' text 
	System.out.println("Taking subject of 2rd message of lyudmila_test_mm@mail.ru email and waiting until it will NOT be 'Registration in NearMe'... ");
	for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { 
			S1  = driver.findElement(By.xpath("//div[@id=\"ML0\"]/div[2]//span[contains(text(),'')]")).getText();
			System.out.println("S1 = "+S1);
			if (!S1.contains("Registration in NearMe")) 
			break; 
			} catch (Exception e) {}
    	Thread.sleep(1000);
    }		
	//opening first messages
	System.out.println("Opening first message of " + mailruEmail + " email...");
	driver.findElement(By.cssSelector("span.messageline__body__name")).click();
	System.out.println("OK!");	
	//taking subject of email
	System.out.println("Taking subject of first message of " + mailruEmail + " email...");	
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
}

public void checkPendingBusiness() throws Exception {
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
}

public void checkDisabledUser(String email) throws Exception{
	/* Checking that User with <email> email was created and has 'Disabled' status */

	System.out.println("Checking that User with " + email + " email was created and has 'Disabled' status...");	
	//Going to Users tab
	driver.findElement(By.cssSelector("#usersTab > span.nav_btn_text")).click();
	//entering <email> to "Keywords' field
	driver.findElement(By.id("keywords")).clear();
	driver.findElement(By.id("keywords")).sendKeys(email);
	driver.findElement(By.id("action_button")).click();	
	//checking that "searchResultList" table contains <email> text
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//td[contains(text(),'" + email + "')]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }	
	//checking that "searchResultList" table contains 'status_rejected' class	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//table[@id=\"searchResultList\"]//div[@class='status_rejected']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
	System.out.println("OK!");
}

public void clickClickHereLinkAndFindCompleteBRwindow()throws Exception {
	//clicking on the 'click here' link
	System.out.println("Clicking on the 'click here' link...");	
    driver.findElement(By.linkText("click here")).click();
    //finding window with 'Complete Business Registration' title and go to it
    for (String handle : driver.getWindowHandles())
       {
       driver.switchTo().window(handle);
	   Thread.sleep(2000);
       if (driver.getTitle().equals("Complete Business Registration")){break;};
       }
	Thread.sleep(2000);	   
	System.out.println("OK!");	
}

public void finalRegistrationStepOfOnlineRegistration() throws Exception {

	/*  Completing final registration step */

	System.out.println("--- Completing final registration step... ---");	
	//checking that 'Complete Business Registration' page was opened
	System.out.println("Checking that 'Complete Business Registration' page was opened...");	
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//input[@id='managers[0].firstName']"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }
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
	Thread.sleep(1000);
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
}

public void logoutFromMailru() throws Exception{
	/* Log out from email */
	System.out.println("Logging out from email...");	
	//opening mail.ru site
    driver.get( "www.mail.ru");	
	//log out from mail.ru
    driver.findElement(By.xpath("//a[@id=\"PH_logoutLink\"]")).click();		
	System.out.println("OK!");
}	

public static String postRequest(String request_uri) throws Exception { 
    DefaultHttpClient httpclient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(request_uri);		
        try {
		System.out.println("executing request:" );
		System.out.println(httpPost.getURI());
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpPost, responseHandler);
		return responseBody;
	    } 
		finally { httpPost.releaseConnection();}	
}	

public static String postRequestWithToken(String request_uri, String token) throws Exception { 
    DefaultHttpClient httpclient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(request_uri);		
	httpPost.setHeader("session_token",token);	
    try {
		System.out.println(httpPost.getURI());
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httpPost, responseHandler);
		return responseBody;
	    } 
	finally { httpPost.releaseConnection();}	
}

  public static boolean	checkResponse(String str, String subStr) throws Exception {
		boolean b;
		int otv =str.indexOf(subStr);
			if (otv>=0) {b=true;} 
			else {
			System.out.println("Response doesn't contain "+ subStr); 
			b=false;
			assertEquals(b,true);} 
		return b;
  }

	public static int	indexOfToken(String str, String subStr) throws Exception {
		int otv =str.indexOf(subStr);
		return otv;
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
//	@AfterMethod
//	public void setScreenshot(ITestResult result) {
//		if (!result.isSuccess()) {
//			try {
//				WebDriver returned = new Augmenter().augment(driver);
//				if (returned != null) {
//					File f = ((TakesScreenshot) returned)
//							.getScreenshotAs(OutputType.FILE);
//					try {
//						FileUtils.copyFile(f, new File(SCREENSHOT_FOLDER
//								+ result.getName() + SCREENSHOT_FORMAT));
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			} catch (ScreenshotException se) {
//				se.printStackTrace();
//			}
//		}
//	}
}

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

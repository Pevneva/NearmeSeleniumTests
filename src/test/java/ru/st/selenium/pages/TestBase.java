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
		try{
		driver.get(baseURL + "auth/login");
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
		} catch (Exception e) {}
	}
	
		public void loginAsAdmin() throws Exception {
		try{
		driver.get(baseURL + "auth/login");
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("4680092575");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4680092575");
		driver.findElement(By.name("_action_save")).click();
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.xpath("//li[@class=\"profile_menu_top\"]"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
			}		
		} catch (Exception e) {}
	}	

		public void logout() throws Exception{
		try{
		driver.findElement(By.cssSelector("a.profile_menu_dropdown_link")).click();
		driver.findElement(By.linkText("Log Out")).click();	
		checkStartPage();		
		} catch (Exception e) {}
	}
	
		public void checkStartPage() throws Exception {
		try{
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.id("headerSearchQuery"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
			}
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.id("headerSearchLocation"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
			}
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.xpath("//div[@class=\"topnav\"]/a[contains(@href,'signup')]"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
			}
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (isElementPresent(By.xpath("//div[@class=\"topnav\"]/a[contains(@href,'login')]"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
			}	
		} catch (Exception e) {}
	}	
	
		public void removeUser(String Email) throws Exception {
		try{
		driver.findElement(By.cssSelector("#usersTab > span.nav_btn_text")).click();
		driver.findElement(By.id("keywords")).clear();
		driver.findElement(By.id("keywords")).sendKeys(Email);
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
		} catch (Exception e) {}
	}


		public void removeBusiness(String TradingName) throws Exception {
		try{
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
		} catch (Exception e) {}		
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

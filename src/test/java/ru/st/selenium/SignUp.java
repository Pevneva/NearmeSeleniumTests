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
  driver.manage().window().maximize();
  driver.get("http://95.110.204.46/nearme-portal/auth/consumer/signup");
    driver.findElement(By.id("firstName")).clear();
    driver.findElement(By.id("firstName")).sendKeys("First01");
    driver.findElement(By.id("lastName")).clear();
    driver.findElement(By.id("lastName")).sendKeys("Last01");
    new Select(driver.findElement(By.id("gender"))).selectByVisibleText("I'd rather not say");
    driver.findElement(By.id("birthday")).clear();
    driver.findElement(By.id("birthday")).sendKeys("01/04/80");
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys("123@123.com");
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("Username01");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("12345678");
    new Select(driver.findElement(By.id("countryCode"))).selectByVisibleText("Belarus");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//*[@id=\"homeCity\"]/*[@value=\"Minsk\"]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    new Select(driver.findElement(By.id("homeCity"))).selectByVisibleText("Minsk (Horad Minsk)");
    driver.findElement(By.name("_action_join")).click();
    driver.findElement(By.xpath("//div[@class='wrapper']/div/a[contains(@href,'login')]")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("4680092575");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("4680092575");
    driver.findElement(By.name("_action_save")).click();
    driver.findElement(By.cssSelector("#usersTab > span.nav_btn_text")).click();
    driver.findElement(By.id("keywords")).clear();
    driver.findElement(By.id("keywords")).sendKeys("123@123.com");
    driver.findElement(By.id("action_button")).click();
    driver.findElement(By.id("selectAll")).click();
    driver.findElement(By.name("_action_bulkDelete")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Are you sure[\\s\\S]$"));
    driver.findElement(By.cssSelector("a.profile_menu_dropdown_link")).click();
    driver.findElement(By.linkText("Log Out")).click();
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

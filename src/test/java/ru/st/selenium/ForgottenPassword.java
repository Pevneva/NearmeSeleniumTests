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

public class ForgottenPassword extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Test
  public void testForgottenPassword() throws Exception {    driver.get("http://95.110.204.46/nearme-portal/");
    driver.findElement(By.linkText("Log In")).click();
    driver.findElement(By.linkText("exact:Forgotten your password?")).click();
    driver.findElement(By.linkText("Cancel")).click();
    driver.findElement(By.linkText("exact:Forgotten your password?")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("4680092575");
    driver.findElement(By.name("_action_forgotPassword")).click();
    driver.findElement(By.linkText("Go to Log In screen")).click();
    driver.findElement(By.linkText("exact:Forgotten your password?")).click();
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys("millo4ka@list.ru");
    driver.findElement(By.name("_action_forgotPassword")).click();
    driver.findElement(By.linkText("Go to Log In screen")).click();
    driver.findElement(By.linkText("exact:Forgotten your password?")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("1");
    driver.findElement(By.name("_action_forgotPassword")).click();
    driver.findElement(By.linkText("Cancel")).click();
    driver.findElement(By.linkText("exact:Forgotten your password?")).click();
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys("2");
    driver.findElement(By.name("_action_forgotPassword")).click();
    driver.findElement(By.linkText("Cancel")).click();
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

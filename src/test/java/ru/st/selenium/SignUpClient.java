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

import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;



public class SignUpClient extends ru.st.selenium.pages.TestBase {
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  String respBody;
  

  @Test
  public void testSignUpClient() throws Exception { 

		System.out.println("========== SIGN UP CLIENT ==========");  
		driver.manage().window().maximize();
		
		//Removing user with 'lyudmila_test_03@mail.ru' email
		System.out.println("Removing user with 'lyudmila_test_03@mail.ru' email...");		
		loginAsAdmin();
		removeUser("lyudmila_test_03@mail.ru");
		logout();
		
		//Executing API Sign Up process
		System.out.println("Executing API Sign Up process...");
		String signupUri="http://95.110.204.46/nearme-portal/api/v1/signup?hash=3/3sVlGXuPb/IYUqbflui5DwjgIEJHAtCcYoJGrefVE=&email=lyudmila_test_03@mail.ru";
		respBody=postRequest(signupUri);
		System.out.println("Response:");
		System.out.println(respBody);
		
		//Checking that Response Body is correct
		System.out.println("Checking that Response Body is correct...");
		checkResponse(respBody,"\"code\": 200");
		checkResponse(respBody,"\"username\": \"TestUserName02\"");
		checkResponse(respBody,"\"firstName\": \"__first_name__\"");
		checkResponse(respBody,"\"lastName\": \"__last_name__\"");
		checkResponse(respBody,"\"email\": \"lyudmila_test_03@mail.ru\"");
		checkResponse(respBody,"\"accountType\": \"consumer\"");
		checkResponse(respBody,"\"accountStatus\": \"active\"");
		checkResponse(respBody,"\"birthday\": null");
		checkResponse(respBody,"\"gender\": null");
		checkResponse(respBody,"\"hometown\": null");
		checkResponse(respBody,"\"photoUrl\": null");
		System.out.println("OK");
		
		//Taking token for logout
		System.out.println("Taking token...");
		int num=indexOfToken(respBody,"\"token\"");
		String token=respBody.substring(num+10, num+20);
		int num2=token.indexOf("\"");
		if (num2>-1) {			
			token=token.substring(0,num2);
			}
		System.out.println("token="+token);
		
		//Executing API Log Out process with taken token
		System.out.println("Executing API Log Out with taken token...");
		String logoutUri="http://95.110.204.46/nearme-portal/api/v1/logout?hash=3/3sVlGXuPb/IYUqbflui5DwjgIEJHAtCcYoJGrefVE=";
		respBody=postRequestWithToken(logoutUri,token);
		System.out.println("Response:");
		System.out.println(respBody);

		System.out.println("Checking that Response Body is correct...");
		checkResponse(respBody,"\"message\": \"User was successfully logged out\"");		
		System.out.println("OK");
		
		//Executing API Log In process
		System.out.println("Executing API Log In process...");
		String loginUri="http://95.110.204.46/nearme-portal/api/v1/login?hash=3/3sVlGXuPb/IYUqbflui5DwjgIEJHAtCcYoJGrefVE=";
		respBody=postRequest(loginUri);
		System.out.println("Response:");
		System.out.println(respBody);
		checkResponse(respBody,"\"email\": \"lyudmila_test_03@mail.ru\"");

		//Executing API Forgot process
		System.out.println("Executing API Forgot process...");
		String forgotUri="http://95.110.204.46/nearme-portal/api/v1/forgot?user=lyudmila_test_03@mail.ru";
		respBody=postRequest(forgotUri);		
		System.out.println("Response:");
		System.out.println(respBody);
		checkResponse(respBody,"\"message\": \"Password successfully changed and sent to User's email\"");
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

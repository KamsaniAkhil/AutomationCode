package com.ots.test.ui;

import java.lang.reflect.Constructor;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.api.services.gmail.Gmail.Users.Drafts.List;
import com.ots.test.ui.common.utils.UITestSeleniumHelper;

@Listeners({ com.ots.test.ui.common.utils.TestNGCustomResults.class })

public class LoginTest   {

	Logger logger = LoggerFactory.getLogger("LoginTest");

	public static String currentRunningMethodName = "";
	private int retryCount = 0;
	private int maxRetryCount = 1;
	public static String username = System.getProperty("email", "v-arungopi@aims360.com");
	public static String password = System.getProperty("password", "Creai@ims22!@");

	private String countNotification = null;
	// Helper classes
	UITestSeleniumHelper webAppHelper = new UITestSeleniumHelper();

	public static WebDriver driver;
	Calendar cal = Calendar.getInstance();

	// Getting present day
	int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

	@BeforeClass
	public void setup() {
		webAppHelper.openBrowser("chrome");
		webAppHelper.maximizeWindow();

		logger.info("Navigating to the URL");
		webAppHelper.navigateTo("https://preprod-creai.aims360.com");
		webAppHelper.waitSimply(3);
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		currentRunningMethodName = method.getName();
	}


	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			System.out.println("Retrying test " + result.getName() + " with status "
					+ getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
			retryCount++;

			return true;
		}
		return false;
	}

	
	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		IRetryAnalyzer retry = testannotation.getRetryAnalyzer();

		if (retry == null) {
			testannotation.setRetryAnalyzer((Class<? extends IRetryAnalyzer>) LoginTest.class);
		}

	}

	public String getResultStatusName(int status) {
		String resultName = null;
		if (status == 1)
			resultName = "SUCCESS";
		else if (status == 2)
			resultName = "FAILURE";
		else if (status == 3)
			resultName = "SKIP";
		return resultName;
	}

	@Test(enabled = true, priority = 1)
	public void AppLogTest() {

		webAppHelper.waitSimply(3);
		logger.info("Getting logo path");
		String imgpath = webAppHelper.findElementByXPath("//div[@class='logo']//img").getAttribute("src");

		logger.info("Asserting logo");
		Assert.assertTrue(imgpath.contains("top-nav-logo-New"));

	}

	@Test(enabled = true, priority = 2, dependsOnMethods = "AppLogTest")

	public void loginText() {

		logger.info("Getting login text");
		String text = webAppHelper.findElementByXPath("//div[@class='logindiv']//h3").getText();

		logger.info("Asserting Login");
		Assert.assertEquals(text, "Login");

		logger.info("Getting Login Icon class attribute");
		String LoginIcon = webAppHelper.findElementByXPath("//*[text()=' Login ']//child::span").getAttribute("class");

		logger.info("Asserting Login Icon Symbol class");
		Assert.assertTrue(LoginIcon.contains(" glyphicon-lock "));
	}

	@Test(enabled = true, priority = 3, dependsOnMethods = "AppLogTest")

	public void userNameText() {
		logger.info(" getting UserName Text");

		String userNameText = webAppHelper
				.findElementByXPath("//input[@id='email']//parent::div//parent::div//child::label").getText();

		logger.info("Asserting Username text");
		Assert.assertTrue(userNameText.contains("Username:"));
	}

	@Test(enabled = true, priority = 4, dependsOnMethods = "userNameText")

	public void userNameComponent() {

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("Getting UserName component value");
		boolean flag = userNameComponent.isDisplayed();

		logger.info("Asserting UserName component");
		Assert.assertTrue(flag);
	}

	@Test(enabled = true, priority = 5, dependsOnMethods = "userNameComponent")

	public void userNamePlaceholder() {

		logger.info("Getting email placeholder text");
		String emailText = webAppHelper.findElementByXPath("//input[@type='email']").getAttribute("placeholder");

		logger.info("Asserting email placeholder text");
		Assert.assertEquals(emailText, "Enter email");

	}

	@Test(enabled = true, priority = 6, dependsOnMethods = "userNamePlaceholder")

	public void passwordText() {

		logger.info(" getting Password Text");

		String passwordText = webAppHelper
				.findElementByXPath("//input[@id='pwd']//parent::div//parent::div//child::label").getText();

		logger.info("Asserting Password text");
		Assert.assertTrue(passwordText.contains("Password:"));

	}

	@Test(enabled = true, priority = 7, dependsOnMethods = "passwordText")

	public void passwordComponent() {

		logger.info("Getting password component");
		WebElement passwordComponent = webAppHelper.findElementById("pwd");

		logger.info("Getting password component is displayed");
		boolean passwordflag = passwordComponent.isDisplayed();

		logger.info("Asserting password value");
		Assert.assertTrue(passwordflag);

	}

	@Test(enabled = true, priority = 8, dependsOnMethods = "passwordComponent")

	public void passwordPlaceholder() {

		logger.info("Getting password placeholder text");
		String passwordText = webAppHelper.findElementByXPath("//input[@type='password']").getAttribute("placeholder");

		logger.info("Asserting password placeholder text");
		Assert.assertEquals(passwordText, "Enter password");

	}

	@Test(enabled = true, priority = 9, dependsOnMethods = "passwordPlaceholder")

	public void fieldValidationUserName() {

		String starValidationUsername = webAppHelper.findElementsByXPath("//span[@class='field-validation-error']")
				.get(0).getText();

		logger.info("Asserting Username star validation error symbol");
		Assert.assertEquals(starValidationUsername, "*");

	}

	@Test(enabled = true, priority = 10, dependsOnMethods = "fieldValidationUserName")

	public void fieldValidationPassword() {

		String starValidationPassword = webAppHelper.findElementsByXPath("//span[@class='field-validation-error']")
				.get(1).getText();

		logger.info("Asserting Username star validation error symbol");
		Assert.assertEquals(starValidationPassword, "*");
	}

	@Test(enabled = true, priority = 11, dependsOnMethods = "fieldValidationPassword")

	public void signInComponent() {

		logger.info("Getting SignIn component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		logger.info("Getting SignIn component displayed");
		boolean signInflag = signInComponent.isDisplayed();

		logger.info("Asserting SignIn value");
		Assert.assertTrue(signInflag);

	}

	@Test(enabled = true, priority = 12, dependsOnMethods = "signInComponent")
	public void sendUserName() {
		
		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send text in email component");
		userNameComponent.sendKeys(username);
	}

	@Test(enabled = true, priority = 13, dependsOnMethods = "sendUserName")
	public void sendPassword() {

		logger.info("Getting password component");
		WebElement passwordComponent = webAppHelper.findElementById("pwd");

		logger.info("sending text in password component");
		passwordComponent.sendKeys(password);

	}

	@Test(enabled = true, priority = 14, dependsOnMethods = "sendPassword")
	public void signInTest() {

		webAppHelper.waitSimply(3);

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();

	}

	@Test(enabled = true, priority = 15, dependsOnMethods = "signInTest")
	public void logOut() {
	
		logger.info("Selecting  profile icon");
		webAppHelper.findElementByXPath("//div[contains(@class ,'c-avatar')]").click();

		logger.info("clicking Logout option");
		webAppHelper.findElementByXPath("//a[text()='Logout']").click();

		webAppHelper.waitSimply(5);

		logger.info("Getting login text");
		String text = webAppHelper.findElementByXPath("//div[@class='logindiv']//h3").getText();

		logger.info("Asserting Login");
		Assert.assertEquals(text, "Login");
		
		
		
	}
	

	@AfterClass
	public void unsetup() {
		webAppHelper.quitBrowser();
	}
}

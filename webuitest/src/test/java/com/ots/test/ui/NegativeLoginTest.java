package com.ots.test.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.gmail.Gmail.Users.Drafts.List;
import com.ots.test.ui.common.utils.UITestSeleniumHelper;

@Listeners({ com.ots.test.ui.common.utils.TestNGCustomResults.class })

public class NegativeLoginTest implements IRetryAnalyzer, IAnnotationTransformer {

	Logger logger = LoggerFactory.getLogger("LoginTest");

	public static String currentRunningMethodName = "";
	private int retryCount = 0;
	private int maxRetryCount = 1;
	public static String username = System.getProperty("email", "v-arungopi@aims360.com");
	public static String password = System.getProperty("password", "Creai@ims22!@");
	// Helper classes
	UITestSeleniumHelper webAppHelper = new UITestSeleniumHelper();

	public static WebDriver driver;

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

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			System.out.println("Retrying test " + result.getName() + " with status "
					+ getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
			retryCount++;

			return true;
		}
		return false;
	}

	@Override
	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		IRetryAnalyzer retry = testannotation.getRetryAnalyzer();

		if (retry == null) {
			testannotation.setRetryAnalyzer(NegativeLoginTest.class);
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
	public void LoginTitleName() {

		webAppHelper.waitSimply(3);
		logger.info("Getting login text");
		String text = webAppHelper.findElementByXPath("//div[@class='logindiv']//h3").getText();

		logger.info("Asserting Login");
		Assert.assertEquals(text, "Login");

		logger.info("Getting Login Icon class attribute");
		String LoginIcon = webAppHelper.findElementByXPath("//*[text()=' Login ']//child::span").getAttribute("class");

		logger.info("Asserting Login Icon Symbol class");
		Assert.assertTrue(LoginIcon.contains(" glyphicon-lock "));

	}

	@Test(enabled = true, priority = 2)
	public void LoginComponents() {
		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("Getting UserName component value");
		boolean flag = userNameComponent.isDisplayed();

		logger.info("Asserting UserName component");
		Assert.assertTrue(flag);

		logger.info("Getting password component");
		WebElement passwordComponent = webAppHelper.findElementById("pwd");

		logger.info("Getting password component is displayed");
		boolean passwordflag = passwordComponent.isDisplayed();

		logger.info("Asserting password value");
		Assert.assertTrue(passwordflag);

		logger.info("Getting SignIn component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		logger.info("Getting SignIn component displayed");
		boolean signInflag = signInComponent.isDisplayed();

		logger.info("Asserting SignIn value");
		Assert.assertTrue(signInflag);

	}

	@Test(enabled = true, priority = 3)
	public void loginWithoutDetails() {

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();

		logger.info("Getting login title text");
		String text = webAppHelper.findElementByXPath("//div[@class='logindiv']//h3").getText();

		logger.info("Asserting Login title name");
		Assert.assertEquals(text, "Login");

	}

	@Test(enabled = true, priority = 4)
	public void validUserNameTextAndBlankPassword() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(4);

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send text in email component");
		userNameComponent.sendKeys(username);

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();

		logger.info("Getting Login Welcome message");
		String welcomeAFtech = webAppHelper.findElementByXPath("//div[contains(@class , 'col-lg-6')]//child::h2")
				.getText();

		logger.info("Asserting a Welcome Login message");
		Assert.assertEquals(welcomeAFtech, "Welcome to AF Technology Solutions Inc.");
	}

	@Test(enabled = true, priority = 5)
	public void invalidUserNameSendOnlyName() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(4);

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send only name without any @ symbol and .com extension ");
		userNameComponent.sendKeys("varungopi");

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();
		webAppHelper.waitSimply(3);

		logger.info("Getting logo path in Welcome Login page");
		String imgpath = webAppHelper.findElementByXPath("//div[@class='logo']//img").getAttribute("src");

		logger.info("Asserting logo in welcome login page");
		Assert.assertTrue(imgpath.contains("top-nav-logo-New"));
		webAppHelper.waitSimply(3);

	}

	@Test(enabled = true, priority = 6)
	public void invalidUserNameSendOnlyNameTest() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(4);

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("Send only name with  @aims and Without .com extension ");
		userNameComponent.sendKeys("varungopi@aims");

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();
		webAppHelper.waitSimply(3);

		logger.info("Getting login title text");
		String text = webAppHelper.findElementByXPath("//div[@class='logindiv']//h3").getText();

		logger.info("Asserting Login");
		Assert.assertEquals(text, "Login");

	}

	@Test(enabled = true, priority = 7)
	public void invalidUserNameSendOnlyNameTest1() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(4);

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send only name with any @ symbol and .co extension ");
		userNameComponent.sendKeys("arungopi@aims.co");

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();
		webAppHelper.waitSimply(3);

		logger.info("Getting logo path in Welcome Login page");
		String imgpath = webAppHelper.findElementByXPath("//div[@class='logo']//img").getAttribute("src");

		logger.info("Asserting logo in welcome login page");
		Assert.assertTrue(imgpath.contains("top-nav-logo-New"));
		webAppHelper.waitSimply(3);

	}

	@Test(enabled = true, priority = 8)
	public void validPasswordAndBlankUserName() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(4);

		logger.info("getting password component");
		WebElement passwordComponent = webAppHelper.findElementByName("password");

		logger.info("send empty text in password component");
		passwordComponent.sendKeys("Creai@ims22!@");

		webAppHelper.waitSimply(2);
		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();
		logger.info("Getting Login Welcome message");
		String welcomeAFtech = webAppHelper.findElementByXPath("//div[contains(@class , 'col-lg-6')]//child::h2")
				.getText();

		logger.info("Asserting a Welcome Login message");
		Assert.assertEquals(welcomeAFtech, "Welcome to AF Technology Solutions Inc.");

	}

	@Test(enabled = true, priority = 9)
	public void invalidUserNameTextAndValidPassword() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(4);

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send text in email component");
		userNameComponent.sendKeys("varungopi@aims.com");

		logger.info("getting password component");
		WebElement passwordComponent = webAppHelper.findElementByName("password");

		logger.info("send valid password in password component");
		passwordComponent.sendKeys("Creai@ims22!@");

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();
		webAppHelper.waitSimply(3);

		logger.info("Getting Error  message in Login page ");
		String welcomeAFtech = webAppHelper.findElementByXPath("//div[contains(@class , 'col-lg-6')]//child::b")
				.getText();

		logger.info("Asserting a Welcome Login message");
		Assert.assertTrue(welcomeAFtech.contains(" Please check the credentials that you have entered."));

	}

	@Test(enabled = true, priority = 10)
	public void invalidUserNameAndPassWord() {

		logger.info("Navigating to the URL");
		webAppHelper.navigateTo("https://preprod-creai.aims360.com");

		webAppHelper.waitSimply(4);

		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send text in email component");
		userNameComponent.sendKeys("arungopi@aims.com");

		logger.info("getting password component");
		WebElement passwordComponent = webAppHelper.findElementByName("password");

		logger.info("send valid password in password component");
		passwordComponent.sendKeys("creaiims22");

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();
		webAppHelper.waitSimply(3);

		logger.info("Getting Error  message in Login page ");
		String welcomeAFtech = webAppHelper.findElementByXPath("//div[contains(@class , 'col-lg-6')]//child::b")
				.getText();

		logger.info("Asserting a Welcome Login message");
		Assert.assertTrue(welcomeAFtech.contains(" Please check the credentials that you have entered."));

	}

	@Test(enabled = true, priority = 11)

	public void login() {

		logger.info("Navigating to the URL");
		webAppHelper.navigateTo("https://preprod-creai.aims360.com");

		webAppHelper.waitSimply(4);
		logger.info("Getting UserName component");
		WebElement userNameComponent = webAppHelper.findElementById("email");

		logger.info("send text in email component");
		userNameComponent.sendKeys(username);

		logger.info("Getting password component");
		WebElement passwordComponent = webAppHelper.findElementById("pwd");

		logger.info("sending text in password component");
		passwordComponent.sendKeys(password);

		webAppHelper.waitSimply(3);

		logger.info("Getting Sign In component");
		WebElement signInComponent = webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]");

		webAppHelper.waitSimply(3);
		logger.info("clicking Sign In component");
		signInComponent.click();

	}

	@Test(enabled = true, priority = 12)
	public void otsDateTestCheckEmptyDate() {

		webAppHelper.waitSimply(3);

		logger.info("clicking reports");

		webAppHelper.findElementByXPath("//*[text()='Reports']").click();

		logger.info("clicking Ots By Date ");
		webAppHelper.findElementByXPath("//a[text()='OTS By Date']").click();

		logger.info("Clicking Include OTS As od Date dropdown icon");
		webAppHelper.findElementByXPath("//div[@class='w-100p']//child::select").click();

		logger.info("clicking Yes option");
		webAppHelper.waitSimply(1);
		webAppHelper.findElementByXPath("//option[text()='Yes']").click();

		logger.info("Clicking Include OTS As od Date dropdown icon");
		webAppHelper.findElementByXPath("//div[@class='w-100p']//child::select").click();

		webAppHelper.waitSimply(2);
		logger.info("Gettting plus field");
		String symbol = webAppHelper.findElementByXPath("//div[@class='d-flex']//following-sibling::form")
				.getAttribute("class");

		logger.info("Asserting plus field");
		Assert.assertEquals(symbol, "mt-3");

		logger.info("Adding date calender fieldbox");
		webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

	}

	@Test(enabled = true, priority = 13, dependsOnMethods = "otsDateTestCheckEmptyDate")
	public void otsDateEmptyDateFieldAndErrorMessage() {

		logger.info("Getting date calender field box before adding");
		int dateCalender = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		logger.info("Asserting of count  Date Calender count");
		Assert.assertTrue(dateCalender == 1);

		logger.info("Getting error message text");
		String otsDateError = webAppHelper.findElementByXPath("//small[@class='text-danger']").getText();

		logger.info("Asserting OTS Date Error Text");
		Assert.assertEquals(otsDateError, "Fields can't be empty");

	}

	@Test(enabled = true, priority = 14, dependsOnMethods = "otsDateEmptyDateFieldAndErrorMessage")
	public void otsDateCSSTest() {

		webAppHelper.waitSimply(2);

		logger.info("Gettting plus field");
		String symbol = webAppHelper.findElementByXPath("//div[@class='d-flex']//following-sibling::form")
				.getAttribute("class");

		logger.info("Asserting plus field");
		Assert.assertEquals(symbol, "mt-3");

		logger.info("Getting CSS class name for OTS Date field");
		String otsDateCSSText = webAppHelper.findElementByXPath("//div[contains(@class , ' pr-2')]//child::input")
				.getAttribute("id");

		logger.info("Asserting OTS Date field css class");
		Assert.assertEquals(otsDateCSSText, "setBorder");

	}

	@Test(enabled = true, priority = 15)
	public void runReportWithEmptyDate() {

		webAppHelper.refreshBrowser();
		logger.info("Getting Run Report Enabled class name");
		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");

		logger.info("Asserting Run Report Enabled class name ");
		if (runReport.contains("enableDiv")) {

			logger.info("Clicking Include OTS As od Date dropdown icon");
			webAppHelper.findElementByXPath("//div[@class='w-100p']//child::select").click();

			logger.info("clicking Yes option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//option[text()='Yes']").click();

			logger.info("Clicking Include OTS As od Date dropdown icon");
			webAppHelper.findElementByXPath("//div[@class='w-100p']//child::select").click();

			logger.info("Getting Run Report Enabled class name");
			String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportAfter.contains("setBgColor custom_opacity"));

		}

	}

	@Test(enabled = true, priority = 16)
	public void otsDatePastDateTest() {

		logger.info("Getting date calender field box before adding");
		int dateCalenderBefore = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		logger.info("Getting date field component");
		WebElement dateBox1 = webAppHelper.findElementByXPath("//input[@class='form-control']");

		logger.info("Asserting a date in date field textbox");
		dateBox1.sendKeys("08/12/2022");
		webAppHelper.waitSimply(3);

		logger.info("Adding date calender fieldbox");
		webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

		logger.info("Getting date calender fieldbox count after adding");
		int dateCalenderAfter = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		logger.info("Asserting of count  Date Calender count");
		Assert.assertTrue(dateCalenderBefore == dateCalenderAfter);

	}

	@Test(enabled = true, priority = 17, dependsOnMethods = "otsDatePastDateTest")
	public void otsDatePastDateErrorText() {

		webAppHelper.waitSimply(2);
		logger.info("Getting error message text");
		String otsDateError = webAppHelper.findElementByXPath("//small[@class='text-danger']").getText();

		logger.info("Asserting OTS Date Error Text");
		Assert.assertEquals(otsDateError, "Date must be future date or greater than previous date");

	}

	@Test(enabled = true, priority = 18, dependsOnMethods = "otsDatePastDateErrorText")
	public void otsDatecssAndRunReportTest() {

		webAppHelper.waitSimply(3);

		logger.info("Getting CSS class name for OTS Date field");
		String otsDateCSSText = webAppHelper
				.findElementByXPath("//div[@class='react-datepicker__input-container']//child::input")
				.getAttribute("id");

		logger.info("Asserting OTS Date field css class");
		Assert.assertEquals(otsDateCSSText, "setBorder");

	}

	@Test(enabled = true, priority = 19)
	public void otsDatefutureDate() {
		
		logger.info("Getting Run Report Enabled class name");
 		String runReportBefore = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");
		
		logger.info("Asserting Run Report Enabled class name ");
		if(runReportBefore.contains("setBgColor custom_opacity")) {

		logger.info("Clicking Date field text box");
		webAppHelper.findElementByXPath("//div[@class='react-datepicker__input-container']//child::input").click();

		logger.info("Clicking arrow icon next month in Date picker");
		webAppHelper.findElementByXPath("//button[contains(@class , ' react-datepicker__navigation--next')]").click();
		webAppHelper.findElementByXPath("//button[contains(@class , ' react-datepicker__navigation--next')]").click();
		

		logger.info("Getting future Day of Month class name");
		webAppHelper.findElementByXPath("//*[text() = '10' ]").click();
		webAppHelper.waitSimply(4);

		logger.info("Getting Run Report Enabled class name");
		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");

		logger.info("Asserting Run Report Enabled class name ");
		Assert.assertTrue(runReport.contains("enableDiv"));
		
		}

	}

	@Test(enabled = true, priority = 20, dependsOnMethods = "otsDatefutureDate")
	public void otsDateAddDateFieldFutureDayTest() {

		logger.info("Adding date calender fieldbox");
		webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

		logger.info("Getting date calender field box");
		int dateCalenderBefore = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		logger.info("Checking date calender fieldbox by Clicking Add symbol icon ");
		webAppHelper.findElementsByXPath("//form[@class='mt-3']//a").get(2).click();

		logger.info("Getting datecalender fieldbox count after adding");
		int dateCalenderAfter = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		logger.info("Asserting of count  Date Calender count");
		Assert.assertTrue(dateCalenderBefore == dateCalenderAfter);

		logger.info("Getting error message text");
		String otsDateError = webAppHelper.findElementByXPath("//small[@class='text-danger']").getText();

		logger.info("Asserting OTS Date Error Text");
		Assert.assertEquals(otsDateError, "Fields can't be empty");

	}

	@Test(enabled = true, priority = 21)
	public void addedDateFieldTest() {

		logger.info("Getting date field component");
		WebElement dateBox = webAppHelper.findElementsByXPath("//input[@class='form-control']").get(1);

		logger.info("Clicking date field text box");
		dateBox.click();

		LocalDate currentdate = LocalDate.now();
		
		int currentYear = currentdate.getYear();

		int currentMonth = currentdate.getMonthValue();

		logger.info("Sending selected date in next date field textbox");
		dateBox.sendKeys((currentMonth) + "/10/" + currentYear);

		logger.info("Adding date calender fieldbox");
		webAppHelper.findElementsByXPath("//form[@class='mt-3']//a").get(2).click();
		
		webAppHelper.waitSimply(2);
		logger.info("Getting error message text");
		String otsDateErrorText = webAppHelper.findElementByXPath("//small[@class='text-danger']").getText();

		logger.info("Asserting OTS Date Error Text");
		Assert.assertEquals(otsDateErrorText, "Date must be future date or greater than previous date");

	}

	@Test(enabled = true, priority = 22, dependsOnMethods = "addedDateFieldTest")
	public void otsDatePresentDate() {

		LocalDate currentdate = LocalDate.now();

		String presentDate = currentdate.format(DateTimeFormatter.ofPattern("MM/d/YYYY"));

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(3);

		logger.info("clicking Yes option");
		webAppHelper.waitSimply(1);
		webAppHelper.findElementByXPath("//option[text()='Yes']").click();

		logger.info("Getting date field component");
		WebElement dateBox1 = webAppHelper.findElementByXPath("//input[@class='form-control']");

		logger.info("Asserting a date in date field textbox");
		dateBox1.sendKeys("" + presentDate + "");

		logger.info("Adding date calender fieldbox");
		webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

		logger.info("Getting date calender fieldbox count after adding");
		int dateCalenderBefore = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		webAppHelper.findElementsByXPath("//input[@class='form-control']").get(1).sendKeys("" + presentDate + "");

		logger.info("Adding date calender fieldbox");
		webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

		logger.info("Getting date calender fieldbox count after adding");
		int dateCalenderAfter = webAppHelper.findElementsByXPath("//form[@class='mt-3']/div").size();

		logger.info("Asserting of count Date Calender count");
		Assert.assertTrue(dateCalenderBefore == dateCalenderAfter);

		logger.info("Getting Run Report Enabled class name");
		String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");

		logger.info("Asserting Run Report Enabled class name ");
		Assert.assertTrue(runReportAfter.contains("setBgColor custom_opacity"));

	}

	@Test(enabled = true, priority = 23, dependsOnMethods = "otsDatePresentDate")
	public void runReportWithSameDate() {

		webAppHelper.refreshBrowser();
		logger.info("Getting Run Report Enabled class name");
		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");

		logger.info("Asserting Run Report Enabled class name ");
		if (runReport.contains("enableDiv")) {

			logger.info("clicking Yes option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//option[text()='Yes']").click();

			logger.info("Getting date field component");
			WebElement dateBox = webAppHelper.findElementByXPath("//input[@class='form-control']");

			logger.info("Asserting a date in date field textbox");
			dateBox.sendKeys("08/21/2023");

			logger.info("Adding date calender fieldbox");
			webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

			logger.info("Getting date field component");
			WebElement dateBox1 = webAppHelper.findElementsByXPath("//input[@class='form-control']").get(1);

			logger.info("Asserting a date in date field textbox");
			dateBox1.sendKeys("08/21/2023");
			logger.info("Adding date calender fieldbox");
			webAppHelper.findElementsByXPath("//form[@class='mt-3']//a").get(2).click();

			webAppHelper.waitSimply(2);
			logger.info("Getting error message text");
			String otsDateErrorText = webAppHelper.findElementByXPath("//small[@class='text-danger']").getText();

			logger.info("Asserting OTS Date Error Text");
			Assert.assertEquals(otsDateErrorText, "Date must be future date or greater than previous date");

			logger.info("Adding date calender fieldbox");
			webAppHelper.findElementsByXPath("//form[@class='mt-3']//a").get(2).click();

			logger.info("Getting Run Report Disabled class name");
			String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Disabled class name ");
			Assert.assertTrue(runReportAfter.contains("setBgColor custom_opacity"));

		}

	}

	@Test(enabled = true, priority = 24, dependsOnMethods = "runReportWithSameDate")
	public void runReportWithPastDate() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(2);

		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");
		logger.info("Asserting Run Report Enabled class name ");
		if (runReport.contains("enableDiv")) {

			logger.info("clicking Yes option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//option[text()='Yes']").click();

			webAppHelper.scrollPageUp();
			logger.info("Clicking Date field text box");
			webAppHelper.findElementByXPath("//div[@class='react-datepicker__input-container']//child::input").click();

			logger.info("Clicking arrow icon next month in Date picker");
			webAppHelper.findElementByXPath("//button[contains(@class , ' react-datepicker__navigation--next')]")
					.click();

			logger.info("Getting A Day of Month class name");
			webAppHelper.findElementByXPath("//*[text() = '15' ]").click();
			webAppHelper.waitSimply(4);

			logger.info("Adding Date field box");
			webAppHelper.findElementByXPath("//form[@class='mt-3']//a").click();

			logger.info("Getting date field component");
			WebElement dateBox1 = webAppHelper.findElementsByXPath("//input[@class='form-control']").get(1);

			logger.info("Asserting a date in date field textbox");
			dateBox1.sendKeys("03/19/2021");

			logger.info("Getting Run Report Enabled class name");
			String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportAfter.contains("setBgColor custom_opacity"));

			logger.info("Adding Date field box");
			webAppHelper.findElementsByXPath("//form[@class='mt-3']//a").get(1).click();

			logger.info("Getting Run Report Enabled class name");
			String runReportPastDate = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportPastDate.contains("setBgColor custom_opacity"));

		}

	}

	@Test(enabled = true, priority = 25, dependsOnMethods = "runReportWithPastDate")
	public void runReportWithPastDateField() {

		webAppHelper.refreshBrowser();
		logger.info("Getting Run Report Enabled class name");
		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");
		logger.info("Asserting Run Report Enabled class name ");
		if (runReport.contains("enableDiv")) {

			logger.info("clicking Yes option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//option[text()='Yes']").click();

			webAppHelper.scrollPageUp();
			logger.info("Getting date field component");
			WebElement dateBox1 = webAppHelper.findElementByXPath("//input[@class='form-control']");

			logger.info("Asserting a date in date field textbox");
			dateBox1.sendKeys("08/21/2022");
			webAppHelper.waitSimply(3);

			logger.info("clicking include WIP  checkbox");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementById("Include WIP").click();

			logger.info("clicking Ignore Stock When Below Zero  checkbox");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementById("Ignore Stock When Below Zero").click();

			logger.info("selecting Display Stock by Specified Warehouse");
			webAppHelper.findElementByXPath("//*[text()='Display Stock by Specified Warehouse']").click();

			logger.info("Selecting Tag All button");
			webAppHelper.findElementByXPath("//*[text()='Tag All']").click();

			webAppHelper.scrollPageDown();
			logger.info("Selecting OTS By Size");
			webAppHelper.findElementByXPath("//*[text()='Display OTS by Size?']").click();

			logger.info("Getting Run Report Enabled class name");
			String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportAfter.contains("setBgColor custom_opacity"));

			logger.info("clicking No option");
			webAppHelper.findElementByXPath("//option[text()='No']").click();

			logger.info("Getting Run Report Enabled class name");
			String runReportNoOption = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportNoOption.contains("enableDiv"));

		}
	}

	@Test(enabled = true, priority = 26, dependsOnMethods = "runReportWithPastDateField")
	public void runReportDatePresentDate() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(3);
		logger.info("Getting Run Report Enabled class name");
		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");

		logger.info("Asserting Run Report Enabled class name ");
		if (runReport.contains("enableDiv")) {

			logger.info("clicking Yes option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//option[text()='Yes']").click();

			webAppHelper.scrollPageUp();
			logger.info("Selecting date field textbox");
			webAppHelper.findElementByXPath("//input[contains(@class,'form-control')]").click();

			logger.info("Selecting arrow icon in datepicker");
			webAppHelper.findElementByXPath("//*[contains(@class,'react-datepicker__navigation--next')]").click();

			logger.info("Selecting a Date");
			webAppHelper.findElementByXPath("//div[text()='15']").click();

			webAppHelper.scrollPageDown();
			webAppHelper.findElementByXPath("//*[text()='Display OTS by Size?']").click();

			logger.info("Getting Run Report Enabled class name");
			String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportAfter.contains("enableDiv"));

		}

	}

	@Test(enabled = true, priority = 27, dependsOnMethods = "runReportDatePresentDate")
	public void runReportDateNoOption() {

		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(2);
		webAppHelper.scrollPageDown();
		logger.info("Getting Run Report Enabled class name");
		String runReport = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
				.getAttribute("class");

		logger.info("Asserting Run Report Enabled class name ");
		if (runReport.contains("enableDiv")) {

			logger.info("clicking Yes option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//option[text()='Yes']").click();

			webAppHelper.scrollPageUp();
			webAppHelper.scrollPageUp();
			logger.info("Clicking Include OTS As of Date dropdown icon");
			webAppHelper.findElementByXPath("//div[@class='w-100p']//child::select").click();

			logger.info("clicking No option");
			webAppHelper.waitSimply(1);
			webAppHelper.findElementByXPath("//div[@class='w-100p']//select//option[@value='No']").click();

			logger.info("Clicking Include OTS As of Date dropdown icon");
			webAppHelper.findElementByXPath("//div[@class='w-100p']//child::select").click();
			webAppHelper.scrollPageDown();
			logger.info("Selecting OTS By Size");
			webAppHelper.findElementByXPath("//*[text()='Display OTS by Size?']").click();

			logger.info("Getting Run Report Enabled class name");
			String runReportAfter = webAppHelper.findElementByXPath("//*[text()='Run Report']//parent::div")
					.getAttribute("class");

			logger.info("Asserting Run Report Enabled class name ");
			Assert.assertTrue(runReportAfter.contains("enableDiv"));

		}
	}

	@AfterClass
	public void unsetup() {
		webAppHelper.quitBrowser();
	}
}

package com.ots.test.ui;

import java.io.IOException;
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
import com.gurock.testrail.APIException;
import com.ots.test.ui.common.utils.UITestSeleniumHelper;

import com.testRail.testManager.TestRailManager;

@Listeners({ com.ots.test.ui.common.utils.TestNGCustomResults.class })

public class DashboardTest implements IRetryAnalyzer, IAnnotationTransformer {

	Logger logger = LoggerFactory.getLogger("LoginTest");

	public static String currentRunningMethodName = "";
	private int retryCount = 0;
	private int maxRetryCount = 1;
	public static String username = System.getProperty("email", "v-arungopi@aims360.com");
	public static String password = System.getProperty("password", "Creai@ims22!@");
	protected String TestcaseId;
	private String countNotification = null;
	// Helper classes
	UITestSeleniumHelper webAppHelper = new UITestSeleniumHelper();
	TestRailManager tr = new TestRailManager();
	public static WebDriver driver;
	Calendar cal = Calendar.getInstance();

	// Getting present day
	int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

	@BeforeClass
	public void setup() {
		webAppHelper.openBrowser("chrome");
		webAppHelper.maximizeWindow();

		logger.info("Navigating to the URL");
		webAppHelper.navigateTo("https://creai.aims360.com/");
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

	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		IRetryAnalyzer retry = testannotation.getRetryAnalyzer();

		if (retry == null) {
			testannotation.setRetryAnalyzer(DashboardTest.class);
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

	public void login() {

		webAppHelper.waitForElementToBeLoadedById("email");

		logger.info("send username in email component");
		webAppHelper.findElementById("email").sendKeys(username);

		logger.info("sending password in password component");
		webAppHelper.findElementById("pwd").sendKeys(password);

		webAppHelper.waitSimply(2);

		logger.info("Clicking Sign In component");
		webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]").click();

	}

	@Test(enabled = true, priority = 2, dependsOnMethods = "login")
	public void landingWelcomePage() throws IOException, APIException {
		webAppHelper.waitSimply(3);
		// TestcaseId = "2";
		logger.info("Getting display welcome message");
		String welcome = webAppHelper.findElementByClassName("welcomeMessage").getText();

		logger.info("Asserting welcome text");
		Assert.assertEquals(welcome, "Welcome to AIMS360");

		logger.info("getting logo position css value");
		String logoPosition = webAppHelper.findElementByXPath("//div[contains(@class,'pt-4')]//child::img")
				.getCssValue("vertical-align");

		logger.info("Asserting Logo css value");
		Assert.assertEquals(logoPosition, "middle");

		if (logoPosition.contains("middle")) {

			tr.addResultForTestCase("2", "2", tr.TEST_CASE_PASSED_STATUS, "");
		} else {
			tr.addResultForTestCase("2", "2", tr.TEST_CASE_PASSED_STATUS, "");

		}

	}

	@Test(enabled = true, priority = 3, dependsOnMethods = "landingWelcomePage")
	public void dashboardIconTest() {

		logger.info("Getting Dashboard Icon");
		String sideBarLogo = webAppHelper
				.findElementsByXPath("//*[local-name()='svg' and (@class='c-sidebar-nav-icon')]").get(1)
				.getAttribute("xmlns");

		logger.info("Asserting OTS By Date Icon");
		Assert.assertTrue(sideBarLogo.contains("http://www.w3.org/2000/svg"));

		logger.info("Getting Dashboard name");
		String dashboardName = webAppHelper.findElementByXPath("//a[contains(@class,'c-sidebar-nav-link')]").getText();

		logger.info("Asserting Dashboard Name");
		Assert.assertTrue(dashboardName.contains("Dashboard"));

	}

	@Test(enabled = true, priority = 4, dependsOnMethods = "dashboardIconTest")
	public void ClientNameTest() throws IOException, APIException {

		logger.info("Getting current url");
		String currentUrl = webAppHelper.getCurrentUrl();

		logger.info("getting client Name");
		// String clientName =
		// currentUrl.split("https://")[1].split(".aims360")[0].split("preprod-")[1];

		String clientName = currentUrl.split("https://")[1].split(".aims360")[0];
		clientName = clientName.toUpperCase();

		logger.info("Getting header customer key");
		String customerName = webAppHelper.findElementByClassName("c-subheader-nav-link").getText();
		customerName = customerName.trim();

		logger.info("Asserting client Name");
		// Assert.assertEquals(clientName, customerName);

		if (customerName.contains(clientName)) {

			tr.addResultForTestCase("2", "3", tr.TEST_CASE_PASSED_STATUS, "");
		} else {
			tr.addResultForTestCase("2", "3", tr.TEST_CASE_FAILED_STATUS, "");

		}

	}

	@Test(enabled = true, priority = 5, dependsOnMethods = "landingWelcomePage")
	public void sideBarLogoTest() {

		logger.info("Getting side bar logo path");
		String sideBarLogo = webAppHelper.findElementsByXPath("//a[contains(@class , 'c-sidebar-brand ')]//child::img")
				.get(0).getAttribute("src");

		logger.info("Asserting side bar Logo");
		Assert.assertTrue(sideBarLogo.contains("logo-light"));

		logger.info("Getting Side bar logo css value");
		String logoCssValue = webAppHelper.findElementByClassName("c-sidebar-brand-full").getCssValue("vertical-align");

		logger.info("Asserting Logo css value");
		Assert.assertEquals(logoCssValue, "middle");
	}

	@Test(enabled = true, priority = 6)
	public void headerToggleIconTestbeforeClick() {

		logger.info("Getting header toggle icon class name");
		String headerToggleIconCSS = webAppHelper
				.findElementByXPath("//*[contains(@class , 'c-sidebar-brand')]//parent::div").getAttribute("class");

		logger.info("Asserting header toggle icon class name");
		Assert.assertTrue(headerToggleIconCSS.contains(" c-sidebar-lg-show"));

		logger.info("Clicking Header toggle icon");
		webAppHelper.findElementByXPath("//button[contains(@class , 'd-md-down-none')]//child::span").click();

	}

	@Test(enabled = true, priority = 7, dependsOnMethods = "headerToggleIconTestbeforeClick")
	public void headerToggleIconTestAfterClick() {

		logger.info("Getting header toggle icon class name");
		String changedHeaderToggleIconCSS = webAppHelper
				.findElementByXPath("//*[contains(@class , 'c-sidebar-brand')]//parent::div").getAttribute("class");

		logger.info("Asserting changed header toggle icon class name");
		Assert.assertTrue(changedHeaderToggleIconCSS.contains("c-sidebar c-sidebar-dark c-sidebar-fixed"));

		logger.info("Clicking Header toggle icon");
		webAppHelper.findElementByXPath("//button[contains(@class , 'd-md-down-none')]//child::span").click();

	}

	@Test(enabled = true, priority = 8)
	public void themeIconTestbeforeClick() {

		logger.info("Getting theme icon class name");
		String themeiconCSS = webAppHelper.findElementByXPath("//*[contains(@class , 'c-app')]").getAttribute("class");

		logger.info("Asserting theme icon class name");
		Assert.assertTrue(themeiconCSS.contains("c-default-layout"));

		logger.info("clicking theme icon");
		webAppHelper.findElementByXPath("//*[contains(@class,'c-d-legacy-none')]").click();

	}

	@Test(enabled = true, priority = 9, dependsOnMethods = "themeIconTestbeforeClick")
	public void themeIconTestAfterClick() {

		logger.info("Getting theme icon class name");
		String changedthemeiconCSS = webAppHelper.findElementByXPath("//*[contains(@class , 'c-app')]")
				.getAttribute("class");

		logger.info("Asserting theme icon changed class name");
		Assert.assertTrue(changedthemeiconCSS.contains("c-dark-theme"));

		logger.info("clicking theme icon");
		webAppHelper.findElementByXPath("//*[contains(@class,'c-d-legacy-none')]").click();

	}

	@Test(enabled = true, priority = 10)
	public void bellIconTestBeforeClick() {

		webAppHelper.waitSimply(3);
		logger.info("Getting bell icon class name");
		String bellIconCSS = webAppHelper
				.findElementByXPath("//*[text()=' notifications']//parent::div//parent::div//parent::li")
				.getAttribute("class");

		logger.info("Asserting bell icon class name");
		Assert.assertTrue(bellIconCSS.contains("c-header-nav-item mx-2 dropdown nav-item"));

		logger.info("clicking notification icon");
		webAppHelper.findElementByXPath("//*[contains(@class , 'c-header-nav-item ')]").click();

	}

	@Test(enabled = true, priority = 11, dependsOnMethods = "bellIconTestBeforeClick")
	public void bellIconAfterClick() {

		logger.info("Getting notification messages in the dashboard");
		String notificationMsge = webAppHelper
				.findElementByXPath("//div[contains(@class ,'dropdown-header')]//child::strong").getText();

		logger.info("Asserting notification message in the Dashboard");
		Assert.assertEquals(notificationMsge, "You have 0 notifications");

	}

	@Test(enabled = true, priority = 12, dependsOnMethods = "bellIconAfterClick")
	public void bellIconAfterClickCSS() {

		webAppHelper.waitSimply(2);
		logger.info("Getting bell icon class name");
		String bellIconCSSChanged = webAppHelper
				.findElementByXPath("//*[text()=' notifications']//parent::div//parent::div//parent::li")
				.getAttribute("class");

		logger.info("Asserting bell icon changed class name");
		Assert.assertTrue(bellIconCSSChanged.contains(" show"));

		webAppHelper.waitSimply(3);
		logger.info("clicking notification icon");
		webAppHelper.findElementByXPath("//*[contains(@class , 'c-header-nav-item ')]").click();

	}

	@Test(enabled = true, priority = 13)
	public void profileName() {

		logger.info("getting profile icon first two char");
		String uName = username.substring(0, 2);

		logger.info("Getting profile name");
		String profileName = webAppHelper.findElementByXPath("//div[contains(@class , 'profile-custiom-wording')]")
				.getText();

		logger.info("Asserting Profile icon");
		Assert.assertEquals(uName, profileName);

	}

	@Test(enabled = true, priority = 14)
	public void profileIconTestBeforeClick() {

		logger.info("Getting profile icon class name");
		String profileIconCSS = webAppHelper
				.findElementByXPath("//div[contains(@class , 'c-avatar')]//parent::a//parent::li")
				.getAttribute("class");

		logger.info("Asserting profile icon class name");
		Assert.assertTrue(profileIconCSS.contains("c-header-nav-items "));

		logger.info("clicking profile icon");
		webAppHelper.findElementByXPath("//*[contains(@class , 'c-header-nav-items')]").click();

	}

	@Test(enabled = true, priority = 15, dependsOnMethods = "profileIconTestBeforeClick")
	public void profileIconTestAfterClick() {

		logger.info("Getting profile icon changed class name");
		String profileIconCSS1 = webAppHelper
				.findElementByXPath("//div[contains(@class , 'c-avatar')]//parent::a//parent::li")
				.getAttribute("class");

		logger.info("Asserting profile icon changed class name");
		Assert.assertTrue(profileIconCSS1.contains(" show"));

		webAppHelper.waitSimply(2);
		logger.info("clicking profile icon");
		webAppHelper.findElementByXPath("//*[contains(@class , 'c-header-nav-items')]").click();
		webAppHelper.waitSimply(3);

	}

	@Test(enabled = true, priority = 16, dependsOnMethods = "profileIconTestAfterClick")
	public void NavigateToDashboard() {

		logger.info("clicking reports");

		webAppHelper.findElementByXPath("//*[text()='Reports']").click();

		logger.info("clicking Ots By Date ");
		webAppHelper.findElementByXPath("//a[text()='OTS By Date']").click();

		logger.info("getting text side nav title name Dashboard");
		String dashboard = webAppHelper.findElementByXPath("//*[contains(@class,'badge-info')]//parent::a").getText();

		logger.info("Asserting of OTS Date card");
		Assert.assertTrue(dashboard.contains("Dashboard"));

	}

	@Test(enabled = true, priority = 17, dependsOnMethods = "NavigateToDashboard")
	public void NavigateToReports() {

		logger.info("clicking text side nav title name Dashboard");
		webAppHelper.findElementByXPath("//*[contains(@class,'badge-info')]//parent::a").click();

		webAppHelper.waitSimply(3);
		logger.info("clicking to close reports");
		webAppHelper.findElementByXPath("//*[text()='Reports']").click();

		logger.info("clicking to open reports");
		webAppHelper.findElementByXPath("//*[text()='Reports']").click();

		logger.info("clicking Ots By Date ");
		webAppHelper.findElementByXPath("//a[text()='OTS By Date']").click();

	}

	@Test(enabled = true, priority = 18, dependsOnMethods = "NavigateToReports")
	public void logOut() {

		logger.info("Selecting  profile icon");
		webAppHelper.findElementByClassName("c-avatar").click();

		logger.info("clicking Logout option");
		webAppHelper.findElementByXPath("//a[text()='Logout']").click();

		webAppHelper.waitSimply(5);

		logger.info("Getting login text");
		String text = webAppHelper.findElementByXPath("//div[@class='logindiv']//h3").getText();

		logger.info("Asserting Login");
		Assert.assertEquals(text, "Login");
	}

	@AfterClass
	public void unsetUp() {
		webAppHelper.quitBrowser();
	}

}

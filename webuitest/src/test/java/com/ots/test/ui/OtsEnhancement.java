package com.ots.test.ui;



import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass; 
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;
import com.ots.test.ui.common.utils.PropsHandler;
import com.ots.test.ui.common.utils.UITestSeleniumHelper;

@Listeners({ com.ots.test.ui.common.utils.TestNGCustomResults.class })

public class OtsEnhancement implements IRetryAnalyzer, IAnnotationTransformer {

	Logger logger = LoggerFactory.getLogger("LoginTest");

	public static String currentRunningMethodName = "";
	private int retryCount = 0;
	private int maxRetryCount = 1;
	public static String username = System.getProperty("email", "v-arungopi@aims360.com");
	public static String password = System.getProperty("password", "Creai@ims22!@");

	private String countNotification = null;
	private String currentUrl = null;
	// Helper classes
	UITestSeleniumHelper webAppHelper = new UITestSeleniumHelper();
	PropsHandler file = new PropsHandler();
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
			testannotation.setRetryAnalyzer(OtsEnhancement.class);
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
	public void topBarTest(){
			
	     webAppHelper.waitForElementToBeLoadedById("email");
 		logger.info("Getting UserName component and send username");
		webAppHelper.findElementById("email").sendKeys(username);

		logger.info("Getting password component and send password");
		webAppHelper.findElementById("pwd").sendKeys(password);

		webAppHelper.waitSimply(2);

		logger.info("Getting Sign In component");
		webAppHelper.findElementByXPath("//*[@value= 'Sign In']").click();

		webAppHelper.waitForElementToBeLoaded("//*[text()='Reports']");

		logger.info("clicking to open reports");
		webAppHelper.findElementByXPath("//*[text()='Reports']").click();

		logger.info("Clicking OTS By Date");
		webAppHelper.findElementByXPath("//a[text()='OTS By Date']").click();

		logger.info("Getting top bar title name");
		String titleName = webAppHelper.findElementsByXPath("//li[@role='presentation']").get(1).getText();

		logger.info("Asserting top bar title name");
		Assert.assertEquals(titleName, "OTS By Date");

		String topbarClass = webAppHelper
				.findElementByXPath("//button[contains(@class,'c-header-toggler')]//parent::header")
				.getAttribute("class");

		logger.info("Clicking theme icon");
		webAppHelper.findElementByXPath("//button[contains(@title,'Light/Dark Mode')]").click();

		Assert.assertTrue(topbarClass.contains("fade-in-top"));
		webAppHelper.scrollPageDown();

	}

	@Test(enabled = true, priority = 2)
	public void topBarTestWithOtsCalculation() {

		logger.info("Getting OTS Calculation checkbox");
		int checkboxSize = webAppHelper.findElementsByXPath("//label[@class='labelstyle']").size();

		for (int i = 0; i < checkboxSize / 2; i++) {

			webAppHelper.findElementsByXPath("//label[@class='labelstyle']").get(i).click();

		}
	}

	@Test(enabled = true, priority = 3)
	public void topBarWithOtsDate() {

		logger.info("clicking Yes option");
		webAppHelper.findElementByXPath("//option[text()='Yes']").click();

		logger.info("Clicking Date field text box");
		webAppHelper.findElementByXPath("//div[@class='react-datepicker__input-container']//child::input").click();

		logger.info("Clicking arrow icon next month in Date picker");
		webAppHelper.findElementByXPath("//button[contains(@class , ' react-datepicker__navigation--next')]").click();

		logger.info("Selecting Day of Month class name");
		webAppHelper.findElementByXPath("//*[text() = '10' ]").click();
		webAppHelper.waitSimply(3);

		logger.info("Adding date field");
		webAppHelper.findElementByXPath("//*[local-name()='svg' and (@data-icon='plus')]").click();

	}

	@Test(enabled = true, priority = 4)
	public void topBarWithDisplayStock() {

		logger.info("Clicking Display Stock by Specified Warehouse");
		webAppHelper.findElementById("Display Stock by Specified Warehouse").click();

		webAppHelper.waitSimply(2);
		logger.info("Clicking tag all button");
		webAppHelper.findElementByXPath("//*[text()='Tag All']").click();

		webAppHelper.waitSimply(2);
		logger.info("Clicking Untag All button");
		webAppHelper.findElementByXPath("//*[text()='Untag All']").click();

		webAppHelper.scrollPageUp();

//		logger.info("Clicking log out");
//		webAppHelper.findElementByXPath("//*[text()='v-']").click();
//		webAppHelper.findElementByXPath("//*[text()='Logout']").click();

	}

	@Test(enabled = true, priority = 5)
	public void reportsUrlPatternTest() {

		logger.info("Getting current url");
		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("#")[0];
		currentUrl = currentUrl + "#/Reports";
		webAppHelper.navigateTo(currentUrl);

		logger.info("Getting Helper message in the body");
		String helpText = webAppHelper.findElementByXPath("//label[@class='welcomeMessage']//child::h4").getText();

		logger.info("Asserting Helper message");
		Assert.assertEquals(helpText, "Please select an item from left menu to get started.");

		logger.info("Getting side bar menu details");
		String menuName = webAppHelper.findElementByClassName("c-sidebar-nav-dropdown-toggle").getText();

		logger.info("Asserting menu Name");
		Assert.assertEquals(menuName, "Reports");

		logger.info("Getting Default dropdown  class name");
		String dropDownClsName = webAppHelper.findElementsByXPath("//ul[contains(@class,'c-sidebar-nav')]//child::li")
				.get(0).getAttribute("class");

		logger.info("Asserting dropdown class name");
		Assert.assertTrue(dropDownClsName.contains("c-show"));
	}

	@Test(enabled = true, priority = 6)
	public void otsByDateUrlTest() {

		logger.info("Getting current url");
		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("#")[0];
		currentUrl = currentUrl + "#/Reports/otsbydate";
		webAppHelper.navigateTo(currentUrl);
		logger.info("Getting side bar menu options");
		String sideMenuName = webAppHelper.findElementByXPath("//div[contains(@class,'c-sidebar-dark')]").getText();

		logger.info("Asserting side bar wms menu");
		Assert.assertFalse(sideMenuName.contains("WMS"));
		Assert.assertTrue(sideMenuName.contains("OTS By Date"));

		logger.info("Getting header title name");
		String titleName = webAppHelper.findElementsByXPath("//li[contains(@class,'breadcrumb-item')]").get(1)
				.getText();

		logger.info("Asserting OTS By Date page title name");
		Assert.assertEquals(titleName, "OTS By Date");

	}

	@Test(enabled = true, priority = 7)
	public void wmsUrlTest() {

		logger.info("Getting current url");
		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("#")[0];
		currentUrl = currentUrl + "#/WMS";
		webAppHelper.navigateTo(currentUrl);

		logger.info("Getting side bar menu options");
		String sideMenuName = webAppHelper.findElementByXPath("//div[contains(@class,'c-sidebar-dark')]").getText();

		logger.info("Asserting side bar wms menu");
		Assert.assertTrue(sideMenuName.contains("WMS"));

		logger.info("Getting Default dropdown  class name");
		String dropDownClsName = webAppHelper.findElementsByXPath("//ul[contains(@class,'c-sidebar-nav')]//child::li")
				.get(0).getAttribute("class");

		logger.info("Asserting dropdown class name");
		Assert.assertTrue(dropDownClsName.contains("c-show"));

		logger.info("Getting Helper message in the body");
		String wmsHelpText = webAppHelper.findElementByXPath("//label[@class='welcomeMessage']//child::h4").getText();

		logger.info("Asserting Helper message");
		Assert.assertEquals(wmsHelpText, "Please select an item from left menu to get started.");

		logger.info("Getting header title name");
		String wmsName = webAppHelper.findElementByXPath("//li[contains(@class,'breadcrumb-item')]").getText();

  		logger.info("Asserting wms title to check topBar disabling or active");
		Assert.assertEquals(wmsName, "WMS");

	}

	@Test(enabled = true, priority = 8)
	public void wmsSubComponentTest() {
      
		logger.info("Getting current url");
		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("#")[0];
		currentUrl = currentUrl + "#/WMS/Inquiry";

		webAppHelper.navigateTo(currentUrl);

		logger.info("Getting side bar menu options");
		String sideBarMenu = webAppHelper.findElementByXPath("//li[contains(@class,'c-sidebar-nav-dropdown')]")
				.getText();

		logger.info("Asserting ide bar menu options");
		Assert.assertTrue(sideBarMenu.contains("Inquiry"));
		Assert.assertFalse(sideBarMenu.contains("Production Jobs"));

		logger.info("Getting Default dropdown  class name");
		String dropDownClsName = webAppHelper.findElementsByXPath("//ul[contains(@class,'c-sidebar-nav')]//child::li")
				.get(0).getAttribute("class");

		logger.info("Asserting dropdown class name");
		Assert.assertTrue(dropDownClsName.contains("c-show"));

		logger.info("Gettng header title name");
		String headerTitleName = webAppHelper.findElementsByXPath("//li[contains(@class,'breadcrumb-item')]").get(1)
				.getText();

		logger.info("Asserting Header title to Checking topbar is enabled or disabled");
		Assert.assertEquals(headerTitleName, "Inquiry");

		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("WMS")[0];
		currentUrl = currentUrl + "WMS/Transfer";

		webAppHelper.navigateTo(currentUrl);

		logger.info("Gettng header title name");
		String headerTitle = webAppHelper.findElementsByXPath("//li[contains(@class,'breadcrumb-item')]").get(1)
				.getText();

		logger.info("Asserting Adjustments title name");
		Assert.assertEquals(headerTitle, "Transfer");

	}
	
	@Test(enabled = true, priority = 9)
	public void refreshURlPattern() {
		
		webAppHelper.refreshBrowser();
		webAppHelper.waitSimply(3);
		
		logger.info("Getting side menu options");
		String sideBarMenu = webAppHelper.findElementByXPath("//li[contains(@class,'c-sidebar-nav-dropdown')]").getText();
		
		logger.info("Asserting  sideBar menu options");
		Assert.assertFalse(sideBarMenu.contains("Reports"));
		Assert.assertTrue(sideBarMenu.contains("WMS"));
		
	}
	
	@Test(enabled= true, priority = 10)
	public void incorrectEndPoints() {
		
		logger.info("Getting current url");
		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("#")[0];
		currentUrl = currentUrl + "#/report";

		webAppHelper.navigateTo(currentUrl);
		
		webAppHelper.waitSimply(2);
		logger.info("Getting body contained message");
		String bodyWelcomeMsge = webAppHelper.findElementByClassName("welcomeMessage").getText();
		
		logger.info("Asserting Welcome Message in the page");
		Assert.assertEquals(bodyWelcomeMsge, "Welcome to AIMS360");
		
	}
	
	@Test(enabled = true, priority = 11)
	public void caseInsensitiveEndPoints() {
		
		logger.info("Getting current url");
		currentUrl = webAppHelper.getCurrentUrl();
		currentUrl = currentUrl.split("#")[0];
		currentUrl = currentUrl + "#/reports";
		
		webAppHelper.navigateTo(currentUrl);
		
		logger.info("Getting Helper message in the body");
		String wmsHelpText = webAppHelper.findElementByXPath("//label[@class='welcomeMessage']//child::h4").getText();

		logger.info("Asserting Helper message");
		Assert.assertEquals(wmsHelpText, "Please select an item from left menu to get started.");
          
		logger.info("Clicking log out");
		webAppHelper.findElementByXPath("//*[text()='v-']").click();
		webAppHelper.findElementByXPath("//*[text()='Logout']").click();
		
	}
	
	@AfterClass
	public void unsetUp() {
		webAppHelper.quitBrowser();
	}

}

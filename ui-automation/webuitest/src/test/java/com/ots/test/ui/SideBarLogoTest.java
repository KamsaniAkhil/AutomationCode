package com.ots.test.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.gurock.testrail.APIClient;
import com.gurock.testrail.APIException;
import com.ots.test.ui.common.utils.UITestSeleniumHelper;

public class SideBarLogoTest {
	
	UITestSeleniumHelper webAppHelper = new UITestSeleniumHelper();
	Logger logger = LoggerFactory.getLogger("SideBarLogoTest");
	public static String username = System.getProperty("email", "v-arungopi@aims360.com");
	public static String password = System.getProperty("password", "Creai@ims22!@");
public static String TEST_RUN_ID = "3";
	
	public static String TESTRAIL_USERNAME = "akhilkamsani98@gmail.com";
	public static String TESTRAIL_PASSWORD = "ghAp2IYTwCW/ox3jZNjf";
	public static String RAILS_ENGINE_URL = "https://akhilautomation.testrail.com/";
	public static final int TEST_CASE_PASSED_STATUS = 1;
	public static final int TEST_CASE_FAILED_STATUS = 5;
	
	


	@Test(enabled = true, priority = 1)
	public void sideBarLogoTest() throws MalformedURLException, IOException, APIException {
		
		webAppHelper.openBrowser("chrome");
		webAppHelper.maximizeWindow();

		logger.info("Navigating to the URL");
		webAppHelper.navigateTo("https://creai.aims360.com/");
		
		
		webAppHelper.waitSimply(3);
		webAppHelper.waitForElementToBeLoadedById("email");

		logger.info("send username in email component");
		webAppHelper.findElementById("email").sendKeys(username);

		logger.info("sending password in password component");
		webAppHelper.findElementById("pwd").sendKeys(password);

		webAppHelper.waitSimply(2);

		logger.info("Clicking Sign In component");
	    webAppHelper.findElementByXPath("//*[contains(@class ,'btn-default')]").click();
		
		logger.info("Getting side bar logo path");
		String sideBarLogo = webAppHelper.findElementsByXPath("//a[contains(@class , 'c-sidebar-brand ')]//child::img")
				.get(0).getAttribute("src");

		logger.info("Asserting side bar Logo");
		Assert.assertTrue(sideBarLogo.contains("logo-light"));
		
		logger.info("Getting Side bar logo css value");
		String logoCssValue = webAppHelper.findElementByClassName("c-sidebar-brand-full").getCssValue("vertical-align");

		
		logger.info("Asserting Logo css value");
		Assert.assertEquals(logoCssValue, "middle");
		if(logoCssValue.contains("middles")) {
		
		SideBarLogoTest.addResultForTestCase("4", TEST_CASE_PASSED_STATUS, "");
		}
		else {
			
			SideBarLogoTest.addResultForTestCase("4", TEST_CASE_FAILED_STATUS, "");
		}
	
	}
	

public static void addResultForTestCase(String testCaseId ,int status,String error) throws IOException, APIException {
		
		String testRunId  = TEST_RUN_ID;
		testCaseId ="4";
		 APIClient client = new APIClient(RAILS_ENGINE_URL);
		    client.setUser(TESTRAIL_USERNAME);
		    client.setPassword(TESTRAIL_PASSWORD);
		    
		    Map data = new HashMap();
		    data.put("status_id", status);
		    data.put("comment", "This test worked fine!");
		    JSONObject r = (JSONObject) client.sendPost("add_result_for_case/"+testRunId+"/"+testCaseId, data);
		    System.out.print(r);
	}
}

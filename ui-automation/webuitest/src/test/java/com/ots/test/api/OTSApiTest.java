package com.ots.test.api;

import static io.restassured.RestAssured.expect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.ots.test.ui.common.utils.APITestHelper;
import com.ots.test.ui.common.utils.UITestSeleniumHelper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class OTSApiTest {
	static Logger logger = LoggerFactory.getLogger(OTSApiTest.class);

	public static String username = System.getProperty("email", "v-arungopi@aims360.com");
	public static String password = System.getProperty("password", "JR365@ims22!@");
	public static String redirectUrl = System.getProperty("redirectURL",
			"https://preprod-jr365.aims360.com/#500");
	public String xfunctionKey = System.getProperty("x-functions-key",
			"k0u2aKraEyN56O0BZAlxtkyEy_49KH5utS6r4eUagglQAzFueo10FQ==");

	private String clientKey = null;
	private String clientCode = null;
	private String callBackCode = null;
	private String accessToken = null;

	private int retryCount = 0;
	private int maxRetryCount = 2;

	APITestHelper apiHelper = new APITestHelper();
	UITestSeleniumHelper webAppHelper = new UITestSeleniumHelper();

	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			System.out.println("Retrying test " + result.getName() + " with status "
					+ getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
			retryCount++;
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public void transform(ITestAnnotation testannotation, Class testClass, Constructor testConstructor,
			Method testMethod) {
		IRetryAnalyzer retry = testannotation.getRetryAnalyzer();

		if (retry == null) {
			testannotation.setRetryAnalyzer((Class<? extends IRetryAnalyzer>) OTSApiTest.class);
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
	public void authenticate() {

		JsonObject reqObject = new JsonObject();
		reqObject.addProperty("clientKey", "JR365");
		reqObject.addProperty("grant_type", "client_code");
		Response res = apiHelper.doLogin(reqObject);

		clientCode = apiHelper.getStringValue(res, "clientCode");

	}

	@Test(enabled = true, priority = 2, dependsOnMethods = "authenticate")
	public void login() {

		webAppHelper.openBrowser("chrome");
		webAppHelper.maximizeWindow();

		webAppHelper
				.navigateTo("https://auth.aims360.rest/v1/authentication/authorize?response_type=code&state=&client_id="
						+ clientCode + "&scope=user.read&redirect_uri=" + redirectUrl);

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

		webAppHelper.waitSimply(2);
		logger.info("clicking Sign In component");
		signInComponent.click();
		webAppHelper.waitSimply(5);

		String currentURl = webAppHelper.getCurrentUrl();
		callBackCode = currentURl.split("=")[1].split("#")[0];
	}

	@Test(enabled = true, priority = 3, dependsOnMethods = "login")
	public void getclientKey() {
		
		String currentUrl = webAppHelper.getCurrentUrl();
		
		clientKey = currentUrl.split("-")[1].split(".aims360")[0];

		Assert.assertEquals(clientKey, "jr365");
		
		clientKey = clientKey.toUpperCase();
		
	}

	@Test(enabled = true, priority = 4, dependsOnMethods = "login")
	public void getAccessTokenApi() {

		JsonObject sendObject = new JsonObject();

		sendObject.addProperty("clientKey", clientKey);
		sendObject.addProperty("callBackCode", callBackCode);
		sendObject.addProperty("grant_type", "authorization_code");
		sendObject.addProperty("callUrl", "https://preprod-jr365.aims360.com/");

		RestAssured.baseURI = APITestHelper.API_HOST;
		Response res = expect().statusCode(Integer.parseInt("200")).given().contentType("application/json")
				.header("x-functions-key", xfunctionKey).body(sendObject.toString()).log().everything().when()
				.post(APITestHelper.API_LOGIN_PATH);
		res.then().log().all();

		String json = res.asString();
		JsonPath jsonPath = new JsonPath(json);

		boolean maintanancePage = jsonPath.getBoolean("isUnderMaintenance");

		Assert.assertEquals(maintanancePage, false);
		HashMap tokentDetails = jsonPath.get("tokenDetils");
		System.out.println("isUnderMaintenance :" + maintanancePage);

		accessToken = tokentDetails.get("access_token").toString();
																																																																																																																																																																																																																																																					
		System.out.println("accessToken :" + accessToken);

	}

	@Test(enabled = true, priority = 5, dependsOnMethods = "getAccessTokenApi")
	public void getWarehouse() {
		RestAssured.baseURI = "https://runwaywarehousesservice.azurewebsites.net";
		Response response = expect().statusCode(Integer.parseInt("200")).given()
				.header("Authorization", "Bearer " + accessToken).contentType("application/json").log().everything()
				.when().get(APITestHelper.API_WAREHOUSES_PATH);

		response.then().log().all();

	}

}

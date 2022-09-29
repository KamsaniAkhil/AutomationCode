package com.ots.test.ui.common.utils;

import static io.restassured.RestAssured.expect;

import java.util.HashMap;

import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import  io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class APITestHelper {

	public static final String API_HOST = System.getProperty("app.base.url","https://runway-preprod-auth-service.azurewebsites.net");
	public static final String WEB_UI_CONTEXT = System.getProperty("web.ui.context","/api");
    public static final String API_LOGIN_PATH = System.getProperty("api.login.path", WEB_UI_CONTEXT+"/Authentication");
    public static final String API_WAREHOUSES_PATH = System.getProperty("api.warehouses.path", WEB_UI_CONTEXT+"/GetWarehouses");
	
    
	private String accessToken = null;
	private String clientCode = null;
	private  Response response = null;
	
	
	public static final int HTTP_STATUS_OK = 200;
	public static final int HTTP_STATUS_CREATED = 201;
			
	Logger logger = LoggerFactory.getLogger(APITestHelper.class);
	public Object res;
	
	public APITestHelper() {
		logger.info(">>>Setting base uri..."+API_HOST);
		RestAssured.baseURI = API_HOST;
		RestAssured.basePath="/";
	}

	public Response doLogin(JsonObject reqObject, String expStatusCode) {
		
	
		 RestAssured.baseURI=API_HOST;
		 Response res = expect().statusCode(Integer.parseInt(expStatusCode)).given()
			.contentType("application/json")
			.header("x-functions-key", "k0u2aKraEyN56O0BZAlxtkyEy_49KH5utS6r4eUagglQAzFueo10FQ==")
			.body(reqObject.toString())
			.log().everything()
			 
		 .when().post(API_LOGIN_PATH);
		 res.then().log().all();
		 response = res;
		 if(expStatusCode.equals(getIntValue(res, "statusCode")+"")) {
			 
		
			 clientCode = getStringValue(res,"clientCode");
		 
			 logger.info("After login--> clientCode ="+clientCode);
		 } 
		 return res;
	}
	
	public Response doLogin(JsonObject reqObject) {
		return doLogin(reqObject ,"200");
	}	
	
public String getStringValue(Response res, String key) {
		
		String json = res.asString();
		JsonPath jsonPath = new JsonPath(json);
		return jsonPath.get(key);

	}


	/**
	 * Get a key's HashMap value from response body
	 * 
	 * @param res
	 * @param key
	 * @return
	 */

public HashMap getArrayValue(Response res, String key) {
	
	String json = res.asString();
	JsonPath jsonPath = new JsonPath(json);
	return  jsonPath.get(key);
	
}

/**
 * Get a key's string value from response body
 * 
 * @param res
 * @param key
 * @return
 */
    
	public String getStringValue(ResponseBody res, String key) {
		
		String json = res.asString();
		JsonPath jsonPath = new JsonPath(json);
		return jsonPath.get(key)
;

	}	
	/**
	 * Get a key's int value from response
	 * 
	 * @param res
	 * @param key
	 * @return
	 */
	public int getIntValue(Response res, String key) {
		
		String json = res.asString();
		JsonPath jsonPath = new JsonPath(json);
		return (Integer)(jsonPath.get(key));

	}
}

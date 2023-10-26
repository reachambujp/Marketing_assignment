/**
 * 
 */
package com.restassured.test.helper;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restassured.constant.RestAPIConstant;
import com.restassured.pojo.LoginDataPOJO;
import com.restassured.util.PropertiesUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ReusableMethodsHelper {
	
	PropertiesUtility propUtil= new PropertiesUtility();
	Properties restAPIProperties;


	
	@BeforeClass
	public void init() {
		restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		RestAssured.baseURI= restAPIProperties.getProperty("API_BASE_URL");
	}
	
	//TC1- Login API -Positive
	@Test(priority = 1)
	public String LoginAPITC1() {
		String extractedtoken;
		
		LoginDataPOJO loginDataPojo = new LoginDataPOJO();
		loginDataPojo.setEmail(restAPIProperties.getProperty("email"));
		loginDataPojo.setPassword(restAPIProperties.getProperty("password"));
		
		RequestSpecification req=RestAssured.given()
		.contentType(ContentType.JSON)
		.body(loginDataPojo);
				
		Response res= req.when()
	    .post(restAPIProperties.getProperty("LOGIN_URI"));
		 res.then()
		.statusCode(200)
		.contentType(ContentType.JSON)
		.time(Matchers.lessThan(5000L));
		 
		 res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginSchema.json"));
		 System.out.println("LoginJsonSchemaValidator is passed for TC1");
		 
		 
		  extractedtoken= res.body().jsonPath().getString("data.Token");
		System.out.println("Token TC1="+ extractedtoken );
		
		return extractedtoken;
		
		
	}
}

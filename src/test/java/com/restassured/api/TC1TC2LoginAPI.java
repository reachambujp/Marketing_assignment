package com.restassured.api;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restassured.constant.RestAPIConstant;
import com.restassured.pojo.LoginDataPOJO;
import com.restassured.util.PropertiesUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC1TC2LoginAPI {
	
	PropertiesUtility propUtil= new PropertiesUtility();
	Properties restAPIProperties;
	
	@BeforeClass
	public void init() {
		restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		RestAssured.baseURI= restAPIProperties.getProperty("API_BASE_URL");
	}
	
	//TC1- Login API -Positive
	@Test
	public void LoginAPITC1() {
		
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
		 
		 
		 //res.prettyPrint();
		 
		 String extractedtoken= res.body().jsonPath().getString("data.Token");
		System.out.println("Token TC1="+ extractedtoken );
		
		
	}
	
	
	//TC2- Login API -Negative
		@Test
		public void LoginAPITC2() {
			
			LoginDataPOJO loginDataPojo = new LoginDataPOJO();
			loginDataPojo.setEmail(restAPIProperties.getProperty("invalidEmail"));
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
			 System.out.println("LoginJsonSchemaValidator is passed for TC2");
			 
			 
			 //res.prettyPrint();
			 
			 String extractedData= res.body().jsonPath().getString("data");
			System.out.println("Data TC2="+ extractedData );
			
			
		}

}

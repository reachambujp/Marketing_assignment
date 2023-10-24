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

public class TC4InvalidAPIDomain {
	
	PropertiesUtility propUtil= new PropertiesUtility();
	Properties restAPIProperties;
	
	@BeforeClass
	public void init() {
		restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		RestAssured.baseURI= restAPIProperties.getProperty("INVALID_API_BASE_URL2");
	}
	
	//TC4-Change Top level API Domain to incorrect value - Negative
	@Test
	public void invalidDomainNameTC4() {
		
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
	
	
	

}

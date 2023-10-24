package com.restassured.api;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restassured.constant.RestAPIConstant;
import com.restassured.pojo.DeleteDataPOJO;
import com.restassured.pojo.LoginDataPOJO;
import com.restassured.util.PropertiesUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC24DeleteAPIByID {
	
	PropertiesUtility propUtil= new PropertiesUtility();
	Properties restAPIProperties;
	String extractedtoken;
	
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
		 
	    extractedtoken= res.body().jsonPath().getString("data.Token");
		System.out.println("Token TC1="+ extractedtoken );
		
		
	}
	
	
	//TC24 DeleteUserByID
	
	@Test(dependsOnMethods = "LoginAPITC1")
	public void deleteUserByIDTC24() {
		
		Properties restAPIProperties= propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		
		DeleteDataPOJO deleteDataPojo=new DeleteDataPOJO();
		deleteDataPojo.setId(restAPIProperties.getProperty("id"));
		
		
		System.out.println("Create user TC24 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer "+extractedtoken);
		
		RequestSpecification req= RestAssured.given()
		.header(ob)
		.contentType(ContentType.JSON)
		.body(deleteDataPojo);
		
		Response res= req.when()
			    .delete(restAPIProperties.getProperty("DELETEUSER_URI"));
		res.then()
		.statusCode(405);
		
		System.out.println("Delete User By ID TC24 is passed");
		
		 
		
	}

	
	
	
}

package com.restassured.api;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restassured.constant.RestAPIConstant;
import com.restassured.pojo.LoginDataPOJO;
import com.restassured.pojo.UpdateDataPOJO;
import com.restassured.util.PropertiesUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC18UpdateUserByIdNameUpdate {
	
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
	
	
	//TC18- UpdateUserByID - Name Update
		
	
	@Test(dependsOnMethods = "LoginAPITC1" )
	public void updateUserNameAPITC18() {
		Properties restAPIProperties= propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		
		UpdateDataPOJO updateDataPojo= new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("Update_name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("location"));
		
		
		System.out.println("Update user TC18 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer "+extractedtoken);
		
		RequestSpecification req= RestAssured.given()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(updateDataPojo);
		
		Response res= req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));
		
		res.then()
		.statusCode(405);
		
		System.out.println("Update User Name API TC18 is Passed");
		
	}
	
	
	//TC19- UpdateUserByID - Email Update
	
	
	@Test(dependsOnMethods = "LoginAPITC1" )
	public void updateUserEmailAPITC19() {
		Properties restAPIProperties= propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		
		UpdateDataPOJO updateDataPojo= new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("Update_email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("location"));
		
		
		System.out.println("Update user TC19 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer "+extractedtoken);
		
		RequestSpecification req= RestAssured.given()
				.header(ob)
				.contentType(ContentType.JSON)
				.body(updateDataPojo);
		
		Response res= req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));
		
		res.then()
		.statusCode(405);
		
		System.out.println("Update User Email API TC19 is Passed");
		
	}
	
	
	
	//TC20- UpdateUserByID - location Update
	
	
		@Test(dependsOnMethods = "LoginAPITC1" )
		public void updateUserLocationAPITC20() {
			Properties restAPIProperties= propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
			
			UpdateDataPOJO updateDataPojo= new UpdateDataPOJO();
			updateDataPojo.setId(restAPIProperties.getProperty("id"));
			updateDataPojo.setName(restAPIProperties.getProperty("name"));
			updateDataPojo.setEmail(restAPIProperties.getProperty("email"));
			updateDataPojo.setLocation(restAPIProperties.getProperty("Update_location"));
			
			
			System.out.println("Update user TC20 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer "+extractedtoken);
			
			RequestSpecification req= RestAssured.given()
					.header(ob)
					.contentType(ContentType.JSON)
					.body(updateDataPojo);
			
			Response res= req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));
			
			res.then()
			.statusCode(405);
			
			System.out.println("Update User location API TC20 is Passed");
		}
		
		

		// TC21- UpdateUserByID - Name and Location

		@Test(dependsOnMethods = "LoginAPITC1")
		public void updateUserNameAndLocationAPITC21() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
			updateDataPojo.setId(restAPIProperties.getProperty("id"));
			updateDataPojo.setName(restAPIProperties.getProperty("Update_name"));
			updateDataPojo.setEmail(restAPIProperties.getProperty("email"));
			updateDataPojo.setLocation(restAPIProperties.getProperty("Update_location"));

			System.out.println("Update user TC21 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer "+extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON)
					.body(updateDataPojo);

			Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

			res.then().statusCode(405);
			
			System.out.println("Update User Name and Location API TC21 is Passed");
		}
		
		
		
		// TC22- UpdateUserByID - Name and Email

		@Test(dependsOnMethods = "LoginAPITC1")
		public void updateUserNameAndEmailAPITC22() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
			updateDataPojo.setId(restAPIProperties.getProperty("id"));
			updateDataPojo.setName(restAPIProperties.getProperty("Update_name"));
			updateDataPojo.setEmail(restAPIProperties.getProperty("Update_email"));
			updateDataPojo.setLocation(restAPIProperties.getProperty("location"));

			System.out.println("Update user TC22 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer "+extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON)
					.body(updateDataPojo);

			Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

			res.then().statusCode(405);
			
			System.out.println("Update User Name and Email API TC122 is Passed");
		}
		
		
		
		// TC23- UpdateUserByID -  Email and Location

		@Test(dependsOnMethods = "LoginAPITC1")
		public void updateUserNameAndLocationAPITC23() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
			updateDataPojo.setId(restAPIProperties.getProperty("id"));
			updateDataPojo.setName(restAPIProperties.getProperty("name"));
			updateDataPojo.setEmail(restAPIProperties.getProperty("Update_email"));
			updateDataPojo.setLocation(restAPIProperties.getProperty("Update_location"));

			System.out.println("Update user TC23 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer "+extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON)
					.body(updateDataPojo);

			Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

			res.then().statusCode(405);
			
			System.out.println("Update User Email and Location API TC123 is Passed");
			
		}
		
		

	}

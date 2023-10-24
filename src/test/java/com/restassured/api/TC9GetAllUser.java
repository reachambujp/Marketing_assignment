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

public class TC9GetAllUser {

	PropertiesUtility propUtil = new PropertiesUtility();
	Properties restAPIProperties;
	String extractedtoken;

	@BeforeClass
	public void init() {
		restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		RestAssured.baseURI = restAPIProperties.getProperty("API_BASE_URL");
	}

	// TC1- Login API -Positive
	@Test
	public void LoginAPITC1() {

		LoginDataPOJO loginDataPojo = new LoginDataPOJO();
		loginDataPojo.setEmail(restAPIProperties.getProperty("email"));
		loginDataPojo.setPassword(restAPIProperties.getProperty("password"));

		RequestSpecification req = RestAssured.given().contentType(ContentType.JSON).body(loginDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("LOGIN_URI"));
		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC1");

		// res.prettyPrint();

		extractedtoken = res.body().jsonPath().getString("data.Token");
		System.out.println("Token TC1=" + extractedtoken);

	}

	// TC9- Get All Users with page number- Positive

	@Test(dependsOnMethods = "LoginAPITC1")
	public void getAllUerTC9() {
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		System.out.println("GETALLUsers TC9 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI"));

		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		res.prettyPrint();
		// res.body().jsonPath().get("data.length");
		System.out.println("Total number of record= " + res.body().jsonPath().get("data.size()"));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC9");

		int id = res.body().jsonPath().get("data[0].id");
		System.out.println("ID Extracted from TC9 = " + id);

		String name = res.body().jsonPath().get("data[5].name");
		System.out.println("ID Extracted from TC9 = " + name);

	}

	// TC10- GetAllUsers with page number where page number contains leading 0's.-
	// Positive

	@Test(dependsOnMethods = "LoginAPITC1")
	public void getAllUerTC10() {
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		System.out.println("GETALLUsers TC10 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI2"));

		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		res.prettyPrint();
		// res.body().jsonPath().get("data.length");
		System.out.println("Total number of record= " + res.body().jsonPath().get("data.size()"));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC10");

		int id = res.body().jsonPath().get("data[0].id");
		System.out.println("ID Extracted from TC10 = " + id);

		String name = res.body().jsonPath().get("data[5].name");
		System.out.println("ID Extracted from TC10 = " + name);

	}

	// TC11- GetAllUsers by keeping the query parameter page value as blank-
	// Negative

	@Test(dependsOnMethods = "LoginAPITC1")
	public void getAllUerTC11() {
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		System.out.println("GETALLUsers TC11 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI3"));

		res.then().statusCode(400).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		String extractedResponse = res.body().jsonPath().getString("Message");
		System.out.println("Extracted data From TC11: " + extractedResponse);

	}

	// TC12- GetUserByID - Positive

	@Test(dependsOnMethods = "LoginAPITC1")
	public void getAllUerTC12() {
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		System.out.println("GETALLUsers TC12 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI4"));

		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserDataSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC12");

		System.out.println("Extract Location: " + res.body().jsonPath().get("location"));

		res.prettyPrint();

		

	}
	
	
	// TC13- GetUserByID - Negative

		@Test(dependsOnMethods = "LoginAPITC1")
		public void getAllUerTC13() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			System.out.println("GETALLUsers TC13 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer " + extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

			Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI5"));

			res.then().statusCode(404).time(Matchers.lessThan(20000L));

			int statusCode = res.getStatusCode();
			System.out.println("Extracted Status code: "+ statusCode);
			

		}
		
		// TC14- GetUserByID - Negative(Lower Boundary value)

		@Test(dependsOnMethods = "LoginAPITC1")
		public void getAllUerTC14() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			System.out.println("GETALLUsers TC14 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer " + extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

			Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI6"));

			res.then().statusCode(500).time(Matchers.lessThan(20000L));

			int statusCode = res.getStatusCode();
			System.out.println("Extracted Status code From TC14: " + statusCode);
			
			String extractedResponse = res.body().jsonPath().getString("Message");
			System.out.println("Extracted data From TC14: " + extractedResponse);

		}
		
		
		// TC15- GetUserByID - Negative(Higher Boundary value)

		@Test(dependsOnMethods = "LoginAPITC1")
		public void getAllUerTC15() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			System.out.println("GETALLUsers TC15 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer " + extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

			Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI7"));

			res.then().statusCode(200).time(Matchers.lessThan(20000L));

			int statusCode = res.getStatusCode();
			System.out.println("Extracted Status code From TC15: " + statusCode);

			res.prettyPrint();

			res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
			System.out.println("LoginJsonSchemaValidator is passed for TC15");

		}
		
		
		
		// TC16- GetAllUsers by dropping the query parameter - Negative

		@Test(dependsOnMethods = "LoginAPITC1")
		public void getAllUerTC16() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			System.out.println("GETALLUsers TC16 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer " + extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

			Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI8"));

			res.then().statusCode(200).time(Matchers.lessThan(20000L));

			int statusCode = res.getStatusCode();
			System.out.println("Extracted Status code: " + statusCode);

			res.prettyPrint();

			res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
			System.out.println("LoginJsonSchemaValidator is passed for TC15");

			int pageNum = res.body().jsonPath().getInt("page");
			System.out.println("Page Number is: " + pageNum);

		}
		
		// TC17- GetAllUsers by invalid Endpoint - Negative - Negative

		@Test(dependsOnMethods = "LoginAPITC1")
		public void getAllUerTC17() {
			Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

			System.out.println("GETALLUsers TC17 extractedtoken =" + extractedtoken);
			Header ob = new Header("Authorization", "Bearer " + extractedtoken);

			RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

			Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI9"));

			res.then().statusCode(404).time(Matchers.lessThan(20000L));

			int statusCode = res.getStatusCode();
			System.out.println("Extracted Status code From TC17: " + statusCode);


		}
				


}

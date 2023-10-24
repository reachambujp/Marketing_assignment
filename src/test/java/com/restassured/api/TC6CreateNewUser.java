package com.restassured.api;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.restassured.constant.RestAPIConstant;
import com.restassured.pojo.CreateDataPOJO;
import com.restassured.pojo.LoginDataPOJO;
import com.restassured.util.PropertiesUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC6CreateNewUser {

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
		System.out.println("LoginJsonSchemaValidator is passed for TC6");

		// res.prettyPrint();

		extractedtoken = res.body().jsonPath().getString("data.Token");
		System.out.println("Token TC6=" + extractedtoken);

	}

	// TC6- Create New User -Psitive
	@Test(dependsOnMethods = "LoginAPITC1")
	public void createUserTC6() {

		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		CreateDataPOJO createDataPojo = new CreateDataPOJO();
		createDataPojo.setName(restAPIProperties.getProperty("name"));
		createDataPojo.setEmail(restAPIProperties.getProperty("email_New"));
		createDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Create user TC 6 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(createDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("CREATEUSER_URI"));
		System.out.println("URI=" + restAPIProperties.getProperty("CREATEUSER_URI"));

		res.then().statusCode(201).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserDataSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC6");

	}

	// TC7- Create New User -Negative Do not pass Email in payload
	@Test(dependsOnMethods = "LoginAPITC1")
	public void createUserTC7() {

		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		CreateDataPOJO createDataPojo = new CreateDataPOJO();
		createDataPojo.setName(restAPIProperties.getProperty("name"));
		createDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Create user TC7 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(createDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("CREATEUSER_URI"));
		System.out.println("URI=" + restAPIProperties.getProperty("CREATEUSER_URI"));

		res.then().statusCode(500).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		// res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserDataSchema.json"));
		// System.out.println("LoginJsonSchemaValidator is passed for TC7");

		String extractedResponse = res.body().jsonPath().getString("Message");
		System.out.println("Extracted data From TC7: " + extractedResponse);

	}

	// TC8- Create New User -Negative Create New User again with same data as in TC6
	@Test(dependsOnMethods = { "LoginAPITC1", "createUserTC6" })
	public void createUserDuplicateUserCreationTC8() {

		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);

		CreateDataPOJO createDataPojo = new CreateDataPOJO();
		createDataPojo.setName(restAPIProperties.getProperty("name"));
		createDataPojo.setEmail(restAPIProperties.getProperty("email_New"));
		createDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Create user TC8 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(createDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("CREATEUSER_URI"));
		System.out.println("URI=" + restAPIProperties.getProperty("CREATEUSER_URI"));

		res.then().statusCode(400).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		// res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserDataSchema.json"));
		// System.out.println("LoginJsonSchemaValidator is passed for TC7");

		String extractedResponse = res.body().jsonPath().getString("Message");
		System.out.println("Extracted data From TC8: " + extractedResponse);

	}

}

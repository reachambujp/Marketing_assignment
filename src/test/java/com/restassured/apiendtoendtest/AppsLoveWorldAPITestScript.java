package com.restassured.apiendtoendtest;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.restassured.constant.RestAPIConstant;
import com.restassured.pojo.CreateDataPOJO;
import com.restassured.pojo.DeleteDataPOJO;
import com.restassured.pojo.LoginDataPOJO;
import com.restassured.pojo.UpdateDataPOJO;
import com.restassured.test.helper.ReusableMethodsHelper;
import com.restassured.util.PropertiesUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class AppsLoveWorldAPITestScript extends ReusableMethodsHelper {

	PropertiesUtility propUtil = new PropertiesUtility();
	Properties restAPIProperties;
	String extractedtoken;
	private static final Logger logger=LogManager.getLogger(AppsLoveWorldAPITestScript.class);

	// TC2- Login API -Negative
	@Test
	public void LoginAPITC2() {
		
		
		restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		logger.info("LoginAPITC2 Execution started");

		LoginDataPOJO loginDataPojo = new LoginDataPOJO();
		loginDataPojo.setEmail(restAPIProperties.getProperty("invalidEmail"));
		loginDataPojo.setPassword(restAPIProperties.getProperty("password"));

		RequestSpecification req = RestAssured.given().contentType(ContentType.JSON).body(loginDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("LOGIN_URI"));
		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC2");

		// res.prettyPrint();

		String extractedData = res.body().jsonPath().getString("data");
		System.out.println("Data TC2=" + extractedData);
		
		logger.info("LoginAPITC2 Execution ended");

	}

	// TC3- HTTPS Protocol API support - Negative
	@Test
	public void invalidProtocolTC3() {
		
		logger.info("invalidProtocolTC3 Execution started");

		LoginDataPOJO loginDataPojo = new LoginDataPOJO();
		loginDataPojo.setEmail(restAPIProperties.getProperty("email"));
		loginDataPojo.setPassword(restAPIProperties.getProperty("password"));

		RequestSpecification req = RestAssured.given().contentType(ContentType.JSON).body(loginDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("LOGIN_URI"));
		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC3");
		
		logger.info("invalidProtocolTC3 Execution ended");

		// res.prettyPrint();

		// String extractedtoken= res.body().jsonPath().getString("data.Token");
		// System.out.println("Token TC1="+ extractedtoken );

	}

	// TC4-Change Top level API Domain to incorrect value - Negative
	@Test
	public void invalidDomainNameTC4() {
		
		logger.info("invalidDomainNameTC4 Execution started");

		LoginDataPOJO loginDataPojo = new LoginDataPOJO();
		loginDataPojo.setEmail(restAPIProperties.getProperty("email"));
		loginDataPojo.setPassword(restAPIProperties.getProperty("password"));

		RequestSpecification req = RestAssured.given().contentType(ContentType.JSON).body(loginDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("LOGIN_URI"));
		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		/*res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC4");*/
		
		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC4: " + statusCode);

		
		logger.info("invalidDomainNameTC4 Execution ended");

	}

	// TC5-Change in server address name - Negative
	@Test
	public void invalidSeverAddressNameTC5() {
        
		logger.info("invalidSeverAddressNameTC5 Execution started");
		LoginDataPOJO loginDataPojo = new LoginDataPOJO();
		loginDataPojo.setEmail(restAPIProperties.getProperty("email"));
		loginDataPojo.setPassword(restAPIProperties.getProperty("password"));

		RequestSpecification req = RestAssured.given().contentType(ContentType.JSON).body(loginDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("LOGIN_URI"));
		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(5000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("LoginSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC5");

		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC5: " + statusCode);
		
		logger.info("invalidSeverAddressNameTC5 Execution ended");

	}

	// TC6- Create New User -Positive
	@Test
	public void createUserTC6() {
		
		// logger.info("createUserTC6 Execution started - Step1");          
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
	    extractedtoken = LoginAPITC1();
	    logger.info("createUserTC6 Execution started");

		CreateDataPOJO createDataPojo = new CreateDataPOJO();
		createDataPojo.setName(restAPIProperties.getProperty("name"));
		createDataPojo.setEmail(restAPIProperties.getProperty("email_New"));
		createDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Create user TC 6 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(createDataPojo);

		Response res = req.when().post(restAPIProperties.getProperty("CREATEUSER_URI"));
		System.out.println("URI=" + restAPIProperties.getProperty("CREATEUSER_URI"));

		res.then().statusCode(201).contentType(ContentType.JSON).time(Matchers.lessThan(10000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserDataSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC6");
		
		logger.info("createUserTC6 Execution ended");

	}

	// TC7- Create New User -Negative Do not pass Email in payload
	@Test
	public void createUserTC7() {
		
		logger.info("createUserTC7 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

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
		
		logger.info("createUserTC7 Execution ended");

	}

	// TC8- Create New User -Negative Create New User again with same data as in TC6
	@Test(dependsOnMethods =  "createUserTC6" )
	public void createUserDuplicateUserCreationTC8() {
		
		logger.info("createUserDuplicateUserCreationTC8 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

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
		// System.out.println("LoginJsonSchemaValidator is passed for TC8");

		String extractedResponse = res.body().jsonPath().getString("Message");
		System.out.println("Extracted data From TC8: " + extractedResponse);
		
		logger.info("createUserDuplicateUserCreationTC8 Execution ended");

	}

	// TC9- Get All Users with page number- Positive

	@Test
	public void getAllUerTC9() {
		
		logger.info("getAllUerTC9 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

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
		System.out.println("Name Extracted from TC9 = " + name);
		
		logger.info("getAllUerTC9 Execution ended");

	}

	// TC10- GetAllUsers with page number where page number contains leading 0's.-
	// Positive

	@Test
	public void getAllUerTC10() {
		
		logger.info("getAllUerTC10 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		System.out.println("GETALLUsers TC10 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI2"));

		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		res.prettyPrint();
		// res.body().jsonPath().get("data.length");
		System.out.println("Total number of record from TC10= " + res.body().jsonPath().get("data.size()"));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC10");

		int id = res.body().jsonPath().get("data[0].id");
		System.out.println("ID Extracted from TC10 = " + id);

		String name = res.body().jsonPath().get("data[5].name");
		System.out.println("Name Extracted from TC10 = " + name);
		
		logger.info("getAllUerTC10 Execution ended");

	}

	// TC11- GetAllUsers by keeping the query parameter page value as blank-
	// Negative

	@Test
	public void getAllUerTC11() {
		
		logger.info("getAllUerTC11 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();

		System.out.println("GETALLUsers TC11 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI3"));

		res.then().statusCode(400).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		String extractedResponse = res.body().jsonPath().getString("Message");
		System.out.println("Extracted data From TC11: " + extractedResponse);
		
		logger.info("getAllUerTC11 Execution ended");

	}

	// TC12- GetUserByID - Positive

	@Test
	public void getAllUerTC12() {
		
		logger.info("getAllUerTC12 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		
		System.out.println("GETALLUsers TC12 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI4"));

		res.then().statusCode(200).contentType(ContentType.JSON).time(Matchers.lessThan(20000L));

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("UserDataSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC12");

		System.out.println("Extracted Location from TC12: " + res.body().jsonPath().get("location"));

		res.prettyPrint();
		
		logger.info("getAllUerTC12 Execution ended");

	}

	// TC13- GetUserByID - Negative

	@Test
	public void getAllUerTC13() {
		
		logger.info("getAllUerTC13 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		
		System.out.println("GETALLUsers TC13 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI5"));

		res.then().statusCode(404).time(Matchers.lessThan(20000L));

		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC13: " + statusCode);
		
		logger.info("getAllUerTC13 Execution ended");

	}

	// TC14- GetUserByID - Negative(Lower Boundary value)

	@Test
	public void getAllUerTC14() {
		
		logger.info("getAllUerTC14 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		System.out.println("GETALLUsers TC14 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI6"));

		res.then().statusCode(500).time(Matchers.lessThan(20000L));

		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC14: " + statusCode);

		String extractedResponse = res.body().jsonPath().getString("Message");
		System.out.println("Extracted data From TC14: " + extractedResponse);
		
		logger.info("getAllUerTC14 Execution ended");

	}

	// TC15- GetUserByID - Negative(Higher Boundary value)

	@Test
	public void getAllUerTC15() {
		
		logger.info("getAllUerTC15 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		System.out.println("GETALLUsers TC15 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI7"));

		res.then().statusCode(200).time(Matchers.lessThan(20000L));

		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC15: " + statusCode);

		res.prettyPrint();

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC15");
		
		logger.info("getAllUerTC15 Execution ended");

	}

	// TC16- GetAllUsers by dropping the query parameter - Negative

	@Test
	public void getAllUerTC16() {
		
		logger.info("getAllUerTC16 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		System.out.println("GETALLUsers TC16 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI8"));

		res.then().statusCode(200).time(Matchers.lessThan(20000L));

		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC16: " + statusCode);

		res.prettyPrint();

		res.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("GetAllUserSchema.json"));
		System.out.println("LoginJsonSchemaValidator is passed for TC16");

		int pageNum = res.body().jsonPath().getInt("page");
		System.out.println("Page Number is: " + pageNum);
		
		logger.info("getAllUerTC16 Execution ended");

	}

	// TC17- GetAllUsers by invalid Endpoint - Negative - Negative

	@Test
	public void getAllUerTC17() {
		
		logger.info("getAllUerTC17 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		System.out.println("GETALLUsers TC17 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON);

		Response res = req.when().get(restAPIProperties.getProperty("GETALLDATA_URI9"));

		res.then().statusCode(404).time(Matchers.lessThan(20000L));

		int statusCode = res.getStatusCode();
		System.out.println("Extracted Status code from TC17: " + statusCode);
		
		logger.info("getAllUerTC17 Execution ended");

	}

	// TC18- UpdateUserByID - Name Update

	@Test
	public void updateUserNameAPITC18() {
		
		logger.info("updateUserNameAPITC18 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("Update_name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Update user TC18 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(updateDataPojo);

		Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

		res.then().statusCode(405);

		logger.info("updateUserNameAPITC18 Execution ended");

	}

	// TC19- UpdateUserByID - Email Update

	@Test
	public void updateUserEmailAPITC19() {
		
		logger.info("updateUserEmailAPITC19 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		logger.info("updateUserEmailAPITC19 Execution started");

		UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("Update_email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Update user TC19 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(updateDataPojo);

		Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

		res.then().statusCode(405);

		logger.info("updateUserEmailAPITC19 Execution ended");

	}

	// TC20- UpdateUserByID - location Update

	@Test
	public void updateUserLocationAPITC20() {
		
		logger.info("updateUserLocationAPITC20 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("Update_location"));

		System.out.println("Update user TC20 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(updateDataPojo);

		Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

		res.then().statusCode(405);

		logger.info("updateUserLocationAPITC20 Execution ended");
	}

	// TC21- UpdateUserByID - Name and Location

	@Test
	public void updateUserNameAndLocationAPITC21() {
		
		logger.info("updateUserNameAndLocationAPITC21 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("Update_name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("Update_location"));

		System.out.println("Update user TC21 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(updateDataPojo);

		Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

		res.then().statusCode(405);

		logger.info("updateUserNameAndLocationAPITC21 Execution ended");
	}

	// TC22- UpdateUserByID - Name and Email

	@Test
	public void updateUserNameAndEmailAPITC22() {
		
		logger.info("updateUserNameAndEmailAPITC22 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("Update_name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("Update_email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("location"));

		System.out.println("Update user TC22 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(updateDataPojo);

		Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

		res.then().statusCode(405);

		logger.info("updateUserNameAndEmailAPITC22 Execution ended");
	}

	// TC23- UpdateUserByID - Email and Location

	@Test
	public void updateUserNameAndLocationAPITC23() {
		
		logger.info("updateUserNameAndLocationAPITC23 Execution started");
		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();
		

		UpdateDataPOJO updateDataPojo = new UpdateDataPOJO();
		updateDataPojo.setId(restAPIProperties.getProperty("id"));
		updateDataPojo.setName(restAPIProperties.getProperty("name"));
		updateDataPojo.setEmail(restAPIProperties.getProperty("Update_email"));
		updateDataPojo.setLocation(restAPIProperties.getProperty("Update_location"));

		System.out.println("Update user TC23 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(updateDataPojo);

		Response res = req.when().put(restAPIProperties.getProperty("UPDATEUSER_URI"));

		res.then().statusCode(405);

		logger.info("updateUserNameAndLocationAPITC23 Execution ended");

	}

	// TC24 DeleteUserByID

	@Test
	public void deleteUserByIDTC24() {
		
		logger.info("deleteUserByIDTC24 Execution started");

		Properties restAPIProperties = propUtil.loadPropFile(RestAPIConstant.REST_API_PROPERTIES_FILE_PATH);
		extractedtoken = LoginAPITC1();

		DeleteDataPOJO deleteDataPojo = new DeleteDataPOJO();
		deleteDataPojo.setId(restAPIProperties.getProperty("id"));

		System.out.println("Create user TC24 extractedtoken =" + extractedtoken);
		Header ob = new Header("Authorization", "Bearer " + extractedtoken);

		RequestSpecification req = RestAssured.given().header(ob).contentType(ContentType.JSON).body(deleteDataPojo);

		Response res = req.when().delete(restAPIProperties.getProperty("DELETEUSER_URI"));
		res.then().statusCode(405);

		logger.info("deleteUserByIDTC24 Execution ended");

	}

}

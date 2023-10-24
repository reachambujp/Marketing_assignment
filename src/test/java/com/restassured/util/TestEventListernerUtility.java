package com.restassured.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestEventListernerUtility implements ITestListener{
	private static final Logger logger=LogManager.getLogger(TestEventListernerUtility.class);

	@Override
	public void onTestStart(ITestResult result) {
		logger.info("Test Started");
	}

	
	@Override
	public void onTestSuccess(ITestResult result) {
		logger.info("Test '" + result.getName()+ "' PASSED");
	}

	
	@Override
	public void onTestFailure(ITestResult result) {
		logger.info("Test '" + result.getName()+ "' FAILED");
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		logger.info("Test '" + result.getName()+ "' SKIPPED");
		
	}

	
	
	
	
	
	

}

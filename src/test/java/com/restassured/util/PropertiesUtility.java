package com.restassured.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtility {
	
	public Properties loadPropFile(String filePath) {
		Properties propertyFile = new Properties();
		
		try {
			FileInputStream inputStream = new FileInputStream(filePath);
			propertyFile.load(inputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return propertyFile;
		
	}
	
	public String readDataFromPropertyFile(File Path, String key) {
		
		return null;
		
	}

}

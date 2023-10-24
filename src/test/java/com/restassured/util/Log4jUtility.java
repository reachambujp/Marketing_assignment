package com.restassured.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jUtility {
	private Logger log=null;
	private static Log4jUtility ob=null;
	
	
	private Log4jUtility() {
		
	}
	
	public static Log4jUtility getInstance() {
		if(ob==null) {
			ob=new Log4jUtility();
		}
		return ob;
	}
	
	public Logger getLogger() {
		
		log=LogManager.getLogger(Log4jUtility.class);
		return log;
	}

}

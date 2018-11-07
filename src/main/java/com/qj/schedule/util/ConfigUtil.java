package com.qj.schedule.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConfigUtil {
 
	static PropertyUtil propertyUtil;
 
	static{
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		propertyUtil = (PropertyUtil) ac.getBean("propertyUtil");
	}
	
	public static String get(String key) {
		return propertyUtil.getProperty(key);
	}
	
}
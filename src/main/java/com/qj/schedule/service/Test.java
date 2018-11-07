package com.qj.schedule.service;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.qj.schedule.util.ConfigUtil;
import com.qj.schedule.util.PropertyUtil;


public class Test {
	
	public static void main(String[] args){
		System.out.println("configUtil:" + ConfigUtil.get("project_name"));
		
		System.out.println("configUtil:" + ConfigUtil.get("project_name"));
		
		System.out.println("configUtil:" + ConfigUtil.get("project_name"));
		
		System.out.println("configUtil:" + ConfigUtil.get("project_name"));
		
		System.out.println("system.property:" + System.getProperty("config"));
	}

}

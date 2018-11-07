package com.qj.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;
import java.util.TimeZone;

import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.dao.ExecutionDao;
import com.qj.schedule.dao.HostDao;
import com.qj.schedule.service.ScheduleMasterV2;
import com.qj.schedule.util.ConfigUtil2;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.FileUtil;
import com.qj.schedule.util.PropertyUtil;
import com.qj.schedule.util.StringUtil;

public class Test {
	
	public static void main(String[] args){
//		String date = "yesterday ";
//		if(date == "yesterday") {
//			System.out.println("ok");
//		}
//		System.out.println(DateUtil.time2str(DateUtil.oral2time("now")));
//		System.out.println(DateUtil.time2str(DateUtil.oral2time("today")));
//		System.out.println(DateUtil.time2str(DateUtil.oral2time("yesterday")));
//		System.out.println(DateUtil.time2str(DateUtil.oral2time("tomorrow")));
//		System.out.println(new Test().getTodayTime());
//		
//		System.out.println(ConfigUtil.get("zookeeper_leaderpath"));
		
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		PropertyUtil propertyUtil = (PropertyUtil) ac.getBean("propertyUtil");
		System.out.println("propertyUtil:" + propertyUtil.getProperty("project_name"));
		System.out.println("configUtil:" + ConfigUtil2.get("project_name"));
	}
	
	public long getTodayTime() {
		Calendar c = Calendar.getInstance();    
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long today = c.getTimeInMillis()/1000;
		return today;
	}
}

package com.qj.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Set;

import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.dao.ScheduleDao;
import com.qj.schedule.service.ScheduleMaster;
import com.qj.schedule.util.DateUtil;

public class MasterTest {
	private ScheduleDao scheduleDao;
	public static void main(String[] args){
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleMaster master = (ScheduleMaster) factory.getBean("scheduleMaster");
		//long now = new Date().getTime()/1000;
		long now = DateUtil.str2time("2018-07-18 08:00:28");
		List<ScheduleBean> scheduleList = master.getScheduleDao().getRunableScheduleList();
		
		for (ScheduleBean schedule : scheduleList) {
			System.out.println(schedule.getScheduleId() + "#" + schedule.getCron());
			if(master.checkSchedule(schedule, now)){
				boolean isOk = master.doSchedule(schedule, now);
				String logStr = "[id:" + schedule.getScheduleId() + "]";
				if(isOk){
					System.out.println("Schedule" + logStr + " suceess");
				}else{
					System.out.println("Schedule" + logStr + " failed");
				}
			}
		}
	}
}

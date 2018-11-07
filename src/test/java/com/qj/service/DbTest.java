package com.qj.service;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.service.ScheduleMaster;

public class DbTest {

	public static void main(String[] args){
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleMaster master = (ScheduleMaster) factory.getBean("scheduleMaster");
		List<ScheduleBean> scheduleList = master.getScheduleDao().getRunableScheduleList();
		
		for(ScheduleBean s : scheduleList){
			System.out.println(s.getTitle());
		}
	}
}

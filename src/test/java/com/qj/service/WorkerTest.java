package com.qj.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qj.schedule.service.ScheduleWorker;

public class WorkerTest {

	public static void main(String[] args) {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleWorker worker = (ScheduleWorker) factory.getBean("scheduleWorker");
		System.out.println("aaaaaaaaaaaa");
		boolean flag = worker.getExecutionDao().setExecutionStatus(15, 2, 3);
		System.out.println(flag);
	}

}

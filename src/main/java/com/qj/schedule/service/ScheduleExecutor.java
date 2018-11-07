package com.qj.schedule.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.*;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.qj.schedule.dao.JobDao;
import com.qj.schedule.runnable.Executor;


class Job extends TimerTask{
	
	private String name;
	
	private String cmd;
	
	public Job(String name, String cmd){
		this.name = name;
		this.cmd = cmd;
	}
	
	@Override
	public void run() {
		int exitCode = 0;
		String osName = System.getProperty("os.name" );
		if(osName.contains("Windows")) {
			cmd =  "cmd.exe /C " + cmd ;
		}
		try {
			System.out.println("job:" + this.name + " is running");
			Process process = Runtime.getRuntime().exec(cmd);
			Thread.sleep(10000);
			exitCode = process.waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("job:" + this.name + ", exitCode:" + exitCode);
	}
}

public class ScheduleExecutor {
	
	private static final int THREAD_POOL_SIZE = 2;
	
	private static final Logger LOGGER = LogManager.getLogger(ScheduleExecutor.class);
	
	public static void main(String[] args) throws InterruptedException {
		CronDefinition cronDefinition =
		CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);

		//create a parser based on provided definition
		CronParser parser = new CronParser(cronDefinition);
		Cron quartzCron = parser.parse("0 * * 1-3 * ? *");

		//create a descriptor for a specific Locale
		CronDescriptor descriptor = CronDescriptor.instance(Locale.UK);

		//parse some expression and ask descriptor for description
		String description = descriptor.describe(parser.parse("*/45 * * * * ?"));
		//description will be: "every 45 seconds"

		//validate expression
		quartzCron.validate();

		ScheduledExecutorService ses = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
		ses.schedule(new Job("job1", "php -v"), 1, TimeUnit.SECONDS);
		ses.schedule(new Job("job2", "php -v"), 1, TimeUnit.SECONDS);
		ses.schedule(new Job("job3", "php -v"), 1, TimeUnit.SECONDS);

		Thread.sleep(3000);
		ses.shutdown();
		LOGGER.info("ScheduleThreadPoolExecutor");
	}

}

package com.qj.schedule.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.qj.schedule.bean.JobBean;
import com.qj.schedule.bean.JobFlowBean;
import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.dao.ExecutionDao;
import com.qj.schedule.dao.JobDao;
import com.qj.schedule.dao.JobFlowDao;
import com.qj.schedule.dao.ScheduleDao;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.InetUtil;

/**
 * @Title Master负责调度控制（V2版为高可用版本）
 * @Description 轮询调度表，将满足执行条件的作业（流）添加至执行队列
 * 
 * @author qiaojiang
 *
 */
public class ScheduleMasterV2 implements Callback{
	
	private static final Logger LOGGER = LogManager.getLogger(ScheduleMasterV2.class);
	
	private ScheduleDao scheduleDao;
	
	private JobDao jobDao;
	
	private JobFlowDao jobFlowDao;
	
	private ExecutionDao executionDao;
	
	public JobFlowDao getJobFlowDao() {
		return jobFlowDao;
	}

	public void setJobFlowDao(JobFlowDao jobFlowDao) {
		this.jobFlowDao = jobFlowDao;
	}
	
	public ExecutionDao getExecutionDao() {
		return executionDao;
	}

	public void setExecutionDao(ExecutionDao executionDao) {
		this.executionDao = executionDao;
	}

	public ScheduleDao getScheduleDao() {
		return scheduleDao;
	}

	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}

	public JobDao getJobDao() {
		return jobDao;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public boolean checkSchedule(ScheduleBean schedule, long time){
		//判断是否已经调度，每个调度每分钟内只能执行1次
		long stime = Long.parseLong(schedule.getScheduleTime());
		if(stime > 0) {
			String m1 = DateUtil.time2str(time, "yyyy-MM-dd HH:mm");
			String m2 = DateUtil.time2str(stime, "yyyy-MM-dd HH:mm");
			if(m1.equals(m2)) {
				return false;
			}
		}
		//根据类型判断是否可以调度
		int type = (int) schedule.getType();
		if(type == ScheduleDao.TYPE_SIMPLE){
			return checkSimpleSchedule(schedule);
		}else if(type == ScheduleDao.TYPE_CRON){
			return checkCronSchedule(schedule, time);
		}else{
			return false;
		}
	}
	
	public boolean checkSimpleSchedule(ScheduleBean schedule){
		long currTime = System.currentTimeMillis()/1000;
		long startTime = Long.parseLong(schedule.getStartTime());
		long endTime = Long.parseLong(schedule.getEndTime());
		if(currTime >= startTime && currTime <= endTime){
			return true;
		}
		return false;
	}
	
	public boolean checkCronSchedule(ScheduleBean schedule, long time){
		try {
			String cronExpression = (String)schedule.getCron();
	        CronParser cronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
	        Cron cron = cronParser.parse(cronExpression);
	        cron = cron.validate();
       
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));
	        //ZonedDateTime now = ZonedDateTime.now();
	        //不用当前时间，防止跨分钟导致任务不能被调度问题
	        ZonedDateTime now = ZonedDateTime.parse(DateUtil.time2str(time), formatter);
	        ExecutionTime executionTime = ExecutionTime.forCron(cron);
	        Optional<ZonedDateTime> next = executionTime.nextExecution(now);
	        long i = next.get().toEpochSecond() - now.toEpochSecond();
			if(i < 60){
				return true;
			}
			return false;
		} catch (Exception e) {
			String logStr = "[id:" + schedule.getScheduleId() + "] ";
			LOGGER.error("Schedule" + logStr + e.getMessage());
		}
		return false;
	}
	
	public boolean doSchedule(ScheduleBean schedule, long time){
		this.scheduleDao.doSchedule(schedule.getScheduleId(), time);
		if(schedule.getEntityType() == ScheduleDao.ENTITY_TYPE_JOB){
			JobBean jobBean = this.jobDao.getJobById(schedule.getEntityId());
			return (jobBean != null) ? this.executionDao.executeJob(jobBean, schedule, null) : false;
		}else if(schedule.getEntityType() == ScheduleDao.ENTITY_TYPE_JOBFLOW){
			JobFlowBean jobFlowBean = this.jobFlowDao.getJobFlowById(schedule.getEntityId());
			return (jobFlowBean != null) ? this.executionDao.executeJobFlow(jobFlowBean, schedule, null) : false;
		}
		return false;
	}
	
	public void callback() {
		boolean flag = true;
		while(flag){
			try {
				long now = new Date().getTime()/1000;
				List<ScheduleBean> scheduleList = this.scheduleDao.getRunableScheduleList();
				for (ScheduleBean schedule : scheduleList) {
					if(this.checkSchedule(schedule, now)){
						boolean isOk = this.doSchedule(schedule, now);
						String logStr = "[id:" + schedule.getScheduleId() + "]";
						if(isOk){
							LOGGER.info("Schedule" + logStr + " suceess");
						}else{
							LOGGER.error("Schedule" + logStr + " failed");
						}
					}
				}
				LOGGER.info("Waiting 30s...");
				Thread.sleep(30 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		LOGGER.info("Startup");
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleMasterV2 master = (ScheduleMasterV2) factory.getBean("scheduleMasterV2");
		String host = InetUtil.getHostName(InetUtil.getInetAddress());
		MasterSelector selector = new MasterSelector("Server#" + host, master);
		try {
			selector.start();
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			selector.close();
		}
		LOGGER.info("Shutdown!");
	}

}

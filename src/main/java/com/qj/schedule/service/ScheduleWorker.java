package com.qj.schedule.service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.qj.schedule.bean.ExecutionBean;
import com.qj.schedule.bean.HostBean;
import com.qj.schedule.dao.ExecutionDao;
import com.qj.schedule.dao.HostDao;
import com.qj.schedule.dao.JobDao;
import com.qj.schedule.dao.JobFlowDao;
import com.qj.schedule.dao.JobFlowNodeDao;
import com.qj.schedule.dao.ScheduleDao;
import com.qj.schedule.runnable.Executor;

/**
 * worker负责任务执行控制
 * 轮询调度表，将满足执行条件的作业（流）添加至执行队列
 * 
 * @author qiaojiang
 *
 */
public class ScheduleWorker {
	
	private static final int THREAD_POOL_SIZE = 8;
	
	private static final Logger LOGGER = LogManager.getLogger(ScheduleWorker.class);
	
	private ScheduledExecutorService ses;
	
	private ScheduleDao scheduleDao;
	
	private JobDao jobDao;
	
	private JobFlowDao jobFlowDao;
	
	private ExecutionDao executionDao;
	
	private JobFlowNodeDao jobFlowNodeDao;
	
	private HostDao hostDao;
	
	public ScheduleWorker(){
		//初始化线程池
		this.ses = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
	}

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
	
	public JobFlowNodeDao getJobFlowNodeDao() {
		return jobFlowNodeDao;
	}

	public void setJobFlowNodeDao(JobFlowNodeDao jobFlowNodeDao) {
		this.jobFlowNodeDao = jobFlowNodeDao;
	}

	public HostDao getHostDao() {
		return hostDao;
	}

	public void setHostDao(HostDao hostDao) {
		this.hostDao = hostDao;
	}
	
	private String makeExecutableCmd(ExecutionBean eb, String cmd){
		if(eb.getHostId() == 0){
			return cmd;
		}
		HostBean hostBean = this.hostDao.getHostById(eb.getHostId());
		if(hostBean == null){
			return cmd;
		}
		return "ssh " + hostBean.getUsername() + "@" + hostBean.getHost() + " " + cmd;
	} 

	public boolean doExecute(ExecutionBean eb){
		if(isExecutive(eb)){
			boolean flag = this.executionDao.setExecutionStatus(eb.getId(), eb.getStatus(), ExecutionDao.STATUS_RUNNING);
			if(flag){
				LOGGER.info("Execution[id:"+ eb.getId() + ",cmd:" + eb.getCmd()+ "] is scheduling");
				String cmd = this.makeExecutableCmd(eb, eb.getCmd());
				Executor executor = new Executor(eb.getId(), eb.getJobTitle(), cmd, this.executionDao);
				//TODO 延迟指定秒数，精确任务执行
				this.ses.schedule(executor, 1, TimeUnit.SECONDS);
				LOGGER.info("Execution[id:"+ eb.getId() + ",cmd:" + eb.getCmd()+ "] has scheduled");
				return true;
			}
		}
		return false;
	}
	
	public boolean isExecutive(ExecutionBean eb){
		if(eb.getNodeId() == 0){
			return true;
		}
		if(eb.getRelyNode() == 0) {
			return true;
		}
		//检查依赖任务是否完成
		ExecutionBean relyEb = this.executionDao.getExecutionByTokenNodeId(eb.getToken(), eb.getRelyNode());
		if(relyEb != null && relyEb.getStatus() == ExecutionDao.STATUS_SUCCESS){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		LOGGER.info("Startup");
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleWorker worker = (ScheduleWorker) factory.getBean("scheduleWorker");
		boolean flag = true;
		while(flag){
			try {
				List<ExecutionBean> executionList = worker.executionDao.getRunableExecutionList(0, 100);
				for(ExecutionBean eb : executionList){
					worker.doExecute(eb);
					Thread.sleep(100);
				}
				LOGGER.info("Waiting 1s...");
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		worker.ses.shutdown();
		LOGGER.info("Shutdown!");
	}

}

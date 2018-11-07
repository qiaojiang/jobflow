package com.qj.schedule.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qj.schedule.bean.ExecutionBean;
import com.qj.schedule.bean.HostBean;
import com.qj.schedule.bean.JobBean;
import com.qj.schedule.bean.JobFlowBean;
import com.qj.schedule.bean.JobFlowNodeBean;
import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.bean.UserBean;
import com.qj.schedule.dao.ExecutionDao;
import com.qj.schedule.dao.HostDao;
import com.qj.schedule.dao.JobDao;
import com.qj.schedule.dao.JobFlowDao;
import com.qj.schedule.dao.JobFlowNodeDao;
import com.qj.schedule.dao.ScheduleDao;
import com.qj.schedule.dao.UserDao;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.FileUtil;
import com.qj.schedule.util.PropertyUtil;
import com.qj.schedule.util.StatusUtil;

@Controller
@RequestMapping("/api")
public class ApiController extends AdminController{

	@Autowired
	private HttpServletRequest request;
	
	/**
	 * JOB
	 */
	@ResponseBody
    @RequestMapping(value="/jobs/{id}",method=RequestMethod.GET)
    public JobBean getJob(@PathVariable("id") Integer id){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
    	JobBean jobBean = jobDao.getJobById(id);
    	jobDao.formatBean(jobBean);
        return jobBean;
    }
    
	@ResponseBody
    @RequestMapping(value="/jobs",method=RequestMethod.GET)
    public Map<String,Object> getJobList(HttpServletRequest request){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        int start = offset != null ? Integer.parseInt(offset) : 0;
        int size = limit != null ? Integer.parseInt(limit) : 10;
        int groupId = this.getGroupId(request);
        short identity = (short) request.getSession().getAttribute("type");
        if(identity == 1) groupId = 0;
        List<JobBean> jobBeanList = jobDao.getJobListByGroupId(groupId, start, size);
        for (JobBean jobBean : jobBeanList) {
        	jobDao.formatBean(jobBean);
		}
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", jobBeanList);
        map.put("total", jobDao.getJobCountByGroupId(groupId));
        return map;
    }

	@ResponseBody
    @RequestMapping(value="/jobs",method=RequestMethod.PUT)
    public Map<String,Object> addJob(@RequestParam Map<String, Object> map){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        map.put("group_id", this.getGroupId(request));
        jobDao.addJob(map);
        return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_ADD_SUCCESS, "/view/job");
    }
    
	@ResponseBody
    @RequestMapping(value="/jobs/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateJob(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        jobDao.updateJob(id, map);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/job");
    }
    
	@ResponseBody
    @RequestMapping(value="/jobs/{id}",method=RequestMethod.DELETE)
    public Map<String,Object> deleteJob(@PathVariable("id") Integer id){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        jobDao.deleteJob(id);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_DELETE_SUCCESS, "/view/job");
    }
	
	@ResponseBody
    @RequestMapping(value="/jobs/exec/{id}",method=RequestMethod.POST)
    public Map<String,Object> execJob(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
        JobBean jobBean = jobDao.getJobById(id);
        if(jobBean == null){
        	return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_START_FAILED, "/view/job");
        }
        String cmdParam = (String) map.get("cmd_param");
        Map<String,String> paramMap = new HashMap<>();
        if(cmdParam != null && cmdParam != "") {
        	String[] params = cmdParam.split("\n");
        	for (String param : params) {
				if(param != "") {
					String[] ps = param.split("=");
					if(ps.length == 2) {
						paramMap.put(ps[0], ps[1]);
					}
				}
			}
        }
        ScheduleBean scheduleBean = new ScheduleBean();
        scheduleBean.setRetryNum(Short.parseShort((String) map.get("retry_num")));
        scheduleBean.setRetryInterval(Integer.parseInt((String) map.get("retry_interval")));
        scheduleBean.setHostId(Integer.parseInt((String) map.get("host_id")));
        if(executionDao.executeJob(jobBean, scheduleBean, paramMap)){
        	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_START_SUCCESS, "/view/job");
        }else{
        	return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_START_FAILED, "/view/job");
        }
    }
	
	/**
	 * JOBFLOW
	 */
	@ResponseBody
    @RequestMapping(value="/jobflows/{id}",method=RequestMethod.GET)
    public JobFlowBean getJobFlow(@PathVariable("id") Integer id){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(id);
    	jobFlowDao.formatBean(jobFlowBean);
        return jobFlowBean;
    }
    
	@ResponseBody
    @RequestMapping(value="/jobflows",method=RequestMethod.GET)
    public Map<String,Object> getJobFlowList(HttpServletRequest request){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        int start = offset != null ? Integer.parseInt(offset) : 0;
        int size = limit != null ? Integer.parseInt(limit) : 10;
        int groupId = this.getGroupId(request);
        short identity = (short) request.getSession().getAttribute("type");
        if(identity == 1) groupId = 0;
        List<JobFlowBean> jobFlowBeanList = jobFlowDao.getJobFlowListByGroupId(groupId, start, size);
        for (JobFlowBean jobFlowBean : jobFlowBeanList) {
        	jobFlowDao.formatBean(jobFlowBean);
		}
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", jobFlowBeanList);
        map.put("total", jobFlowDao.getJobFlowCountByGroupId(groupId));
        return map;
    }

	@ResponseBody
    @RequestMapping(value="/jobflows",method=RequestMethod.PUT)
    public Map<String,Object> addJobFlow(@RequestParam Map<String, Object> map){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	map.put("group_id", this.getGroupId(request));
        jobFlowDao.addJobFlow(map);
        return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_ADD_SUCCESS, "/view/jobflow");
    }
    
	@ResponseBody
    @RequestMapping(value="/jobflows/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateJobFlow(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
		jobFlowDao.updateJobFlow(id, map);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/jobflow");
    }
    
	@ResponseBody
    @RequestMapping(value="/jobflows/{id}",method=RequestMethod.DELETE)
    public Map<String,Object> deleteJobFlow(@PathVariable("id") Integer id){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
		jobFlowDao.deleteJobFlow(id);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_DELETE_SUCCESS, "/view/jobflow");
    }
	
	@ResponseBody
    @RequestMapping(value="/jobflows/exec/{id}",method=RequestMethod.POST)
    public Map<String,Object> execJobFlow(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
        ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
        JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(id);
        if(jobFlowBean == null){
        	return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_START_FAILED, "/view/job");
        }
        String cmdParam = (String) map.get("cmd_param");
        Map<String,String> paramMap = new HashMap<>();
        if(cmdParam != null && cmdParam != "") {
        	String[] params = cmdParam.split("\n");
        	for (String param : params) {
				if(param != "") {
					String[] ps = param.split("=");
					if(ps.length == 2) {
						paramMap.put(ps[0], ps[1]);
					}
				}
			}
        }
        ScheduleBean scheduleBean = new ScheduleBean();
        scheduleBean.setRetryNum(Short.parseShort((String) map.get("retry_num")));
        scheduleBean.setRetryInterval(Integer.parseInt((String) map.get("retry_interval")));
        scheduleBean.setHostId(Integer.parseInt((String) map.get("host_id")));
        if(executionDao.executeJobFlow(jobFlowBean, scheduleBean, paramMap)){
        	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_START_SUCCESS, "/view/jobflow");
        }else{
        	return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_START_FAILED, "/view/jobflow");
        }
    }
	
	/**
	 * JOBFLOWNODE
	 */
	@ResponseBody
    @RequestMapping(value="/jobflownodesbyfid/{flowid}",method=RequestMethod.GET)
    public Map<String,Object> getJobFlowNodeListByFlowId(@PathVariable("flowid") Integer flowId){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobFlowNodeDao jobFlowNodeDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
        List<JobFlowNodeDetailBean> jobFlowNodeBeanList = jobFlowNodeDao.getJobFlowNodeDetailListByFlowId(flowId);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", jobFlowNodeBeanList);
        map.put("total", 100);
        return map;
    }

	@ResponseBody
    @RequestMapping(value="/jobflownodes",method=RequestMethod.PUT)
    public Map<String,Object> addJobFlowNode(@RequestParam Map<String, Object> map){
		if(!map.containsKey("flow_id") || !map.containsKey("job_id")) {
			return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_ADD_FAILED, "");
		}
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	JobFlowNodeDao jobFlowNodeDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
		Map<String,Object> data = new HashMap<>();
		data.put("job_id", Integer.parseInt((String) map.get("job_id")));
		data.put("flow_id", Integer.parseInt((String) map.get("flow_id")));
		data.put("rely", Integer.parseInt((String) map.get("rely")));
    	jobFlowNodeDao.addJobFlowNode(data);
        return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_ADD_SUCCESS, "/view/jobflowdetail/" + data.get("flow_id"));
    }
    
	@ResponseBody
    @RequestMapping(value="/jobflownodes/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateJobFlowNode(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		if(!map.containsKey("job_id")) {
			return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_ADD_FAILED, "");
		}
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		JobFlowNodeDao jobFlowNodeDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
		JobFlowNodeBean nodeBean = jobFlowNodeDao.getJobFlowNodeById(id);
		Map<String,Object> data = new HashMap<>();
		data.put("job_id", Integer.parseInt((String) map.get("job_id")));
		data.put("rely", Integer.parseInt((String) map.get("rely")));
		jobFlowNodeDao.updateJobFlowNode(id, data);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/jobflowdetail/" + nodeBean.getFlowId());
    }
    
	@ResponseBody
    @RequestMapping(value="/jobflownodes/{id}",method=RequestMethod.DELETE)
    public Map<String,Object> deleteJobFlowNode(@PathVariable("id") Integer id){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		JobFlowNodeDao jobFlowNodeDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
		JobFlowNodeBean nodeBean = jobFlowNodeDao.getJobFlowNodeById(id);
		if(nodeBean != null) {
			jobFlowNodeDao.deleteJobFlowNode(id);
			return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_DELETE_SUCCESS, "/view/jobflowdetail/" + nodeBean.getFlowId());
		}
    	return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_DELETE_FAILED, "");
    }
	
	/**
	 * SCHEDULE
	 */
	@ResponseBody
    @RequestMapping(value="/schedules/{id}",method=RequestMethod.GET)
    public ScheduleBean getSchedule(@PathVariable("id") Integer id){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
        ScheduleBean scheduleBean = scheduleDao.getScheduleById(id);
        scheduleDao.formatBean(scheduleBean);
        return scheduleBean;
    }
    
	@ResponseBody
    @RequestMapping(value="/schedules",method=RequestMethod.GET)
    public Map<String,Object> getScheduleList(HttpServletRequest request){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        int start = offset != null ? Integer.parseInt(offset) : 0;
        int size = limit != null ? Integer.parseInt(limit) : 10;
        int groupId = this.getGroupId(request);
        short identity = (short) request.getSession().getAttribute("type");
        if(identity == 1) groupId = 0;
        List<ScheduleBean> scheduleBeanList = scheduleDao.getScheduleListByGroupId(groupId, start, size);
        for (ScheduleBean scheduleBean : scheduleBeanList) {
        	scheduleDao.formatBean(scheduleBean);
		}
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", scheduleBeanList);
        map.put("total", scheduleDao.getScheduleCountByGroupId(groupId));
        return map;
    }

	@ResponseBody
    @RequestMapping(value="/schedules",method=RequestMethod.PUT)
    public Map<String,Object> addSchedule(@RequestParam Map<String, Object> map){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
    	map.put("group_id", this.getGroupId(request));
    	boolean flag = scheduleDao.addSchedule(map);
    	if(flag){
    		return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_ADD_SUCCESS, "/view/schedule");
    	}else{
    		return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_FAILED, "/view/schedule");
    	}
    }
    
	@ResponseBody
    @RequestMapping(value="/schedules/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateSchedule(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
		scheduleDao.updateSchedule(id, map);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/schedule");
    }
    
	@ResponseBody
    @RequestMapping(value="/schedules/{id}",method=RequestMethod.DELETE)
    public Map<String,Object> deleteSchedule(@PathVariable("id") Integer id){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
		scheduleDao.deleteSchedule(id);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_DELETE_SUCCESS, "/view/schedule");
    }
	
	/**
	 * EXECUTION
	 */
	@ResponseBody
    @RequestMapping(value="/executions/{id}",method=RequestMethod.GET)
    public ExecutionBean getExecution(@PathVariable("id") Integer id){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
        ExecutionBean executionBean = executionDao.getExecutionById(id);
        executionDao.formatBean(executionBean);
        return executionBean;
    }
    
	@ResponseBody
    @RequestMapping(value="/executions",method=RequestMethod.GET)
    public Map<String,Object> getExecutionList(HttpServletRequest request){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        int start = offset != null ? Integer.parseInt(offset) : 0;
        int size = limit != null ? Integer.parseInt(limit) : 10;
        int groupId = this.getGroupId(request);
        short identity = (short) request.getSession().getAttribute("type");
        if(identity == 1) groupId = 0;
        List<ExecutionBean> executionBeanList = executionDao.getExecutionListByGroupId(groupId, start, size);
        for (ExecutionBean executionBean : executionBeanList) {
        	executionDao.formatBean(executionBean);
		}
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", executionBeanList);
        map.put("total", executionDao.getExecutionCountByGroupId(groupId));
        return map;
    }
	
	@ResponseBody
    @RequestMapping(value="/executions/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateExecution(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
		executionDao.updateExecution(id, map);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/execution");
    }
	
	@ResponseBody
    @RequestMapping(value="/executions/restart/{id}",method=RequestMethod.POST)
    public Map<String,Object> restartExecution(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
		executionDao.restartExecution(id);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_RESTART_SUCCESS, "/view/execution");
    }
	
	@ResponseBody
    @RequestMapping(value="/executions/log/{id}",method=RequestMethod.POST)
    public Map<String,Object> getExecutionLog(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
		ExecutionBean executionBean = executionDao.getExecutionById(id);
		String day = DateUtil.time2str(Long.parseLong(executionBean.getStartTime()), "yyyyMMdd");
		String fileType = map.get("file_type") == null ? "log" : map.get("file_type").toString();
		int page = map.get("page") == null ? 1 : Integer.parseInt((String) map.get("page"));
		int pageSize = 100;
    	String logFile = ExecutionDao.getExecutionLogFile(id, day, fileType);
    	File file = new File(logFile);
    	if(file.exists()) {
    		Map<String, Object> data = new HashMap<String, Object>();
    		int start = (page - 1) * pageSize + 1;
    		data.put("rows", FileUtil.getLineContent(logFile, start, pageSize));
    		data.put("page", page);
    		return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_SUCCESS, data);
    	}else {
    		return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_FAILED, "");
    	}
    }
	
	/**
	 * HOST
	 */
	@ResponseBody
    @RequestMapping(value="/hosts/{id}",method=RequestMethod.GET)
    public HostBean getHost(@PathVariable("id") Integer id){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        HostDao hostDao = (HostDao) ac.getBean("hostDao");
        HostBean hostBean = hostDao.getHostById(id);
        hostDao.formatBean(hostBean);
        return hostBean;
    }
    
	@ResponseBody
    @RequestMapping(value="/hosts",method=RequestMethod.GET)
    public Map<String,Object> getHostList(HttpServletRequest request){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        HostDao hostDao = (HostDao) ac.getBean("hostDao");
        int groupId = this.getGroupId(request);
        short identity = (short) request.getSession().getAttribute("type");
        if(identity == 1) groupId = 0;
        List<HostBean> hostBeanList = hostDao.getHostListByGroupId(groupId);
        for (HostBean hostBean : hostBeanList) {
        	hostDao.formatBean(hostBean);
		}
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", hostBeanList);
        map.put("total", hostDao.getHostCountByGroupId(groupId));
        return map;
    }

	@ResponseBody
    @RequestMapping(value="/hosts",method=RequestMethod.PUT)
    public Map<String,Object> addHost(@RequestParam Map<String, Object> map){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	map.put("group_id", this.getGroupId(request));
    	boolean flag = hostDao.addHost(map);
    	if(flag){
    		return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_ADD_SUCCESS, "/view/host");
    	}else{
    		return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_FAILED, "/view/host");
    	}
    }
    
	@ResponseBody
    @RequestMapping(value="/hosts/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateHost(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		HostDao hostDao = (HostDao) ac.getBean("hostDao");
		hostDao.updateHost(id, map);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/host");
    }
    
	@ResponseBody
    @RequestMapping(value="/hosts/{id}",method=RequestMethod.DELETE)
    public Map<String,Object> deleteHost(@PathVariable("id") Integer id){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		HostDao hostDao = (HostDao) ac.getBean("hostDao");
		hostDao.deleteHost(id);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_DELETE_SUCCESS, "/view/host");
    }
	
	/**
	 * USER
	 */
	@ResponseBody
    @RequestMapping(value="/users/{id}",method=RequestMethod.GET)
    public UserBean getUser(@PathVariable("id") Integer id){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        UserDao userDao = (UserDao) ac.getBean("userDao");
    	UserBean userBean = userDao.getUserById(id);
    	userDao.formatBean(userBean);
        return userBean;
    }
    
	@ResponseBody
    @RequestMapping(value="/users",method=RequestMethod.GET)
    public Map<String,Object> getUserList(HttpServletRequest request){
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        UserDao userDao = (UserDao) ac.getBean("userDao");
        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        int start = offset != null ? Integer.parseInt(offset) : 0;
        int size = limit != null ? Integer.parseInt(limit) : 10;
        List<UserBean> userBeanList = userDao.getUserList(start, size);
        for (UserBean userBean : userBeanList) {
        	userDao.formatBean(userBean);
		}
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rows", userBeanList);
        map.put("total", userDao.getUserCount());
        return map;
    }

	@ResponseBody
    @RequestMapping(value="/users",method=RequestMethod.PUT)
    public Map<String,Object> addUser(@RequestParam Map<String, Object> map){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        UserDao userDao = (UserDao) ac.getBean("userDao");
        userDao.addUser(map);
        return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_ADD_SUCCESS, "/view/user");
    }
    
	@ResponseBody
    @RequestMapping(value="/users/{id}",method=RequestMethod.POST)
    public Map<String,Object> updateUser(@PathVariable("id") Integer id,@RequestParam Map<String, Object> map){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		UserDao userDao = (UserDao) ac.getBean("userDao");
        userDao.updateUser(id, map);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_UPDATE_SUCCESS, "/view/user");
    }
    
	@ResponseBody
    @RequestMapping(value="/users/{id}",method=RequestMethod.DELETE)
    public Map<String,Object> deleteUser(@PathVariable("id") Integer id){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        UserDao userDao = (UserDao) ac.getBean("userDao");
        userDao.deleteUser(id);
    	return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_DELETE_SUCCESS, "/view/user");
    }
	
	@ResponseBody
    @RequestMapping(value="/test",method=RequestMethod.GET)
    public Map<String,Object> test(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		PropertyUtil propertyUtil = (PropertyUtil) ac.getBean("propertyUtil"); 
		System.out.println("propertyUtil:" + propertyUtil.getProperty("project_name"));
		return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_SUCCESS, "");
	}
}
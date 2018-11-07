package com.qj.schedule.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qj.schedule.bean.ExecutionBean;
import com.qj.schedule.bean.GroupBean;
import com.qj.schedule.bean.HostBean;
import com.qj.schedule.bean.JobBean;
import com.qj.schedule.bean.JobFlowBean;
import com.qj.schedule.bean.JobFlowNodeBean;
import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.bean.UserBean;
import com.qj.schedule.dao.ExecutionDao;
import com.qj.schedule.dao.GroupDao;
import com.qj.schedule.dao.HostDao;
import com.qj.schedule.dao.JobDao;
import com.qj.schedule.dao.JobFlowDao;
import com.qj.schedule.dao.JobFlowNodeDao;
import com.qj.schedule.dao.ScheduleDao;
import com.qj.schedule.dao.UserDao;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.StatusUtil;


@Controller
@RequestMapping("/view")
public class ViewController extends AdminController{
	
	@Autowired
	private HttpServletRequest request;
	
	private ModelAndView getModelAndView(String view){
		ModelAndView mav = new ModelAndView(view);
		int port = request.getServerPort();
		String uri = request.getRequestURI();
		String[] acts = uri.split("/");
		String project = "";
		if(acts.length > 3){
			project = acts[1];
		}
		if(port == 80) {
			mav.addObject("baseUrl", request.getScheme() + "://" + request.getServerName() + "/" + project);
		}else{
			mav.addObject("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + port + "/" + project);
		}
    	return mav;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
    public ModelAndView login(){
    	ModelAndView mv = this.getModelAndView("login");
    	mv.addObject("type", "login");
        return mv;
    }
	
    @RequestMapping(value="/index",method=RequestMethod.GET)
    public ModelAndView index(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
        ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
    	ModelAndView mv = this.getModelAndView("index");
    	int groupId = this.getGroupId(request);
    	short identity = (short) request.getSession().getAttribute("type");
        if(identity == 1) groupId = 0;
        String date = (String)request.getParameter("date");
        if(date == null) date = "today";
        long beginTime = DateUtil.oral2time("today");
        String type = "hour";
        if(date.equals("yesterday")) {
        	beginTime = DateUtil.oral2time("yesterday");
        }else if(date.equals("recent1w")) {
        	beginTime = DateUtil.oral2time("today") - 6 * 24 * 3600;
        	type = "day";
        }
        long endTime = DateUtil.oral2time("now");
        Map<String,Integer> hourExecCount = executionDao.getDateExecutionCountStat(type, groupId, beginTime, endTime);
    	mv.addObject("type", "index");
    	mv.addObject("execStatusCount", executionDao.getExecutionCountByStatus(groupId, beginTime, endTime));
    	mv.addObject("execCount", executionDao.getExecutionCountByGroupId(groupId));
    	mv.addObject("jobCount", jobDao.getJobCountByGroupId(groupId));
    	mv.addObject("jobFlowCount", jobFlowDao.getJobFlowCountByGroupId(groupId));
    	mv.addObject("scheduleCount", scheduleDao.getScheduleCountByGroupId(groupId));
    	try {
			mv.addObject("hourExecKeys",new ObjectMapper().writeValueAsString(hourExecCount.keySet()));
			mv.addObject("hourExecValues",new ObjectMapper().writeValueAsString(hourExecCount.values()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return mv;
    }
    
    @RequestMapping(value="/error/{code}",method=RequestMethod.GET)
    public ModelAndView error(@PathVariable("code") String code){
    	ModelAndView mv = this.getModelAndView("error");
    	String message = StatusUtil.HTTP_ERROR.containsKey(code) ? StatusUtil.HTTP_ERROR.get(code) : StatusUtil.MSG_FAILED;
    	mv.addObject("message", message);
        return mv;
    }
    
    @RequestMapping(value="/test",method=RequestMethod.GET)
    public ModelAndView test(){
    	ModelAndView mv = this.getModelAndView("test");
    	mv.addObject("type", "test");
        return mv;
    }
    
    /**
     * JOB VIEW
     */
    @RequestMapping(value="/job",method=RequestMethod.GET)
    public ModelAndView job(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	ModelAndView mv = this.getModelAndView("job/index");
    	mv.addObject("type", "job");
    	try {
			mv.addObject("typeMapping", new ObjectMapper().writeValueAsString(jobDao.getTypeMapping()));
			mv.addObject("groupMapping", new ObjectMapper().writeValueAsString(groupDao.getGroupMapping()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return mv;
    }
    
    @RequestMapping(value="/addjob",method=RequestMethod.GET)
    public ModelAndView addJob(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
    	ModelAndView mv = this.getModelAndView("job/add");
    	mv.addObject("type", "job");
    	mv.addObject("typeMapping", jobDao.getTypeMapping());
        return mv;
    }
    
    @RequestMapping(value="/updatejob/{id}",method=RequestMethod.GET)
    public ModelAndView updateJob(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
    	JobBean jobBean = jobDao.getJobById(id);
    	ModelAndView mv = this.getModelAndView("job/update");
    	mv.addObject("type", "job");
    	mv.addObject("job", jobBean);
    	mv.addObject("typeMapping", jobDao.getTypeMapping());
        return mv;
    }
    
    @RequestMapping(value="/execjob/{id}",method=RequestMethod.GET)
    public ModelAndView execJob(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
        JobDao jobDao = (JobDao) ac.getBean("jobDao");
        JobBean jobBean = jobDao.getJobById(id);
    	ModelAndView mv = this.getModelAndView("job/exec");
    	mv.addObject("type", "job");
    	mv.addObject("job", jobBean);
    	mv.addObject("hostMapping", hostDao.getHostMapping(this.getGroupId(request)));
        return mv;
    }
    
    /**
     * JOBFLOW VIEW
     */
    @RequestMapping(value="/jobflow",method=RequestMethod.GET)
    public ModelAndView jobflow(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	ModelAndView mv = this.getModelAndView("jobflow/index");
    	mv.addObject("type", "jobflow");
    	try {
			mv.addObject("groupMapping", new ObjectMapper().writeValueAsString(groupDao.getGroupMapping()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return mv;
    }
    
    @RequestMapping(value="/addjobflow",method=RequestMethod.GET)
    public ModelAndView addJobFlow(){
    	ModelAndView mv = this.getModelAndView("jobflow/add");
    	mv.addObject("type", "jobflow");
        return mv;
    }
    
    @RequestMapping(value="/updatejobflow/{id}",method=RequestMethod.GET)
    public ModelAndView updateJobFlow(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
        JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(id);
    	ModelAndView mv = this.getModelAndView("jobflow/update");
    	mv.addObject("type", "jobflow");
    	mv.addObject("jobflow", jobFlowBean);
        return mv;
    }
    
    @RequestMapping(value="/execjobflow/{id}",method=RequestMethod.GET)
    public ModelAndView execJobFlow(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(id);
    	ModelAndView mv = this.getModelAndView("jobflow/exec");
    	mv.addObject("type", "job");
    	mv.addObject("jobflow", jobFlowBean);
    	mv.addObject("hostMapping", hostDao.getHostMapping(this.getGroupId(request)));
        return mv;
    }
    
    @RequestMapping(value="/jobflowdetail/{id}",method=RequestMethod.GET)
    public ModelAndView jobflowDetail(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(id);
    	ModelAndView mv = this.getModelAndView("jobflow/detail");
    	mv.addObject("type", "jobflow");
    	mv.addObject("jobflow", jobFlowBean);
        return mv;
    }
    
    @RequestMapping(value="/jobflowdag/{id}",method=RequestMethod.GET)
    public ModelAndView jobflowDag(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	JobFlowNodeDao jobFlowNodeDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(id);
    	ModelAndView mv = this.getModelAndView("jobflow/dag");
    	Map<String,Object> canvas = new HashMap<>();
    	canvas.put("width", 1024);
    	canvas.put("height", 1024);
    	List<JobFlowNodeDetailBean> nodeBeanList = jobFlowNodeDao.getJobFlowNodeDetailListByFlowId(id);
    	List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
    	for (JobFlowNodeDetailBean nodeBean : nodeBeanList) {
    		Map<String, Object> node = new HashMap<>();
			node.put("id", nodeBean.getNodeId());
			node.put("title", nodeBean.getJobTitle());
			node.put("rely", nodeBean.getRely());
			node.put("status", ExecutionDao.STATUS_SUCCESS);
			nodeList.add(node);
		}
    	mv.addObject("dag",this.makeDag(nodeList, canvas));
    	mv.addObject("type", "jobflow");
    	mv.addObject("jobflow", jobFlowBean);
    	mv.addObject("canvas",canvas);
        return mv;
    }
    
    @RequestMapping(value="/addjobflownode/{flowid}",method=RequestMethod.GET)
    public ModelAndView addJobFlowNode(@PathVariable("flowid") Integer flowId){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(flowId);
    	ModelAndView mv = this.getModelAndView("jobflow/addnode");
    	mv.addObject("type", "jobflow");
    	mv.addObject("jobflow", jobFlowBean);
        return mv;
    }
    
    @RequestMapping(value="/updatejobflownode/{id}",method=RequestMethod.GET)
    public ModelAndView updateJobFlowNode(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	JobFlowNodeDao jobFlowNodeDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
    	JobFlowNodeBean jobFlowNodeBean = jobFlowNodeDao.getJobFlowNodeById(id);
    	ModelAndView mv = this.getModelAndView("jobflow/updatenode");
    	mv.addObject("type", "jobflow");
    	mv.addObject("jobflownode", jobFlowNodeBean);
        return mv;
    }
    
    /**
     * SCHEDULE VIEW
     */
    @RequestMapping(value="/schedule",method=RequestMethod.GET)
    public ModelAndView schedule(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	ModelAndView mv = this.getModelAndView("schedule/index");
    	mv.addObject("type", "schedule");
    	try {
			mv.addObject("typeMapping", new ObjectMapper().writeValueAsString(scheduleDao.getTypeMapping()));
			mv.addObject("entityTypeMapping", new ObjectMapper().writeValueAsString(scheduleDao.getEntityTypeMapping()));
			mv.addObject("statusMapping", new ObjectMapper().writeValueAsString(scheduleDao.getStatusMapping()));
			mv.addObject("groupMapping", new ObjectMapper().writeValueAsString(groupDao.getGroupMapping()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return mv;
    }
    
    @RequestMapping(value="/scheduledetail/{id}",method=RequestMethod.GET)
    public ModelAndView scheduledetail(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	JobDao jobDao = (JobDao) ac.getBean("jobDao");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	ScheduleBean scheduleBean = scheduleDao.getScheduleById(id);
    	scheduleDao.formatBean(scheduleBean);
    	HostBean hostBean = hostDao.getHostById(scheduleBean.getHostId());
    	String entity = "";
    	if(scheduleBean.getEntityType() == 1) {
    		JobBean jobBean = jobDao.getJobById(scheduleBean.getEntityId());
    		entity = "[id: " + jobBean.getJobId() +", title: " + jobBean.getTitle() +"]";
    	}else {
    		JobFlowBean jobFlowBean = jobFlowDao.getJobFlowById(scheduleBean.getEntityId());
    		entity = "[id: " + jobFlowBean.getFlowId() +", title: " + jobFlowBean.getTitle() +"]";
		}
    	ModelAndView mv = this.getModelAndView("schedule/detail");
		mv.addObject("type", "schedule");
		mv.addObject("schedule", scheduleBean);
		mv.addObject("host", hostBean);
		mv.addObject("entity", entity);
		mv.addObject("statusMapping", scheduleDao.getStatusMapping());
		mv.addObject("groupMapping", groupDao.getGroupMapping());
        return mv;
    }
    
    @RequestMapping(value="/addschedule",method=RequestMethod.GET)
    public ModelAndView addSchedule(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	ModelAndView mv = this.getModelAndView("schedule/add");
    	int groupId = this.getGroupId(request);
    	mv.addObject("type", "schedule");
		mv.addObject("hostMapping", hostDao.getHostMapping(groupId));
        return mv;
    }
    
    @RequestMapping(value="/updateschedule/{id}",method=RequestMethod.GET)
    public ModelAndView updateSchedule(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	ScheduleDao scheduleDao = (ScheduleDao) ac.getBean("scheduleDao");
    	ScheduleBean scheduleBean = scheduleDao.getScheduleById(id);
    	scheduleDao.formatBean(scheduleBean);
    	int groupId = this.getGroupId(request);
    	ModelAndView mv = this.getModelAndView("schedule/update");
    	mv.addObject("type", "schedule");
    	mv.addObject("schedule", scheduleBean);
    	mv.addObject("hostMapping", hostDao.getHostMapping(groupId));
        return mv;
    }
    
    /**
     * EXECUTION VIEW
     */
    @RequestMapping(value="/execution",method=RequestMethod.GET)
    public ModelAndView execution(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	ModelAndView mv = this.getModelAndView("execution/index");
    	try {
    		mv.addObject("type", "execution");
			mv.addObject("statusMapping", new ObjectMapper().writeValueAsString(executionDao.getStatusMapping()));
			mv.addObject("groupMapping", new ObjectMapper().writeValueAsString(groupDao.getGroupMapping()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return mv;
    }
    
    @RequestMapping(value="/executiondetail/{id}",method=RequestMethod.GET)
    public ModelAndView executiondetail(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	JobFlowDao jobFlowDao = (JobFlowDao) ac.getBean("jobFlowDao");
    	ExecutionBean executionBean = executionDao.getExecutionById(id);
    	executionDao.formatBean(executionBean);
    	HostBean hostBean = hostDao.getHostById(executionBean.getHostId());
    	JobFlowBean jobFlowBean = jobFlowDao.getJobFlowByNodeId(executionBean.getNodeId());
    	ModelAndView mv = this.getModelAndView("execution/detail");
		mv.addObject("type", "execution");
		mv.addObject("execution", executionBean);
		mv.addObject("host", hostBean);
		mv.addObject("jobflow", jobFlowBean);
		mv.addObject("statusMapping", executionDao.getStatusMapping());
		mv.addObject("groupMapping", groupDao.getGroupMapping());
        return mv;
    }
    
    @RequestMapping(value="/updateexecution/{id}",method=RequestMethod.GET)
    public ModelAndView updateExecution(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
    	ExecutionBean executionBean = executionDao.getExecutionById(id);
    	ModelAndView mv = this.getModelAndView("execution/update");
    	mv.addObject("type", "execution");
    	mv.addObject("execution", executionBean);
    	mv.addObject("statusMapping", executionDao.getStatusMapping());
        return mv;
    }
    
    /**
     * Make DAG
     * node:
     * {
     * 	id:int:节点ID
     * 	title:string:节点名称
     * 	rely:int:依赖
     * 	status:int:节点状态
     * }
     * status: ['failed':'红色','success':'蓝色','waiting':'黄色' ,'running':'绿色']
     */
    private Map<String, Object> makeDag(List<Map<String, Object>> nodeList, Map canvas){
    	//框架集
    	Map<String,Set<Map<String, Object>>> frame = new HashMap<>();
    	//层元素集合
    	Set<Map<String, Object>> nodeSet = new HashSet<Map<String, Object>>();
    	//依赖集合
		Set<Integer> replySet = new HashSet<Integer>();
		//节点颜色 0 红色 1 蓝色 2 黄色 3 绿色
		String[] colors = {"#FF4500","#1890FF","#FFD700","#00FF00"};
    	replySet.add(0);
    	int i = 0,j = 0;
    	while(i++ < nodeList.size()){
    		for (Map<String, Object> node : nodeList) {
    			if(replySet.contains(node.get("rely"))){
    				nodeSet.add(node);
    			}
    		}
    		if(!nodeSet.isEmpty()){
    			frame.put("" + (++j), nodeSet);
    			replySet.clear();
    			for (Map<String, Object> node : nodeSet) {
    				replySet.add((int)node.get("id"));
				}
    			nodeSet = new HashSet<Map<String, Object>>();
    		}else {
    			break;
    		}
    	}
    	int w = (int) canvas.get("width");
    	int h = (int) canvas.get("height");
    	int layerSize = frame.size();
    	int layerHeight = 80;
    	List<Map<String, Object>> dataList = new ArrayList<>();
    	List<Map<String, Object>> linkList = new ArrayList<>();
    	for (Entry<String, Set<Map<String, Object>>> entry: frame.entrySet()) {
    		int rank = Integer.parseInt(entry.getKey());
    		int size = entry.getValue().size();
    		int n = 0;
    		for (Map<String, Object> node : entry.getValue()) {
    			Map<String, Object> data = new HashMap<>();
    			data.put("id", node.get("id"));
    			data.put("label", node.get("title"));
    			data.put("x", w * (++n) / (size + 1));
    			data.put("y", (rank - 1) * layerHeight);
    			int status = Integer.parseInt("" + node.get("status"));
    			data.put("color",colors[status]);
    			dataList.add(data);
    			
    			if((int)node.get("rely") > 0) {
    				Map<String, Object> link = new HashMap<>();
        			link.put("source", node.get("id"));
        			link.put("target", node.get("rely"));
        			linkList.add(link);
    			}
			}
    	}
    	Map<String, Object> result = new HashMap<>();
    	try {
			result.put("nodes", new ObjectMapper().writeValueAsString(dataList));
			result.put("edges", new ObjectMapper().writeValueAsString(linkList));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    @RequestMapping(value="/executiondag/{token}",method=RequestMethod.GET)
    public ModelAndView executionDAG(@PathVariable("token") String token){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
    	ModelAndView mv = this.getModelAndView("execution/dag");
    	Map<String,Object> canvas = new HashMap<>();
    	canvas.put("width", 1024);
    	canvas.put("height", 1024);
    	List<ExecutionBean> execBeanList = executionDao.getExecutionListByToken(token);
    	List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
    	for (ExecutionBean execBean : execBeanList) {
    		Map<String, Object> node = new HashMap<>();
			node.put("id", execBean.getNodeId());
			node.put("title", execBean.getJobTitle());
			node.put("rely", execBean.getRelyNode());
			node.put("status", execBean.getStatus());
			nodeList.add(node);
		}
    	mv.addObject("dag",this.makeDag(nodeList, canvas));
    	mv.addObject("canvas", canvas);
    	mv.addObject("type", "execution");
        return mv;
    }
    
    @RequestMapping(value="/executionout/{id}/{otype}",method=RequestMethod.GET)
    public ModelAndView executionOutput(@PathVariable("id") Integer id, @PathVariable("otype") String otype){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	ExecutionDao executionDao = (ExecutionDao) ac.getBean("executionDao");
    	ExecutionBean executionBean = executionDao.getExecutionById(id);
    	ModelAndView mv = this.getModelAndView("execution/output");
    	String fileType = "log";
    	if(otype.equals("stderr")) {
    		fileType = "err";
    	}
    	String day = DateUtil.time2str(Long.parseLong(executionBean.getStartTime()), "yyyyMMdd");
    	String logFile = ExecutionDao.getExecutionLogFile(id, day, fileType);
    	File file = new File(logFile);
    	if(file.exists()) {
    		mv.addObject("hasLogFile", 1);
    	}else {
    		mv.addObject("hasLogFile", 0);
    	}
    	mv.addObject("file_type", fileType);
    	mv.addObject("execution", executionBean);
    	mv.addObject("type", "execution");
        return mv;
    }
    
    /**
     * HOST VIEW
     */
    @RequestMapping(value="/host",method=RequestMethod.GET)
    public ModelAndView host(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	ModelAndView mv = this.getModelAndView("host/index");
    	mv.addObject("type", "host");
    	try {
			//mv.addObject("typeMapping", new ObjectMapper().writeValueAsString(scheduleDao.getTypeMapping()));
			mv.addObject("groupMapping", new ObjectMapper().writeValueAsString(groupDao.getGroupMapping()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return mv;
    }
    
    @RequestMapping(value="/addhost",method=RequestMethod.GET)
    public ModelAndView addHost(){
    	ModelAndView mv = this.getModelAndView("host/add");
    	mv.addObject("type", "host");
        return mv;
    }
    
    @RequestMapping(value="/updatehost/{id}",method=RequestMethod.GET)
    public ModelAndView updateHost(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	HostDao hostDao = (HostDao) ac.getBean("hostDao");
    	HostBean hostBean = hostDao.getHostById(id);
    	ModelAndView mv = this.getModelAndView("host/update");
    	mv.addObject("type", "host");
    	mv.addObject("host", hostBean);
        return mv;
    }
    
    /**
     * USER VIEW
     */
    @RequestMapping(value="/user",method=RequestMethod.GET)
    public ModelAndView user(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	UserDao userDao = (UserDao) ac.getBean("userDao");
    	ModelAndView mv = this.getModelAndView("user/index");
    	try {
			mv.addObject("typeMapping", new ObjectMapper().writeValueAsString(userDao.getTypeMapping()));
			mv.addObject("groupMapping", new ObjectMapper().writeValueAsString(groupDao.getGroupMapping()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	mv.addObject("type", "user");
        return mv;
    }
    
    @RequestMapping(value="/adduser",method=RequestMethod.GET)
    public ModelAndView addUser(){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	List<GroupBean> groupList = groupDao.getGroupList();
    	ModelAndView mv = this.getModelAndView("user/add");
    	mv.addObject("type", "user");
    	mv.addObject("groupList", groupList);
        return mv;
    }
    
    @RequestMapping(value="/updateuser/{id}",method=RequestMethod.GET)
    public ModelAndView updateUser(@PathVariable("id") Integer id){
    	ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
    	GroupDao groupDao = (GroupDao) ac.getBean("groupDao");
    	List<GroupBean> groupList = groupDao.getGroupList();
        UserDao userDao = (UserDao) ac.getBean("userDao");
    	UserBean userBean = userDao.getUserById(id);
    	ModelAndView mv = this.getModelAndView("user/update");
    	mv.addObject("type", "user");
    	mv.addObject("groupList", groupList);
    	mv.addObject("user", userBean);
        return mv;
    }
}

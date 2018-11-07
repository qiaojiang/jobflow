package com.qj.schedule.dao;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qj.schedule.bean.ExecutionBean;
import com.qj.schedule.bean.JobBean;
import com.qj.schedule.bean.JobFlowBean;
import com.qj.schedule.bean.JobFlowNodeBean;
import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.util.ConfigUtil;
import com.qj.schedule.util.ConfigUtil2;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.PropertyUtil;
import com.qj.schedule.util.StringUtil;


public class ExecutionDao extends Dao{
	
	private String tableName = "jf_execution";
	
	public static final int STATUS_SUCCESS = 1;
	
	public static final int STATUS_FAILED = 0;
	
	public static final int STATUS_WAITING = 2;
	
	public static final int STATUS_RUNNING = 3;
	
	public Map<String,String> getStatusMapping(){
		Map<String,String> type = new HashMap<>();
		type.put("" + STATUS_SUCCESS, "success");
		type.put("" + STATUS_FAILED, "failed");
		type.put("" + STATUS_WAITING, "waiting");
		type.put("" + STATUS_RUNNING, "running");
		return type;
	}
	
	public ExecutionBean getExecutionById(int id){
		String sql = "SELECT * FROM "+ this.tableName +" where id = ?";
        ExecutionBean executionBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            executionBean = qr.query(sql, new BeanHandler<>(ExecutionBean.class,super.getRowProcessor()), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionBean;
	}
	
	public ExecutionBean getExecutionByTokenNodeId(String token, int nodeId){
		String sql = "SELECT * FROM "+ this.tableName +" where token = ? and  node_id = ? ";
        ExecutionBean executionBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            executionBean = qr.query(sql, new BeanHandler<>(ExecutionBean.class,super.getRowProcessor()), token, nodeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionBean;
	}

	public List<ExecutionBean> getExecutionListByToken(String token){
        String sql = "SELECT * FROM "+ this.tableName +" where token = ?";
        List<ExecutionBean> executionList = new ArrayList<ExecutionBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			executionList = qr.query(sql, new BeanListHandler<>(ExecutionBean.class,super.getRowProcessor()), token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionList;
	}
	
	public List<ExecutionBean> getAllExecutionList(int start,int limit){
        String sql = "SELECT * FROM "+ this.tableName +" ORDER BY id DESC LIMIT ?,?";
        List<ExecutionBean> executionList = new ArrayList<ExecutionBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			executionList = qr.query(sql, new BeanListHandler<>(ExecutionBean.class,super.getRowProcessor()), start, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionList;
	}
	
	public List<ExecutionBean> getExecutionListByGroupId(int groupId, int start,int limit){
        List<ExecutionBean> executionList = new ArrayList<ExecutionBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
			String sql = "SELECT * FROM "+ this.tableName +" WHERE "+ condition +" 1=1 ORDER BY id DESC LIMIT ? , ?";
			executionList = qr.query(sql, new BeanListHandler<>(ExecutionBean.class,super.getRowProcessor()), start, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionList;
	}
	
	public int getExecutionCountByGroupId(int groupId){
        int count = 0;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
            String sql = "SELECT count(*) FROM "+ this.tableName +" WHERE "+ condition +" 1=1";
            count = qr.query(sql, new ResultSetHandler<Integer>(){  
                public Integer handle(ResultSet rs) throws SQLException {  
                    if(rs.next()){  
                        return rs.getInt(1); 
                    }  
                    return 0;  
                }  
            });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	} 
	
	public List<ExecutionBean> getExecutionListByStatus(int status,int start,int limit){
		String sql = "SELECT * FROM jf_execution WHERE status = ? ORDER BY id DESC LIMIT ?,?";
		List<ExecutionBean> executionList = new ArrayList<ExecutionBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			executionList = qr.query(sql, new BeanListHandler<>(ExecutionBean.class,super.getRowProcessor()), status, start, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionList;
	}
	
	public List<ExecutionBean> getRunableExecutionList(int start,int limit){
		String sql = "SELECT * FROM jf_execution "
				+ "WHERE status = ? OR (status = ? and exec_num <= retry_num and retry_interval < (? - end_time)) "
				+ "ORDER BY id DESC LIMIT ?,?";
		List<ExecutionBean> executionList = new ArrayList<ExecutionBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			long time = new Date().getTime()/1000;
			executionList = qr.query(sql, new BeanListHandler<>(ExecutionBean.class,super.getRowProcessor()), STATUS_WAITING, STATUS_FAILED, time, start, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionList;
	} 
	
	public boolean executeJob(JobBean jobBean, ScheduleBean scheduleBean, Map<String,String> params){
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String sql = "insert into jf_execution(token,job_id,job_title,cmd,create_time,retry_num,retry_interval,host_id,group_id,status) values(?,?,?,?,?,?,?,?,?,?)";
			Object[] param = {
					StringUtil.makeToken(10),
					jobBean.getJobId(),
					jobBean.getTitle(),
					this.formatCmd(jobBean.getCmd(), params),
					new Date().getTime()/1000,
					scheduleBean.getRetryNum(),
					scheduleBean.getRetryInterval(),
					scheduleBean.getHostId(),
					jobBean.getGroupId(),
					STATUS_WAITING
			}; 
			qr.update(sql, param);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean executeJobFlow(JobFlowBean jobFlowBean, ScheduleBean scheduleBean, Map<String,String> params){
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
			JobFlowNodeDao jfnDao = (JobFlowNodeDao) ac.getBean("jobFlowNodeDao");
			List<JobFlowNodeDetailBean> nodeList = jfnDao.getJobFlowNodeDetailListByFlowId(jobFlowBean.getFlowId());
			String sql = "insert into jf_execution(token,job_id,job_title,node_id,rely_node,cmd,create_time,retry_num,retry_interval,host_id,group_id,status) values(?,?,?,?,?,?,?,?,?,?,?,?)";
			String token = StringUtil.makeToken(10);
			for(JobFlowNodeDetailBean node : nodeList){
				Object[] param = {
						token,
						node.getJobId(),
						node.getJobTitle(),
						node.getNodeId(),
						node.getRely(),
						this.formatCmd(node.getCmd(), params),
						new Date().getTime()/1000,
						scheduleBean.getRetryNum(),
						scheduleBean.getRetryInterval(),
						scheduleBean.getHostId(),
						jobFlowBean.getGroupId(),
						STATUS_WAITING
				}; 
				qr.update(sql, param);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setExecutionStatus(int id, int fromStatus, int toStatus){
		try {
			QueryRunner qr = new QueryRunner();
			//锁操作需要使用同一连接, 否则会造成死锁
			Connection conn = super.getDataSource().getConnection();
			qr.execute(conn, "LOCK TABLES jf_execution WRITE");
			int unum = 0;
			switch (toStatus) {
			case STATUS_RUNNING:
				long startTime = new Date().getTime()/1000;
				unum = qr.execute(conn, "update jf_execution set status = ?, start_time = ?, exec_num = exec_num + 1 where id = ? and status = ?", toStatus, startTime, id, fromStatus);
				break;
			case STATUS_SUCCESS:
			case STATUS_FAILED:
				long endTime = new Date().getTime()/1000;
				unum = qr.execute(conn, "update jf_execution set status = ?, end_time = ? where id = ? and status = ?", toStatus, endTime, id, fromStatus);
				break;
			default:
				unum = qr.execute(conn, "update jf_execution set status = ? where id = ? and status = ?", toStatus, id, fromStatus);
				break;
			}
			qr.execute(conn, "UNLOCK TABLES");
			//记得释放连接
			conn.close();
			return unum > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateExecution(int id, Map<String,Object> map){
		Map<String,Object> where = new HashMap<>();
		where.put("id", id);
		return this.update(this.tableName, map, where);
	}
	
	public boolean deleteExecution(int id){
		Map<String,Object> where = new HashMap<>();
		where.put("id", id);
		return this.delete(this.tableName, where);
	}
	
	public boolean restartExecution(int id){
		Map<String,Object> map = new HashMap<>();
		map.put("status", STATUS_WAITING);
		Map<String,Object> where = new HashMap<>();
		where.put("id", id);
		return this.update(this.tableName, map, where);
	}
	
	public String formatCmd(String cmd, Map<String,String> params) {
		if(cmd == null) return cmd;
		if(params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				cmd = cmd.replace(entry.getKey(), entry.getValue());
			}
		}
		//yyyy-MM-dd HH:mm:ss
		cmd = cmd.replace("{hour}", DateUtil.oral2str("now","HH"))
			.replace("{minute}", DateUtil.oral2str("now","mm"))
			.replace("{today}", DateUtil.oral2str("today","yyyy-MM-dd"))
			.replace("{today_nodash}", DateUtil.oral2str("today","yyyyMMdd"))
			.replace("{yesterday}", DateUtil.oral2str("yesterday","yyyy-MM-dd"))
			.replace("{yesterday_nodash}", DateUtil.oral2str("yesterday","yyyyMMdd"))
			.replace("{tomorrow}", DateUtil.oral2str("tomorrow","yyyy-MM-dd"))
			.replace("{tomorrow_nodash}", DateUtil.oral2str("tomorrow","yyyyMMdd"));
		return cmd;
	}
	
	public void formatBean(ExecutionBean executionBean){
		executionBean.setCreateTime(DateUtil.time2str(Long.parseLong(executionBean.getCreateTime())));
		executionBean.setStartTime(DateUtil.time2str(Long.parseLong(executionBean.getStartTime())));
		executionBean.setEndTime(DateUtil.time2str(Long.parseLong(executionBean.getEndTime())));
	}

	public Map<String,Object> getExecutionCountByStatus(int groupId, long beginTime, long endTime){
		Map<String,Object> status = new HashMap<>();
		status.put("" + STATUS_SUCCESS, 0);
		status.put("" + STATUS_FAILED, 0);
		status.put("" + STATUS_WAITING, 0);
		status.put("" + STATUS_RUNNING, 0);
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
			String sql = "SELECT status,count(*) AS count FROM "+ this.tableName +" WHERE create_time >= ? AND create_time <= ? AND "+ condition +" 1=1 GROUP BY status";
			List<Map<String,Object>> list = qr.query(sql, new MapListHandler(), beginTime, endTime);
			for(Map<String,Object> map : list){
				status.put(map.get("status").toString(), map.get("count").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public Map<String,Integer> getDateExecutionCountStat(String type, int groupId, long beginTime, long endTime){
		String format = (type == "hour") ? "dd日HH时" : "yyyy-MM-dd";
		Map<String,Integer> stat = new TreeMap<>();
		for(long t = beginTime; t < endTime; t+=3600) {
			stat.put(DateUtil.time2str(t, format), 0);
		}
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
			String sql = "SELECT id,end_time FROM "+ this.tableName +" WHERE end_time >= ? AND end_time <= ? AND "+ condition +" status = 1 ORDER BY end_time ASC";
			List<Map<String,Object>> list = qr.query(sql, new MapListHandler(), beginTime, endTime);
			for(Map<String,Object> map : list){
				String key = DateUtil.time2str(Long.parseLong(map.get("end_time").toString()), format);
				if(stat.containsKey(key)) {
					stat.put(key, stat.get(key) + 1);
				}else {
					stat.put(key, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat;
	}
	
	public static String getExecutionLogFile(int id, String day, String fileType){
		String logPath = ConfigUtil.get("log_path");
		if(logPath == null) logPath = "logs";
		String path = logPath + "/" + day;
		File logDir = new File(path);  
		if(!logDir.exists()){
			logDir.mkdirs();
		}
		return path + "/execution_" + id + "." + fileType;
	}

}

package com.qj.schedule.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.qj.schedule.bean.ExecutionBean;
import com.qj.schedule.bean.JobBean;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.StringUtil;


public class JobDao extends Dao{
	
	private String tableName = "jf_job";
	
	/*
	 * 作业类型：1 SHELL,2 PHP,3 PYTHON,4 JAVA,5 HIVE
	 */
	public static final int TYPE_SCHELL = 1;
	public static final int TYPE_PHP = 2;
	public static final int TYPE_PYTHON = 3;
	public static final int TYPE_JAVA = 4;
	public static final int TYPE_HIVE = 5;
	
	public Map<String,String> getTypeMapping(){
		Map<String,String> mappings = new HashMap<>();
		mappings.put("" + TYPE_SCHELL, "SCHELL");
		mappings.put("" + TYPE_PHP, "PHP");
		mappings.put("" + TYPE_PYTHON, "PYTHON");
		mappings.put("" + TYPE_JAVA, "JAVA");
		mappings.put("" + TYPE_HIVE, "HIVE");
		return mappings;
	}
	
	public JobBean getJobById(int jobId){
        JobBean jobBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM "+ this.tableName +" where job_id = ? AND status = 1";
            jobBean = qr.query(sql, new BeanHandler<>(JobBean.class,super.getRowProcessor()), jobId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobBean;
	}
	
	public List<JobBean> getJobListByGroupId(int groupId, int start, int limit){
		String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
		String sql = "SELECT * FROM "+ this.tableName + " WHERE " + condition + " status = 1 ORDER BY job_id DESC LIMIT ?,?";
		List<JobBean> jobList = new ArrayList<JobBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			jobList = qr.query(sql, new BeanListHandler<>(JobBean.class,super.getRowProcessor()),start,limit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobList;
	}
	
	public List<JobBean> getJobListByIds(int[] jobIds){
		String sql = "SELECT * FROM "+ this.tableName +" where job_id in ('" + StringUtil.implode(jobIds, "','") + "') AND status = 1";
		List<JobBean> jobList = new ArrayList<JobBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			jobList = qr.query(sql, new BeanListHandler<>(JobBean.class,super.getRowProcessor()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobList;
	}
	
	public List<JobBean> getJobListByFlowId(int flowId){
		String sql = "SELECT j.* FROM jf_jobflow_node n "
        		+ "join jf_job j on n.job_id = j.job_id "
        		+ "where n.flow_id = ? AND j.status = 1";
		List<JobBean> jobList = new ArrayList<JobBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			jobList = qr.query(sql, new BeanListHandler<>(JobBean.class,super.getRowProcessor()),flowId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobList;
	}
	
	public int getJobCountByGroupId(int groupId){
        int count = 0;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
            String sql = "SELECT count(*) FROM " + this.tableName + " WHERE " + condition + " status = 1";
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
	
	public boolean addJob(Map<String,Object> map){
		map.put("create_time", new Date().getTime()/1000);
		map.put("update_time", new Date().getTime()/1000);
		return insert(this.tableName, map);
	}
	
	public boolean updateJob(int jobId, Map<String,Object> map){
		map.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("job_id", jobId);
		return update(this.tableName, map, where);
	}
	
	public boolean deleteJob(int jobId){
		Map<String,Object> set = new HashMap<>();
		set.put("status", 0);
		set.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("job_id", jobId);
		return update(this.tableName, set, where);
	}

	public void formatBean(JobBean jobBean){
		jobBean.setCreateTime(DateUtil.time2str(Long.parseLong(jobBean.getCreateTime())));
		jobBean.setUpdateTime(DateUtil.time2str(Long.parseLong(jobBean.getUpdateTime())));
	}
}

package com.qj.schedule.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.qj.schedule.bean.JobBean;
import com.qj.schedule.bean.JobFlowBean;
import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.util.DateUtil;


public class JobFlowDao extends Dao{
	
	private String tableName = "jf_jobflow";
	
	public JobFlowBean getJobFlowById(int flowId){
		JobFlowBean jobFlowBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM "+ this.tableName +" WHERE flow_id = ? AND status = 1";
            jobFlowBean = qr.query(sql, new BeanHandler<>(JobFlowBean.class,super.getRowProcessor()), flowId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobFlowBean;
	}
	
	public JobFlowBean getJobFlowByNodeId(int nodeId){
		JobFlowBean jobFlowBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT j.* "
            		+ "FROM jf_jobflow j "
            		+ "JOIN jf_jobflow_node n ON n.flow_id = j.flow_id "
            		+ "where n.node_id = ? AND j.status = ?";
            jobFlowBean = qr.query(sql, new BeanHandler<>(JobFlowBean.class,super.getRowProcessor()), nodeId, 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jobFlowBean;
	}
	
	public List<JobFlowBean> getJobFlowListByGroupId(int groupId, int start, int limit){
        List<JobFlowBean> jobFlowList = new ArrayList<JobFlowBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
            String sql = "SELECT * FROM "+ this.tableName +" WHERE "+ condition +" status = 1 ORDER BY flow_id DESC LIMIT ? , ?";
            jobFlowList = qr.query(sql, new BeanListHandler<>(JobFlowBean.class,super.getRowProcessor()), start, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobFlowList;
	}
	
	public int getJobFlowCountByGroupId(int groupId){
        int count = 0;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
            String sql = "SELECT count(*) FROM "+ this.tableName +" WHERE "+ condition +" status = 1";
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
	
	public boolean addJobFlow(Map<String,Object> map){
		map.put("create_time", new Date().getTime()/1000);
		map.put("update_time", new Date().getTime()/1000);
		return insert(this.tableName, map);
	}
	
	public boolean updateJobFlow(int flowId, Map<String,Object> map){
		map.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("flow_id", flowId);
		return update(this.tableName, map, where);
	}
	
	public boolean deleteJobFlow(int flowId){
		Map<String,Object> set = new HashMap<>();
		set.put("status", 0);
		set.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("flow_id", flowId);
		return update(this.tableName, set, where);
	}
	
	public void formatBean(JobFlowBean jobFlowBean){
		jobFlowBean.setCreateTime(DateUtil.time2str(Long.parseLong(jobFlowBean.getCreateTime())));
		jobFlowBean.setUpdateTime(DateUtil.time2str(Long.parseLong(jobFlowBean.getUpdateTime())));
	}
}

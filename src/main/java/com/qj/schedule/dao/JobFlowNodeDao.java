package com.qj.schedule.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qj.schedule.bean.JobBean;
import com.qj.schedule.bean.JobFlowBean;
import com.qj.schedule.bean.JobFlowNodeBean;
import com.qj.schedule.bean.JobFlowNodeDetailBean;
import com.qj.schedule.util.StringUtil;


public class JobFlowNodeDao extends Dao{
	
	private String tableName = "jf_jobflow_node";
	
	public JobFlowNodeBean getJobFlowNodeById(int nodeId){
		JobFlowNodeBean nodeBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM "+ this.tableName +" where node_id = ?";
            nodeBean = qr.query(sql, new BeanHandler<>(JobFlowNodeBean.class,super.getRowProcessor()), nodeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nodeBean;
	}
	
	public JobFlowNodeBean getRelyJobFlowNodeById(int nodeId){
		JobFlowNodeBean nodeBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT b.* FROM "+ this.tableName +" a join jf_jobflow_node b on a.rely = b.node_id where a.node_id = ?";
            nodeBean = qr.query(sql, new BeanHandler<>(JobFlowNodeBean.class,super.getRowProcessor()), nodeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nodeBean;
	}
	
	public List<JobFlowNodeDetailBean> getJobFlowNodeDetailListByFlowId(int flowId){
		List<JobFlowNodeDetailBean> nodeDetailList = new ArrayList<JobFlowNodeDetailBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT n.*,j.title as job_title,j.cmd "
            		+ "FROM jf_jobflow_node n "
            		+ "JOIN jf_job j ON n.job_id = j.job_id "
            		+ "where n.flow_id = ? AND j.status = ?";
            nodeDetailList = qr.query(sql, new BeanListHandler<>(JobFlowNodeDetailBean.class,super.getRowProcessor()),flowId, 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nodeDetailList;
	}
	
	public List<JobFlowNodeDetailBean> getJobFlowNodeDetailListByNodeIds(int[] ids){
		List<JobFlowNodeDetailBean> nodeDetailList = new ArrayList<JobFlowNodeDetailBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT n.*,j.title as job_title "
            		+ "FROM jf_jobflow_node n "
            		+ "JOIN jf_job j ON n.job_id = j.job_id "
            		+ "where n.node_id in ('"+ StringUtil.implode(ids, ",")+"') AND j.status = ?";
            nodeDetailList = qr.query(sql, new BeanListHandler<>(JobFlowNodeDetailBean.class,super.getRowProcessor()), 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nodeDetailList;
	}
	
	public boolean addJobFlowNode(Map<String,Object> map){
		return insert(this.tableName, map);
	}
	
	public boolean updateJobFlowNode(int nodeId, Map<String,Object> map){
		Map<String,Object> where = new HashMap<>();
		where.put("node_id", nodeId);
		return update(this.tableName, map, where);
	}
	
	public boolean deleteJobFlowNode(int nodeId){
		Map<String,Object> where = new HashMap<>();
		where.put("node_id", nodeId);
		return delete(this.tableName, where);
	}
	
}

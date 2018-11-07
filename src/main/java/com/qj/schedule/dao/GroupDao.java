package com.qj.schedule.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.qj.schedule.bean.GroupBean;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.StringUtil;


public class GroupDao extends Dao{
	
	private String tableName = "jf_group";
	
	public GroupBean getGroupById(int groupId){
		GroupBean groupBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM " + this.tableName + " where group_id = ?";
            groupBean = qr.query(sql, new BeanHandler<>(GroupBean.class,super.getRowProcessor()), groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groupBean;
	}

	public List<GroupBean> getGroupList(){
		String sql = "SELECT * FROM " + this.tableName + " ORDER BY group_id ASC";
		List<GroupBean> groupList = new ArrayList<GroupBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			groupList = qr.query(sql, new BeanListHandler<>(GroupBean.class,super.getRowProcessor()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupList;
	}
	
	public Map<String,String> getGroupMapping(){
		List<GroupBean> groupBeanList = this.getGroupList();
		Map<String,String> mappings = new HashMap<>();
		for (GroupBean groupBean : groupBeanList) {
			mappings.put("" + groupBean.getGroupId(), groupBean.getTitle());
		}
		return mappings;
	} 

	public void formatBean(GroupBean groupBean) {

	}



}

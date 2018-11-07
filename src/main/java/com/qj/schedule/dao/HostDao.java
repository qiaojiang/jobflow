package com.qj.schedule.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.qj.schedule.bean.HostBean;
import com.qj.schedule.bean.HostBean;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.StringUtil;


public class HostDao extends Dao{
	
	private String tableName = "jf_host";
	
	public HostBean getHostById(int hostId){
		HostBean hostBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM " + this.tableName + " where host_id = ?";
            hostBean = qr.query(sql, new BeanHandler<>(HostBean.class,super.getRowProcessor()), hostId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostBean;
	}

	public List<HostBean> getHostListByGroupId(int groupId){
		String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
		String sql = "SELECT * FROM " + this.tableName + " WHERE "+ condition +" 1=1 ORDER BY host_id ASC";
		List<HostBean> hostList = new ArrayList<HostBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			hostList = qr.query(sql, new BeanListHandler<>(HostBean.class,super.getRowProcessor()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hostList;
	}
	
	public int getHostCountByGroupId(int groupId){
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
	
	public Map<String,String> getHostMapping(int groupId){
		List<HostBean> hostBeanList = this.getHostListByGroupId(groupId);
		Map<String,String> mappings = new HashMap<>();
		mappings.put("0", "调度机");
		for (HostBean hostBean : hostBeanList) {
			mappings.put("" + hostBean.getHostId(), hostBean.getHost());
		}
		return mappings;
	} 

	public boolean addHost(Map<String,Object> map){
		map.put("create_time", new Date().getTime()/1000);
		map.put("update_time", new Date().getTime()/1000);
		return insert(this.tableName, map);
	}
	
	public boolean updateHost(int hostId, Map<String,Object> map){
		map.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("host_id", hostId);
		return update(this.tableName, map, where);
	}
	
	public boolean deleteHost(int hostId){
		Map<String,Object> where = new HashMap<>();
		where.put("host_id", hostId);
		return delete(this.tableName, where);
	}

	public void formatBean(HostBean hostBean){
		hostBean.setCreateTime(DateUtil.time2str(Long.parseLong(hostBean.getCreateTime())));
		hostBean.setUpdateTime(DateUtil.time2str(Long.parseLong(hostBean.getUpdateTime())));
	}



}

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

import com.qj.schedule.bean.UserBean;
import com.qj.schedule.util.DateUtil;
import com.qj.schedule.util.StringUtil;


public class UserDao extends Dao{
	
	private String tableName = "jf_user";
	
	public static final int TYPE_ADMIN = 1;
	
	public static final int TYPE_USER = 2;
	
	public Map<String,String> getTypeMapping(){
		Map<String,String> type = new HashMap<>();
		type.put("" + TYPE_ADMIN, "管理员");
		type.put("" + TYPE_USER, "用户");
		return type;
	}
	
	public UserBean getUserById(int userId){
        UserBean userBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM jf_user where user_id = ?";
            userBean = qr.query(sql, new BeanHandler<>(UserBean.class,super.getRowProcessor()), userId);
            if(userBean != null) userBean.setPassword("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userBean;
	}
	
	public UserBean getUserByAccount(String username, String password){
        UserBean userBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM jf_user where username = ? and password = ? and status = ?";
            userBean = qr.query(sql, new BeanHandler<>(UserBean.class,super.getRowProcessor()), username, password, 1);
            if(userBean != null) userBean.setPassword("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userBean;
	}
	
	public List<UserBean> getUserList(int start,int limit){
		String sql = "SELECT * FROM jf_user WHERE status = 1 ORDER BY user_id DESC LIMIT ?,?";
		List<UserBean> userList = new ArrayList<UserBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			userList = qr.query(sql, new BeanListHandler<>(UserBean.class,super.getRowProcessor()),start,limit);
			for (UserBean userBean : userList) {
				userBean.setPassword("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	public List<UserBean> getUserListByIds(int[] userIds){
		String sql = "SELECT * FROM jf_user where user_id in ('" + StringUtil.implode(userIds, "','") + "') and status = 1";
		List<UserBean> userList = new ArrayList<UserBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			userList = qr.query(sql, new BeanListHandler<>(UserBean.class,super.getRowProcessor()));
			for (UserBean userBean : userList) {
				userBean.setPassword("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	public int getUserCount(){
		int count = 0;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT count(*) FROM "+ this.tableName +" WHERE status = 1";
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

	public void formatBean(UserBean userBean) {
		userBean.setCreateTime(DateUtil.time2str(Long.parseLong(userBean.getCreateTime())));
		userBean.setUpdateTime(DateUtil.time2str(Long.parseLong(userBean.getUpdateTime())));
	}

	public boolean addUser(Map<String,Object> map){
		map.put("create_time", new Date().getTime()/1000);
		map.put("update_time", new Date().getTime()/1000);
		return insert(this.tableName, map);
	}
	
	public boolean updateUser(int userId, Map<String,Object> map){
		map.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("user_id", userId);
		return update(this.tableName, map, where);
	}
	
	public boolean deleteUser(int userId){
		Map<String,Object> set = new HashMap<>();
		set.put("status", 0);
		set.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("user_id", userId);
		return update(this.tableName, set, where);
	}

}

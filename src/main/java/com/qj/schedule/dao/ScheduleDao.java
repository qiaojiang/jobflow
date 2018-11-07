package com.qj.schedule.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.qj.schedule.bean.ScheduleBean;
import com.qj.schedule.util.DateUtil;


public class ScheduleDao extends Dao{
	
	private String tableName = "jf_schedule";
	
	/*
	 * 调度类型：1 简单调度 ，2 周期调度
	 */
	public static final int TYPE_SIMPLE = 1;
	public static final int TYPE_CRON = 2;
	
	/*
	 * 实体类型：1 作业，2 作业流
	 */
	public static final int ENTITY_TYPE_JOB = 1;
	public static final int ENTITY_TYPE_JOBFLOW = 2;
	
	/*
	 * 调度状态：0 删除 ，1 正常，2暂停
	 */
	public static final int STATUS_DELETE = 0;
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_PAUSE = 2;
	
	public Map<String,String> getTypeMapping(){
		Map<String,String> mappings = new HashMap<>();
		mappings.put("" + TYPE_SIMPLE, "简单调度");
		mappings.put("" + TYPE_CRON, "周期调度");
		return mappings;
	}
	
	public Map<String,String> getEntityTypeMapping(){
		Map<String,String> mappings = new HashMap<>();
		mappings.put("" + ENTITY_TYPE_JOB, "作业");
		mappings.put("" + ENTITY_TYPE_JOBFLOW, "作业流");
		return mappings;
	}
	
	public Map<String,String> getStatusMapping(){
		Map<String,String> type = new HashMap<>();
		type.put("" + STATUS_DELETE, "删除");
		type.put("" + STATUS_NORMAL, "正常");
		type.put("" + STATUS_PAUSE, "暂停");
		return type;
	}
	
	public ScheduleBean getScheduleById(int scheduleId){
        ScheduleBean scheduleBean = null;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM "+ this.tableName +" where schedule_id = ? and status > ?";
            scheduleBean = qr.query(sql, new BeanHandler<>(ScheduleBean.class,super.getRowProcessor()), scheduleId, 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scheduleBean;
	}

	public List<ScheduleBean> getRunableScheduleList(){
        List<ScheduleBean> scheduleList = new ArrayList<ScheduleBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
            String sql = "SELECT * FROM "+ this.tableName +" where status = ?";
            scheduleList = qr.query(sql, new BeanListHandler<>(ScheduleBean.class,super.getRowProcessor()), 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scheduleList;
	}
	
	public List<ScheduleBean> getScheduleListByGroupId(int groupId, int start, int limit){
        List<ScheduleBean> scheduleList = new ArrayList<ScheduleBean>();
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
			String sql = "SELECT * FROM "+ this.tableName +" WHERE "+ condition +" status > ? ORDER BY schedule_id DESC LIMIT ? , ?";
            scheduleList = qr.query(sql, new BeanListHandler<>(ScheduleBean.class,super.getRowProcessor()), 0, start, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scheduleList;
	}
	
	public int getScheduleCountByGroupId(int groupId){
        int count = 0;
		try {
			QueryRunner qr = new QueryRunner(super.getDataSource());
			String condition = groupId == 0 ? "" : "group_id = '" + groupId + "' AND ";
            String sql = "SELECT count(*) FROM "+ this.tableName +" WHERE "+ condition +" status > 0";
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
	
	public boolean addSchedule(Map<String,Object> map){
		map.put("create_time", new Date().getTime()/1000);
		map.put("update_time", new Date().getTime()/1000);
		if(map.get("start_time") != null){
			map.put("start_time","" + DateUtil.str2time((String)map.get("start_time")));
		}
		if(map.get("end_time") != null){
			map.put("end_time","" + DateUtil.str2time((String)map.get("end_time")));
		}
		return insert(this.tableName, map);
	}
	
	public boolean updateSchedule(int id, Map<String,Object> map){
		map.put("update_time", new Date().getTime()/1000);
		if(map.get("start_time") != null){
			map.put("start_time","" + DateUtil.str2time((String)map.get("start_time")));
		}
		if(map.get("end_time") != null){
			map.put("end_time", "" + DateUtil.str2time((String)map.get("end_time")));
		}
		Map<String,Object> where = new HashMap<>();
		where.put("schedule_id", id);
		return update(this.tableName, map, where);
	}
	
	public boolean deleteSchedule(int id){
		Map<String,Object> set = new HashMap<>();
		set.put("status", 0);
		set.put("update_time", new Date().getTime()/1000);
		Map<String,Object> where = new HashMap<>();
		where.put("schedule_id", id);
		return update(this.tableName, set, where);
	}
	
	public boolean doSchedule(int id, long time){
		Map<String,Object> map = new HashMap<>();
		map.put("schedule_time", time);
		
		Map<String,Object> where = new HashMap<>();
		where.put("schedule_id", id);
		return update(this.tableName, map, where);
	}
	
	public void formatBean(ScheduleBean scheduleBean){
		scheduleBean.setCreateTime(DateUtil.time2str(Long.parseLong(scheduleBean.getCreateTime())));
		scheduleBean.setUpdateTime(DateUtil.time2str(Long.parseLong(scheduleBean.getUpdateTime())));
		scheduleBean.setStartTime(DateUtil.time2str(Long.parseLong(scheduleBean.getStartTime())));
		scheduleBean.setEndTime(DateUtil.time2str(Long.parseLong(scheduleBean.getEndTime())));
		scheduleBean.setScheduleTime(DateUtil.time2str(Long.parseLong(scheduleBean.getScheduleTime())));
	}
	
}

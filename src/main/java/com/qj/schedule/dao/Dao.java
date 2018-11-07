package com.qj.schedule.dao;

import java.util.Set;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.qj.schedule.util.StringUtil;

public class Dao {
	
	private ProxoolDataSource dataSource;
	
	private RowProcessor rowProcessor;
	
	public ProxoolDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(ProxoolDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public RowProcessor getRowProcessor(){
		BeanProcessor bean = new GenerousBeanProcessor();
		RowProcessor processor = new BasicRowProcessor(bean);
		return processor;
	}
	
	public boolean insert(String tableName, Map<String,Object> map){
		String[] keys = new String[map.size()];
		Object[] values = new String[map.size()];
		String[] placeholders = new String[map.size()];
		Set<Entry<String, Object>> entries = map.entrySet();
		int i = 0;
		for (Entry<String, Object> entry : entries) {
			keys[i] = "`" + entry.getKey() + "`";
			values[i] = "" + entry.getValue();
			placeholders[i] = "?";
		    i++;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ")
		   .append(tableName)
		   .append("(")
		   .append(StringUtil.implode(keys, ","))
		   .append(") values (")
		   .append(StringUtil.implode(placeholders, ","))
		   .append(")");
		QueryRunner qr = new QueryRunner(getDataSource());
		try {
			qr.update(sql.toString(), values);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean update(String tableName, Map<String,Object> set, Map<String,Object> where){
		String[] skeys = new String[set.size()];
		String[] wkeys = new String[where.size()];
		Object[] values = new String[set.size() + where.size()];
		Set<Entry<String, Object>> sentries = set.entrySet();
		int i = 0;
		for (Entry<String, Object> entry : sentries) {
			skeys[i] = "`" + entry.getKey() + "` = ?";
			values[i] = "" + entry.getValue();
		    i++;
		}

		Set<Entry<String, Object>> wentries = where.entrySet();
		int j = 0;
		for (Entry<String, Object> entry : wentries) {
			wkeys[j++] = "`" + entry.getKey() + "` = ?" ;
			values[i++] = "" + entry.getValue();
		}
		StringBuffer sql = new StringBuffer();
		sql.append("update ")
		   .append(tableName)
		   .append(" set ")
		   .append(StringUtil.implode(skeys, ","))
		   .append(" where ")
		   .append(StringUtil.implode(wkeys, " and "));
		
		QueryRunner qr = new QueryRunner(getDataSource());
		try {
			qr.update(sql.toString() ,values);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete(String tableName, Map<String,Object> where){
		String[] keys = new String[where.size()];
		Object[] values = new String[where.size()];
		Set<Entry<String, Object>> entries = where.entrySet();
		int i = 0;
		for (Entry<String, Object> entry : entries) {
			keys[i] = "`" + entry.getKey() + "` = ?" ;
			values[i] = "" + entry.getValue();
		    i++;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ")
		   .append(tableName)
		   .append(" where ")
		   .append(StringUtil.implode(keys, " and "));
		QueryRunner qr = new QueryRunner(getDataSource());
		try {
			qr.update(sql.toString(), values);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}

package com.qj.schedule.util;

import java.util.HashMap;
import java.util.Map;

public class StatusUtil {
	
	/**
	 * 状态码
	 */
	public final static int CODE_SUCCESS = 0;
	
	public final static int CODE_FAILED = 1;
	
	/**
	 * 状态描述
	 */
	public final static String MSG_SUCCESS = "success";
	
	public final static String MSG_FAILED = "failed";
	
	public final static String MSG_LOGIN_SUCCESS = "登录成功";
	
	public final static String MSG_LOGIN_FAILED = "用户名或密码错误";
	
	public final static String MSG_ADD_SUCCESS = "添加成功";
	
	public final static String MSG_ADD_FAILED = "添加失败";
	
	public final static String MSG_UPDATE_SUCCESS = "更新成功";
	
	public final static String MSG_UPDATE_FAILED = "更新失败";
	
	public final static String MSG_DELETE_SUCCESS = "删除成功";
	
	public final static String MSG_DELETE_FAILED = "删除失败";
	
	public final static String MSG_START_SUCCESS = "启动成功";
	
	public final static String MSG_START_FAILED = "启动失败";
	
	public final static String MSG_RESTART_SUCCESS = "重启成功";
	
	public final static String MSG_RESTART_FAILED = "重启失败";
	
	/**
	 * HTTP 状态码
	 */
	public final static Map<String, String> HTTP_ERROR = new HashMap<String, String>() {
		{
			put("401","Unauthorized");
			put("403","Forbidden");
			put("404","Not Found");
			put("500","Internal Server Error");
			put("501","Not Implemented");
			put("502","Bad Gateway");
			put("503","Service Unavailable");
			put("504","Internal Server Error");
		}
	};
}

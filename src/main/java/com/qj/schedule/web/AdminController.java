package com.qj.schedule.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class AdminController {
    
	private Properties properties;
	
	public Map<String,Object> output(int status, String message, Object data){
		Map<String,Object> output = new HashMap<String,Object>();
		output.put("status", status);
		output.put("message", message);
		output.put("data", data);
		return output;
	}
	
    public Object getConf(String key){
		try {
			if(properties == null){
				Resource resource = new ClassPathResource("config.properties");
				properties = PropertiesLoaderUtils.loadProperties(resource);
	    	}
			return properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	
    public int getGroupId(HttpServletRequest request){
		HttpSession session = request.getSession();
		return (int)session.getAttribute("group_id");
	}
    
    public short getIdentity(HttpServletRequest request){
    	HttpSession session = request.getSession();
		return (short)session.getAttribute("type");
    }
    
}
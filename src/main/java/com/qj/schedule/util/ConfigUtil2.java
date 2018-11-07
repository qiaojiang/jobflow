package com.qj.schedule.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil2 {
 
	static Properties properties;
 
	static{
		properties = new Properties();
		InputStream is = null;
		//先加载目录下面的配置文件，没有再去加载classpath下的配置文件
		try {
			is = new FileInputStream("config.properties");
		} catch (FileNotFoundException e1) {
			is = null;
		}
		
		try {
			if(is == null) {
				is = ConfigUtil2.class.getClassLoader().getResourceAsStream("config.properties");
			}
			properties.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String get(String key) {
		String value = properties.getProperty(key);
		if (value != null) {
			return value;
		} else {
			return null;
		}
	}
	
}
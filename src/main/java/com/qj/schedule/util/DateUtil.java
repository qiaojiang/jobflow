package com.qj.schedule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static long str2time(String date){
		return str2time(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static long str2time(String date, String formater){
		long timestamp = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		try {
			Date d = sdf.parse(date);
			timestamp = d.getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	public static String time2str(long timestamp){
		return time2str(timestamp, "yyyy-MM-dd HH:mm:ss");
	}

	public static String time2str(long timestamp, String formater){
		if(timestamp == 0) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(timestamp * 1000);
	}
	
	public static long oral2time(String oral) {
		long time = 0;
		Calendar c = Calendar.getInstance();    
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long today = c.getTimeInMillis() / 1000;
		switch(oral) {
			case "now":
				time = new Date().getTime() / 1000;
				break;
			case "today":
				time = today;
				break;
			case "yesterday":
				time = today - 24 * 3600;
				break;
			case "tomorrow":
				time = today + 24 * 3600;
				break;
		}
		return time;
	}
	
	public static String oral2str(String oral) {
		long time = oral2time(oral);
		return time2str(time);
	}
	
	public static String oral2str(String oral, String formater) {
		long time = oral2time(oral);
		return time2str(time, formater);
	}
}

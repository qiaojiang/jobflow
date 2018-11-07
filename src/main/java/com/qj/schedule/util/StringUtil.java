package com.qj.schedule.util;

import java.util.Random;

public class StringUtil {

	public static String implode(int[] ids,String delimiter){
		StringBuffer idsStr = new StringBuffer();
		for(int i = 0 ; i < ids.length; i++){
			if(i == ids.length - 1){
				idsStr.append(ids[i]);
			}else{
				idsStr.append(ids[i] + delimiter);
			}
		}
		return idsStr.toString();
	}
	
	public static String implode(String[] ids,String delimiter){
		StringBuffer idsStr = new StringBuffer();
		for(int i = 0 ; i < ids.length; i++){
			if(i == ids.length - 1){
				idsStr.append(ids[i]);
			}else{
				idsStr.append(ids[i] + delimiter);
			}
		}
		return idsStr.toString();
	}
	
	public static String makeToken(int length){
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
	    Random random = new Random();  
	    StringBuffer buf = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int num = random.nextInt(26);  
	        buf.append(str.charAt(num));  
	    }  
	    return buf.toString();  
	}
	
	public static int substrCount(String str, String substr) {
	    int count = 0;
	    int index = 0;
	    while ((index = str.indexOf(substr, index)) != -1) {
	        index = index + substr.length();
	        count++;
	    }
	    return count;
	}
	
	
}

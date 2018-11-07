package com.qj.schedule.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetUtil {

	public static InetAddress getInetAddress(){  
        try{  
            return InetAddress.getLocalHost();  
        }catch(UnknownHostException e){   
        }  
        return null;
    }  
  
    public static String getHostIp(InetAddress netAddress){  
        if(null == netAddress){  
            return null;  
        }  
        String ip = netAddress.getHostAddress();
        return ip;  
    }  
  
    public static String getHostName(InetAddress netAddress){  
        if(null == netAddress){  
            return null;  
        }  
        String name = netAddress.getHostName(); 
        return name;  
    }  
}

/**
 * 
 */
/**
 * @author qiaojiang
 *
 */
package com.qj.schedule;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTimer {

	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer();    
		   
		TimerTask task = new TimerTask() {   
		    public void run() {   
		    	for(int i = 0; i < 10 ;i++){
		    		System.out.println("task" + i);
		    		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		    	}
		    	System.gc();
		    }   
		};   
		   
		//以下是几种常用调度task的方法：   
		   
		timer.schedule(task, 3000);   
		
		timer.schedule(task, 3000);
		
		Thread.sleep(1000);
		
		timer.cancel();
		
		System.out.println("ok");

	}

}

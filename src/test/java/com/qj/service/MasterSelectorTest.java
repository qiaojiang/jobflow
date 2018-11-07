package com.qj.service;

import com.qj.schedule.service.Callback;
import com.qj.schedule.service.MasterSelector;


public class MasterSelectorTest implements Callback{
	
	
	public void callback() {
		int i = 0;
		while (i++ < 5) {
			System.out.println("do something");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		int i = (int)(10 * Math.random()) ;
		MasterSelectorTest t = new MasterSelectorTest();
		MasterSelector example = new MasterSelector("Client#" + i, t);
		try {
			example.start();
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}


package com.qj.schedule.runnable;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Timer implements Runnable{
	
	private static final Logger logger = LogManager.getLogger(Timer.class);
	
	private Process process;
	
	private int timeout;
	
	private String name;
	
	public Timer(Process process, int timeout, String name) {
		this.process = process;
		this.timeout = timeout;
		this.name = name;
	}
	
	@Override
	public void run() {
		long beginTime = new Date().getTime();
		while(new Date().getTime() < (beginTime + this.timeout * 1000)){
			try {
				Thread.sleep(1000);
				if(!this.process.isAlive()) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(this.process.isAlive()) {
			this.process.destroy();
			logger.error(this.name + " execute timeout");
		}
	}

}

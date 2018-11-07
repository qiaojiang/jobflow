package com.qj.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleExecutor {

	public static void main(String[] args) throws InterruptedException {
		Command cmd1 = new Command("php -v");
		Command cmd2 = new Command("java -version");
		Command cmd3 = new Command("dir");
		TimerTask tt1 = new Task("task1", cmd1);
		TimerTask tt2 = new Task("task2", cmd2);
		TimerTask tt3 = new Task("task3", cmd3);
		//璋冨害绾跨▼姹狅紝鍙湁鏀捐繘绾跨▼姹犵殑绾跨▼鎵嶄細琚墽琛屻�傜嚎绋嬫睜婊＄殑璇濓紝schedule杩囩▼浼氳闃诲銆�
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
		ses.schedule(tt1, 1, TimeUnit.SECONDS);
		ses.schedule(tt2, 1, TimeUnit.SECONDS);
		ses.schedule(tt3, 1, TimeUnit.SECONDS);
		
		for(int i = 0; i < 100; i++){
			ses.schedule(tt1, 1, TimeUnit.SECONDS);
		}
		Thread.sleep(3000);
		ses.shutdown();
		System.out.println("ScheduleThreadPoolExecutor");
	}

}

/**
 * Task 
 *
 */
class Task extends TimerTask{
	
	private String name = "";
	
	private Command cmd;
	
	public Task(String name, Command cmd){
		this.name = name;
		this.cmd = cmd;
	}
	
	@Override
	public void run() {
		try {
			this.cmd.exec();
			System.out.println(this.name + ":" + System.currentTimeMillis());
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
	
}

/**
 * 
 * Command
 *
 */
class Command {
	
	private String cmd;
	
	public Command(String cmd){
		this.cmd = this.initCmd(cmd);
	}
	
	public String initCmd(String cmd){
		String osName = System.getProperty("os.name" );
		if(osName.contains("Windows")) {
		    return "cmd.exe /C " + cmd ;
		}
		return cmd;
	}
	
	public boolean exec(){
		try {
			Process process = Runtime.getRuntime().exec(this.cmd);
			//鏍囧噯杈撳嚭
			BufferedReader bis = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//閿欒杈撳嚭
			BufferedReader bes = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			int exitCode = process.waitFor();
			String line = null;
			while((line = bis.readLine()) != null){
				System.out.println("stdout:" + line);
			}
			while((line = bes.readLine()) != null){
				System.err.println("stderr:" + line);
			}
			System.out.println("exitcode:" + exitCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}

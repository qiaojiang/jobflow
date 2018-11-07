package com.qj.schedule.runnable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qj.schedule.dao.ExecutionDao;
import com.qj.schedule.util.ConfigUtil2;
import com.qj.schedule.util.DateUtil;

public class Executor extends TimerTask{
	
	private static final Logger LOGGER = LogManager.getLogger(Executor.class);
	
	private static final int PROCESS_TIMEOUT = 3600;
	
	private int id;
	
	private String name;
	
	private String cmd;
	
	private ExecutionDao executionDao;
	
	private Process process;
	
	public Executor(int id, String name, String cmd, ExecutionDao executionDao){
		this.id = id;
		this.cmd = this.initCmd(cmd);
		this.name = "Execution[id:" + this.id + ",cmd:" + this.cmd + "]";
		this.executionDao = executionDao;
	}
	
	@Override
	public void run() {
		try {
			LOGGER.info(this.name + " is executing");
			int exitCode = (this.cmd == null) ? 1 : this.exec(this.cmd);
			if(exitCode == 0){
				this.executionDao.setExecutionStatus(this.id, ExecutionDao.STATUS_RUNNING, ExecutionDao.STATUS_SUCCESS);
				LOGGER.info(this.name + " execute success");
			}else{
				this.executionDao.setExecutionStatus(this.id, ExecutionDao.STATUS_RUNNING, ExecutionDao.STATUS_FAILED);
				LOGGER.error(this.name + " execute failed");
			}
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
	
	public String initCmd(String cmd){
		String osName = System.getProperty("os.name" );
		if(osName.contains("Windows")) {
		    return "cmd.exe /C " + cmd ;
		}
		return cmd;
	}
	
	public int exec(String cmd){
		int exitCode = 1;
		try {
			process = Runtime.getRuntime().exec(cmd);
			BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			//输出到日志文件
			File stdOutFile = makeLogFile(this.id,"log");
			File stdErrFile = makeLogFile(this.id,"err");
			BufferedWriter stdOutWriter = new BufferedWriter(new FileWriter(stdOutFile));
			BufferedWriter stdErrWriter = new BufferedWriter(new FileWriter(stdErrFile));
			new Thread(new Writer(stdOutReader, stdOutWriter)).start();
			new Thread(new Writer(stdErrReader, stdErrWriter)).start();
			//计时器,防止线程僵死
			new Thread(new Timer(process, PROCESS_TIMEOUT, this.name)).start();
			exitCode = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return exitCode;
	}
	
	private File makeLogFile(int id, String fileType) throws IOException{
		String day = DateUtil.oral2str("now","yyyyMMdd");
		String filePath = ExecutionDao.getExecutionLogFile(id, day, fileType);
		File logFile = new File(filePath);
		if(!logFile.exists()){
			logFile.createNewFile();
		}
		return logFile;
	}
	
}

package com.qj.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.qj.schedule.runnable.Timer;
import com.qj.schedule.runnable.Writer;

public class JobTest {

	private File makeLogFile(int id, String fileType) throws IOException{
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
		String path = "logs/" + dateformat.format(date);
		File logDir = new File(path);  
		if(!logDir.exists()){
			logDir.mkdir();
		}
		File logFile = new File(path + "/execution_" + id + "." + fileType);
		if(!logFile.exists()){
			logFile.createNewFile();
		}
		return logFile;
	}
	
	public int exec(String cmd){
		int exitCode = 1;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			//输出到日志文件
			File stdOutFile = makeLogFile(0,"log");
			File stdErrFile = makeLogFile(0,"err");
			BufferedWriter stdoutWriter = new BufferedWriter(new FileWriter(stdOutFile));
			BufferedWriter stderrWriter = new BufferedWriter(new FileWriter(stdErrFile));
			new Thread(new Writer(stdoutReader, stdoutWriter)).start();
			new Thread(new Writer(stderrReader, stderrWriter)).start();
			new Thread(new Timer(process, 10, cmd)).start();
			exitCode = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return exitCode;
	}
	
	public static void main(String[] args) {
		String cmd = "php d:/workspace/php/test/index.php";
		JobTest jt = new JobTest();
		System.out.println(jt.exec(cmd));
	}

}

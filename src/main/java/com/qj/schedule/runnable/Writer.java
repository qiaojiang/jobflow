package com.qj.schedule.runnable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Writer implements Runnable{
	
	private BufferedReader br;
	
	private BufferedWriter bw;
	
	public Writer(BufferedReader br, BufferedWriter bw) {
		this.br = br;
		this.bw = bw;
	}
	
	@Override
	public void run() {
		String line = null;
		try {
			while(((line = br.readLine()) != null)){
				bw.write(line + "\n");
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(br != null) br.close();
				if(bw != null) bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

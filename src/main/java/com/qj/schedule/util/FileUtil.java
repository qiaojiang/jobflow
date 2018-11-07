package com.qj.schedule.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class FileUtil {

	public static String getLineContent(String file, int start, int size){  
		LineNumberReader lineReader = null;
		FileReader fileReader = null;
		StringBuffer rows = new StringBuffer();
		try {
			fileReader = new FileReader(file);
			lineReader = new LineNumberReader(fileReader);
			int i = 0, j = 0;
			String row;
			while((row = lineReader.readLine()) != null) {
				if(++i >= start) {
					rows.append(row + "\n");
					j++;
				}
				if(j >= size) {
					break;
				}
			}
			return rows.toString();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if(fileReader != null) {
					fileReader.close();
				}
				if(lineReader != null) {
					lineReader.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
    }  
  
    
}

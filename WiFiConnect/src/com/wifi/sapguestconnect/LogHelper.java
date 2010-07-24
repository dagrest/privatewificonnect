package com.wifi.sapguestconnect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import android.util.Log;

public class LogHelper {

	private static final String logDirectoryPath = "/sdcard/WiFiConnect/";
	private static final String logFileName = "WiFiConnect.log";
	private static LogHelper instance;
	
	private LogHelper(){
		// create a File object for the log directory
		File logDirectory = new File(logDirectoryPath);
		if(logDirectory.exists() == false){
			// create log directory if needed
			logDirectory.mkdirs();
		}

		File logFile = new File(logDirectoryPath + logFileName);
		if(logFile.exists()){
			logFile.delete();
		}
		
		this.toLog(true, new Date().toString() + "\n");
	}
	
	public static LogHelper getLog(){
		if(instance == null){
			instance = new LogHelper();
		}
		return instance;
	}
	
	public void toLog(final boolean isLogEnabled, final String logMessage){
		PrintWriter pw;
		if(isLogEnabled == true){
		    try {
//		        pw = new PrintWriter(
//		                new FileWriter(Environment.getExternalStorageDirectory()+File.separator+logName, true));
		        pw = new PrintWriter(
		                new FileWriter(logDirectoryPath + File.separator +logFileName, true));
		        //ex.printStackTrace(pw);
		        pw.println(logMessage);
		        pw.flush();
		        pw.close();
		    } catch (IOException e) {
		    	Log.e("WiFiConnect: LogHelper->toLog", e.toString());
		        e.printStackTrace();
		    }
		}
	}
}

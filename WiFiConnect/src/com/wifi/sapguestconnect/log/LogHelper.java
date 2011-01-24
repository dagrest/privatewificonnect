package com.wifi.sapguestconnect.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import android.util.Log;

public class LogHelper {

	private static final String logDirectoryPath = "/sdcard/WiFiConnect/";
	private static final String logFileName = "WiFiConnect.log";
	// if the file exists in "/sdcard/WiFiConnect/" folder
	// write logs to "/sdcard/WiFiConnect/WiFiConnect.log"
	private static final String configFile = "config.dat";
	private static LogHelper instance;
	private static boolean isLogEnabled;
	private Object lockMe = null;
	
	private LogHelper(){
		lockMe = new Object();
		
		checkIsLogEnabled();
		if(isLogEnabled() == true){
			// create a File object for the log directory
			File logDirectory = new File(logDirectoryPath);
			if(logDirectory.exists() == false){
				// create log directory if needed
				logDirectory.mkdirs();
			}
	
			File logFile = new File(logDirectoryPath + logFileName);
			if(logFile.exists()){
				//logFile.delete();
			}
			
		}
	}
	
	public static LogHelper getLog(){
		if(instance == null){
			instance = new LogHelper();
		}
		return instance;
	}
	
	public void toLog(final boolean isLogEnabled, final String logMessage){
		
		if(isLogEnabled == true)
		{
			final String timeStamp = getTimestamp();
			Thread t = new Thread(new Runnable() { // TODO Change to Threadpool
				
			@Override
			public void run() 
			{
				synchronized(lockMe)
				{
					try {
						PrintWriter pw = new PrintWriter(
				                new FileWriter(logDirectoryPath + File.separator +logFileName, true));
				        //ex.printStackTrace(pw);
						
						
				        pw.println(timeStamp+" "+logMessage);
				        pw.flush();
				        pw.close();
				    } catch (IOException e) {
				    	Log.e(timeStamp+" WiFiConnect: LogHelper->toLog", e.toString());
					}
				} // of syncrhonized
			} // of run()
			}); // of new Runnable()	
			
			t.start();
		}
	}
	
	private void checkIsLogEnabled(){
		File settingsFile = new File(logDirectoryPath + configFile);
		if(settingsFile.exists()){
			isLogEnabled = true;
		}
		else{
			isLogEnabled = false;
		}
	}
	
	public boolean isLogEnabled(){
		return isLogEnabled;
	}
	
	private String getTimestamp()
	{
		Calendar now = Calendar.getInstance();
		int day = now.get(Calendar.DAY_OF_MONTH);
		int month = now.get(Calendar.MONTH) - Calendar.JANUARY +1;
		int year = now.get(Calendar.YEAR);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		
		return "["+year+"."+month+"."+day+" "+hour+":"+minute+":"+second+"]";
	}
}

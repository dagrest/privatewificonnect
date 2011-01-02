package com.wifi.sapguestconnect.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.wifi.sapguestconnect.ConnectHelper;
import com.wifi.sapguestconnect.LogHelper;
import com.wifi.sapguestconnect.notification.NotificationManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;


public class AutoconnectService extends Service 
{
	private final int TIME_SECOND = 1000;
	private final int TIME_MINUTE = 60 * TIME_SECOND;
	private final int TIMER_PERIOD = 5 * TIME_MINUTE;
	
    private LogHelper logHelper;
    private boolean isLogEnabled;
    private boolean isTimerSet = false;
    private Timer timer;
    
    public static boolean Start(Context context)
    {
    	ConnectHelper connectHelper = new ConnectHelper(context);
    	connectHelper.LoadLoginData();
    	
    	if (connectHelper.isConnectedToCorrectWiFi())
    	{
    		Intent autoConnectService = new Intent(context, AutoconnectService.class);
    		if (context.startService(autoConnectService) != null)
    		{
    			return true;
    		}
    	}

    	Stop(context); // Fallback
		return false;
    }
    
    public static boolean Stop(Context context)
    {
    	Intent autoConnectService = new Intent(context, AutoconnectService.class);
    	return context.stopService(autoConnectService);
    }
    
	@Override
	public void onCreate() 
	{
		// Init Log
		logHelper = LogHelper.getLog();
		isLogEnabled = logHelper.isLogEnabled();
	
		// Log
		logHelper.toLog(isLogEnabled, "AutoconnectService -> onCreate() started.");
		
		// Log
		logHelper.toLog(isLogEnabled, "AutoconnectService -> onCreate () started.");
		
		// Init Super
		super.onCreate();
		
		// Init Timer
		timer = new Timer();
		
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) 
	{
		logHelper.toLog(isLogEnabled, "AutoconnectService -> onStart(Intent intent, int startId) started.");
		
		super.onStart(intent, startId);
    
		if (isTimerSet) // if Timer already set - return
			return;
		
		// Start Timer Task
		TimerTask timerTask = new ConnectionTimerTask(this);
		timer.scheduleAtFixedRate (timerTask, new Date() ,TIMER_PERIOD);
		isTimerSet = true;
		
		
		// Display Notification Text
		NotificationManager.displayServiceRunningNotificationMessage(this);
	}

	@Override
	public void onDestroy() 
	{
		logHelper.toLog(isLogEnabled, "AutoconnectService -> onDestroy() started.");
		
		super.onDestroy();
		
		timer.cancel();
		isTimerSet = false;
		NotificationManager.clearServiceRunningNotification(this);
	}
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		// Log
		logHelper.toLog(isLogEnabled, "AutoconnectService -> onBind(Intent arg0) started.");
		
		return null;
	}
	
	
	private class ConnectionTimerTask extends TimerTask 
    { 
		private WifiManager wm;
		private ConnectHelper connectHelper;
		
		public ConnectionTimerTask(Context context)
		{
			logHelper.toLog(isLogEnabled, "AutoconnectService -> ConnectionTimerTask -> C'tor(Context context) started.");
			
			wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			connectHelper = new ConnectHelper(context, wm);
			connectHelper.LoadLoginData();
		}
		
        public void run() 
        { 
        	logHelper.toLog(isLogEnabled, "AutoconnectService -> ConnectionTimerTask -> run() started.");
        	
        	if (!connectHelper.isLoggedInToSAP())
        	{
        		connectHelper.loginToSAPWiFi();
        	}
        	
        } 
    }

}

package com.wifi.sapguestconnect.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.wifi.sapguestconnect.connection.ConnectHelper;
import com.wifi.sapguestconnect.log.LogManager;
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
	
    private boolean isTimerSet = false;
    private Timer timer;
    
    
    public static boolean Start(Context context)
    {
    	LogManager.LogFunctionCall("AutoconnectService", "Start()");
    	
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
    	LogManager.LogFunctionCall("AutoconnectService", "Stop()");
    	
    	Intent autoConnectService = new Intent(context, AutoconnectService.class);
    	return context.stopService(autoConnectService);
    }
    
	@Override
	public void onCreate() 
	{
		LogManager.LogFunctionCall("AutoconnectService", "onCreate()");
		
		// Init Super
		super.onCreate();
		
		// Init Timer
		timer = new Timer();
		
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) 
	{
		LogManager.LogFunctionCall("AutoconnectService", "onStart()");
		
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
		LogManager.LogFunctionCall("AutoconnectService", "onDestroy()");
		
		super.onDestroy();
		
		timer.cancel();
		isTimerSet = false;
		NotificationManager.clearServiceRunningNotification(this);
	}
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		// Log
		LogManager.LogFunctionCall("AutoconnectService", "onBind()");
		
		return null;
	}
	
	
	private class ConnectionTimerTask extends TimerTask 
    { 
		private WifiManager wm;
		private ConnectHelper connectHelper;
		
		public ConnectionTimerTask(Context context)
		{
			LogManager.LogFunctionCall("AutoconnectService.ConnectionTimerTask", "C'tor()");
			
			wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			connectHelper = new ConnectHelper(context, wm);
			connectHelper.LoadLoginData();
		}
		
        public void run() 
        { 
        	LogManager.LogFunctionCall("AutoconnectService.ConnectionTimerTask", "run()");

        	try
        	{
        		connectHelper.connectToWifi();	
        	}
        	catch (Exception e)
        	{
        		LogManager.LogException(e, "AutoconnectService.ConnectionTimerTask", "run()");
        	}
        } 
    }

}

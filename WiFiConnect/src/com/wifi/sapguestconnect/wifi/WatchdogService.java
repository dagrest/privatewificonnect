package com.wifi.sapguestconnect.wifi;

import com.wifi.sapguestconnect.log.LogManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class WatchdogService extends Service
{
	
	private BroadcastReceiver mWifiWatchDog;
    
    public static boolean Start(Context context)
    {
    	LogManager.LogFunctionCall("WatchdogService", "Start()");
    	
		Intent watchdogService = new Intent(context, WatchdogService.class);
		if (context.startService(watchdogService) != null)
		{
			return true;
		}
		return false;
    }
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		LogManager.LogFunctionCall("WatchdogService", "onCreate()");
		
		mWifiWatchDog = WifiWatchdog.register(this);
	}
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		LogManager.LogFunctionCall("WatchdogService", "onBind()");
		
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		LogManager.LogFunctionCall("WatchdogService", "onStart()");
		
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() 
	{
		LogManager.LogFunctionCall("WatchdogService", "onDestroy()");
		
		super.onDestroy();
	}
}

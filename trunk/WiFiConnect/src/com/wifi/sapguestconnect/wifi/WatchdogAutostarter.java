package com.wifi.sapguestconnect.wifi;

import com.wifi.sapguestconnect.log.LogManager;
import com.wifi.sapguestconnect.preferences.PreferencesFacade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WatchdogAutostarter extends BroadcastReceiver 
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if (PreferencesFacade.isStartAtBoot(context))
		{
			LogManager.LogFunctionCall("WatchdogAutostarter", "onReceive()");
			
			WatchdogService.Start(context);	
		}
	}

}

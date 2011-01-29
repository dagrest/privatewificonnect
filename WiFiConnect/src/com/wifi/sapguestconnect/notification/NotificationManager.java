package com.wifi.sapguestconnect.notification;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

import android.content.Context;
import android.content.res.Resources;

public class NotificationManager 
{
	public static void displayServiceRunningNotificationMessage(Context context)
	{
		LogManager.LogFunctionCall("NotificationManager", "displayServiceRunningNotificationMessage()");
		
		// Init Resources
	    Resources resources = context.getResources();
		
		// Display Notification Text
		String serviceRunningTitle = resources.getString(R.string.app_name);
		String serviceRunningSummary = resources.getString(R.string.notif_service_still_running_summary); 
		
		NotificationHelper.displayNotificationMessage(context, 0, serviceRunningTitle, serviceRunningSummary);
	}
	
	public static void clearServiceRunningNotification(Context context)
	{
		LogManager.LogFunctionCall("NotificationManager", "clearServiceRunningNotification()");
		
		clearAllNotifications(context); // TODO fix if it'll be needed in the future
	}
	
	public static void clearAllNotifications(Context context)
	{
		LogManager.LogFunctionCall("NotificationManager", "clearAllNotifications()");
		
		NotificationHelper.clearAllNotificationMessages(context);
	}
}

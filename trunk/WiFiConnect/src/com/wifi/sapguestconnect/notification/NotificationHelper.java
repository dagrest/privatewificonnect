package com.wifi.sapguestconnect.notification;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.WiFiConnect;
import com.wifi.sapguestconnect.preferences.PreferencesFacade;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class NotificationHelper 
{
    private NotificationManager notificationMgr;
    private boolean isNotificationSet = false; // Not relevant in case of multi notifications
    
    private static NotificationHelper instance = null;
    
    private NotificationHelper(Context context)
    {
    	notificationMgr =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    private void sendNotification(Context context, int id, String title, String summary, boolean vibrate, boolean playSound, Uri soundURI) 
    { 
    	if (isNotificationSet)
    		return;
    	
        Notification notification = new Notification(R.drawable.sap_connect, summary, System.currentTimeMillis());
 
        PendingIntent contentIntent =  PendingIntent.getActivity(context, 0, new Intent(context, WiFiConnect.class), 0); 
 
        notification.setLatestEventInfo(context, title, summary, contentIntent); 
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        
        if (playSound)
        {
        	if (soundURI != null)
        	{
        		notification.sound = soundURI;
        	}
        	else
        	{
        		notification.defaults |= Notification.DEFAULT_SOUND;
        	}
        }
        
        if (vibrate)
        {
        	notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
 
        notificationMgr.notify(id, notification);
        isNotificationSet = true;
    }
    
    private void clearNotification(Context context, int id)
    {
    	notificationMgr.cancel(id);
    	isNotificationSet = false;
    }
    
    private void clearAllNotifications(Context context)
    {
    	notificationMgr.cancelAll();
    	isNotificationSet = false;
    }
    
    public static void displayNotificationMessage(Context context, int id, String title, String summary)
    {
    	if (!PreferencesFacade.isShowIcon(context))
    		return;
    	
    	if (instance == null)
    		instance = new NotificationHelper(context);
    	
    	instance.sendNotification(context, id, title, summary, PreferencesFacade.isEnableConnectVibration(context), PreferencesFacade.isEnableConnectSound(context), PreferencesFacade.getRingtone(context));
    }

    public static void clearAllNotificationMessages(Context context)
    {
    	if (instance == null)
    		instance = new NotificationHelper(context);
    	
    	instance.clearAllNotifications(context);
    }
}

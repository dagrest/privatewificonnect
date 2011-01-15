package com.wifi.sapguestconnect.preferences;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.notification.NotificationManager;
import com.wifi.sapguestconnect.service.AutoconnectService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;

public class PreferencesFacade 
{	
	/***
	 * Used to deal with preferences that affect the App at Startup
	 * @param context
	 */

	public static boolean isRunAsService(Context context)
	{
		return getPrefBoolValueByResource(context, R.string.pref_settings_run_as_service_key, true);
	}
	
	public static void refreshRunAsService(Context context)
	{
    	if (isRunAsService(context))
    	{
    		AutoconnectService.Start(context);
    	}
    	else
    	{
    		AutoconnectService.Stop(context);	
    	}
	}
	
	public static boolean isShowIcon(Context context)
	{
		return getPrefBoolValueByResource(context, R.string.pref_settings_show_icon_key, true);
	}
	
	public static void refreshShowIcon(Context context)
	{
		 if (isShowIcon(context) && isRunAsService(context))
		 {
			 NotificationManager.displayServiceRunningNotificationMessage(context);
		 }
		 else
		 {
			 NotificationManager.clearAllNotifications(context);
		 }
		 
	}
	
	public static boolean isStartAtBoot(Context context)
	{
		return getPrefBoolValueByResource(context, R.string.pref_settings_start_at_boot_key, true);
	}
	
	public static boolean isEnableConnectSound(Context context)
	{
		return getPrefBoolValueByResource(context, R.string.pref_settings_enable_connection_sound_key, true);
	}

	public static Uri getRingtone(Context context)
	{
		return getPrefUriValueByResource(context, R.string.pref_settings_ringtone_key, "DEFAULT_RINGTONE_URI");
	}
	
	public static boolean isEnableConnectVibration(Context context)
	{
		return getPrefBoolValueByResource(context, R.string.pref_settings_vibrate_key, true);
	}
	
	//************************************************
	// Privates
	//************************************************/
	
	private static SharedPreferences getSharedPreferences(Context context)
	{
		Resources resources = context.getResources();
		String sharedPrefsName = resources.getString(R.string.shared_settings_file_name);
		return context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
	}
	
	
	private static boolean getPrefBoolValueByResource(Context context, int resource, boolean preferenceDefaultValue)
	{
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		
		String preferenceKey = context.getResources().getString(resource);
		return sharedPreferences.getBoolean(preferenceKey, preferenceDefaultValue);		
	}
	
	private static String getPrefStringValueByResource(Context context, int resource, String preferenceDefaultValue)
	{
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		
		String preferenceKey = context.getResources().getString(resource);
		return sharedPreferences.getString(preferenceKey, preferenceDefaultValue);		
	}
	
	private static Uri getPrefUriValueByResource(Context context, int resource, String preferenceDefaultValue)
	{
		String prefStringValue = getPrefStringValueByResource(context, resource, preferenceDefaultValue);

		return Uri.parse(prefStringValue);
	}

}

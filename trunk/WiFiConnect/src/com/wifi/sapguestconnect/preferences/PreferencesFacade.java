package com.wifi.sapguestconnect.preferences;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;
import com.wifi.sapguestconnect.notification.NotificationManager;
import com.wifi.sapguestconnect.preferences.location.ILocation;
import com.wifi.sapguestconnect.preferences.location.LocationManager;
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
		LogManager.LogFunctionCall("PreferencesFacade", "isRunAsService()");
		
		return getPrefBoolValueByResource(context, R.string.pref_settings_run_as_service_key, true);
	}
	
	public static void refreshRunAsService(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "refreshRunAsService()");
		
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
		LogManager.LogFunctionCall("PreferencesFacade", "isShowIcon()");
		
		return getPrefBoolValueByResource(context, R.string.pref_settings_show_icon_key, true);
	}
	
	public static void refreshShowIcon(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "refreshShowIcon()");
		
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
		LogManager.LogFunctionCall("PreferencesFacade", "isStartAtBoot()");
		
		return getPrefBoolValueByResource(context, R.string.pref_settings_start_at_boot_key, true);
	}
	
	public static boolean isEnableConnectSound(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "isEnableConnectSound()");
		
		return getPrefBoolValueByResource(context, R.string.pref_settings_enable_connection_sound_key, true);
	}

	public static Uri getRingtone(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "getRingtone()");
		
		return getPrefUriValueByResource(context, R.string.pref_settings_ringtone_key, "DEFAULT_RINGTONE_URI");
	}
	
	public static ILocation getLocation(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "getLocation()");
		
		return LocationManager.getLocation(context, getPrefStringValueByResource(context, R.string.pref_settings_location_key, context.getResources().getString(R.string.pref_settings_location_default_value) ));
	}
	
	public static boolean isEnableConnectVibration(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "isEnableConnectVibration()");
		
		return getPrefBoolValueByResource(context, R.string.pref_settings_vibrate_key, true);
	}
	
	//************************************************
	// Privates
	//************************************************/
	
	private static SharedPreferences getSharedPreferences(Context context)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "getSharedPreferences()");
		
		Resources resources = context.getResources();
		String sharedPrefsName = resources.getString(R.string.shared_settings_file_name);
		return context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
	}
	
	
	private static boolean getPrefBoolValueByResource(Context context, int resource, boolean preferenceDefaultValue)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "getPrefBoolValueByResource()");
		
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		
		String preferenceKey = context.getResources().getString(resource);
		return sharedPreferences.getBoolean(preferenceKey, preferenceDefaultValue);		
	}
	
	private static String getPrefStringValueByResource(Context context, int resource, String preferenceDefaultValue)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "getPrefStringValueByResource()");
		
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		
		String preferenceKey = context.getResources().getString(resource);
		return sharedPreferences.getString(preferenceKey, preferenceDefaultValue);		
	}
	
	private static Uri getPrefUriValueByResource(Context context, int resource, String preferenceDefaultValue)
	{
		LogManager.LogFunctionCall("PreferencesFacade", "getPrefUriValueByResource()");
		
		String prefStringValue = getPrefStringValueByResource(context, resource, preferenceDefaultValue);

		return Uri.parse(prefStringValue);
	}

}

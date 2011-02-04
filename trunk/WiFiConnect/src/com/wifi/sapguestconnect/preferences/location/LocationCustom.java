package com.wifi.sapguestconnect.preferences.location;

import android.content.Context;
import android.content.res.Resources;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;
import com.wifi.sapguestconnect.preferences.PreferencesFacade;

public class LocationCustom implements ILocation 
{
	private final int codeResource = R.string.pref_settings_location_custom_id;
	
	private Context mContext;
	private Resources mResources;
	
	public LocationCustom(Context context)
	{
		LogManager.LogFunctionCall("LocationCustom", "C'tor()");
		
		mContext = context;
		
		mResources = mContext.getResources();
	}
	
	
	@Override
	public String getConnectionHostName() 
	{
		LogManager.LogFunctionCall("LocationCustom", "getConnectionHostName()");
		
		return PreferencesFacade.getCustomConnectionSettingsHostName(mContext);
	}

	@Override
	public String getLocationCode() 
	{
		LogManager.LogFunctionCall("LocationCustom", "getLocationCode()");
		
		return mResources.getString(codeResource);
	}

}

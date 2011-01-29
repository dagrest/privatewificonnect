package com.wifi.sapguestconnect.preferences.location;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

import android.content.Context;
import android.content.res.Resources;

class LocationIL implements ILocation
{
	private final int codeResource = R.string.israel_code;
	private Context mContext;
	private Resources mResources;
	
	public LocationIL(Context context)
	{
		mContext = context;
		
		// Init Log
		LogManager.LogFunctionCall("LocationIL", "C'tor()");

		mResources = mContext.getResources();
	}
	
	@Override
	public String getConnectionHostName() 
	{
		LogManager.LogFunctionCall("LocationIL", "getConnectionHostName()");
		
		return "wlan.sap.com";
	}

	@Override
	public String getLocationCode() 
	{
		LogManager.LogFunctionCall("LocationIL", "getLocationCode()");
		
		return mResources.getString(codeResource);
	}
}

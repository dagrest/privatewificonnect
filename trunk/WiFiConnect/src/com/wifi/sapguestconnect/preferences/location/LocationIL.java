package com.wifi.sapguestconnect.preferences.location;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogHelper;

import android.content.Context;
import android.content.res.Resources;

class LocationIL implements ILocation
{
	private final int codeResource = R.string.israel_code;
	private Context mContext;
	private Resources mResources;
	private LogHelper mLogHelper;
	private boolean isLogEnabled = false;
	
	public LocationIL(Context context)
	{
		mContext = context;
		
		// Init Log
		mLogHelper = LogHelper.getLog();
		isLogEnabled = mLogHelper.isLogEnabled();
		mLogHelper.toLog(isLogEnabled, "LocationIL -> C'tor()");

		mResources = mContext.getResources();
	}
	
	@Override
	public String getConnectionHostName() 
	{
		mLogHelper.toLog(isLogEnabled, "LocationIL -> getConnectionHostName()");
		
		return "wlan.sap.com";
	}

	@Override
	public String getLocationCode() 
	{
		mLogHelper.toLog(isLogEnabled, "LocationIL -> getLocationCode()");
		
		return mResources.getString(codeResource);
	}
}

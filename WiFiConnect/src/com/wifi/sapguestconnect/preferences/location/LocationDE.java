package com.wifi.sapguestconnect.preferences.location;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogHelper;

import android.content.Context;
import android.content.res.Resources;

class LocationDE implements ILocation 
{
	private final int codeResource = R.string.germany_code;
	private Context mContext;
	private Resources mResources;
	private LogHelper mLogHelper;
	private boolean isLogEnabled = false;
	
	public LocationDE(Context context)
	{
		mContext = context;
		
		// Init Log
		mLogHelper = LogHelper.getLog();
		isLogEnabled = mLogHelper.isLogEnabled();
		mLogHelper.toLog(isLogEnabled, "LocationDE -> C'tor()");

		mResources = mContext.getResources();
	}
	
	@Override
	public String getConnectionHostName() 
	{
		mLogHelper.toLog(isLogEnabled, "LocationDE -> getConnectionHostName()");
		
		return "securelogin.arubanetworks.com";
	}

	@Override
	public String getLocationCode() 
	{
		mLogHelper.toLog(isLogEnabled, "LocationDE -> getLocationCode()");
		
		return mResources.getString(codeResource);
	}
	
}

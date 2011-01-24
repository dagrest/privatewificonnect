package com.wifi.sapguestconnect.preferences.location;

import java.util.HashMap;
import java.util.Map;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogHelper;

import android.content.Context;
import android.content.res.Resources;

public class LocationManager 
{
	private static LocationManager instance = null;
	
	private Context mContext = null;
	private Resources mResources = null;
	private Map<String, ILocation> mLocationStrategy = null;
	private LogHelper mLogHelper = null;
	private boolean isLogEnabled = false;
	
	
	private LocationManager(Context context)
	{
		mContext = context;
		mResources = mContext.getResources();
		mLocationStrategy = new HashMap<String, ILocation>();
	    
		// Init Log
		mLogHelper = LogHelper.getLog();
		isLogEnabled = mLogHelper.isLogEnabled();
		mLogHelper.toLog(isLogEnabled, "LocationManager -> C'tor()");
		
		// Init Strategy
		initStrategy();
	}
	
	private void initStrategy()
	{
		mLogHelper.toLog(isLogEnabled, "LocationManager -> initStrategy()");
		
		mLocationStrategy.put(mResources.getString(R.string.israel_code), new LocationIL());
		mLocationStrategy.put(mResources.getString(R.string.germany_code), new LocationDE());
	}
	
	private ILocation getLocation(String locationId)
	{
		mLogHelper.toLog(isLogEnabled, "LocationManager -> getLocation()");
		
		ILocation location = mLocationStrategy.get(locationId);
		if (location == null)
		{
			mLogHelper.toLog(isLogEnabled, "EXCEPTION: LocationManager -> getLocation("+locationId+")"+" Strategy Returned NULL. Using Fallback.");
			location = new LocationIL(); // Fallback
		}
		
		return location;
	}
	
	public static ILocation getLocation(Context context, String locationId)
	{
		if (LocationManager.instance == null) // not thread safe - doesn't need to be *YET*
		{
			LocationManager.instance = new LocationManager(context);
		}
		
		return LocationManager.instance.getLocation(locationId);
	}
}

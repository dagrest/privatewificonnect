package com.wifi.sapguestconnect.preferences.location;

import java.util.HashMap;
import java.util.Map;

import com.wifi.sapguestconnect.log.LogHelper;

import android.content.Context;

public class LocationManager 
{
	private static LocationManager instance = null;
	
	private Context mContext = null;
	private Map<String, ILocation> mLocationStrategy = null;
	private LogHelper mLogHelper = null;
	private boolean isLogEnabled = false;
	
	
	private LocationManager(Context context)
	{
		mContext = context;
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
		
		addNewStrategyEntry(new LocationIL(mContext));
		addNewStrategyEntry(new LocationDE(mContext));
	}
	
	private void addNewStrategyEntry(ILocation location)
	{
		mLocationStrategy.put(location.getLocationCode(), location);
	}
	
	private ILocation getLocation(String locationId)
	{
		mLogHelper.toLog(isLogEnabled, "LocationManager -> getLocation()");
		
		ILocation location = mLocationStrategy.get(locationId);
		if (location == null)
		{
			mLogHelper.toLog(isLogEnabled, "EXCEPTION: LocationManager -> getLocation("+locationId+")"+" Strategy Returned NULL. Using Fallback.");
			location = new LocationIL(mContext); // Fallback
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
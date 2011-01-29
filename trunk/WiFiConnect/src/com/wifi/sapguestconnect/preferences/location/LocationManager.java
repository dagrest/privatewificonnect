package com.wifi.sapguestconnect.preferences.location;

import java.util.HashMap;
import java.util.Map;

import com.wifi.sapguestconnect.log.LogManager;

import android.content.Context;

public class LocationManager 
{
	private static LocationManager instance = null;
	
	private Context mContext = null;
	private Map<String, ILocation> mLocationStrategy = null;
	
	
	private LocationManager(Context context)
	{
		LogManager.LogFunctionCall("LocationManager", "C'tor()");
		
		mContext = context;
		mLocationStrategy = new HashMap<String, ILocation>();
		
		// Init Strategy
		initStrategy();
	}
	
	private void initStrategy()
	{
		LogManager.LogFunctionCall("LocationManager", "initStrategy()");
		
		addNewStrategyEntry(new LocationIL(mContext)); // TODO Fix - create an instance ON DEMAND and not all 
		addNewStrategyEntry(new LocationDE(mContext));
	}
	
	private void addNewStrategyEntry(ILocation location)
	{
		LogManager.LogFunctionCall("LocationManager", "addNewStrategyEntry()");
		
		mLocationStrategy.put(location.getLocationCode(), location);
	}
	
	private ILocation getLocation(String locationId)
	{
		LogManager.LogFunctionCall("LocationManager", "getLocation()");
		
		ILocation location = mLocationStrategy.get(locationId);
		if (location == null)
		{
			LogManager.LogErrorMsg("LocationManager", "getLocation()", "Strategy Returned NULL. Using Fallback.");
			location = new LocationIL(mContext); // Fallback
		}
		
		return location;
	}
	
	public static ILocation getLocation(Context context, String locationId)
	{
		LogManager.LogFunctionCall("LocationManager", "getLocation() [static]");
		
		if (LocationManager.instance == null) // not thread safe - doesn't need to be *YET*
		{
			LocationManager.instance = new LocationManager(context);
		}
		
		return LocationManager.instance.getLocation(locationId);
	}
}

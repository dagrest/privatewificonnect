package com.wifi.sapguestconnect.preferences.location;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.wifi.sapguestconnect.log.LogManager;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

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
		
		addNewStrategyEntry(new LocationBR(mContext)); // TODO Fix - create an instance ON DEMAND and not all
		addNewStrategyEntry(new LocationCA(mContext));
		addNewStrategyEntry(new LocationDE(mContext));
		addNewStrategyEntry(new LocationIL(mContext));
		addNewStrategyEntry(new LocationCustom(mContext));
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
			location = new LocationCustom(mContext); // Fallback
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
	
	public static String getLastKnownCountryCode(Context context)
	{	
		LogManager.LogFunctionCall("LocationManager", "getLastKnownCountryCode");
		
		String countryCode = "";
		try
		{
			android.location.LocationManager lm = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
			Criteria crit = new Criteria();
			crit.setAccuracy(Criteria.ACCURACY_COARSE);
			String provider = lm.getBestProvider(crit, true);
			Location loc = lm.getLastKnownLocation(provider);
			Geocoder gc = new Geocoder(context, Locale.getDefault());
			List<Address> addresses = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses.size() > 0) 
            {
                Address address = addresses.get(0);
                countryCode = address.getCountryCode();
            }
		}
		catch (Exception e)
		{
			LogManager.LogException(e, "LocationManager", "getLastKnownCountryCode");
			return "";
		}
		
		if (countryCode == null)
			countryCode = "";
		
		return countryCode;
	}
}

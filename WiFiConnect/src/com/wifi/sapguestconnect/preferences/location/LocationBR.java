package com.wifi.sapguestconnect.preferences.location;

import android.content.Context;
import android.content.res.Resources;

import com.wifi.sapguestconnect.R;
import com.wifi.sapguestconnect.log.LogManager;

public class LocationBR implements ILocation 
{
	private final int codeResource = R.string.brazil_code;
	private final int hostNameResource = R.string.host_name_aruba_networks;
	
	private Context mContext;
	private Resources mResources;
	
	public LocationBR(Context context)
	{
		mContext = context;
		
		LogManager.LogFunctionCall("LocationBR", "C'tor()");

		mResources = mContext.getResources();
	}
	
	@Override
	public String getConnectionHostName() 
	{
		LogManager.LogFunctionCall("LocationBR", "getConnectionHostName()");
		
		return mResources.getString(hostNameResource);
	}

	@Override
	public String getLocationCode() 
	{
		LogManager.LogFunctionCall("LocationBR", "getLocationCode()");
		
		return mResources.getString(codeResource);
	}
	
}

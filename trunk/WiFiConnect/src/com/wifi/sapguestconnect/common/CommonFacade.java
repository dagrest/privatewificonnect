package com.wifi.sapguestconnect.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wifi.sapguestconnect.dialog.AboutDialog;
import com.wifi.sapguestconnect.log.LogManager;

public class CommonFacade 
{
	private static PackageInfo getPackageInfo(Context context) throws NameNotFoundException
	{
		LogManager.LogFunctionCall("PackageInfo", "getPackageInfo()");
		
	    ComponentName comp = new ComponentName(context, AboutDialog.class);
	    return context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
	}
	
	public static String getVersionName(Context context) 
	{
		LogManager.LogFunctionCall("PackageInfo", "getVersionName()");
		
	  try {
		  return getPackageInfo(context).versionName;
	  } 
	  catch (android.content.pm.PackageManager.NameNotFoundException e) {
		  LogManager.LogException(e, "PackageInfo", "getVersionName()");
		  return "";
	  }
	}
	
	public static int getVersionCode(Context context) 
	{
		LogManager.LogFunctionCall("PackageInfo", "getVersionName()");
		
	  try {
		  return getPackageInfo(context).versionCode;
	  } 
	  catch (android.content.pm.PackageManager.NameNotFoundException e) {
		  LogManager.LogException(e, "PackageInfo", "getVersionCode()");
	    return -1;
	  }
	}
}

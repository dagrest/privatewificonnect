package com.wifi.sapguestconnect.data;

import android.content.Context;

import com.wifi.sapguestconnect.LoginData;
import com.wifi.sapguestconnect.connection.ConnectHelper;
import com.wifi.sapguestconnect.log.LogManager;

public class DataFacade 
{
	public static LoginData LoadLoginData(Context context)
	{
		LogManager.LogFunctionCall("DataFacade", "LoadLoginData()");
		
		ConnectHelper connectHelper = new ConnectHelper(context);
		connectHelper.LoadLoginData();
		return connectHelper.getLoginData();
	}
	
	public static void PersistLoginData(Context context, LoginData loginData)
	{
		LogManager.LogFunctionCall("DataFacade", "PersistLoginData()");
		
		ConnectHelper connectHelper = new ConnectHelper(context);
		connectHelper.LoadLoginData();
		connectHelper.saveLoginData(loginData.getUser(), loginData.getPass(), loginData.getSSID());
	}
}

package com.wifi.sapguestconnect.data;

import android.content.Context;

import com.wifi.sapguestconnect.LoginData;
import com.wifi.sapguestconnect.connection.ConnectHelper;

public class DataFacade 
{
	public static LoginData LoadLoginData(Context context)
	{
		ConnectHelper connectHelper = new ConnectHelper(context);
		connectHelper.LoadLoginData();
		return connectHelper.getLoginData();
	}
	
	public static void PersistLoginData(Context context, LoginData loginData)
	{
		ConnectHelper connectHelper = new ConnectHelper(context);
		connectHelper.LoadLoginData();
		connectHelper.saveLoginData(loginData.getUser(), loginData.getPass(), loginData.getSSID());
	}
}

package com.wifi.sapguestconnect.wifi;

import com.wifi.sapguestconnect.connection.ConnectHelper;
import com.wifi.sapguestconnect.log.LogManager;
import com.wifi.sapguestconnect.preferences.PreferencesFacade;
import com.wifi.sapguestconnect.service.AutoconnectService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

class WifiWatchdog extends BroadcastReceiver
{
	private WifiManager mWifiManager;
	private ConnectHelper mConnectHelper;
	private Context mContext;
	
	private WifiWatchdog(Context context)
	{
		LogManager.LogFunctionCall("WifiWatchdog", "C'tor()");
		
		mContext = context;
		
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		mConnectHelper = new ConnectHelper(context, mWifiManager);
		mConnectHelper.LoadLoginData();
	}
	
	public static BroadcastReceiver register(Context context)
	{
		LogManager.LogFunctionCall("WifiWatchdog", "register()");
		
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        WifiWatchdog watchDog = new WifiWatchdog(context);
        context.registerReceiver(watchDog, intentFilter);
        return watchDog;
	}
	
    @Override
    public void onReceive(Context context, Intent intent) 
    {
    	LogManager.LogFunctionCall("WifiWatchdog", "onReceive()");
    	
        final String action = intent.getAction();
        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) 
        {
            handleNetworkStateChanged((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO));
        } 
        else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) 
        {
            handleSupplicantConnectionChanged(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false));
        } 
        else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            handleWifiStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN));
        }
    }

    private void handleNetworkStateChanged(NetworkInfo info) 
    {
    	LogManager.LogFunctionCall("WifiWatchdog", "handleNetworkStateChanged()");
    	
        switch (info.getState()) 
        {
            case CONNECTED:
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                if (wifiInfo.getSSID() == null || wifiInfo.getBSSID() == null) {
                    return;
                }
                onConnected(wifiInfo.getSSID(), wifiInfo.getBSSID());
                break;

            case DISCONNECTED:
                onDisconnected();
                break;
        }
    }

	private void handleSupplicantConnectionChanged(boolean connected) 
    {
		LogManager.LogFunctionCall("WifiWatchdog", "handleSupplicantConnectionChanged()");
		
        if (!connected) 
        {
            onDisconnected();
        }
    }

	private void handleWifiStateChanged(int wifiState) 
    {
		LogManager.LogFunctionCall("WifiWatchdog", "handleWifiStateChanged()");
		
        if (wifiState == WifiManager.WIFI_STATE_DISABLED) 
        {
            onDisconnected();
        } 
        else if (wifiState == WifiManager.WIFI_STATE_ENABLED) 
        {
            //onEnabled();
        }
    }
	
    private void onConnected(String ssid, String bssid) 
    {
    	LogManager.LogFunctionCall("WifiWatchdog", "onConnected()");
    	
    	try
    	{        	
        	mConnectHelper.LoadLoginData(); // should refactor - maybe create a singleton 
        	if (mConnectHelper.isConnectedToCorrectWiFi(ssid))
        	{
        		PreferencesFacade.refreshRunAsService(mContext);
        	}
        	else
        	{
        		AutoconnectService.Stop(mContext);
        	}
    	}
    	catch (Exception e)
    	{
    		LogManager.LogException(e, "WifiWatchdog", "onConnected()");
    	}

	}
	
    private void onDisconnected() 
    {
    	try
    	{
    		LogManager.LogFunctionCall("WifiWatchdog", "onDisconnected()");
        	
        	AutoconnectService.Stop(mContext);
    	}
    	catch (Exception e)
    	{
    		LogManager.LogException(e, "WifiWatchdog", "onDisconnected()");
    	}
	}

}

package com.wifi.sapguestconnect.connection;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.wifi.sapguestconnect.LoginData;
import com.wifi.sapguestconnect.connection.ConnectionFacade.IConnectionAttemptResponse;
import com.wifi.sapguestconnect.log.LogHelper;
import com.wifi.sapguestconnect.preferences.PreferencesFacade;

class ConnectManager implements Runnable
{
	private Context mContext = null;
	private ConnectionAttemptResponseHandler mConnAttemptHandler = null;
	private WifiManager mWifiManager = null;
	private LoginData mLoginData = null;
	private LogHelper mLogHelper = null;
	private boolean isLogEnabled = false;
	
	public ConnectManager(Context context, ConnectionAttemptResponseHandler connectionHandler, LoginData loginData)
	{
		mContext = context;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mConnAttemptHandler = connectionHandler;
		
		// Init Log
	    mLogHelper = LogHelper.getLog();
		isLogEnabled = mLogHelper.isLogEnabled();
		mLogHelper.toLog(isLogEnabled, "ConnectManager -> C'tor()");
		
		mLoginData = loginData;	
	}
	
	@Override
	public void run() 
	{
		mConnAttemptHandler.sendMessage(connect());
	}

	
	public Message connect() 
	{	
		mLogHelper.toLog(isLogEnabled, "ConnectManager -> connect()");
	    
		Message responseMsg = new Message();
		
		try 
	    {
			if(mWifiManager == null)
			{
				responseMsg.obj = responseMsg.obj = ConnectionErrorMessages.UNKNOWN_WIFI;
			}
			else if ((mWifiManager.isWifiEnabled() == false) || (mLoginData == null)) 
			{
				responseMsg.obj = responseMsg.obj = ConnectionErrorMessages.UNKNOWN_WIFI;
			}
			else if(mLoginData.getSSID().compareToIgnoreCase((mWifiManager.getConnectionInfo().getSSID())) == 0) 
			{
				responseMsg = connectToWifi();
			}
			else 
			{
				responseMsg.obj = ConnectionErrorMessages.UNKNOWN_WIFI;
			}
			
		} 
	    catch (Exception e) 
	    {
	    	responseMsg.obj = ConnectionErrorMessages.FAIL;
    		mLogHelper.toLog(isLogEnabled, "EXCEPTION: ConnectManager -> connect(): " + e.getMessage());
		}
	    
		if (responseMsg.obj == null)
		{
			responseMsg.obj = ConnectionErrorMessages.FAIL;
		}
	    
	    return responseMsg;
	}
	
	private Message connectToWifi() 
	{
    	mLogHelper.toLog(isLogEnabled, "ConnectManager -> connectToWifi() started.");

    	ConnectionStatus isConnected = ConnectionFacade.isConnected(mContext);
    	boolean isConnectedBool = false;
    	if (isConnected == ConnectionStatus.CONNECTED)
    	{
    		isConnectedBool = true;
    	}
		
		ConnectHelper connectHelper = new ConnectHelper(mContext);
		connectHelper.LoadLoginData();
		
		Message responseMsg = new Message();

		if (mLoginData == null)
		{
			responseMsg.obj = responseMsg.obj = ConnectionErrorMessages.FAIL;
			return responseMsg; 
		}
		
		String user = mLoginData.getUser();
		String pass = mLoginData.getPass();
		String ssid = mLoginData.getSSID();
		
		try 
		{
			if(isConnectedBool == false) 
			{
				if(connectHelper.isLoginDataExist(user, pass, ssid) == false){
					connectHelper.saveLoginData(user, pass, ssid);
					PreferencesFacade.refreshRunAsService(mContext);
				}
				else if(connectHelper.isLoginDataExist(user, pass, ssid) == true && 
					connectHelper.isLoginDataChanged() == false){
					responseMsg.obj = connectHelper.connectToWifi();
				}
				else
				{
					connectHelper.saveLoginData(user, pass, ssid);
					PreferencesFacade.refreshRunAsService(mContext);
					responseMsg.obj = connectHelper.connectToWifi();
				}
			}
			else
			{
				connectHelper.saveLoginData(user, pass, ssid);
				PreferencesFacade.refreshRunAsService(mContext);
				responseMsg.obj = ConnectionErrorMessages.ALREADY_CONNECTED; // TODO move from here
			}
		} 
		catch (Exception e) 
		{
			mLogHelper.toLog(isLogEnabled, "EXCEPTION: ConnectManager -> connectToWifi(): " + e.getMessage());
		}
		
		if (responseMsg.obj == null)
		{
			responseMsg.obj = ConnectionErrorMessages.FAIL;
		}
		
		return responseMsg;
	}
	
	static public class ConnectionAttemptResponseHandler extends Handler
	{
		IConnectionAttemptResponse connectionResponse = null;
		
		public ConnectionAttemptResponseHandler(IConnectionAttemptResponse connResponse)
		{
			connectionResponse = connResponse;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			if (connectionResponse != null)
			{
				connectionResponse.OnResponse((ConnectionErrorMessages)msg.obj);
			}
		}
	}
}

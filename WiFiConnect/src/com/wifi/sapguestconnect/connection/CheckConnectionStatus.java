package com.wifi.sapguestconnect.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wifi.sapguestconnect.connection.ConnectionFacade.IConnectionStatusResponse;
import com.wifi.sapguestconnect.log.LogHelper;

class CheckConnectionStatus implements Runnable
{
	private Context context;
	private ConnectionStatusResponseHandler loginStatusResponseHandler = null;
	private LogHelper logHelper;
	private boolean isLogEnabled;
	
	public CheckConnectionStatus(Context context, ConnectionStatusResponseHandler loginStatusResponseHandler)
	{
		logHelper = LogHelper.getLog();
		isLogEnabled = logHelper.isLogEnabled();
		
		logHelper.toLog(isLogEnabled, "CheckLoginStatus -> C'tor()");
		
		this.context = context;
		this.loginStatusResponseHandler = loginStatusResponseHandler;
	}

	@Override
	public void run() 
	{
		logHelper.toLog(isLogEnabled, "CheckLoginStatus -> run()");
		
		Message responseMsg = new Message();
		responseMsg.obj = ConnectHelper.IsOnline(context);
		loginStatusResponseHandler.sendMessage(responseMsg);
	}
	
	static public class ConnectionStatusResponseHandler extends Handler
	{
		IConnectionStatusResponse connectionResponse = null;
		
		public ConnectionStatusResponseHandler(IConnectionStatusResponse connResponse)
		{
			connectionResponse = connResponse;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			if (connectionResponse != null)
			{
				connectionResponse.OnResponse((ConnectionStatus)msg.obj);
			}
		}
	}
}

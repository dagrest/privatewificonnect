package com.wifi.sapguestconnect.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wifi.sapguestconnect.connection.ConnectionFacade.IConnectionStatusResponse;
import com.wifi.sapguestconnect.log.LogManager;

class CheckConnectionStatus implements Runnable
{
	private Context context;
	private ConnectionStatusResponseHandler loginStatusResponseHandler = null;
	
	public CheckConnectionStatus(Context context, ConnectionStatusResponseHandler loginStatusResponseHandler)
	{
		LogManager.LogFunctionCall("CheckConnectionStatus", "C'tor()");
		
		this.context = context;
		this.loginStatusResponseHandler = loginStatusResponseHandler;
	}

	@Override
	public void run() 
	{
		LogManager.LogFunctionCall("CheckConnectionStatus", "C'tor()");
		
		Message responseMsg = new Message();
		responseMsg.obj = ConnectHelper.IsOnline(context);
		loginStatusResponseHandler.sendMessage(responseMsg);
	}
	
	static public class ConnectionStatusResponseHandler extends Handler
	{
		IConnectionStatusResponse connectionResponse = null;
		
		public ConnectionStatusResponseHandler(IConnectionStatusResponse connResponse)
		{
			LogManager.LogFunctionCall("CheckConnectionStatus.ConnectionStatusResponseHandler", "C'tor()");
			
			connectionResponse = connResponse;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			LogManager.LogFunctionCall("CheckConnectionStatus.ConnectionStatusResponseHandler", "handleMessage()");
			
			if (connectionResponse != null)
			{
				connectionResponse.OnResponse((ConnectionStatus)msg.obj);
			}
		}
	}
}

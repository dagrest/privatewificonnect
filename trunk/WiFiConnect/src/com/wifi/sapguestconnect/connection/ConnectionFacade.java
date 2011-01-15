package com.wifi.sapguestconnect.connection;

import com.wifi.sapguestconnect.LoginData;
import com.wifi.sapguestconnect.connection.CheckConnectionStatus.ConnectionStatusResponseHandler;
import com.wifi.sapguestconnect.connection.ConnectManager.ConnectionAttemptResponseHandler;

import android.content.Context;

public class ConnectionFacade 
{
	public static ConnectionStatus isConnected(Context context)
	{
		return ConnectHelper.IsOnline(context);
	}
	
	public static void isConnectedAsync(Context context, IConnectionStatusResponse responseCallback)
	{
		ConnectionStatusResponseHandler loginStatusResponseHandler = new ConnectionStatusResponseHandler(responseCallback);
		CheckConnectionStatus checkLoginStatus = new CheckConnectionStatus(context, loginStatusResponseHandler);
		Thread checkLoginThread = new Thread(checkLoginStatus);
		checkLoginThread.start();
	}
	
	public interface IConnectionStatusResponse
	{
	    void OnResponse(ConnectionStatus response);
	}
	
	public static boolean Connect(Context context)
	{
		// TODO implement
		return false;
	}
	
	public static void ConnectAsync(Context context, LoginData loginData, IConnectionAttemptResponse responseCallback)
	{
		ConnectionAttemptResponseHandler connAttemptResponseHandler = new ConnectionAttemptResponseHandler(responseCallback);
		ConnectManager connManager = new ConnectManager(context, connAttemptResponseHandler, loginData);
		Thread connAttemptThread = new Thread(connManager);
		connAttemptThread.start();
	}
	
	public interface IConnectionAttemptResponse
	{
	    void OnResponse(ConnectionErrorMessages response);
	}
	
}
